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
	
	private boolean weekNo;
	private String name;
	private boolean gradeNo;
	private boolean wordName;
	private boolean meening;
	private boolean hasSound;
	private boolean enable;

	public VocabularySaxParserHandler(String name) {
		this.name = name;
	}
	
	public List<Week> getWeekList() {
		return weekList;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Week")) {
			week = new Week();
			week.setPersonName(name);
			if (weekList == null)
				weekList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("WeekNo")) {
			weekNo = true;
		} else if (qName.equalsIgnoreCase("GradeNo")) {
			gradeNo = true;
		} else if (qName.equalsIgnoreCase("WordName")) {
			wordName = true;
		} else if (qName.equalsIgnoreCase("HasSound")) {
			hasSound = true;
		} else if (qName.equalsIgnoreCase("Enable")) {
			enable = true;
		} else if (qName.equalsIgnoreCase("Meening")) {
			meening = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (weekNo) {
			int wkNo = Integer.valueOf(data.toString());
			week.setWeekNo(wkNo);
			weekNo = false;
		} else if (gradeNo) {
			int gdNo = Integer.valueOf(data.toString());
			week.setGradeNo(gdNo);
			gradeNo = false;
		} else if (wordName) {
			week.setWordName(data.toString());
			wordName = false;
		} else if (hasSound) {
			week.setHasSound(Boolean.valueOf(data.toString()));
			hasSound = false;
		} else if (enable) {
			week.setEnable(Boolean.valueOf(data.toString()));
			enable = false;
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
