package com.pypmannetjies.pointerlocation;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.descriptive.*;

import com.pypmannetjies.pointerlocation.AwesomeGestureListener.GestureType;

import android.content.res.Configuration;
import android.view.MotionEvent;

public class GestureData {
	
	public enum ScreenOrientation {PORTRAIT, LANDSCAPE}
	
	private GestureType type;
	private int pointerID; 
	private SynchronizedDescriptiveStatistics x_coords;
	private SynchronizedDescriptiveStatistics y_coords;
	private double x_diff;
	private double y_diff;
	private SynchronizedDescriptiveStatistics pressure;
	private SynchronizedDescriptiveStatistics size;
	private SynchronizedDescriptiveStatistics touch_major;
	private SynchronizedDescriptiveStatistics touch_minor;
	private SynchronizedDescriptiveStatistics tool_major;
	private SynchronizedDescriptiveStatistics tool_minor;
	//private SynchronizedDescriptiveStatistics x_velocity;
	//private SynchronizedDescriptiveStatistics y_velocity;
	private SynchronizedDescriptiveStatistics tool_orientation;
	private ScreenOrientation screen_orientation;
	//private double screen_rotation;
	private String fancy_time;
	private double start_time;
	private double end_time;
	private double total_time;
	private int user_id;
	private int featureVectorStartingSize = 25;
	
	private ArrayList<String> featureVector;
	
	public GestureData(GestureType type, int pointerID, int user_id) {
		this.type = type;
		this.setPointerID(pointerID);
		this.user_id = user_id;
		
		this.featureVector = new ArrayList<String>(featureVectorStartingSize);
		
		x_coords = new SynchronizedDescriptiveStatistics();
		y_coords = new SynchronizedDescriptiveStatistics();
		pressure = new SynchronizedDescriptiveStatistics();
		size = new SynchronizedDescriptiveStatistics();
		touch_major = new SynchronizedDescriptiveStatistics();
		touch_minor = new SynchronizedDescriptiveStatistics();
		tool_major = new SynchronizedDescriptiveStatistics();
		tool_minor = new SynchronizedDescriptiveStatistics();
		tool_orientation = new SynchronizedDescriptiveStatistics();		
	}
	
	public void addMotionData(MotionEvent event) {
		x_coords.addValue(event.getRawX());
		y_coords.addValue(event.getRawY());
		pressure.addValue(event.getPressure());
		size.addValue(event.getSize());
		touch_major.addValue(event.getTouchMajor());
		touch_minor.addValue(event.getTouchMinor());
		tool_major.addValue(event.getToolMajor());
		tool_minor.addValue(event.getToolMinor());
		tool_orientation.addValue(event.getOrientation());
	}
	
	public void setFinalData(MotionEvent event, int screen_orientation) {
		this.setFinalCoordinates(event.getRawX(), event.getRawY());
		this.screen_orientation = (screen_orientation == Configuration.ORIENTATION_PORTRAIT) ? ScreenOrientation.PORTRAIT :  ScreenOrientation.LANDSCAPE;
		this.total_time = end_time - start_time;
	}
	
	public ArrayList<String> getFeatureVector() {
		//TODO:Make this more efficient if needed
		featureVector.clear();
		
		featureVector.add(fancy_time);
		
		featureVector.add(type.toString());
		featureVector.add(pointerID + "");
		
		featureVector.add(x_coords.getMean() + "");
		featureVector.add(x_coords.getStandardDeviation() + "");
		featureVector.add(y_coords.getMean() + "");
		featureVector.add(y_coords.getStandardDeviation() + "");
		
		featureVector.add(x_diff + "");
		featureVector.add(y_diff + "");
		
		featureVector.add(pressure.getMean() + "");
		featureVector.add(pressure.getStandardDeviation() + "");
		
		featureVector.add(size.getMean() + "");
		featureVector.add(size.getStandardDeviation() + "");
		
		featureVector.add(touch_major.getMean() + "");
		featureVector.add(touch_major.getStandardDeviation() + "");
		featureVector.add(touch_minor.getMean() + "");
		featureVector.add(touch_minor.getStandardDeviation() + "");
		
		featureVector.add(tool_major.getMean() + "");
		featureVector.add(tool_major.getStandardDeviation() + "");
		featureVector.add(tool_minor.getMean() + "");
		featureVector.add(tool_minor.getStandardDeviation() + "");
		
		
		/*featureVector.add(x_velocity.getMean());
		featureVector.add(x_velocity.getStandardDeviation());
		featureVector.add(y_velocity.getMean());
		featureVector.add(y_velocity.getStandardDeviation());*/
		
		featureVector.add(tool_orientation.getMean() + "");
		featureVector.add(tool_orientation.getStandardDeviation() + "");
		
		featureVector.add(screen_orientation + "");
		
		featureVector.add(total_time + "");
		
		featureVector.add(user_id + "");
		
		return featureVector;
	}
	
	public void setFancyTime(String fancy_time) {
		this.fancy_time = fancy_time;
	}
	
	public void setStartTime(double start_time) {
		this.start_time = start_time;
	}
	
	public void setEndTime(double end_time) {
		this.end_time = end_time;
	}
	
	public int getFeatureVectorSize() {
		return featureVector.size();
	}

	
	public void setType(GestureType type) {
		this.type = type;
	}
	
	private void setPointerID(int pointerID) {
		this.pointerID = pointerID;
	}
	
	public void addCoordinates(double x, double y) {
		x_coords.addValue(x);
		y_coords.addValue(y);
	}
	
	public void setFinalCoordinates(double x, double y) {
		this.x_diff = x - x_coords.getValues()[0];
		this.y_diff = y - y_coords.getValues()[0];
	}
	
	public void addPressure(double pressure) {
		this.pressure.addValue(pressure);
	}
	
	public void addSize(double size) {
		this.size.addValue(size);
	}
	
	public void addTouchMajor(double touchMajor) {
		this.touch_major.addValue(touchMajor);
	}
	
	public void addTouchMinor(double touchMinor) {
		this.touch_minor.addValue(touchMinor);
	}
	
	public void addToolMajor(double touchMajor) {
		this.tool_major.addValue(touchMajor);
	}
	
	public void addToolMinor(double touchMinor) {
		this.tool_minor.addValue(touchMinor);
	}
	
	public void addToolOrientation(double tool_orientation) {
		this.tool_orientation.addValue(tool_orientation);
	}
	
	
}
