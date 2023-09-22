package com.anish.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Question;
import com.anish.data.Week;
import com.anish.data.Word;
import com.anish.parsers.ReadWordSaxParserHandler;
import com.anish.parsers.VocabularySaxParserHandler;
import com.google.gson.Gson;

@Controller
public class RedWordsController {
	
	@Autowired private Environment env;
	@Autowired private MyHelper helper;
	private Gson gson = new Gson();
	
	@GetMapping(value = "/redwords/takeTest")
    public ModelAndView getTakeTest() {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Word word = helper.getAllReadWordRecords(false, 0);
			if(word != null) {
	    		modelAndView.addObject("data", word);
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("read_word_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	@GetMapping(value = "/redwords/nextTest/{id}")
    public ModelAndView getTakeNextTest(@PathVariable("id") String id) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Word word = helper.getAllReadWordRecords(true, Integer.valueOf(id));
			if(word != null) {
	    		modelAndView.addObject("data", word);
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("read_word_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	@RequestMapping(value = "/redwords/create", method = RequestMethod.GET)
	public String createVocabulary() {
		if(createVocabularyFile()) {
			return "done";
		}
		return "error";
	}
	
	public boolean createVocabularyFile() {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "read_words.xml";
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				ReadWordSaxParserHandler handler = new ReadWordSaxParserHandler();
		        saxParser.parse(fObj, handler);
		        List<Word> dataList = handler.getWordList();
				if(dataList.size()>0) {
					groupData(dataList);
					return createFinalFile(dataList);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void groupData(List<Word> wordList) {
		wordList.stream().forEach(word -> {
			word.setTotalWords(wordList.size());
		});
	}
	
	public boolean createFinalFile(List<Word> data) {
		BufferedWriter writer = null;
    	try {   
    		String path = env.getProperty("base.path");
    		String finalFilePath = path+"read_words/read_words.txt";
    		File fileObj = new File(finalFilePath);
    		writer = new BufferedWriter(new FileWriter(fileObj, true)); 
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
}
