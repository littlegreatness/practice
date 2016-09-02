package com.lxj.spirt;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;


public abstract class Sprite {
	// ͼƬ
	protected Bitmap img;
	// λ��
	protected Point pos;
	public Sprite(Bitmap i,Point p){
		this.img = i;
		this.pos = p;
		if(this.pos == null){
			this.pos = new Point(0,0);
		}
	}
	public void draw(Canvas canvas){
		if(img != null)
		canvas.drawBitmap(this.img, this.pos.x, this.pos.y, null);
	}
	public Point getPoint(){
		return this.pos;
	}
}
