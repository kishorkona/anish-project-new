package com.anish.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Question;
import com.anish.parsers.IXLQuestionSaxParserHandler;
import com.google.gson.Gson;

@Controller
public class IxlController {
	
	private List<Integer> anishGrade = Arrays.asList("4,5".split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	private List<Integer> ishantGrade = Arrays.asList("1,2".split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	private String anish_name = "anish";
	private String ishant_name = "ishant";
	
	@Autowired private Environment env;
	private Gson gson = new Gson();
	public static String destPath = "ixl/";
	private static String url = "https://www.ixl.com/";


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homepage() {
		ModelAndView model=new ModelAndView("loginpage");
		return model;
	}
	
	@GetMapping(value = "/tests/all")
    public ModelAndView viewAllTests() {	
		ModelAndView obj = new ModelAndView("all_topics", HttpStatus.OK);
		return obj;
    }
	
	@GetMapping(value = "/tests/view/{name}")
    public ModelAndView viewTests(@PathVariable("name") String name) {	
		List<Question> dataList = getData();
		ModelAndView obj = new ModelAndView("view_tests", HttpStatus.OK);
		List<Question> finalDataList = dataList.stream().filter(x -> {
			if(name.equalsIgnoreCase(anish_name)) {
				obj.addObject("other_name", ishant_name.toUpperCase());
				obj.addObject("other_name_key", ishant_name);
				boolean flag = anishGrade.contains(Integer.parseInt(x.getGrade()));
				if(flag) {
					return true;
				}
				return false;
			} else if(name.equalsIgnoreCase(ishant_name)) {
				obj.addObject("other_name", anish_name.toUpperCase());
				obj.addObject("other_name_key", anish_name);
				boolean flag = ishantGrade.contains(Integer.parseInt(x.getGrade()));
				if(flag) {
					return true;
				}
				return false;
			}
			return false;
		}).collect(Collectors.toList());
		obj.addObject("name", name.toUpperCase());
		obj.addObject("data", finalDataList);
		return obj;
    }
	
	@GetMapping(value = "/tests/disabled")
    public ModelAndView viewDisabledTests() {	
		List<Question> dataList = getDisabledData();
		ModelAndView obj = new ModelAndView("view_disabled_tests", HttpStatus.OK);
		obj.addObject("data", dataList);
		return obj;
    }

	@GetMapping(value = "/tests/getTestLink/{subject}/{grade}")
    public ModelAndView getTestLink(@PathVariable("subject") String subject, @PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);
			
			List<Question> finalLines = getAllRecords(val+"#"+valDone);
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
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);
			
			List<Question> tempLines = getQuestionListByStatus(val+"#"+valDone, 0);
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
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);
			
