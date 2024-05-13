package com.anish.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.anish.data.Word;

public class ReadWordSaxParserHandler extends DefaultHandler{

	private List<Word> wordList = null;
	private Word word = null;
	private StringBuilder data = null;
	
	private boolean name;
	private boolean fileNo;
	private boolean hasSentenceFlag;
	private boolean sentence;
	private boolean combination;
	private boolean testName;
	private AtomicInteger counter = new AtomicInteger(0);

	public List<Word> getWordList() {
		return wordList;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Word")) {
			word = new Word();
			word.setId(counter.incrementAndGet());
			if (wordList == null)
				wordList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Name")) {
			name = true;
		} else if (qName.equalsIgnoreCase("FileNo")) {
			fileNo = true;
		} else if (qName.equalsIgnoreCase("Sentence")) {
			sentence = true;
		} else if (qName.equalsIgnoreCase("Combination")) {
			combination = true;
		} else if (qName.equalsIgnoreCase("HasSentence")) {
			hasSentenceFlag = true;
		} else if (qName.equalsIgnoreCase("TestName")) {
			testName = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (name) {
			word.setName(data.toString());
			name = false;
		} else if (qName.equalsIgnoreCase("FileNo")) {
			String fileNoVal = data.toString();
			word.setFileNo(Integer.valueOf(fileNoVal));
			fileNo = false;
		} else if (qName.equalsIgnoreCase("Sentence")) {
			sentence = false;
			word.setSentence(data.toString());
		} else if (qName.equalsIgnoreCase("Combination")) {
			combination = false;
			word.setCombination(data.toString());
		} else if (qName.equalsIgnoreCase("TestName")) {
			testName = false;
			word.setTestName(data.toString());
		} else if (qName.equalsIgnoreCase("HasSentence")) {
			String hasSentence = data.toString();
			if("0".equalsIgnoreCase(hasSentence)) {
				word.setHasSentence(false);
			} else if("1".equalsIgnoreCase(hasSentence)) {
				word.setHasSentence(true);
			}
			hasSentenceFlag = false;
		} else if (qName.equalsIgnoreCase("Word")) {
			wordList.add(word);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
