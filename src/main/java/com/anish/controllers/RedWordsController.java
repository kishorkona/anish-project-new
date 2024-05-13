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

import org.apache.commons.collections.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	
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
	
	@GetMapping(value = "/redwords/takeTest/{name}/{fileName}")
    public ModelAndView getTakeTest(@PathVariable("name") String name, @PathVariable("fileName") String fileName) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			List<Word> currentList = getRedWordsFile(fileName);
			String path = env.getProperty("destination.path");
			String doneFilePath = path+name+"/done/"+fileName+"_done.txt";
			List<Word> doneList = getReadWordDoneData(doneFilePath);
			List<Integer> doneIds = new ArrayList<Integer>();
			if(doneList.size()>0) {
				for(Word wrd: doneList) {
					doneIds.add(wrd.getId());
				}	
			}
			List<Word> finalList = new ArrayList<Word>();
			for(Word wrd: currentList) {
				if(!doneIds.contains(wrd.getId())) {
					if(wrd.getSentence().equalsIgnoreCase("Nothing")) {
						wrd.setSentence("");
					}
					finalList.add(wrd);
				}
			}
			if(finalList.size()>0) {
				modelAndView.addObject("testName", finalList.get(0).getTestName());
				modelAndView.addObject("redWordsExists", "true");
				if(finalList.size()==1) {
					modelAndView.addObject("subList1", finalList);
					modelAndView.addObject("subList2", new ArrayList<Word>());
				} else {
					List<Word> subList1 = finalList.subList(0, finalList.size()/2);
					List<Word> subList2 = finalList.subList((finalList.size()/2)+1, finalList.size());
					modelAndView.addObject("subList1", subList1);
					modelAndView.addObject("subList2", subList2);
				}
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("read_word_test_new");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	public List<Word> getRedWordsFile(String fileName) {
		List<Word> dataList = new ArrayList<Word>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/"+fileName+".xml";
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
	
	public Word getAllReadWordRecords(boolean next, Integer id, String spellValue, String name, String fileName) {
		String path = env.getProperty("destination.path");
		String doneFilePath = path+name+"/done/"+fileName+"_done.txt";
		String xmlFilePath = "xmldata/"+fileName+".xml";
		List<Word> currentList = getRedWordsFile(xmlFilePath);
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
	
	@GetMapping(value = "/redwords/doneword/{id}/{fileName}")
    public ModelAndView completeWord(@PathVariable("id") String id, @PathVariable("fileName") String fileName) {
		try {
			String path = env.getProperty("destination.path");
			String doneFilePath = path+ishant_name+"/done/"+fileName+"_done.txt";
			List<Word> currentList = getRedWordsFile(fileName);
			Word myWord = null;
			for(Word wrd: currentList) {
				if(wrd.getId()==Integer.parseInt(id)) {
					myWord = wrd;
				}
			}
			writeWordRecordToFile(doneFilePath, myWord);
			return getTakeTest(ishant_name, fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		return null;
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
}
