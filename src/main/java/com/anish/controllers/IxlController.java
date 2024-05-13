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
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Question;
import com.anish.data.Word;
import com.anish.data.XLData;
import com.anish.parsers.IXLQuestionSaxParserHandler;
import com.anish.parsers.ReadWordSaxParserHandler;
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
	private static String url = "https://www.ixl.com/";

	@GetMapping(value = "/")
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
		List<Question> dataList = getQuestions(name);
		ModelAndView obj = new ModelAndView("view_tests", HttpStatus.OK);
		List<Question> finalDataList = dataList.stream().filter(x1 -> {
			if (name.equalsIgnoreCase(anish_name)) {
				obj.addObject("other_name", ishant_name.toUpperCase());
				obj.addObject("other_name_key", ishant_name);
				obj.addObject("user_name_key", anish_name);
			} else if (name.equalsIgnoreCase(ishant_name)) {
				obj.addObject("other_name", anish_name.toUpperCase());
				obj.addObject("other_name_key", anish_name);
				obj.addObject("user_name_key", ishant_name);
			}
			return true;
		}).collect(Collectors.toList());
		List<Word> redWordsList = getRedWords();
		if(redWordsList.size()>0) {
			obj.addObject("redWords", redWordsList);
			obj.addObject("redWordsExists", "true");
		}
		obj.addObject("name", name.toUpperCase());
		obj.addObject("data", finalDataList);
		return obj;
	}

	public Map<Integer, XLData> getDataNew(String name) {
		Map<Integer, XLData> finalData = new HashMap<Integer, XLData>();
		String propValue = env.getProperty(name+".datafiles");
		String[] arr = propValue.split("#"); 
		for (int i = 0; i < arr.length; i++) {
			String val = env.getProperty("anish.datafiles."+arr[i]);
			XLData xlData = new XLData();
			xlData.setQuestionId(Integer.parseInt(arr[i]));
			xlData.setQuestionFileName(arr[i]+".xml");
			xlData.setQuestionName(val.trim());
			xlData.setQuestionCompletedFileName(arr[i]+"_done.txt");
			finalData.put(Integer.parseInt(arr[i]), xlData);
		}
		return finalData;
	}
	
	public List<Question> getQuestions(String name) {
		List<Question> dataList = new ArrayList<Question>();
		Map<Integer, XLData> finalData = getDataNew(name);
		finalData.values().forEach(xlData -> {
			try {
				List<Question> tempList = getAllRecordsNew(xlData, name);
				if (!tempList.isEmpty()) {
					Question item = tempList.get(0);
					item.setTotalCurrentQuestions(tempList.size());
					item.setQuestionId(xlData.getQuestionId());
					item.setQuestionName(xlData.getQuestionName());
					dataList.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}
	
	public List<Question> getAllRecordsNew(XLData xlData, String name) {
		List<Question> srcData = getQuestionListByStatusNew(xlData, 1);
		List<String> existingIds = getCompletedDataByName(xlData.getQuestionCompletedFileName(), name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			if (!existingIds.contains(x.getSectionId() + "#" + x.getSubSectionId())) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}
	
	public List<Question> getQuestionListByStatusNew(XLData xlData, int stauts) {
		return getCurrentXmlFile(xlData.getQuestionFileName(), "xmldata").stream().filter(x -> {
			if (x.getQuestionStatus().intValue() == stauts) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	@GetMapping(value = "/tests/getTestLink/{testId}/{name}")
	public ModelAndView getTestLink(@PathVariable("testId") String testId, @PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);
			List<Question> finalLines = getAllRecords(val + "#" + valDone, name, testId);
			if (finalLines.size() > 0) {
				Question item = finalLines.get(0);
				item.setTotalCurrentQuestions(finalLines.size());
				item.setQuestionId(Integer.valueOf(testId));
				String testIdVal = env.getProperty("anish.datafiles."+testId);
				item.setQuestionName(testIdVal.trim());
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
	
	public List<Question> getAllRecords(String fileName, String name, String testId) {
		String[] fileArr = fileName.split("#");
		List<Question> srcData = getQuestionListByStatus(fileName, 1, testId);
		List<String> existingIds = getCompletedDataByName(fileArr[1], name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			if (!existingIds.contains(x.getSectionId() + "#" + x.getSubSectionId())) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}
	
	public String getDoneFile(String testId) {
		return testId+"_done.txt";
	}
	
	public String getXmlFile(String testId) {
		return testId+".xml";
	}
	
	@GetMapping(value = "/tests/nextQuestion/{testId}/{sectionId}/{subSectionId}/{name}")
	public ModelAndView nextQuestion(@PathVariable("testId") String testId, 
			@PathVariable("sectionId") String sectionId, @PathVariable("subSectionId") String subSectionId,
			@PathVariable("name") String name) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);
			System.out.println("val="+val+",valDone="+valDone+",sectionId="+sectionId+",subSectionId="+subSectionId+",name="+name+",testId="+testId);
			Question question = getCurrentQuestionNew(val + "#" + valDone, sectionId, subSectionId, name, testId);
			System.out.println("Selected question=" + question);
			String testIdVal = env.getProperty("anish.datafiles."+testId);
			question.setQuestionId(Integer.valueOf(testId));
			question.setQuestionName(testIdVal.trim());
			writeRecordToFile(val, question, name);

			List<Question> finalLines = getAllRecordsByQuestion(val + "#" + valDone, question, name, testId);
			if (finalLines.size() > 0) {
				Question item = finalLines.get(0);
				item.setTotalCurrentQuestions(finalLines.size());
				item.setQuestionId(Integer.valueOf(testId));
				item.setQuestionName(testIdVal.trim());
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
	
	public Question getCurrentQuestionNew(String fileName, String sectionId, String subSectionId, String name, String testId) {
		Question[] question = new Question[1];
		Question[] x = new Question[1];
		try {
			List<Question> srcData = getQuestionListByStatus(fileName, 1, testId);
			srcData.stream().forEach(x1 -> {
				x[0] = x1;
				if (x[0].getSectionId().equals(sectionId) && x[0].getSubSectionId().equals(subSectionId)) {
					question[0] = x[0];
				}
			});	
		} catch (Exception e) {
			System.out.println("question="+gson.toJson(x[0]));
			e.printStackTrace();
			throw e;
		}
		return question[0];
	}
	
	public List<Question> getAllRecordsByQuestion(String fileName, Question question, String name, String testId) {
		String[] fileArr = fileName.split("#");
		List<Question> srcData = getQuestionListByStatus(fileName, 1, testId);
		List<String> existingIds = getCompletedDataByName(fileArr[1], name);
		List<Question> finalLines = new ArrayList<Question>();
		srcData.stream().forEach(x -> {
			String key = x.getSectionId() + "#" + x.getSubSectionId();
			if (!existingIds.contains(key)) {
				finalLines.add(x);
			}
		});
		return finalLines;
	}
	
	@GetMapping(value = "/tests/disabled/{name}")
	public ModelAndView viewDisabledTests(@PathVariable("name") String name) {
		List<Question> dataList = getDisabledData(name);
		ModelAndView obj = new ModelAndView("view_disabled_tests", HttpStatus.OK);
		obj.addObject("user_name_key", name);
		obj.addObject("data", dataList);
		return obj;
	}
	
	public List<Question> getDisabledData(String name) {
		List<Question> dataList = new ArrayList<Question>();
		Map<Integer, XLData> finalData = getDataNew(name);
		finalData.values().forEach(x -> {
			try {
				Question question = new Question();
				List<Question> tempList = getQuestionListByStatusNew(x, 1);
				List<Question> tempComplexList = getQuestionListByStatusNew(x, 0);
				List<Question> tempCompletedList = getQuestionListByStatusNew(x, 2);
				List<Question> tempPracticeList = getQuestionListByStatusNew(x, 3);
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
				question.setQuestionId(x.getQuestionId());
				question.setQuestionName(x.getQuestionName());
				dataList.add(question);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return dataList;
	}

	@GetMapping(value = "/tests/currentQuestions/{testId}")
	public ModelAndView getCurrentQuestions(@PathVariable("testId") String testId) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 1, testId);
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
	
	public List<Question> getQuestionListByStatus(String fileName, int stauts, String testId) {
		String[] fileArr = fileName.split("#");
		return getCurrentXmlFile(fileArr[0], "xmldata").stream().filter(x -> {
			x.setQuestionId(Integer.parseInt(testId));
			String testIdVal = env.getProperty("anish.datafiles."+testId);
			x.setQuestionName(testIdVal);
			if (x.getQuestionStatus().intValue() == stauts) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
	}
	
	@GetMapping(value = "/tests/complexQuestions/{testId}")
	public ModelAndView getPendingQuestions(@PathVariable("testId") String testId) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 0, testId);
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
	
	@GetMapping(value = "/tests/completedQuestions/{testId}")
	public ModelAndView getCompletedQuestions(@PathVariable("testId") String testId) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 2, testId);
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
	
	@GetMapping(value = "/tests/practiceQuestions/{testId}")
	public ModelAndView getPracticeQuestions(@PathVariable("testId") String testId) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			String val = getXmlFile(testId);
			String valDone = getDoneFile(testId);

			List<Question> tempLines = getQuestionListByStatus(val + "#" + valDone, 3, testId);
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
	
	public List<String> getCompletedDataByName(String fileName, String name) {
		List<String> lines = new ArrayList<String>();
		Scanner myReader = null;
		String val = null;
		try {
			String path = env.getProperty("destination.path");
			String finalPath = path+name+"/done_ixl/" + fileName;
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
	
	public List<Question> getCurrentXmlFile(String fileName, String folderName) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = folderName+"/" + fileName;
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
	
	public void writeRecordToFile(String fileName, Question question, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/done_ixl/" + fileName.replace(".xml", "_done.txt");
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, "\n" + gson.toJson(question), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
									+ item.getGrade() + "/" + item.getUrlKey());
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
	
	public List<Word> getRedWords() {
		List<Word> dataList = new ArrayList<Word>();
		String fileStr = env.getProperty("ishant.redwords.datafiles");
		String[] fileStrArr = fileStr.split("#");
		for(int i=0;i<fileStrArr.length;i++) {
			List<Word> currentList = finalRedWords(fileStrArr[i]);
			if(currentList != null && currentList.size()>0) {
				Word wrd = currentList.get(0);
				wrd.setTotalWords(currentList.size());
				dataList.add(wrd);
			}
		}
		return dataList;
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
	
	public void groupData(List<Word> wordList) {
		wordList.stream().forEach(word -> {
			word.setTotalWords(wordList.size());
		});
	}
	
	public List<Word> getRedWordsCompletedFile(String fileName) {
		List<Word> dataList = new ArrayList<Word>();
		Scanner myReader = null;
		String val = null;
		try {
			String path = env.getProperty("destination.path");
			String doneFilePath = path+ishant_name+"/done/"+fileName+"_done.txt";
			File myObj = new File(doneFilePath);
			myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				val = myReader.nextLine();
				if (val != null && val.length() > 10) {
					Word q = gson.fromJson(val, Word.class);
					dataList.add(q);
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
		return dataList;
	}
	
	public List<Word> finalRedWords(String fileName) {
		List<Word> dataList = new ArrayList<Word>();
		try {
			List<Word> completedRedWrods = getRedWordsCompletedFile(fileName);
			List<Integer> completedIds = new ArrayList<Integer>();
			for(Word wrd: completedRedWrods) {
				completedIds.add(wrd.getId());
			}
			List<Word> redWrods = getRedWordsFile(fileName);
			for(Word wrd: redWrods) {
				if(!completedIds.contains(wrd.getId())) {
					dataList.add(wrd);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
}