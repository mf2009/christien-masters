package com.pypmannetjies.pointerlocation;

import java.util.ArrayList;

import org.apache.commons.math3.stat.descriptive.*;
import org.apache.commons.math3.util.FastMath;

import com.pypmannetjies.pointerlocation.AwesomeGestureListener.GestureType;

import android.graphics.Point;
import android.view.MotionEvent;

public class GestureData {
	
	public enum ScreenOrientation {PORTRAIT, LANDSCAPE}
	public enum MotionDirection {UP, DOWN, LEFT, RIGHT}
		
	 GestureType type; //constructor
	 int pointerID; //constructor - always -1 for now
	 double start_time_milliseconds, end_time_milliseconds; //RecordTime
	 String start_time_fancy, end_time_fancy; //RecordTime
	 public double total_time; //setFinalData	 
	 private double interstroke_time_milliseconds; //Method called from View
	 double start_x, start_y, end_x, end_y; //setFinalData
	 SynchronizedDescriptiveStatistics x_coords, y_coords; //addMotionData
	 SynchronizedDescriptiveStatistics pressure; //addMotionData
	 SynchronizedDescriptiveStatistics size; //addMotionData
	 SynchronizedDescriptiveStatistics touch_major, touch_minor; //addMotionData
	 SynchronizedDescriptiveStatistics tool_major, tool_minor; //addMotionData
	 SynchronizedDescriptiveStatistics tool_orientation; //addMotionData
	 ScreenOrientation screen_orientation; //setFinalData
	 boolean keyboard_visible; //setFinalData
	 
	 Vector2D motion_vector; //calculateMotionFeatures
	 double motion_vector_angle; //calculateMotionFeatures
	 public MotionDirection motion_vector_direction; //calculateMotionFeatures
	 double motion_vector_length; //calculateMotionFeatures
	 double motion_speed; 
	 double motion_acceleration;
	
	 int featureVectorStartingSize = 35;
	
	 ArrayList<String> featureVector;
	 
	 static String header = "POINTERLOCATION," + 
				"Gesture type," +
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
	
	public void setFinalData(MotionEvent event, double interstroke_time_milliseconds, ScreenOrientation screenOrientation, boolean keyboardVisible) {		
		this.screen_orientation = screenOrientation;
		this.keyboard_visible = keyboardVisible;
		
		this.total_time = end_time_milliseconds - start_time_milliseconds;
		this.interstroke_time_milliseconds = interstroke_time_milliseconds;
		
		this.start_x = x_coords.getValues()[0];
		this.start_y = y_coords.getValues()[0];
		this.end_x = x_coords.getValues()[x_coords.getValues().length-1]; //Last recorded x_coord
		this.end_y = y_coords.getValues()[y_coords.getValues().length-1]; //Last recorded x_coord
		
		this.calculateMotionFeatures();
	}
	
