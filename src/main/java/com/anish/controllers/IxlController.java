package com.anish.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
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

	private List<Integer> anishGrade = Arrays.asList("4,5".split(",")).stream().map(x -> Integer.parseInt(x))
			.collect(Collectors.toList());
	private List<Integer> ishantGrade = Arrays.asList("1,2".split(",")).stream().map(x -> Integer.parseInt(x))
			.collect(Collectors.toList());
	private String anish_name = "anish";
	private String ishant_name = "ishant";

	@Autowired
	private Environment env;
	private Gson gson = new Gson();
	public static String destPath = "ixl/";
	private static String url = "https://www.ixl.com/";

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homepage() {
		ModelAndView model = new ModelAndView("loginpage");
		return model;
	}

	@GetMapping(value = "/tests/all")
	public ModelAndView viewAllTests() {
		ModelAndView obj = new ModelAndView("all_topics", HttpStatus.OK);
		return obj;
	}

	@GetMapping(value = "/tests/view/{name}")
	public ModelAndView viewTests(@PathVariable("name") String name) {
		List<Question> dataList = getData(name);
		ModelAndView obj = new ModelAndView("view_tests", HttpStatus.OK);
		List<Question> finalDataList = dataList.stream().filter(x -> {
			if (name.equalsIgnoreCase(anish_name)) {
				obj.addObject("other_name", ishant_name.toUpperCase());
				obj.addObject("other_name_key", ishant_name);
				obj.addObject("user_name_key", anish_name);
				boolean flag = anishGrade.contains(Integer.parseInt(x.getGrade()));
				if (flag) {
					return true;
				}
				return false;
			} else if (name.equalsIgnoreCase(ishant_name)) {
				obj.addObject("other_name", anish_name.toUpperCase());
				obj.addObject("other_name_key", anish_name);
				obj.addObject("user_name_key", ishant_name);
				boolean flag = ishantGrade.contains(Integer.parseInt(x.getGrade()));
				if (flag) {
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

	@GetMapping(value = "/tests/disabled/{name}")
	public ModelAndView viewDisabledTests(@PathVariable("name") String name) {
		List<Question> dataList = getDisabledData(name);
		ModelAndView obj = new ModelAndView("view_disabled_tests", HttpStatus.OK);
		obj.addObject("user_name_key", name);
		obj.addObject("data", dataList);
		return obj;
	}

	@GetMapping(value = "/tests/getTestLink/{subject}/{grade}/{name}")
	public ModelAndView getTestLink(@PathVariable("subject") String subject, 
			@PathVariable("grade") String grade, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);
			List<Question> finalLines = getAllRecords(val + "#" + valDone, name);
			if (finalLines.size() > 0) {
				Question item = finalLines.get(0);
				item.setTotalCurrentQuestions(finalLines.size());
				modelAndView.addObject("data", item);
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("ixl_test");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}

	@GetMapping(value = "/tests/complexQuestions/{subject}/{grade}")
	public ModelAndView getPendingQuestions(@PathVariable("subject") String subject,
			@PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 0);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				modelAndView.addObject("data", finalLines);
			}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("complex_questions");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	private Function<List<Question>, List<Question>> sortData = (List<Question> data) -> {
		Map<String, List<Question>> groupBySubject = data.stream()
				.collect(Collectors.groupingBy(Question::getSectionId));
		List<String> sortedQuestions = groupBySubject.keySet().stream().sorted().collect(Collectors.toList());
		Map<String, List<Question>> finalListData = new HashMap<String, List<Question>>();
		for(String key: sortedQuestions) {
			finalListData.put(key, groupBySubject.get(key));
		}
		List<Question> finalLines = new ArrayList<Question>();
		finalListData.entrySet().stream().forEach(x -> {
			x.getValue().stream().forEach(y -> {
				finalLines.add(y);
			});
		});
		return finalLines;
	};

	@GetMapping(value = "/tests/completedQuestions/{subject}/{grade}")
	public ModelAndView getCompletedQuestions(@PathVariable("subject") String subject,
			@PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 2);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				modelAndView.addObject("data", finalLines);
			}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("completed_questions");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	@GetMapping(value = "/tests/practiceQuestions/{subject}/{grade}")
	public ModelAndView getPracticeQuestions(@PathVariable("subject") String subject,
			@PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 3);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				modelAndView.addObject("data", finalLines);
			}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("practice_questions");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	
	@GetMapping(value = "/tests/currentQuestions/{subject}/{grade}")
	public ModelAndView getCurrentQuestions(@PathVariable("subject") String subject,
			@PathVariable("grade") String grade) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 1);
			List<Question> finalLines = sortData.apply(tempLines);
			if (finalLines.size() > 0) {
				modelAndView.addObject("data", finalLines);
			}
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("completed_questions");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}

	@GetMapping(value = "/tests/nextQuestion/{subject}/{grade}/{sectionId}/{subSectionId}/{name}")
	public ModelAndView nextQuestion(@PathVariable("subject") String subject, @PathVariable("grade") String grade,
			@PathVariable("sectionId") String sectionId, @PathVariable("subSectionId") String subSectionId,
			@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getPathMapper(subject, grade, false);
			String valDone = getPathMapper(subject, grade, true);

			Question question = getCurrentQuestionNew(val + "#" + valDone, sectionId, subSectionId, name);
			System.out.println("Selected question=" + question);
			writeRecordToFile(val, question, name);

			List<Question> finalLines = getAllRecordsByQuestion(val + "#" + valDone, question, name);
			if (finalLines.size() > 0) {
				Question item = finalLines.get(0);
				item.setTotalCurrentQuestions(finalLines.size());
				System.out.println("Next Record=" + gson.toJson(item));
				modelAndView.addObject("data", item);
			}
			modelAndView.addObject("user_name_key", name);
			modelAndView.setStatus(HttpStatus.OK);
			modelAndView.setViewName("ixl_test");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return modelAndView;
	}
	/*
	 * @GetMapping(value = "/tests/create") public ModelAndView home() {
	 * ModelAndView obj = new ModelAndView("create_tests", HttpStatus.OK); return
	 * obj; }
	 * 
	 * @PostMapping(value = "/tests/build") public ModelAndView
	 * createTest(@ModelAttribute("subject") String
	 * subject, @ModelAttribute("grade") String grade) { helper.createTest(subject,
	 * Integer.parseInt(grade)); return viewTests(); }
	 */

	public List<Question> getDataXml() {
		Integer valCnt = Integer.valueOf(env.getProperty("read.datafiles.names.count"));
		List<String> filesList = new ArrayList<String>();
		for (int i = 1; i <= valCnt; i++) {
			String val = env.getProperty("read.datafiles.names." + i);
			filesList.add(val);
		}
		List<Question> dataList = new ArrayList<Question>();
		filesList.forEach(x -> {
			try {
				List<Question> tempList = getAllRecords(x, null);
				if (!tempList.isEmpty()) {
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

	public List<Question> getData(String name) {
		Integer valCnt = Integer.valueOf(env.getProperty("read.datafiles.names."+name+".count"));
		List<String> filesList = new ArrayList<String>();
		for (int i = 1; i <= valCnt; i++) {
			String val = env.getProperty("read.datafiles.names."+name+"."+i);
			filesList.add(val);
		}
		List<Question> dataList = new ArrayList<Question>();
		filesList.forEach(x -> {
			try {
				List<Question> tempList = getAllRecords(x, name);
				if (!tempList.isEmpty()) {
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

	public List<Question> getAllRecords(String fileName, String name) {
		String[] fileArr = fileName.split("#");
		List<Question> srcData = getQuestionListByStatus(fileName, 1);
		List<String> existingIds = getCompletedDataByName(fileArr[1], destPath, name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			if (!existingIds.contains(x.getSectionId() + "#" + x.getSubSectionId())) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}

	public List<Question> getQuestionListByStatus(String fileName, int stauts) {
		String[] fileArr = fileName.split("#");
		return getCurrentXmlFile(fileArr[0]).stream().filter(x -> {
			if (x.getQuestionStatus().intValue() == stauts) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}

	public List<String> getCompletedDataByName(String fileName, String subPath, String name) {
		List<String> lines = new ArrayList<String>();
		Scanner myReader = null;
		String val = null;
		try {
			String path = env.getProperty("destination.path");
			String finalPath = path+name+"/" + subPath + fileName;
			File myObj = new File(finalPath);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				val = myReader.nextLine();
				if (val != null && val.length() > 10) {
					Question q = gson.fromJson(val, Question.class);
					lines.add(q.getSectionId() + "#" + q.getSubSectionId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (myReader != null) {
					myReader.close();
				}
			} finally {
			}
		}
		return lines;
	}

	public List<Question> getCurrentXmlFile(String fileName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/" + fileName;
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if (fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				IXLQuestionSaxParserHandler handler = new IXLQuestionSaxParserHandler();
				saxParser.parse(fObj, handler);
				List<Question> dataList = handler.getQuestionList();
				if (dataList.size() > 0) {
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
		Map<String, List<Question>> groupBySubject = dataList.stream()
				.collect(Collectors.groupingBy(Question::getSectionId));
		boolean flag = true;
		while (flag) {
			for (Entry<String, List<Question>> entry : groupBySubject.entrySet()) {
				for (Question item : entry.getValue()) {
					if (finalItems.size() == dataList.size()) {
						flag = false;
						break;
					} else {
						if (!excludeList.contains(item.getId())) {
							// item.setTotalRows(totalQuestions);
							item.setUrl(url + getPathSubject(item.getSubject()).toLowerCase() + "/grade-"
									+ item.getGrade() + "/" + item.getSubSectionName());
							finalItems.add(item);
							excludeList.add(item.getId());
							break;
						}
					}
				}
				if (finalItems.size() == dataList.size()) {
					flag = false;
					break;
				}
			}
		}
		return finalItems;
	}

	public String getPathSubject(String subject) {
		if (subject.equalsIgnoreCase("language arts")) {
			return "ela";
		} else if (subject.equalsIgnoreCase("math")) {
			return "math";
		}
		return null;
	}

	public String getPathMapper(String subject, String grade, boolean done) {
		if (subject.equalsIgnoreCase("language arts")) {
			if (done) {
				return "ela" + "_" + grade + "_done.txt";
			}
			return "ela" + "_" + grade + ".xml";
		} else if (subject.equalsIgnoreCase("math")) {
			if (done) {
				return "math" + "_" + grade + "_done.txt";
			}
			return "math" + "_" + grade + ".xml";
		}
		return null;
	}

	public Question getCurrentQuestionNew(String fileName, String sectionId, String subSectionId, String name) {
		List<Question> srcData = getQuestionListByStatus(fileName, 1);
		Question[] question = new Question[1];
		srcData.stream().forEach(x -> {
			if (x.getSectionId().equals(sectionId) && x.getSubSectionId().equals(subSectionId)) {
				question[0] = x;
			}
		});
		return question[0];
	}

	public List<Question> getAllRecordsByQuestion(String fileName, Question question, String name) {
		String[] fileArr = fileName.split("#");
		List<Question> srcData = getQuestionListByStatus(fileName, 1);
		List<String> existingIds = getCompletedDataByName(fileArr[1], destPath, name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			String key = x.getSectionId() + "#" + x.getSubSectionId();
			if (!existingIds.contains(key)) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}

	public void writeRecordToFile(String fileName, Question question, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/" + destPath + fileName.replace(".xml", "_done.txt");
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, "\n" + gson.toJson(question), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Question> getDisabledData(String name) {
		Integer valCnt = Integer.valueOf(env.getProperty("read.datafiles.names."+name+".count"));
		List<String> filesList = new ArrayList<String>();
		for (int i = 1; i <= valCnt; i++) {
			String val = env.getProperty("read.datafiles.names."+name+"."+i);
			filesList.add(val);
		}
		List<Question> dataList = new ArrayList<Question>();
		filesList.forEach(x -> {
			try {
				Question question = new Question();

				List<Question> tempList = getQuestionListByStatus(x, 1);
				List<Question> tempComplexList = getQuestionListByStatus(x, 0);
				List<Question> tempCompletedList = getQuestionListByStatus(x, 2);
				List<Question> tempPracticeList = getQuestionListByStatus(x, 3);
				if (!tempComplexList.isEmpty()) {
					question = tempComplexList.get(0);
				} else if (!tempCompletedList.isEmpty()) {
					question = tempCompletedList.get(0);
				} else if (!tempList.isEmpty()) {
					question = tempList.get(0);
				}
				if (!tempComplexList.isEmpty()) {
					question.setTotalComplexQuestions(tempComplexList.size());
				}
				if (!tempCompletedList.isEmpty()) {
					question.setTotalCompletedQuestions(tempCompletedList.size());
				}
				if (!tempList.isEmpty()) {
					question.setTotalCurrentQuestions(tempList.size());
				}
				if (!tempPracticeList.isEmpty()) {
					question.setTotalPracticeQuestions(tempPracticeList.size());
				}
				dataList.add(question);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}

}