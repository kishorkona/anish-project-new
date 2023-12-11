package com.anish.parsers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.anish.data.Jei;

public class JeiSaxParserHandler extends DefaultHandler{

	private List<Jei> jeiList = null;
	private Jei jei = null;
	private StringBuilder data = null;
	
	private boolean name;
	private boolean subject;
	private boolean bookId;
	private boolean topic;
	private boolean date;
	
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

	public List<Jei> getJeiList() {
		return jeiList;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Book")) {
			jei = new Jei();
			if (jeiList == null)
				jeiList = new ArrayList<>();
		} else if (qName.equalsIgnoreCase("Name")) {
			name = true;
		} else if (qName.equalsIgnoreCase("Subject")) {
			subject = true;
		} else if (qName.equalsIgnoreCase("BookId")) {
			bookId = true;
		} else if (qName.equalsIgnoreCase("Topic")) {
			topic = true;
		} else if (qName.equalsIgnoreCase("Date")) {
			date = true;
		}
		data = new StringBuilder();
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (name) {
			jei.setName(data.toString());
			name = false;
		} else if (subject) {
			jei.setSubject(data.toString());
			subject = false;
		} else if (bookId) {
			jei.setBookId(data.toString());
			bookId = false;
		} else if (topic) {
			jei.setTopic(data.toString());
			topic = false;
		} else if (date) {
			String dateVal = data.toString();
			jei.setDate(LocalDate.parse(dateVal, formatter));
			date = false;
		} else if (qName.equalsIgnoreCase("Book")) {
			jeiList.add(jei);
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		data.append(new String(ch, start, length));
	}
}
