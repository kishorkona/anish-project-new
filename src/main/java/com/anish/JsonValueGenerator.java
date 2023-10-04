package com.anish;

import com.google.gson.Gson;
import com.anish.view.UserView;

public class JsonValueGenerator {

	
	public static void main(String[] args) {
		UserView view = new UserView();
		view.setUsername("kishor");
		view.setPassword("1235");
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(view));
	}

}
