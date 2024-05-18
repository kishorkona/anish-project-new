package com.anish.helper;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.anish.data.Week;
import com.anish.parsers.VocabularySaxParserHandler;
import com.google.gson.Gson;

@Component
public class VocabularyHelper {
	
	@Autowired
	private Environment env;
	
	private Gson gson = new Gson();
	
	public String buildVocabularyXmlNew(String name) {
		String rtn = "failed";
		try {
			Map<String, Week> keyMap = new HashMap<String, Week>();
			List<Week> qList = getVocabularyFile("vocabulary_"+name+".xml", "buildXml", name);
			if(qList.size()>0) {
				qList.stream().forEach(q -> {
					String val = q.getWeekNo()+"#"+q.getGradeNo()+"#"+q.getWordName().toUpperCase();
					try {
						if(!keyMap.containsKey(val)) {
							keyMap.put(val, q);
						} else {
							throw new RuntimeException("Already Exists:"+val);
						}
					} catch (Exception e) {
						System.out.println("Error [val="+val+"][week="+gson.toJson(q)+"]");
						e.printStackTrace();
						throw e;
					}
				});
			}
			
			Map<String, List<Week>> qMap = new HashMap<String, List<Week>>();
			if(qList.size()>0) {
				qList.stream().forEach(q -> {
					String val = q.getWeekNo()+"#"+q.getGradeNo();
					if(!qMap.containsKey(val)) {
						List<Week> lst = new ArrayList<Week>();
						lst.add(q);
						qMap.put(val, lst);
					} else {
						qMap.get(val).add(q);
					}
				});
			}
			
			if(qMap.size()>0) {
				qMap.entrySet().stream().forEach(key -> {
					Week wk = key.getValue().get(0);
					String fName = name+"_"+wk.getGradeNo()+"_"+wk.getWeekNo();
					writeToFile("<Vocabulary>\n", fName, name, StandardOpenOption.CREATE);
					AtomicInteger counter = new AtomicInteger(1);
					key.getValue().stream().forEach(q-> {
						String questionStr = getVocabularyString(q, counter);
						writeToFile(questionStr, fName, name);		
					});
					writeToFile("</Vocabulary>", fName, name, StandardOpenOption.APPEND);
					
				});
				rtn = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	public String buildVocabularyXml(String name) {
		String rtn = "failed";
		try {
			Map<String, Week> qMap = new HashMap<String, Week>();
			List<Week> qList = getVocabularyFile("vocabulary_"+name+".xml", "buildXml", name);
			if(qList.size()>0) {
				qList.stream().forEach(q -> {
					String val = q.getWeekNo()+"#"+q.getGradeNo()+"#"+q.getWordName().toUpperCase();
					try {
						if(!qMap.containsKey(val)) {
							qMap.put(val, q);
						} else {
							throw new RuntimeException("Already Exists:"+val);
						}
					} catch (Exception e) {
						System.out.println("Error [val="+val+"][week="+gson.toJson(q)+"]");
						e.printStackTrace();
						throw e;
					}
				});
			}
			if(qMap.size()>0) {
				HashSet<String> sortedHashSet = qMap.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
				String envValX = env.getProperty(name+".vocabword.splitFiles");
				String[] splitFilesArr = envValX.split("#");				
				int stCounter = 0;
				Map<String, AtomicInteger> idCounter = new HashMap<String, AtomicInteger>();
				for(int i=0;i<splitFilesArr.length;i++) {
					writeToFile("<Vocabulary>\n", splitFilesArr[i], name, StandardOpenOption.CREATE);
					idCounter.put(splitFilesArr[i], new AtomicInteger(1));
				}
				for(String key: sortedHashSet) {
					String questionStr = getVocabularyString(qMap.get(key), idCounter.get(splitFilesArr[stCounter]));
					writeToFile(questionStr, splitFilesArr[stCounter], name);
					stCounter++;
					if(stCounter >= splitFilesArr.length) {
						stCounter = 0;
					}
				}
				for(int i=0;i<splitFilesArr.length;i++) {
					writeToFile("</Vocabulary>", splitFilesArr[i], name, StandardOpenOption.APPEND);
				}
				rtn = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	public void writeToFile(String xmlData, String fileName, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, xmlData, StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeToFile(String value, String fileName, String name, StandardOpenOption option) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, value, option);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Week> getVocabularyFile(String fileName, String folderName, String personName) {
		List<Week> dataList = new ArrayList<Week>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = folderName+"/" + fileName;
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
		        VocabularySaxParserHandler handler = new VocabularySaxParserHandler(personName);
		        saxParser.parse(fObj, handler);
		        dataList = handler.getWeekList();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	private String getVocabularyString(Week q, AtomicInteger counter) {
		StringBuffer sb = new StringBuffer();
		sb.append("\t<Week>\n");
			sb.append("\t\t<Id>"+counter.getAndIncrement()+"</Id>\n");
			sb.append("\t\t<WeekNo>"+q.getWeekNo()+"</WeekNo>\n");
			sb.append("\t\t<GradeNo>"+q.getGradeNo()+"</GradeNo>\n");
			sb.append("\t\t<WordName>"+q.getWordName()+"</WordName>\n");
			sb.append("\t\t<HasSound>"+q.getHasSound()+"</HasSound>\n");
			sb.append("\t\t<Meening>"+q.getMeening()+"</Meening>\n");
			sb.append("\t\t<Enable>"+q.getEnable()+"</Enable>\n");
		sb.append("\t</Week>\n");
		return sb.toString();
	}
	
}
