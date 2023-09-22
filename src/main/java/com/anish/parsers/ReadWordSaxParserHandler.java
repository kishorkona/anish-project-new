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
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (name) {
			word.setName(data.toString());
			name = false;
		}
		if (qName.equalsIgnoreCase("Word")) {
			wordList.add(word);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
