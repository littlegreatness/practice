package com.lxj.spirt;

import android.R.integer;
import android.R.xml;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.text.method.MovementMethod;
import android.text.method.Touch;

public class Animal extends Sprite {
	int speed = 10;
	double dx = 0;
	double dy = 0;

	

	public Animal(Bitmap i, Point p,Point touch) {
		super(i, p);
		this.pos = p;
		// TODO Auto-generated constructor stub
		float x = touch.x - p.x;
		float y = touch.y - p.y;
		
		double  d= Math.sqrt(x*x+y*y);
		dx = x*speed/d;
		dy = y*speed/d;
	}
	public void move(){
		pos.x += dx;
		pos.y += dy;
	}
	
}
