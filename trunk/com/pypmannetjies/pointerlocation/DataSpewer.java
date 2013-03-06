package com.pypmannetjies.pointerlocation;

import java.util.ArrayList;

public class DataSpewer {
	
	public static void spewGesture(GestureData data) {
		String s = "POINTERLOCATION,";
		ArrayList<String> features = data.getFeatureVector();
		
		for (String f : features) {
			s += f + ",";
		}
		
		s = s.substring(0, s.length()-1);
		
		System.out.println(s);
		//System.out.println("LOPLOPLOPLOPLOPLOPLOP");
	}
	
	public static void spewString(String s) {
		System.out.println(s);
	}

}
