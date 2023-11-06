package com.anish.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.anish.data.Jei;
import com.anish.data.Question;
import com.anish.data.Word;
import com.anish.parsers.JeiSaxParserHandler;
import com.anish.parsers.ReadWordSaxParserHandler;
import com.google.gson.Gson;

@Controller
public class JeiController {
	
	@Autowired private Environment env;
	private Gson gson = new Gson();
	
	@GetMapping(value = "/jei/{name}/{subject}")
    public ModelAndView getJEITopics(@PathVariable("name") String name, @PathVariable("subject") String subject) {	
		List<Jei> dataList = getAllDataFile();
		List<Jei> finalList = dataList.stream().filter(x -> {
			if(x.getName().equalsIgnoreCase(name) && x.getSubject().equalsIgnoreCase(subject)) {
				return true;
			}
			return false;
		}).collect(Collectors.toList());
		ModelAndView obj = new ModelAndView("jei_topics", HttpStatus.OK);
		obj.addObject("data", finalList);
		return obj;
    }
	
	public List<Jei> getAllDataFile() {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			String filePath = "jei.xml";
			URL resource = classLoader.getResource(filePath);
			File fObj = new File(resource.getFile());
			List<Jei> dataList = new ArrayList<Jei>();
			if(fObj.isFile() && fObj.exists()) {
				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				SAXParser saxParser = saxParserFactory.newSAXParser();
				JeiSaxParserHandler handler = new JeiSaxParserHandler();
		        saxParser.parse(fObj, handler);
		        dataList = handler.getJeiList();
			}	
			if(dataList.size()>0) {
				return arrangeData(dataList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Jei>();
	}
	
	private List<Jei> arrangeData(List<Jei> dataList) {
		return dataList.stream().sorted(Comparator.comparing(Jei::getDate)).collect(Collectors.toList());
	}
}
