package com.lxj.spirt;

import android.graphics.Bitmap;
import android.graphics.Point;

public class Tree extends Sprite{
	
	public Tree(Bitmap i, Point p) {
		super(i, p);
		// TODO Auto-generated constructor stub	
	}
	public Animal createAnimal(Bitmap faceImg, Point touchPos){
		Point p = new Point(pos.x+img.getWidth()/2,pos.y+img.getHeight()/2);
		Animal animal = new Animal(faceImg, p, touchPos);
		return animal;
	}
}
