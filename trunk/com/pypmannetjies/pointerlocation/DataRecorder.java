package com.pypmannetjies.pointerlocation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class DataRecorder {
	
	private static FileOutputStream fOut;
	private static FileWriter fwriter;
	private static OutputStreamWriter writer;
	private static boolean isOpen;
	
	public static void openFile(String filename, Context context) {
		if (!isOpen) {
			try {
				File directory = new File(Environment.getExternalStorageDirectory() + "/touch/");
				
				if (!directory.exists()) {
					if (directory.mkdir()) {
						System.out.println("Directory created");
						//Toast.makeText(context, "Directory created for user " + userID, Toast.LENGTH_LONG).show();
					}
					
				}
				
				File file = new File(directory.getPath() + "/" + filename);
				fwriter = new FileWriter(file, true);
				isOpen = true;
				writeHeader();
				
			} catch (FileNotFoundException e) {
				System.err.println("Error creating file");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void writeHeader() {
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
		
		addToFile(header);
	}
	
	public static void addToFile(GestureData gesture) {
		String s = "";//Helpers.getComplexDate() + ",";
		ArrayList<String> features = gesture.getFeatureVector();
		
		for (String f : features) {
			s += f + ",";
		}
		
		s = s.substring(0, s.length()-1);
		
		DataRecorder.addToFile(s);
	}
	
	public static void addToFile(String s) {
		if (isOpen) {
			try {
				fwriter.append(s + "\n");
				fwriter.flush();
			} catch (IOException e) {
				System.err.println("Could not append to file");
				e.printStackTrace();
			}
		} else {
			System.err.println("File has not yet been opened");
		}
	}
	
	public static void closeFile() {
		if (isOpen) {
			try {
				fwriter.close();
				isOpen = false;
			} catch (IOException e) {
				System.err.println("Error closing file");
				e.printStackTrace();
			}
		} else {
			System.err.println("File has not yet been opened");
		}
	}
	
}
