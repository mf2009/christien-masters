package com.pypmannetjies.pointerlocation;

import java.util.ArrayList;

public class DataSpewer {
	
	public static void spewHeader() {
		String header = "Gesture type," +
				"Pointer ID," +
				
				"Start time fancy," +
				"End time fancy," +
				"Total time ms," +
				"Interstroke time ms," +
				
				"Start x,Starty,End x,End y," +
				"X coordinate mean, X coordinate sd," +
				"Y coordinate mean, Y coordinate sd," +
				
				"Pressure mean,Pressure sd," +
				"Size mean,Size sd," +
				"Touch major mean,Touch major sd," +
				"Touch minor mean,Touch minor sd," +
				"Tool major mean,Tool major sd," +
				"Tool minor mean,Tool minor sd," +
				"Tool orientation mean,Tool orientation sd," +
				
				"Screen orientation," +
				
				"Vector angle,Vector direction,Vector length,Average speed,Average acceleration";
		
		System.out.println("<*>" + header);
	}
	
	public static void spew(GestureData data) {
		String s = "<*>POINTERLOCATION<*>,";
		ArrayList<String> features = data.getFeatureVector();
		
		for (String f : features) {
			s += f + ",";
		}
		
		s = s.substring(0, s.length()-1);
		
		System.out.println(s);
	}

}
