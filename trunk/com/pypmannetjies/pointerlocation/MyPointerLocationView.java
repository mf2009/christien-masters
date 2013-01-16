package com.pypmannetjies.pointerlocation;

import com.pypmannetjies.pointerlocation.AwesomeGestureListener.GestureType;

import android.content.Context;
import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class MyPointerLocationView extends com.android.internal.widget.PointerLocationView {
	
	GestureData gestureData;
	double endOfLastGestureTime;
	AwesomeGestureListener awesome;
	GestureDetector detector;
	Context theContext;
	
	public MyPointerLocationView(Context c) {
		super(c);
		this.theContext = c;
		DataRecorder.openFile("touch_data" + System.currentTimeMillis() + ".csv", c);
		endOfLastGestureTime = -1;
		
		awesome = new AwesomeGestureListener();
		detector = new GestureDetector(awesome);
	}

	@Override
	public void addTouchEvent(MotionEvent event) {
		DataRecorder.openFile("touch_data" + System.currentTimeMillis() + ".csv", theContext); // Will check if file exists (hack)
		detector.onTouchEvent(event);
		
		int action = event.getAction(); 
		
		if (action == MotionEvent.ACTION_DOWN) {
			gestureData = new GestureData(GestureType.UNKNOWN, 0); 
			gestureData.recordStartTime();
			gestureData.addMotionData(event);				
		}
		else if (action == MotionEvent.ACTION_MOVE) {
			gestureData.addMotionData(event);
		}
		else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
			gestureData.setType(awesome.getLastGestureType());			
			gestureData.recordEndTime();
			gestureData.addMotionData(event);
			gestureData.setFinalData(event, getResources().getConfiguration().orientation, getInterstrokeTime());
			
			DataRecorder.addToFile(gestureData);
		}
		
		super.addTouchEvent(event);
	}
	
	/**
	 * Find the time that elapsed since the previous motionEvent completed. 
	 * @return The inter-stroke time
	 */
	//TODO: test this
	private double getInterstrokeTime() {
		double interstrokeTime;
		if (endOfLastGestureTime > 0)
			interstrokeTime = System.currentTimeMillis() - endOfLastGestureTime;
		else 
			interstrokeTime = 0;
		
		endOfLastGestureTime = System.currentTimeMillis();
		
		return interstrokeTime;
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		
	}
	
}
