package com.anish.helper;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.anish.data.Word;
import com.google.gson.Gson;

@Component
public class RedWordsHelper {
	
	@Autowired
	private Environment env;
	
	private Gson gson = new Gson();
	
	public String getWordString(Word word, AtomicInteger counter, String testFileName) {
		String tstName = env.getProperty("ishant.redwords.datafiles."+testFileName);
		StringBuffer sb = new StringBuffer();
		int no = counter.getAndIncrement();
		sb.append("\t<Word>\n");
			sb.append("\t\t<Id>"+no+"</Id>\n");
			sb.append("\t\t<FileNo>"+testFileName+"</FileNo>\n");
			sb.append("\t\t<Name>"+word.getName()+"</Name>\n");
			if(word.isHasSentence()) {
				sb.append("\t\t<HasSentence>1</HasSentence>\n");
			} else {
				sb.append("\t\t<HasSentence>0</HasSentence>\n");
			}
			sb.append("\t\t<TestName>"+tstName+"</TestName>\n");
			sb.append("\t\t<Sentence>"+word.getSentence()+"</Sentence>\n");
			sb.append("\t\t<Combination>"+word.getCombination()+"</Combination>\n");
		sb.append("\t</Word>\n");
		return sb.toString();
	}
}
