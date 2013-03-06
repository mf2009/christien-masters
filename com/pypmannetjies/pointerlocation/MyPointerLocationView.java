package com.pypmannetjies.pointerlocation;

import com.pypmannetjies.pointerlocation.AwesomeGestureListener.GestureType;
import com.pypmannetjies.pointerlocation.GestureData.ScreenOrientation;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MyPointerLocationView extends com.android.internal.widget.PointerLocationView {
	
	GestureData gestureData;
	double endOfLastGestureTime;
	AwesomeGestureListener awesome;
	GestureDetector detector;
	Context theContext;
	
	public MyPointerLocationView(Context c) {
		super(c);
		this.theContext = c;
		
		writeHeadersAndBounds();
		
		endOfLastGestureTime = -1;
		awesome = new AwesomeGestureListener();
		detector = new GestureDetector(awesome);
	}

	private void writeHeadersAndBounds() {
		DataSpewer.spewString(GestureData.getHeader());		
		DataSpewer.spewString(ContinuousData.getHeader());
		ContinuousData cd = new ContinuousData();
		cd.setBounds(getScreenSize());
		DataSpewer.spewString("BOUNDS," + cd.toString());
	}
	
	@Override
	public void addTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		
		// Orientation + keyboard
		int intOrientation = getResources().getConfiguration().orientation;
        int intKeyboardHidden = getResources().getConfiguration().keyboardHidden;
        ScreenOrientation screenOrientation = (intOrientation == Configuration.ORIENTATION_PORTRAIT) ? ScreenOrientation.PORTRAIT :  ScreenOrientation.LANDSCAPE;
        boolean keyboardVisible = (intKeyboardHidden == Configuration.KEYBOARDHIDDEN_YES) ? false : true;
		
		// Continuous data
		ContinuousData continuousData = new ContinuousData();		
		String s = continuousData.setData(event, awesome.getLastGestureType(), screenOrientation, keyboardVisible);
		DataSpewer.spewString(s);
		
		// Gestures
		int action = event.getAction(); 

		if (action == MotionEvent.ACTION_DOWN) {
			gestureData = new GestureData(GestureType.UNKNOWN, -1); 
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
			gestureData.setFinalData(event, getInterstrokeTime(), screenOrientation, keyboardVisible);
			
			//DataRecorder.addToFile(gestureData);
			DataSpewer.spewGesture(gestureData);
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
	
	/**
	 * Find the screen size
	 * @return The screen size
	 */
	private Point getScreenSize() {
		WindowManager wm = (WindowManager) theContext.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		size.set(display.getWidth(), display.getHeight());
		return size;
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		
	}
	
}
