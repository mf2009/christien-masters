package com.pypmannetjies.pointerlocation;

import android.graphics.Point;
import android.view.MotionEvent;

import com.pypmannetjies.pointerlocation.AwesomeGestureListener.GestureType;
import com.pypmannetjies.pointerlocation.GestureData.ScreenOrientation;

public class ContinuousData {
		
	 GestureType type;
	 int pointerID; 
	 double down_time, event_time; 
	 String system_time;
	 double x_coord, y_coord; 
	 double pressure, size;
	 double touch_major, touch_minor, tool_major, tool_minor, tool_orientation;
	 ScreenOrientation screen_orientation;
	 boolean keyboard_visible;
	
	 int featureVectorStartingSize = 14;
	 
	 private static String header = "CONTINUOUSDATA," +
				"Gesture type," +
				"Pointer ID," +
				
				"Down time," +
				"Event time," +
				"System time," + 
				
				"X coordinate," +
				"Y coordinate," +
				
				"Pressure," +
				"Size," +
				
				"Touch major," +
				"Touch minor," +
				"Tool major," +
				"Tool minor," +
				"Tool orientation," +
				
				"Screen orientation," +
				"Keyboard visible";
	 
	 public String setData(MotionEvent event, GestureType type, ScreenOrientation screenOrientation, boolean keyboardVisible) {
	     String s = "";
		 
		 for (int i = 0; i < event.getPointerCount(); i++) {
	    	 this.type = type;
	    	 
	    	 this.pointerID = event.getPointerId(i);
	    	 
	    	 this.down_time = event.getDownTime();
			 this.event_time = event.getEventTime();
			 this.system_time = Helpers.getComplexDate();
			 
			 this.x_coord = event.getX(i);
			 this.y_coord = event.getY(i);
			 
			 this.pressure = event.getPressure(i);
			 this.size = event.getSize(i);
			 
			 this.touch_major = event.getTouchMajor(i);
			 this.touch_minor = event.getTouchMinor(i);
			 this.tool_major = event.getToolMajor(i);
			 this.tool_minor = event.getToolMinor(i);
			 this.tool_orientation = event.getOrientation(i);
			 
			 this.screen_orientation = screenOrientation;
			 this.keyboard_visible = keyboardVisible;
			
			 s += "CONTINUOUSDATA," + ContinuousData.toString(this)  + "\n";
	     }
		 
		 return s;		 
	 }
	 
	 @Override
	 public String toString() {
		 return ContinuousData.toString(this);
	 }
	 
	 public static String toString(ContinuousData data) {
		 String s = "";
		 
		 s += 	data.type + "," +
				data.pointerID + "," +
				data.down_time + "," +
				data.event_time + "," +
				data.system_time + "," + 
				data.x_coord + "," + 
				data.y_coord + "," + 
				data.pressure + "," + 
				data.size + "," + 
				data.touch_major + "," + 
				data.touch_minor + "," +
				data.tool_major + "," + 
				data.tool_minor + "," + 
				data.tool_orientation + "," + 
				data.screen_orientation + "," + 
				data.keyboard_visible;
		 
		 return s;		 
	 }

	 
	/**
	 * A special method to set all the coordinate values for normalising.
	 * @param screenSize
	 */
	public void setBounds(Point screenSize) {
		this.x_coord = screenSize.x;
		this.y_coord = screenSize.y;
	}
		
	public static String getHeader() {
		return header;
	}


}
