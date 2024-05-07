package com.anish.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.anish.data.Question;
import com.google.gson.Gson;

@RestController
public class IxlControllerBuildData {

	private String anish_name = "anish";
	private String ishant_name = "ishant";

	@Autowired
	private Environment env;
	private Gson gson = new Gson();
	public static String destPath = "ixl/";
	
	@Autowired
	private IxlController controller;
	
	@GetMapping(value = "/buildXmls/{name}")
	public String buildXmls(@PathVariable("name") String name) {
		String rtn = "failed";
		try {
			if(name.contentEquals(anish_name)) {
				return buildXml(name);
			} else if(name.contentEquals(ishant_name)) {
				return buildXml(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	private String buildXml(String name) {
		String rtn = "failed";
		try {
			String value = env.getProperty(name+".buildXmls");
			String[] valArr = value.split("#");
			Map<String, Question> qMap = new HashMap<String, Question>();
			for(int i=0;i<valArr.length;i++) {
				List<Question> qList = controller.getCurrentXmlFile(valArr[i]+".xml", "buildXml");
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
					writeQuestonToFile(qMap.get(key), splitFilesArr[stCounter], name, idCounter.get(splitFilesArr[stCounter]));
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

	private void writeQuestonToFile(Question question, String fileName, String name, AtomicInteger counter) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, getQuestionString(question, counter), StandardOpenOption.APPEND);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeQuestonToFile(String value, String fileName, String name, StandardOpenOption option) {
		try {
			String path = env.getProperty("destination.path");
			String finalFilePath = path+name+"/writeXml/" + fileName+".xml";
			Path filePath = Path.of(finalFilePath);
			Files.writeString(filePath, value, option);
		} catch (Exception e) {
			e.printStackTrace();
		}
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