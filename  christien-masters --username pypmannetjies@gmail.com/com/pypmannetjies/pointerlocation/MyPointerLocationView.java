package com.pypmannetjies.pointerlocation;

import com.pypmannetjies.pointerlocation.AwesomeGestureListener.GestureType;

import android.content.Context;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Picture;
import android.os.SystemClock;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MyPointerLocationView extends com.android.internal.widget.PointerLocationView {
	
	//DataRecorder dataRecorder;
	GestureData gestureData;
	Bitmap houseImage;
	Paint paint;
	double lastX, lastY, nowX, nowY;
	AwesomeGestureListener awesome;
	GestureDetector detector;
	
	public MyPointerLocationView(Context c) {
		super(c);
		DataRecorder.openFile("touch_data" + System.currentTimeMillis() + ".txt", c);
		
		awesome = new AwesomeGestureListener();
		detector = new GestureDetector(awesome);
	}

	@Override
	public void addTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);
		
		int action = event.getAction(); 
		
		if (action == MotionEvent.ACTION_DOWN) {
			gestureData = new GestureData(GestureType.UNKNOWN, 0, -1); //User ID is not known -> -1
			gestureData.setStartTime(System.currentTimeMillis());
			gestureData.setFancyTime(Helpers.getComplexDate());
			gestureData.addMotionData(event);
		}
		else if (action == MotionEvent.ACTION_MOVE) {
			gestureData.addMotionData(event);
		}
		else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
			gestureData.setType(awesome.getLastGestureType());			
			gestureData.setEndTime(System.currentTimeMillis());
			gestureData.addMotionData(event);
			gestureData.setFinalData(event, getResources().getConfiguration().orientation);
			DataRecorder.addToFile(gestureData);
		}
		
		super.addTouchEvent(event);
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		
	}
	
}
