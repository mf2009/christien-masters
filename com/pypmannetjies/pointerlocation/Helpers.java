package com.pypmannetjies.pointerlocation;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.view.MotionEvent;

public class Helpers {

	public static String toString(MotionEvent event) {
		String s =	"\n Action: " + event.getAction() + 
					"\n X:" + event.getX() + " Y:" + event.getY() + 
					"\n Size:" + event.getSize() +
					"\n Pressure: " + event.getPressure() +
					"\n DownTime: " + event.getDownTime() +
					"\n EventTime: " + event.getEventTime() +
					"\n PointerCount: " + event.getPointerCount() +
					"\n MetaState: " + event.getMetaState() +
					"\n XPrecision: " + event.getXPrecision() +
					"\n YPrecision: " + event.getYPrecision() +
					"\n DeviceID: " + event.getDeviceId() +
					"\n EdgeFlags: " + event.getEdgeFlags();
		
		return s;
	}
	
	public static String getComplexDate() {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("MM.dd;hh:mm:ss:SSS");
	    return ft.format(dNow);
	}
	
	public static String getSimpleDate() {
		Date dNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("MM_dd");
	    return ft.format(dNow);
	}
	
	public static String getComplexDateFromMilliseconds(long milliseconds) {
		Date time = new Date(milliseconds);
		SimpleDateFormat ft = new SimpleDateFormat ("MM.dd;hh:mm:ss:SSS");
	    return ft.format(time);
	}
	
}
