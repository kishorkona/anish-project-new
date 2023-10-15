package com.anish.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Week;
import com.anish.parsers.VocabularySaxParserHandler;
import com.google.gson.Gson;

@Controller
public class VocabularyController {
	
	@Autowired private Environment env;
	private Gson gson = new Gson();
	
	@GetMapping(value = "/vocabulory/takeTest/{name}")
    public ModelAndView getTakeTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Week week = getAllVocabularyRecords(name, false, 0, 0);
			if(week != null) {
	    		modelAndView.addObject("data", week);
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("vocabulary_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	@GetMapping(value = "/vocabulory/nextTest/{name}/{weekId}/{weekNo}")
    public ModelAndView getTakeNextTest(@PathVariable("name") String name, 
    		@PathVariable("weekId") String weekId, @PathVariable("weekNo") String weekNo) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Week week = getAllVocabularyRecords(name, true, Integer.valueOf(weekId), Integer.valueOf(weekNo));
			if(week != null) {
	    		modelAndView.addObject("data", week);
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("vocabulary_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	/*
	@RequestMapping(value = "/vocabulory/create", method = RequestMethod.GET)
	public String createVocabulary() {
		//if(createVocabularyFile("anish") && createVocabularyFile("ishant")) {
		//	return "done";
		//}
		return "error";
	}
	
	public boolean createFinalFile(String name, List<Week> data) {
		BufferedWriter writer = null;
    	try {   
    		String path = env.getProperty("base.path");
    		String finalFilePath = path+name+"_vocabulary.txt";
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
	
	public Week getAllVocabularyRecords(String name, boolean next, Integer weekId, Integer workNo) {
		String path = env.getProperty("destination.path");
		String doneFilePath = path+name+"_vocabulary_done.txt";
		List<Week> currentList = getVocabularyFile(name);
		if(next) {
			Optional<Week> weekOpt = currentList.stream().filter(x -> {
				if(x.getId()==weekId && x.getWordNo()==workNo) {
					return true;
				}
				return false;
			}).findFirst();
			writeVocabularyRecordToFile(doneFilePath, weekOpt.get());
		}
		List<Week> doneList = getVocabularyData(doneFilePath);
		Set<String> doneSet = doneList.stream().map(x -> {
			return x.getId()+"#"+x.getWordNo();
		}).collect(Collectors.toSet());
		List<Week> finalCurrentList = currentList.stream()
				.filter(x -> {
					String key = x.getId()+"#"+x.getWordNo();
					return !doneSet.contains(key);
				}).collect(Collectors.toList());
		return finalCurrentList.get(0);
    }
	
	public List<Week> getVocabularyData(String filePath) {
		List<Week> lines = new ArrayList<Week>();
		Scanner myReader = null;
		String val = null;
    	try {
    		File myObj = new File(filePath);
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, Week.class));
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
	
	public void writeVocabularyRecordToFile(String filePath, Week week) {
    	try {   
    		Path path = Path.of(filePath);
    		Files.writeString(path, "\n"+gson.toJson(week), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	
	public List<Week> getVocabularyFile(String name) {
		List<Week> dataList = new ArrayList<Week>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "vocabulary_"+name+".xml";
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
		        VocabularySaxParserHandler handler = new VocabularySaxParserHandler();
		        saxParser.parse(fObj, handler);
		        dataList = handler.getWeekList();
				if(dataList.size()>0) {
					groupData(dataList, name);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	public void groupData(List<Week> dataList, String name) {
		Map<Integer, Integer> weekMap = new HashMap<Integer, Integer>();
		Set<Integer> weekSet = new HashSet<Integer>();
		dataList.stream().forEach(week -> {
			if(weekMap.containsKey(week.getId())) {
				weekMap.put(week.getId(), weekMap.get(week.getId())+1);
			} else {
				weekMap.put(week.getId(), 1);
			}
			weekSet.add(week.getId());
		});
		dataList.stream().forEach(week -> {
			week.setTotalWeeks(weekSet.size());
			week.setTotalWords(weekMap.get(week.getId()));
		});
	}
}
