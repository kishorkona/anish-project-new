package com.anish.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.anish.data.Question;
import com.anish.data.Week;
import com.anish.data.Word;
import com.anish.parsers.IXLQuestionSaxParserHandler;
import com.google.gson.Gson;

@Component
public class MyHelper {

	@Autowired private Environment env;
	private Gson gson = new Gson();
	
	public static String srcPath = "ixl/source/";
	public static String destPath = "ixl/dest/";
	private static String url = "https://www.ixl.com/";
	
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
				List<Question> tempList = getAllRecords(x);
				List<Question> tempComplexList = getQuestionListByStatus(x, 0);
				List<Question> tempCompletedList = getQuestionListByStatus(x, 2);
				if(!tempList.isEmpty()) {
					Question item = tempList.get(0);
					item.setTotalCompletedQuestions(tempCompletedList.size());
					item.setTotalComplexQuestions(tempComplexList.size());
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
	
	public boolean checkExists(String sectionId, String subSectionId, Question question) {
		if(question.getSectionId().equals(sectionId) && question.getSubSectionId().equals(subSectionId)) { 
			return true;
		} else {
			return false;
		}
	}
	
	public List<Question> getQuestionListByStatus(String fileName, int stauts) {
		String[] fileArr = fileName.split("#");
		return getSourceData(fileArr[0], srcPath).stream().filter(x -> {
			if(x.getQuestionStatus().intValue()==stauts) {
				return true;
			}
    		return false;
    	}).collect(Collectors.toList());
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
    
	public List<String> getCompletedData(String fileName, String subPath) {
		List<String> lines = new ArrayList<String>();
		Scanner myReader = null;
    	try {
    		String path = env.getProperty("destination.path");
    		File myObj = new File(path+subPath+fileName);
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			String val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
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
	public List<Question> getSourceData(String fileName, String subPath) {
		List<Question> lines = new ArrayList<Question>();
		Scanner myReader = null;
		String val = null;
    	try {
    		String path = env.getProperty("base.path");
    		File myObj = new File(path+subPath+fileName);
    		myReader = new Scanner(myObj);
    		while (myReader.hasNextLine()) {
    			val = myReader.nextLine();
    			if(!StringUtils.isEmpty(val) && val.length()>0) {
        			lines.add(gson.fromJson(val, Question.class));
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
	
	public void writeRecordToFile(String fileName, Question question) {
    	try {   
    		String path = env.getProperty("destination.path");
    		String finalFilePath = path+destPath+fileName.replace(".txt", "_done.txt");
    		Path filePath = Path.of(finalFilePath);
    		Files.writeString(filePath, "\n"+gson.toJson(question), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
    
	public void createTest(String subject, Integer gradeNo) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "xmldata/"+subject+"_"+gradeNo+".xml";
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
		        IXLQuestionSaxParserHandler handler = new IXLQuestionSaxParserHandler();
		        saxParser.parse(fObj, handler);
		        List<Question> dataList = handler.getQuestionList();
				if(dataList.size()>0) {
					List<Question> finalItems = sortItems(dataList, dataList.size());
					createFinalFile(subject, gradeNo, finalItems);
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
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
							item.setUrl(url+item.getSubject().toLowerCase()+"/grade-"+item.getGrade()+"/"+item.getSubSectionName());
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
	
	public void createFinalFile(String subject, Integer gradeNo, List<Question> data) {
		BufferedWriter writer = null;
    	try {   
    		String path = env.getProperty("base.path");
    		String finalFilePath = path+srcPath+subject+"_"+gradeNo+".txt";
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
    	} catch (Exception e) {
    		e.printStackTrace();
		} finally {
			try {
				if(writer != null) {
					writer.close();
				}
			} catch (Exception e) { }
		}
    }
	
	public Week getAllVocabularyRecords(String name, boolean next, Integer weekId, Integer workNo) {
		String path = env.getProperty("base.path");
		String currentFilePath = path+name+"_vocabulary.txt";
		String doneFilePath = path+name+"_vocabulary_done.txt";
		List<Week> currentList = getVocabularyData(currentFilePath);
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
	
	public Word getAllReadWordRecords(boolean next, Integer id, String spellValue) {
		String path = env.getProperty("base.path");
		String currentFilePath = path+"read_words/read_words.txt";
		String doneFilePath = path+"read_words/read_words_done.txt";
		List<Word> currentList = getReadWordData(currentFilePath);
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
	
	public List<Word> getReadWordData(String filePath) {
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
	
	public void writeWordRecordToFile(String filePath, Word word) {
    	try {   
    		Path path = Path.of(filePath);
    		Files.writeString(path, "\n"+gson.toJson(word), StandardOpenOption.APPEND);
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    }
}
