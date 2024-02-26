package com.anish.controllers;

import java.util.HashMap;
import java.util.Map;

public class Testing {

	public static void main(String[] args) {
		try {
			int min=2;
			int max=20;
			Map<Integer, Boolean> map = new HashMap<>();
			for(int i=0;i<=30;i++) {
				int randomNum = min + (int)(Math.random() * ((max - min) + 1));
				if(!map.containsKey(randomNum)) {
					System.out.print(randomNum+",");
					map.put(randomNum, Boolean.TRUE);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