			List<Question> tempLines = getQuestionListByStatus(val+"#"+valDone, 2);
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
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);
			
			Question question = getCurrentQuestion(val+"#"+valDone, sectionId, subSectionId);
			System.out.println("Selected question="+question);
			writeRecordToFile(val, question);
			
			List<Question> finalLines = getAllRecords(val+"#"+valDone, question);
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
	
	public List<Question> getDataXml() {
		Integer valCnt = Integer.valueOf(env.getProperty("read.datafiles.names.count"));
		List<String> filesList = new ArrayList<String>();
		for (int i=1;i<= valCnt;i++) {
			String val = env.getProperty("read.datafiles.names."+i);
			filesList.add(val);
		}
		List<Question> dataList = new ArrayList<Question>();
		filesList.forEach(x -> {
			try {
				List<Question> tempList = getAllRecords(x);
				if(!tempList.isEmpty()) {
					Question item = tempList.get(0);
					item.setTotalCurrentQuestions(tempList.size());
					dataList.add(item);
				}
	    	} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}
	
	public List<Question> getData() {
		Integer valCnt = Integer.valueOf(env.getProperty("read.datafiles.names.count"));
		List<String> filesList = new ArrayList<String>();
		for (int i=1;i<= valCnt;i++) {
			String val = env.getProperty("read.datafiles.names."+i);
			filesList.add(val);
		}
		List<Question> dataList = new ArrayList<Question>();
		filesList.forEach(x -> {
			try {
				List<Question> tempList = getAllRecords(x);
				if(!tempList.isEmpty()) {
					Question item = tempList.get(0);
					item.setTotalCurrentQuestions(tempList.size());
					dataList.add(item);
				}
	    	} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}
	
	public List<Question> getAllRecords(String fileName) {
		String[] fileArr = fileName.split("#");
    	List<Question> srcData = getQuestionListByStatus(fileName, 1);   	
    	List<String> existingIds = getCompletedData(fileArr[1], destPath);
    	List<Question> finalLines = new ArrayList<Question>();
    	srcData.stream().forEach(x -> {
    		if(!existingIds.contains(x.getSectionId()+"#"+x.getSubSectionId())) {
    			finalLines.add(x);
    		}
    	});
    	return finalLines;
    }
	
	public List<Question> getQuestionListByStatus(String fileName, int stauts) {
		String[] fileArr = fileName.split("#");
		return getCurrentXmlFile(fileArr[0]).stream().filter(x -> {
			if(x.getQuestionStatus().intValue()==stauts) {
				return true;
			}
    		return false;
    	}).collect(Collectors.toList());
	}
	
	public List<String> getCompletedData(String fileName, String subPath) {
		List<String> lines = new ArrayList<String>();
		Scanner myReader = null;
		String val = null;
    	try {
			String path = env.getProperty("destination.path");
    		File myObj = new File(path+subPath+fileName);
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(val!=null && val.length()>10) {
    				Question q = gson.fromJson(val, Question.class);
        			lines.add(q.getSectionId()+"#"+q.getSubSectionId());
    			}
    		}
    	} catch (Exception e) {
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
	public List<Question> getCurrentXmlFile(String fileName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/"+fileName;
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
		        IXLQuestionSaxParserHandler handler = new IXLQuestionSaxParserHandler();
		        saxParser.parse(fObj, handler);
		        List<Question> dataList = handler.getQuestionList();
				if(dataList.size()>0) {
					return sortItems(dataList, dataList.size());
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList();
	}
	
	private List<Question> sortItems(List<Question> dataList, Integer totalQuestions) {
		List<Question> finalItems = new ArrayList<Question>();
		List<Integer> excludeList = new ArrayList<Integer>();
		Map<String, List<Question>> groupBySubject = dataList.stream().collect(Collectors.groupingBy(Question::getSectionId));
		boolean flag = true;
		while(flag) {
			for(Entry<String, List<Question>> entry: groupBySubject.entrySet()) {
				for(Question item: entry.getValue()) {
					if(finalItems.size()==dataList.size()) {
						flag = false;
						break;
					} else {
						if(!excludeList.contains(item.getId())) {
							//item.setTotalRows(totalQuestions);
							item.setUrl(url+getPathSubject(item.getSubject()).toLowerCase()+"/grade-"+item.getGrade()+"/"+item.getSubSectionName());
							finalItems.add(item);
							excludeList.add(item.getId());
							break;
						}
					}
				}
				if(finalItems.size()==dataList.size()) {
					flag = false;
					break;
				}
			}
		}
		return finalItems;
	}
	public String getPathSubject(String subject) {
		if(subject.equalsIgnoreCase("language arts")) {
			return "ela";
		} else if(subject.equalsIgnoreCase("math")) {
			return "math";
		}
		return null;
	}
	public String getPathMapper(String subject, String grade, boolean done) {
		if(subject.equalsIgnoreCase("language arts")) {
			if(done) {
				return "ela"+"_"+grade+"_done.txt";
			}
			return "ela"+"_"+grade+".xml";
		} else if(subject.equalsIgnoreCase("math")) {
			if(done) {
				return "math"+"_"+grade+"_done.txt";
			}
			return "math"+"_"+grade+".xml";
		}
		return null;
	}
	
	public Question getCurrentQuestion(String fileName, String sectionId, String subSectionId) {
    	List<Question> srcData = getQuestionListByStatus(fileName, 1);
    	Question[] question = new Question[1];
    	srcData.stream().forEach(x -> {
    		if(x.getSectionId().equals(sectionId) && x.getSubSectionId().equals(subSectionId)) {
    			question[0] = x;
    		}
    	});
    	return question[0];
	}
	
	public List<Question> getAllRecords(String fileName, Question question) {
		String[] fileArr = fileName.split("#");
    	List<Question> srcData = getQuestionListByStatus(fileName, 1);
    	List<String> existingIds = getCompletedData(fileArr[1], destPath);
    	List<Question> finalLines = new ArrayList<Question>();
    	srcData.stream().forEach(x -> {
    		String key = x.getSectionId()+"#"+x.getSubSectionId();
    		if(!existingIds.contains(key)) {
    			finalLines.add(x);
    		}
    	});
    	return finalLines;
	}
	public void writeRecordToFile(String fileName, Question question) {
    	try {   
    		String path = env.getProperty("destination.path");
    		String finalFilePath = path+destPath+fileName.replace(".xml", "_done.txt");
    		Path filePath = Path.of(finalFilePath);
    		Files.writeString(filePath, "\n"+gson.toJson(question), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
	public List<Question> getDisabledData() {
		Integer valCnt = Integer.valueOf(env.getProperty("read.datafiles.names.count"));
		List<String> filesList = new ArrayList<String>();
		for (int i=1;i<= valCnt;i++) {
			String val = env.getProperty("read.datafiles.names."+i);
			filesList.add(val);
		}
		List<Question> dataList = new ArrayList<Question>();
		filesList.forEach(x -> {
			try {
				Question question = new Question();
				
				List<Question> tempList = getQuestionListByStatus(x, 1);
				List<Question> tempComplexList = getQuestionListByStatus(x, 0);
				List<Question> tempCompletedList = getQuestionListByStatus(x, 2);
				if(!tempComplexList.isEmpty()) {
					question = tempComplexList.get(0);
				} else if(!tempCompletedList.isEmpty()) {
					question = tempCompletedList.get(0);
				} else if(!tempList.isEmpty()) {
					question = tempList.get(0);
				}
				if(!tempComplexList.isEmpty()) {
					question.setTotalComplexQuestions(tempComplexList.size());
				}
				if(!tempCompletedList.isEmpty()) {
					question.setTotalCompletedQuestions(tempCompletedList.size());
				}
				if(!tempList.isEmpty()) {
					question.setTotalCurrentQuestions(tempList.size());
				}
				dataList.add(question);
	    	} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}
	
}