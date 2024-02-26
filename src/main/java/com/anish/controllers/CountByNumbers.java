package com.anish.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Word;
import com.google.gson.Gson;

@Controller
public class CountByNumbers {
	
	@Autowired private Environment env;
	private Gson gson = new Gson();
	int fromMin=1;
	int fromMax=5;
	int toMin=2;
	int toMax=20;
	Map<Integer, Boolean> fromMap = new HashMap<>();
	Map<Integer, Boolean> toMap = new HashMap<>();
	
	@GetMapping(value = "/countBy/takeTest/{name}")
    public ModelAndView getTakeTest(@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			int fromRandomNum = fromMin + (int)(Math.random() * ((fromMax - fromMin) + 1));
			int toRandomNum = toMin + (int)(Math.random() * ((toMax - toMin) + 1));
			//if(!fromMap.containsKey(fromRandomNum)) {
				modelAndView.addObject("startNo", fromRandomNum);
			//}
			//if(!toMap.containsKey(toRandomNum)) {
				modelAndView.addObject("endNo", toRandomNum);
			//}
			modelAndView.addObject("user_name_key", name);
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("count_by_numbers");
			
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
}
