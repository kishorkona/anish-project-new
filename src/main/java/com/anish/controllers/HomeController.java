package com.anish.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Question;
import com.google.gson.Gson;

@Controller
public class HomeController {
	
	@Autowired private MyHelper helper;
	private Gson gson = new Gson();

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homepage() {
		ModelAndView model=new ModelAndView("loginpage");
		return model;
	}
	
	@GetMapping(value = "/tests/view")
    public ModelAndView viewTests() {	
		List<Question> dataList = helper.getData();
		ModelAndView obj = new ModelAndView("view_tests", HttpStatus.OK);
		obj.addObject("data", dataList);
		return obj;
    }
	
	@GetMapping(value = "/tests/disabled")
    public ModelAndView viewDisabledTests() {	
		List<Question> dataList = helper.getDisabledData();
		ModelAndView obj = new ModelAndView("view_disabled_tests", HttpStatus.OK);
		obj.addObject("data", dataList);
		return obj;
    }

	@GetMapping(value = "/tests/getTestLink/{subject}/{grade}")
    public ModelAndView getTestLink(@PathVariable("subject") String subject, @PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = helper.getPathMapper(subject, grade, false);
			String valDone = helper.getPathMapper(subject, grade, true);
			
			List<Question> finalLines = helper.getAllRecords(val+"#"+valDone);
			if(finalLines.size()>0) {
				Question item = finalLines.get(0);
	    		item.setTotalCurrentQuestions(finalLines.size());
	    		modelAndView.addObject("data", item);
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("ixl_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	@GetMapping(value = "/tests/complexQuestions/{subject}/{grade}")
    public ModelAndView getPendingQuestions(@PathVariable("subject") String subject, @PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = helper.getPathMapper(subject, grade, false);
			String valDone = helper.getPathMapper(subject, grade, true);
			
			List<Question> tempLines = helper.getQuestionListByStatus(val+"#"+valDone, 0);
			Map<String, List<Question>> groupBySubject = tempLines.stream().collect(Collectors.groupingBy(Question::getSectionId));
			List<Question> finalLines = new ArrayList<Question>();
			groupBySubject.entrySet().stream().forEach(x -> {
				x.getValue().stream().forEach(y -> {
					finalLines.add(y);
				});
			});;
			if(finalLines.size()>0) {
	    		modelAndView.addObject("data", finalLines);
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("complex_questions");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	@GetMapping(value = "/tests/completedQuestions/{subject}/{grade}")
    public ModelAndView getCompletedQuestions(@PathVariable("subject") String subject, @PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = helper.getPathMapper(subject, grade, false);
			String valDone = helper.getPathMapper(subject, grade, true);
			
			List<Question> tempLines = helper.getQuestionListByStatus(val+"#"+valDone, 2);
			Map<String, List<Question>> groupBySubject = tempLines.stream().collect(Collectors.groupingBy(Question::getSectionId));
			List<Question> finalLines = new ArrayList<Question>();
			groupBySubject.entrySet().stream().forEach(x -> {
				x.getValue().stream().forEach(y -> {
					finalLines.add(y);
				});
			});;
			if(finalLines.size()>0) {
	    		modelAndView.addObject("data", finalLines);
	    	}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("completed_questions");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	
	@GetMapping(value = "/tests/nextQuestion/{subject}/{grade}/{sectionId}/{subSectionId}")
	public ModelAndView nextQuestion(@PathVariable("subject") String subject, 
			@PathVariable("grade") String grade, @PathVariable("sectionId") String sectionId, 
			@PathVariable("subSectionId") String subSectionId) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = helper.getPathMapper(subject, grade, false);
			String valDone = helper.getPathMapper(subject, grade, true);
			
			Question question = helper.getCurrentQuestion(val+"#"+valDone, sectionId, subSectionId);
			System.out.println("Selected question="+question);
			helper.writeRecordToFile(val, question);
			
			List<Question> finalLines = helper.getAllRecords(val+"#"+valDone, question);
			if(finalLines.size()>0) {
				Question item = finalLines.get(0);
	    		item.setTotalCurrentQuestions(finalLines.size());
	    		System.out.println("Next Record="+gson.toJson(item));
	    		modelAndView.addObject("data", item);
	    	}			
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("ixl_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    }
	/*
	@GetMapping(value = "/tests/create")
    public ModelAndView home() {
		ModelAndView obj = new ModelAndView("create_tests", HttpStatus.OK);
		return obj;
    }
	
	@PostMapping(value = "/tests/build")
    public ModelAndView createTest(@ModelAttribute("subject") String subject, @ModelAttribute("grade") String grade) {
		helper.createTest(subject, Integer.parseInt(grade));
        return viewTests();
    }
	*/
}