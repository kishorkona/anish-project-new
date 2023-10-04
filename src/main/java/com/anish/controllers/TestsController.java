package com.anish.controllers;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.anish.entities.Subjects;
import com.anish.entities.SubjectsDetailsByDate;
import com.anish.entities.data.TestsGroupedData;
import com.anish.service.AnishService;
import com.anish.view.EntryView;
import com.anish.view.SessionView;
import com.anish.view.TestsView;
import com.anish.view.UserView;

@RestController
public class TestsController {

	@Autowired AnishService anishService;
	@Autowired HttpSession httpSession;
	
	@GetMapping(value = "/tests/view")
    public ModelAndView viewTests() {
		List<TestsGroupedData> data = anishService.listOfDates();
		ModelAndView obj = new ModelAndView("view_tests", HttpStatus.OK);
		obj.addObject("data", data);
		return obj;
    }
	
	@GetMapping(value = "/tests/create")
    public ModelAndView home() {
		ModelAndView obj = new ModelAndView("create_tests", HttpStatus.OK);
		return obj;
    } 
	
	@PostMapping(value = "/tests/build")
    public ModelAndView createTest(@ModelAttribute("entrydate") String entrydate, 
    		@ModelAttribute("grade") String grade) {
		String responseData = anishService.createTest(LocalDate.parse(entrydate), Integer.parseInt(grade));
		ModelAndView mv = this.viewTests();
		if(!responseData.equalsIgnoreCase("Success")) {
			mv.addObject("error", responseData);
		}
        return mv;
    } 
	
	@GetMapping(value = "/localDate")
    public ResponseEntity<String> getLocalDate() {
    	return new ResponseEntity<>(LocalDate.now().toString(), HttpStatus.OK);
    } 
	
	@GetMapping(value = "/tests/getTestLink/{testDate}")
    public ModelAndView getTestLink(@PathVariable("testDate") String testDate) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			SubjectsDetailsByDate obj = anishService.getTestLink(LocalDate.parse(testDate));
			Subjects subj = anishService.getSubject(obj.getCode());
			
			TestsView testView = new TestsView();
			testView.setId(obj.getSubjectsDetailsByDatePk().getId());
			testView.setTestDate(obj.getSubjectsDetailsByDatePk().getTest_date());
			testView.setUrl(obj.getUrl());
			testView.setCode(obj.getCode());
			
			modelAndView.addObject("data", testView);
			modelAndView.addObject("subjectName", subj.getName());
			modelAndView.addObject("gradeId", subj.getGradeId());
			modelAndView.addObject("detailId", anishService.getDetailId(obj.getDetailId()));
			modelAndView.addObject("totalQuestions", anishService.getTotalRows(LocalDate.parse(testDate)));
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("ixl_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    } 
	
	@GetMapping(value = "/tests/nextQuestion/{testDate}/{id}")
	public ModelAndView nextQuestion(@PathVariable("testDate") String testDate, @PathVariable("id") int id) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			anishService.updateCurrentQuestion(id, LocalDate.parse(testDate));
			SubjectsDetailsByDate obj = anishService.getTestLink(LocalDate.parse(testDate));
			Subjects subj = anishService.getSubject(obj.getCode());
			
			TestsView testView = new TestsView();
			testView.setId(obj.getSubjectsDetailsByDatePk().getId());
			testView.setTestDate(obj.getSubjectsDetailsByDatePk().getTest_date());
			testView.setUrl(obj.getUrl());
			testView.setCode(obj.getCode());
			
			modelAndView.addObject("data", testView);
			modelAndView.addObject("subjectName", subj.getName());
			modelAndView.addObject("gradeId", subj.getGradeId());
			modelAndView.addObject("detailId", anishService.getDetailId(obj.getDetailId()));
			modelAndView.addObject("totalQuestions", anishService.getTotalRows(LocalDate.parse(testDate)));
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("ixl_test");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    	return modelAndView;
    } 
	
	private SessionView getSessionView() {
		return (SessionView) httpSession.getAttribute("user");
	}
	
	@PostMapping(value = "/usingRequestBody")
	public String usingRequestBody(@RequestBody UserView user) {
		System.out.println(user);
		return "Success";
	}
}
