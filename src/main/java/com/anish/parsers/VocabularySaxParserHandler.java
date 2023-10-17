package com.anish.parsers;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.anish.data.Week;

public class VocabularySaxParserHandler extends DefaultHandler{

	private List<Week> weekList = null;
	private Week week = null;
	private StringBuilder data = null;
	
	private boolean id;
	private boolean name;
	private boolean word;
	private boolean wordNo;
	private boolean meening;
	private boolean hasSound;

	public List<Week> getWeekList() {
		return weekList;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Week")) {
			week = new Week();
			if (weekList == null)
				weekList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Id")) {
			id = true;
		} else if (qName.equalsIgnoreCase("Name")) {
			name = true;
		} else if (qName.equalsIgnoreCase("Word")) {
			word = true;
		} else if (qName.equalsIgnoreCase("WordNo")) {
			wordNo = true;
		} else if (qName.equalsIgnoreCase("HasSound")) {
			hasSound = true;
		} else if (qName.equalsIgnoreCase("Meening")) {
			meening = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (id) {
			int idVal = Integer.valueOf(data.toString());
			week.setId(idVal);
			id = false;
		} else if (name) {
			week.setName(data.toString());
			name = false;
		} else if (word) {
			week.setWord(data.toString());
			word = false;
		} else if (wordNo) {
			int idVal = Integer.valueOf(data.toString());
			week.setWordNo(idVal);
			wordNo = false;
		} else if (hasSound) {
			week.setHasSound(data.toString());
			hasSound = false;
		} else if (meening) {
			week.setMeening(data.toString());
			meening = false;
		}
		if (qName.equalsIgnoreCase("Week")) {
			weekList.add(week);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
