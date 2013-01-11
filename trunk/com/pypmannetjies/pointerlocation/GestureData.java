package com.pypmannetjies.pointerlocation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.stat.descriptive.*;
import org.apache.commons.math3.util.FastMath;

import com.pypmannetjies.pointerlocation.AwesomeGestureListener.GestureType;

import android.content.res.Configuration;
import android.view.MotionEvent;

public class GestureData {
	
	public enum ScreenOrientation {PORTRAIT, LANDSCAPE}
	public enum MotionDirection {UP, DOWN, LEFT, RIGHT}
		
	 GestureType type;
	 int pointerID; 
	 double start_time_milliseconds;
	 double end_time_milliseconds;
	 double start_x, start_y, stop_x, stop_y;
	 SynchronizedDescriptiveStatistics x_coords;
	 SynchronizedDescriptiveStatistics y_coords;
	 SynchronizedDescriptiveStatistics pressure;
	 SynchronizedDescriptiveStatistics size;
	 SynchronizedDescriptiveStatistics touch_major;
	 SynchronizedDescriptiveStatistics touch_minor;
	 SynchronizedDescriptiveStatistics tool_major;
	 SynchronizedDescriptiveStatistics tool_minor;
	 SynchronizedDescriptiveStatistics tool_orientation;
	 ScreenOrientation screen_orientation;
	
	
	//TODO
	 String start_time_fancy, end_time_fancy;
	 double total_time;
	 double time_before_milliseconds; //interstroke time
	 Vector3D motion_vector;
	 double motion_vector_length;
	 double motion_velocity;
	 double motion_acceleration;
	 double motion_vector_angle;
	 public MotionDirection motion_vector_direction;
	
	 int featureVectorStartingSize = 35;
	
	 ArrayList<String> featureVector;
	
	public GestureData(GestureType type, int pointerID) {
		this.type = type;
		this.setPointerID(pointerID);
		
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
	
	public void setTimeBefore(double time_before_milliseconds) {
		this.time_before_milliseconds = time_before_milliseconds;
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
		this.screen_orientation = (screen_orientation == Configuration.ORIENTATION_PORTRAIT) ? ScreenOrientation.PORTRAIT :  ScreenOrientation.LANDSCAPE;
		this.total_time = end_time_milliseconds - start_time_milliseconds;
		
		this.calculateMotionFeatures();
	}
	
	//TODO: cleanup this and headers
	public ArrayList<String> getFeatureVector() {
		//TODO:Make this more efficient if needed
		featureVector.clear();
		
		//featureVector.add(fancy_time);
		
		featureVector.add(type.toString());
		featureVector.add(pointerID + "");
		
		featureVector.add(x_coords.getMean() + "");
		featureVector.add(x_coords.getStandardDeviation() + "");
		featureVector.add(y_coords.getMean() + "");
		featureVector.add(y_coords.getStandardDeviation() + "");
		

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
		
		return featureVector;
	}
	
	public void recordStartTime() {
		this.start_time_milliseconds = System.currentTimeMillis();
		this.start_time_fancy = Helpers.getComplexDate();
	}
	
	public void recordEndTime() {
		this.end_time_milliseconds = System.currentTimeMillis();
		this.end_time_fancy = Helpers.getComplexDate();
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
	
	/**
	 * Find features of the motion based on the raw data.
	 */
	public void calculateMotionFeatures() {
		setVector();
		calculateMotionAngleAndDirection();
		
	}
	
	/**
	 * Sets a 3D vector based on the motion data's start and end positions.
	 * @return The new vector
	 */
	public Vector3D setVector() {
		double x_diff = x_coords.getValues()[x_coords.getValues().length-1] - x_coords.getValues()[0];
		double y_diff = y_coords.getValues()[y_coords.getValues().length-1] - y_coords.getValues()[0];
		motion_vector = new Vector3D(x_diff, y_diff, 0);
		return motion_vector;
	}
	
	/**
	 * Calculates the angle of the motion as well as sets the general direction
	 * flag to UP, DOWN, LEFT or RIGHT
	 * @return The angle of the deviation from the general direction in degrees.
	 */
	public double calculateMotionAngleAndDirection() {
		Vector3D up = new Vector3D(0, -1, 0);
		Vector3D down = new Vector3D(0, 1, 0);
		Vector3D left = new Vector3D(-1, 0, 0);
		Vector3D right = new Vector3D(1, 0, 0);
		
		double radians = 100;
		
		double x,y;
		x = motion_vector.getX();
		y = motion_vector.getY();
		
		// If x differs more than y does, the movement is horizontal
		// Compare to straight horizontal line
		if (FastMath.abs(x) > FastMath.abs(y)) {
			// If x is positive, the movement was to the right:
			if (x > 0) {
				radians = Vector3D.angle(motion_vector, right);
				motion_vector_direction = MotionDirection.RIGHT;
			}
			else {
				radians = Vector3D.angle(motion_vector, left);
				motion_vector_direction = MotionDirection.LEFT;
			}
			// If y was negative, make the angle negative too. 
			if (y < 0) 
				radians *= -1;
		}
		// If y differs more than x does, the movement is vertical
		// Compare to straight vertical line
		// Cases where x and y are equal, a vertical line is given preference
		else {
			// If y is positive, the movement was downwards:
			if (y > 0) {
				radians = Vector3D.angle(motion_vector, down);
				motion_vector_direction = MotionDirection.DOWN;
			}
			else {
				radians = Vector3D.angle(motion_vector, up);
				motion_vector_direction = MotionDirection.UP;
			}
			// If x was negative, make the angle negative too. 
			if (x < 0) 
				radians *= -1;
		}
		
		motion_vector_angle = FastMath.toDegrees(radians);
		return motion_vector_angle;
	}
	
	
	//TODO: Remove public access
	//public double calculateVectorLength() {
		
	//}
	
	
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
