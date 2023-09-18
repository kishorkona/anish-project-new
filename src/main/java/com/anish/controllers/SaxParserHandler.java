package com.anish.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.anish.data.Question;

public class SaxParserHandler extends DefaultHandler{

	private List<Question> questionList = null;
	private Question question = null;
	private StringBuilder data = null;
	
	private boolean subject = false;
	private boolean grade = false;
	private boolean sectionId = false;
	private boolean sectionName = false;
	private boolean enabled = false;
	private boolean subSectionId = false;
	private boolean subSectionName = false;
	private AtomicInteger counter = new AtomicInteger(0);
	
	public List<Question> getQuestionList() {
		return questionList;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Question")) {
			// create a new Question and put it in Map
			question = new Question();
			question.setId(counter.incrementAndGet());
			// initialize list
			if (questionList == null)
				questionList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Subject")) {
			// set boolean values for fields, will be used in setting Employee variables
			subject = true;
		} else if (qName.equalsIgnoreCase("Grade")) {
			grade = true;
		} else if (qName.equalsIgnoreCase("SectionId")) {
			sectionId = true;
		} else if (qName.equalsIgnoreCase("SectionName")) {
			sectionName = true;
		} else if (qName.equalsIgnoreCase("Enabled")) {
			enabled = true;
		} else if (qName.equalsIgnoreCase("SubSectionId")) {
			subSectionId = true;
		} else if (qName.equalsIgnoreCase("SubSectionName")) {
			subSectionName = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (subject) {
			question.setSubject(data.toString().toLowerCase());
			subject = false;
		} else if (grade) {
			question.setGrade(data.toString());
			grade = false;
		} else if (sectionId) {
			question.setSectionId(data.toString());
			sectionId = false;
		} else if (sectionName) {
			question.setSectionName(data.toString());
			sectionName = false;
		} else if (enabled) {
			int questionStatus = Integer.valueOf(data.toString());
			question.setQuestionStatus(questionStatus);
			enabled = false;
		} else if (subSectionId) {
			question.setSubSectionId(data.toString());
			subSectionId = false;
		} else if (subSectionName) {
			question.setSubSectionName(data.toString());
			subSectionName = false;
		}
		if (qName.equalsIgnoreCase("Question")) {
			// add Question object to list
			questionList.add(question);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
