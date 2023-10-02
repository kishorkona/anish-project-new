package com.anish.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Week;
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
}
