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
import com.anish.parsers.VocabularySaxParserHandler;
import com.google.gson.Gson;

@Controller
public class VocabularyController {
	
	@Autowired private Environment env;
	@Autowired private MyHelper helper;
	private Gson gson = new Gson();
	
	@GetMapping(value = "/vocabulory/takeTest/{name}")
    public ModelAndView getTakeTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			Week week = helper.getAllVocabularyRecords(name, false, 0, 0);
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
			Week week = helper.getAllVocabularyRecords(name, true, Integer.valueOf(weekId), Integer.valueOf(weekNo));
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
	
	@RequestMapping(value = "/vocabulory/create", method = RequestMethod.GET)
	public String createVocabulary() {
		if(createVocabularyFile("anish") && createVocabularyFile("ishant")) {
			return "done";
		}
		return "error";
	}
	
	public boolean createVocabularyFile(String name) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "vocabulary.xml";
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
		        VocabularySaxParserHandler handler = new VocabularySaxParserHandler();
		        saxParser.parse(fObj, handler);
		        List<Week> dataList = handler.getWeekList();
				if(dataList.size()>0) {
					List<Week> buildData = groupData(dataList, name);
					return createFinalFile(name, buildData);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Week> groupData(List<Week> dataList, String name) {
		List<Week> list = new ArrayList<Week>();
		Map<Integer, Integer> weekMap = new HashMap<Integer, Integer>();
		Set<Integer> weekSet = new HashSet<Integer>();
		dataList.stream().forEach(week -> {
			List<String> lst = Arrays.asList(week.getName().split(","));
			if(lst.contains(name)) {
				week.setName(name);
				list.add(week);
			}
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
		return list;
	}
	
	public boolean createFinalFile(String name, List<Week> data) {
		BufferedWriter writer = null;
    	try {   
    		String path = env.getProperty("base.path");
    		String finalFilePath = path+name+"_vocabulary.txt";
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
