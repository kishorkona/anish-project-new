package com.anish.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Word;
import com.anish.parsers.ReadWordSaxParserHandler;
import com.google.gson.Gson;

@Controller
public class RedWordsController {
	
	//https://ttsmp3.com/
	@Autowired private Environment env;
	private Gson gson = new Gson();
	
	private String[] getNextWord(Integer fileNo) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource("audio/redwords/ishant/"+fileNo+".mp3");
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
			Word word = getAllReadWordRecords(false, 0, null);
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
			Word wordResp = getAllReadWordRecords(true, word.getId(), word.getRepeat());
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
	
	public Word getAllReadWordRecords(boolean next, Integer id, String spellValue) {
		String path = env.getProperty("destination.path");
		String doneFilePath = path+"read_words_done.txt";
		List<Word> currentList = getRedWordsFile();
		if(next) {
			Optional<Word> weekOpt = currentList.stream().filter(x -> {
				if(x.getId()==id) {
					return true;
				}
				return false;
			}).findFirst();
			Word modifiedWord = weekOpt.get();
			modifiedWord.setRepeat(spellValue);
			writeWordRecordToFile(doneFilePath, modifiedWord);
		}
		List<Word> doneList = getReadWordDoneData(doneFilePath);
		Set<Integer> doneSet = doneList.stream().map(x -> {
			return x.getId();
		}).collect(Collectors.toSet());
		List<Word> finalCurrentList = currentList.stream()
				.filter(x -> {
					return !doneSet.contains(x.getId());
				}).collect(Collectors.toList());
		return finalCurrentList.get(0);
    }
	public List<Word> getRedWordsFile() {
		List<Word> dataList = new ArrayList<Word>();
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
		        dataList = handler.getWordList();
				if(dataList.size()>0) {
					groupData(dataList);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	public void writeWordRecordToFile(String filePath, Word word) {
    	try {   
    		Path path = Path.of(filePath);
    		Files.writeString(path, "\n"+gson.toJson(word), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	
	public void groupData(List<Word> wordList) {
		wordList.stream().forEach(word -> {
			word.setTotalWords(wordList.size());
		});
	}
	
	public List<Word> getReadWordDoneData(String filePath) {
		List<Word> lines = new ArrayList<Word>();
		Scanner myReader = null;
		String val = null;
    	try {
    		File myObj = new File(filePath);
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, Word.class));
    			}
    		}
    	} catch (Exception e) {
    		System.out.println("Error at Row="+val);
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(myReader!=null) {
					myReader.close();
				}
			} finally {}
		}
    	return lines;
    }
}