	//TODO: cleanup this and headers
	public ArrayList<String> getFeatureVector() {
		//TODO:Make this more efficient if needed
		featureVector.clear();
		
		// TYPE + POINTER ID
		featureVector.add(type.toString());
		featureVector.add(pointerID + "");
		
		// TIME
		featureVector.add(start_time_fancy + "");
		featureVector.add(end_time_fancy + "");
		
		//TODO: Put in fancy format
		featureVector.add(total_time + "");
		featureVector.add(interstroke_time_milliseconds + "");
		
		// COORDINATES
		featureVector.add(start_x + "");
		featureVector.add(start_y + "");
		featureVector.add(end_x + "");
		featureVector.add(end_y + "");
		
		featureVector.add(x_coords.getMean() + "");
		featureVector.add(x_coords.getStandardDeviation() + "");
		featureVector.add(y_coords.getMean() + "");
		featureVector.add(y_coords.getStandardDeviation() + "");
		
		// PRESSURE, SIZE, TOOLS
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
		
		featureVector.add(tool_orientation.getMean() + "");
		featureVector.add(tool_orientation.getStandardDeviation() + "");
		
		// SCREEN ORIENTATION
		featureVector.add(screen_orientation + "");
		
		// KEYBOARD VISIBLE
		featureVector.add(keyboard_visible + "");
		
		// MOTION FEATURES
		featureVector.add(motion_vector_angle + "");
		featureVector.add(motion_vector_direction + "");
		featureVector.add(motion_vector_length + "");
		featureVector.add(motion_speed + "");
		featureVector.add(motion_acceleration + "");
		
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
	
	//TODO: Remove public access
	/**
	 * Find features of the motion based on the raw data.
	 */
	public void calculateMotionFeatures() {
		setVector();
		calculateMotionAngleAndDirection();
		calculateVectorLength();
		calculateMotionSpeed();
		calculateMotionAcceleration();
	}
	
	//TODO: Remove public access
	/**
	 * Sets a 3D vector based on the motion data's start and end positions.
	 * @return The new vector
	 */
	public Vector2D setVector() {
		double x = x_coords.getValues()[x_coords.getValues().length-1] - x_coords.getValues()[0];
		double y = y_coords.getValues()[y_coords.getValues().length-1] - y_coords.getValues()[0];
		motion_vector = new Vector2D(x, y);
		return motion_vector;
	}
	
	//TODO: Remove public access
	/**
	 * Calculates the angle of the motion as well as sets the general direction
	 * flag to UP, DOWN, LEFT or RIGHT
	 * @return The angle of the deviation from the general direction in degrees.
	 */
	public double calculateMotionAngleAndDirection() {
		Vector2D up = new Vector2D(0, -1);
		Vector2D down = new Vector2D(0, 1);
		Vector2D left = new Vector2D(-1, 0);
		Vector2D right = new Vector2D(1, 0);
		
		double radians = 100;
		
		double x,y;
		x = motion_vector.getX();
		y = motion_vector.getY();
		
		// If x differs more than y does, the movement is horizontal
		// Compare to straight horizontal line
		if (FastMath.abs(x) > FastMath.abs(y)) {
			// If x is positive, the movement was to the right:
			if (x > 0) {
				radians = motion_vector.getAngle(right);
				motion_vector_direction = MotionDirection.RIGHT;
			}
			else {
				radians = motion_vector.getAngle(left);
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
				radians = motion_vector.getAngle(down);
				motion_vector_direction = MotionDirection.DOWN;
			}
			else {
				radians = motion_vector.getAngle(up);
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
	/**
	 * Calculates the length of the vector which describes the motion's 
	 * start and end position.
	 * @return The length of the vector
	 */
	public double calculateVectorLength() {
		motion_vector_length = motion_vector.getLength();
		return motion_vector_length;
	}
	
	//TODO: Remove public access
	/**
	 * Calculates the average speed of the motion as the length 
	 * of the motion vector over time as "pixels per millisecond"
	 * @return The motion's speed
	 */
	public double calculateMotionSpeed() {
		motion_speed = motion_vector_length / total_time;
		return motion_speed;
	}
	
	//TODO: Remove public access
	/**
	 * Calculates the average speed of the motion as the length 
	 * of the motion vector over time as "pixels per millisecond"
	 * @return The motion's speed
	 */
	public double calculateMotionAcceleration() {
		motion_acceleration = motion_speed / total_time;
		return motion_acceleration;
	}
	
	public static String getHeader() {
		return header;
	}
	
	/**
	 * A special method to set all the coordinate values for normalising purposes.
	 * Sets Start(x+y), End(x+y), (x+y)coords
	 * Not used
	 * @param screenSize
	 */
	private void setBounds(Point screenSize) {
		this.start_x = screenSize.x; 
		this.start_y = screenSize.y;
		this.end_x = screenSize.x; 
		this.end_y = screenSize.y;
	}
	
}
