package com.anish.controllers;

import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.anish.data.Word;
import com.anish.helper.IXLHelper;
import com.anish.helper.RedWordsHelper;
import com.anish.helper.VocabularyHelper;
import com.google.gson.Gson;

@RestController
public class IxlControllerBuildData {

	private String anish_name = "anish";
	private String ishant_name = "ishant";

	@Autowired
	private Environment env;
	
	@Autowired
	private RedWordsHelper redWordsHelper;
	
	@Autowired
	private IXLHelper ixlHelper;
	
	@Autowired
	private VocabularyHelper vocabularyHelper;
	
	private Gson gson = new Gson();
	public static String destPath = "ixl/";
	
	@GetMapping(value = "/buildXmls/ixl/{name}")
	public String buildIxlXmls(@PathVariable("name") String name) {
		String rtn = "failed";
		try {
			if(name.contentEquals(anish_name)) {
				return ixlHelper.buildIxlXml(name);
			} else if(name.contentEquals(ishant_name)) {
				return ixlHelper.buildIxlXml(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	@GetMapping(value = "/buildXmls/vocabulary/{name}")
	public String buildVocabularyXmls(@PathVariable("name") String name) {
		String rtn = "failed";
		try {
			if(name.contentEquals(anish_name)) {
				return vocabularyHelper.buildVocabularyXmlNew(name);
			} else if(name.contentEquals(ishant_name)) {
				return vocabularyHelper.buildVocabularyXmlNew(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	//@GetMapping(value = "/buildXmls/vocabulary/{name}")
	public String buildVocabularyByDateXmls(@PathVariable("name") String name) {
		String rtn = "failed";
		try {
			if(name.contentEquals(anish_name)) {
				return vocabularyHelper.buildVocabularyXml(name);
			} else if(name.contentEquals(ishant_name)) {
				return vocabularyHelper.buildVocabularyXml(name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	@GetMapping(value = "/buildXmls/redwords")
	public String buildRedWordsXmls() {
		String rtn = "failed";
		try {
			List<Word> currentList = ixlHelper.getRedWordsFile();
			Map<String, Word> wordMap = new HashMap<String, Word>();
			for(Word wrd: currentList) {
				if(!wordMap.containsKey(wrd.getName())) {
					wordMap.put(wrd.getName().toUpperCase(), wrd);
				} else {
					throw new RuntimeException("Name Exists:"+gson.toJson(wrd));
				}
			}
			if(wordMap.size()>0) {
				String envValX = env.getProperty("ishant.redwords.splitFiles");
				String[] splitFilesArr = envValX.split("#");				
				int stCounter = 0;
				Map<String, AtomicInteger> idCounter = new HashMap<String, AtomicInteger>();
				for(int i=0;i<splitFilesArr.length;i++) {
					ixlHelper.writeQuestonToFile("<ReadWords>\n", splitFilesArr[i], ishant_name, StandardOpenOption.CREATE);
					idCounter.put(splitFilesArr[i], new AtomicInteger(1));
				}
				for(String key: wordMap.keySet()) {
					String wordString = redWordsHelper.getWordString(wordMap.get(key), idCounter.get(splitFilesArr[stCounter]), splitFilesArr[stCounter]);
					ixlHelper.writeQuestonToFile(wordString, splitFilesArr[stCounter], ishant_name);
					stCounter++;
					if(stCounter >= splitFilesArr.length) {
						stCounter = 0;
					}
				}
				for(int i=0;i<splitFilesArr.length;i++) {
					ixlHelper.writeQuestonToFile("</ReadWords>", splitFilesArr[i], ishant_name, StandardOpenOption.APPEND);
				}
				rtn = "success";
			}
		} catch (Exception e) {
			System.out.println("Error [question="+e+"]");
			e.printStackTrace();
			throw e;
		}
		return rtn;
	}
	


}
