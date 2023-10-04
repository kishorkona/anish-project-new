package com.anish.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.anish.entities.EntryEntity;
import com.anish.entities.UserEntity;
import com.anish.entities.data.GroupedData;
import com.anish.service.EntryServiceInterface;
import com.anish.service.UserServiceInterface;
import com.anish.view.EntryView;
import com.anish.view.SessionView;
import com.anish.view.UserView;

@Controller
public class HomeController
{
	@Autowired
	private UserServiceInterface userServiceInterface;
	
	@Autowired 
	HttpSession httpSession;
	@Autowired 
	SessionView sessionView;
	@Autowired
	private EntryServiceInterface entryServiceInterface;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView homepage() {
		ModelAndView model=new ModelAndView("loginpage");
		return model;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registrationpage() {
		
		ModelAndView model=new ModelAndView("registrationpage");
		
		return model;
	}
	
	
	@RequestMapping(value = "/saveuser", method = RequestMethod.POST)
	public ModelAndView saveuser(@ModelAttribute("userView") UserView userView)
	
	{
		System.out.println("hi i am here and i am register method");
		UserEntity entity = new UserEntity();
		entity.setUsername(userView.getUsername());
		entity.setPassword(userView.getPassword());
		//code to save the user details in the  database
		ModelAndView model=new ModelAndView("registersuccess");
		userServiceInterface.save(entity);
		
		return model;
	}
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ModelAndView authenticateuser(@ModelAttribute("userView") UserView userView) {
		ModelAndView model=new ModelAndView("loginpage");
		UserEntity	entity=userServiceInterface.findByUsername(userView.getUsername());
		if(entity!=null && entity.getPassword().equals(userView.getPassword()))
		{
			model.setViewName("userhomepage");
			//model.addObject("user", entity.getUsername());
			model.addObject("user", entity);
			sessionView.setId(entity.getId());
			sessionView.setUsername(entity.getUsername());
			httpSession.setAttribute("user", sessionView);
			
			
			//we can write the above line i.e. model.addObject("user", entity); also by passing the whole object
			//model.addObject("user", entity); in this case in the jsp page
			//we have give as ${user.username}
			List<EntryEntity> entries=null;
			try {
				entries=entryServiceInterface.findByUserId(entity.getId());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			model.addObject("entrieslist", entries);
			model.addObject("userId", entity.getId());
			
			Integer entity1=0;
			try {
				System.out.println("Calling listOfEntriesForUser with userid:-"+ entity.getId());
				entity1=entryServiceInterface.listOfEntriesForUser(entity.getId());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			model.addObject("countOfEntries", entity1);
			//***************************************************
			Integer entity2=0;
			try {
				System.out.println("Calling toalNumberOfEntriesForUser with userid:-"+ entity.getId());
				entity2=entryServiceInterface.toalNumberOfEntriesForUser(entity.getId());
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			System.out.println(model.addObject("toalNumberOfEntriesForUser", entity2));
		}
		//*************************************************
		return model;
		
		/*UserEntity	entity1 = userServiceInterface.findByUsernameAndPassword(userView.getUsername(), userView.getPassword());
		if(entity1 !=null) {
			model.setViewName("userhomepage");
		}*/
	}
	@RequestMapping(value = "/addentry",method = RequestMethod.GET)
	public ModelAndView addentry() {
		
		ModelAndView model=new ModelAndView("addentryform");
		
		return model;
	}
	
	
	
	@RequestMapping(value = "/saveentry",method = RequestMethod.POST)
	public ModelAndView saveentry(@ModelAttribute("saveEntryView") EntryView saveEntryView) {
		
		ModelAndView model=new ModelAndView("userhomepage");
		List<EntryEntity> entries=null;
		
		try {
			EntryEntity entryEntity = new EntryEntity();
			entryEntity.setUserid(saveEntryView.getUserId());
			entryEntity.setDescription(saveEntryView.getDescription());
			
			java.sql.Date entryDate = new java.sql.Date(saveEntryView.getEntrydate().getTime());
			entryEntity.setEntrydate(entryDate);
			
			entryServiceInterface.save(entryEntity);
			entries=entryServiceInterface.findByUserId(saveEntryView.getUserId());
		} catch (Exception e) {		
			e.printStackTrace();
		}
		model.addObject("entrieslist", entries);
	
	
	return model;
}	
	
	@RequestMapping(value = "/viewentry")
	public ModelAndView viewentry(@RequestParam("id")  int id)
	{
		ModelAndView model=new ModelAndView("displayentry");
		EntryEntity entry=entryServiceInterface.findById(id);
		model.addObject("description", entry.getDescription());
		model.addObject("date", entry.getEntrydate());
		return model;
	}
	
	@RequestMapping(value= "/userhome")
	public ModelAndView userhomepage() {
		
		ModelAndView model=new ModelAndView("userhomepage");
		List<EntryEntity> entries=null;
		try {
			entries=entryServiceInterface.findByUserId(getSessionView().getId());
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		model.addObject("entrieslist", entries);
	
	
		return model;
	}
	
	@RequestMapping(value = "/updateentry")
	public ModelAndView updateentry(@RequestParam("id")  int id)
	{
		ModelAndView model=new ModelAndView("displayupdateentry");
		try {
			EntryEntity entry=entryServiceInterface.findById(id);
			
			EntryView entryView = new EntryView();
			entryView.setUserId(entry.getUserid());
			entryView.setDescription(entry.getDescription());
			entryView.setEntrydate(entry.getEntrydate());
			model.addObject("data", entryView);
			
			model.addObject("recordId", entry.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	@RequestMapping(value = "/processentryupdate",method = RequestMethod.POST)
	public ModelAndView processentryupdate(@ModelAttribute("updateEntryView") EntryView updateEntryView,
			@RequestParam("id")  int id) {
		ModelAndView model=new ModelAndView("userhomepage");
		List<EntryEntity> entries=null;
		try {
			EntryEntity entryEntity = new EntryEntity();
			entryEntity.setId(id);
			entryEntity.setUserid(updateEntryView.getUserId());
			entryEntity.setDescription(updateEntryView.getDescription());
			
			java.sql.Date entryDate = new java.sql.Date(updateEntryView.getEntrydate().getTime());
			entryEntity.setEntrydate(entryDate);
			entryServiceInterface.update(entryEntity);
			entries=entryServiceInterface.findByUserId(updateEntryView.getUserId());
		} catch (Exception e) {		
			e.printStackTrace();
		}
		model.addObject("entrieslist", entries);
	
	
	return model;
}	
	@RequestMapping(value = "/deleteentry")
	public ModelAndView deleteeentry(@RequestParam("id")  int id)
	{
		ModelAndView model=new ModelAndView("userhomepage");
		EntryEntity entry=entryServiceInterface.findById(id);
		entryServiceInterface.delete(entry);
		List<EntryEntity> entries=null;
		try {
			entries=entryServiceInterface.findByUserId(getSessionView().getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addObject("entrieslist", entries);
		return model;
	}
	
	@RequestMapping(value = "/signout")
	public ModelAndView signout()
	{
		ModelAndView model=new ModelAndView("loginpage");
		httpSession.invalidate();
		return model;
	}
	
	@RequestMapping(value = "/money", method = RequestMethod.GET)
	public ModelAndView moneydetails()
	{
		ModelAndView model=new ModelAndView("usermoneydetailspage");
		List<GroupedData> data = entryServiceInterface.getMoneyListByUserId(new Long(getSessionView().getId()));
		model.addObject("data", data);
		return model;
	}
	
	@RequestMapping(value = "/mymoney", method = RequestMethod.GET)
	public ModelAndView moneydetail()
	{
		ModelAndView model=new ModelAndView("user1moneydetailspage");
		return model;
	}
	
	
	@RequestMapping(value = "/ourmoney", method = RequestMethod.GET)
	public ModelAndView totalmoney()
	{
		ModelAndView model=new ModelAndView("userstotalmoney");
		return model;
	}
	
	private SessionView getSessionView() {
		return (SessionView) httpSession.getAttribute("user");
	}
}