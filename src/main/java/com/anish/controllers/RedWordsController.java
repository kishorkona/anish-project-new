package com.anish.controllers;

import java.io.File;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Word;
import com.google.gson.Gson;

@Controller
public class RedWordsController {
	
	//https://ttsmp3.com/
	@Autowired private Environment env;
	@Autowired private MyHelper helper;
	private Gson gson = new Gson();
	
	private String[] getNextWord(Integer fileNo) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("audio/"+fileNo+".mp3");
		File fObj = new File(resource.getFile());
		String[] arr = fObj.getAbsolutePath().split("anish-project-new");
		String[] strArr = new String[2];
		strArr[0] = "file://"+arr[0]+"anish-project-new/src/main/resources/audio/"+fileNo+".mp3";
		strArr[1] = "file://"+arr[0]+"anish-project-new/src/main/resources/audio/"+fileNo+"_sentence.mp3";
		return strArr;
	}
	
	@GetMapping(value = "/redwords/takeTest")
    public ModelAndView getTakeTest() {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Word word = helper.getAllReadWordRecords(false, 0, null);
			if(word != null) {
	    		modelAndView.addObject("data", word);
	    		String[] arr = getNextWord(word.getFileNo());
	    		modelAndView.addObject("filePath", arr[0]);
	    		if(word.isHasSentence()) {
		    		modelAndView.addObject("filePathSentence", arr[1]);
	    		}
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("read_word_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	@RequestMapping(value = "/redwords/nextTest", method = RequestMethod.POST)
	public String getTakeNextTest(@ModelAttribute("word") Word word, ModelMap model) {
		try {
			Word wordResp = helper.getAllReadWordRecords(true, word.getId(), word.getRepeat());
			if(word != null) {
				model.addAttribute("data", wordResp);
				String[] arr = getNextWord(wordResp.getFileNo());
				model.addAttribute("filePath", arr[0]);
				if(wordResp.isHasSentence()) {
					model.addAttribute("filePathSentence", arr[1]);
	    		}
	    	}
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return "read_word_test";
    }
	
	/*
	@RequestMapping(value = "/redwords/create", method = RequestMethod.GET)
	public ModelAndView createVocabulary() {
		createVocabularyFile();
		return getTakeTest();
	}
	*/
	
	/*
	public boolean createFinalFile(List<Word> data) {
		BufferedWriter writer = null;
    	try {   
    		String path = env.getProperty("base.path");
    		String finalFilePath = path+"read_words/read_words.txt";
    		File fileObj = new File(finalFilePath);
    		writer = new BufferedWriter(new FileWriter(fileObj, false)); 
    		for(int i=0;i<data.size();i++) {
    			if(i==data.size()-1) {
    				writer.write(gson.toJson(data.get(i)));
    			} else {
    				writer.write(gson.toJson(data.get(i)));
    	    	    writer.write("\n");
    			}
    		}
    		return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
		} finally {
			try {
				if(writer != null) {
					writer.close();
				}
			} catch (Exception e) { }
		}
    }
	*/
}
