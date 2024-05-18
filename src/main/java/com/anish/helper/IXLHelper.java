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

import com.anish.controllers.IxlController;
import com.anish.data.Question;
import com.anish.data.Word;
import com.anish.parsers.ReadWordSaxParserHandler;
import com.google.gson.Gson;

@Component
public class IXLHelper {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IxlController ixlController;
	
	private Gson gson = new Gson();
	
	public String buildIxlXml(String name) {
		String rtn = "failed";
		try {
			String value = env.getProperty(name+".buildXmls");
			String[] valArr = value.split("#");
			Map<String, Question> qMap = new HashMap<String, Question>();
			for(int i=0;i<valArr.length;i++) {
				List<Question> qList = ixlController.getCurrentXmlFile(valArr[i]+".xml", "buildXml");
				if(qList.size()>0) {
					qList.stream().forEach(q -> {
						String val = q.getGrade()+"#"+q.getSectionId()+"#"+q.getSubSectionId();
						try {
							if(!qMap.containsKey(val)) {
								qMap.put(val, q);
							} else {
								throw new RuntimeException("Already Exists:"+val);
							}
						} catch (Exception e) {
							System.out.println("Error [val="+val+"][question="+gson.toJson(q)+"]");
							e.printStackTrace();
							throw e;
						}
					});
				}
			}
			if(qMap.size()>0) {
				HashSet<String> sortedHashSet = qMap.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
				String envValX = env.getProperty(name+".splitFiles");
				String[] splitFilesArr = envValX.split("#");				
				int stCounter = 0;
				Map<String, AtomicInteger> idCounter = new HashMap<String, AtomicInteger>();
				for(int i=0;i<splitFilesArr.length;i++) {
					writeQuestonToFile("<Questions>\n", splitFilesArr[i], name, StandardOpenOption.CREATE);
					idCounter.put(splitFilesArr[i], new AtomicInteger(1));
				}
				for(String key: sortedHashSet) {
					String questionStr = getQuestionString(qMap.get(key), idCounter.get(splitFilesArr[stCounter]));
					writeQuestonToFile(questionStr, splitFilesArr[stCounter], name);
					stCounter++;
					if(stCounter >= splitFilesArr.length) {
						stCounter = 0;
					}
				}
				for(int i=0;i<splitFilesArr.length;i++) {
					writeQuestonToFile("</Questions>", splitFilesArr[i], name, StandardOpenOption.APPEND);
				}
				rtn = "success";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}

	public void writeQuestonToFile(String xmlData, String fileName, String name) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, xmlData, StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void writeQuestonToFile(String value, String fileName, String name, StandardOpenOption option) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, value, option);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Word> getRedWordsFile() {
		List<Word> dataList = new ArrayList<Word>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "read_words.xml";
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
	
	private String getQuestionString(Question q, AtomicInteger counter) {
		StringBuffer sb = new StringBuffer();
		sb.append("\t<Question>\n");
			sb.append("\t\t<Id>"+counter.getAndIncrement()+"</Id>\n");
			sb.append("\t\t<Subject>"+q.getSubject()+"</Subject>\n");
			sb.append("\t\t<Grade>"+q.getGrade()+"</Grade>\n");
			sb.append("\t\t<SectionId>"+q.getSectionId()+"</SectionId>\n");
			sb.append("\t\t<SectionName>"+q.getSectionName()+"</SectionName>\n");
			sb.append("\t\t<Enabled>"+q.getQuestionStatus()+"</Enabled>\n");
			sb.append("\t\t<SubSectionId>"+q.getSubSectionId()+"</SubSectionId>\n");
			sb.append("\t\t<SubSectionName>"+q.getSubSectionName()+"</SubSectionName>\n");
			sb.append("\t\t<UrlKey>"+q.getUrlKey()+"</UrlKey>\n");
		sb.append("\t</Question>\n");
		return sb.toString();
	}
}
