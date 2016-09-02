package com.lxj.game_1;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.lxj.spirt.Animal;
import com.lxj.spirt.Tree;

public class GameUI extends SurfaceView implements SurfaceHolder.Callback{
	
	 private SurfaceHolder mHolder;
	   // private Canvas mCanvas;
	    private boolean mIsDrawing ;
	    private Tree tree;
	    private Animal animal;
		private RenderThread renderThread;
	    private int[] arrays = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,R.drawable.pic6,R.drawable.pic7,R.drawable.pic8};
	    private CopyOnWriteArrayList<Animal> animals = new CopyOnWriteArrayList<Animal>();
	    
	    public GameUI(Context context) {
	        super (context,null);
	        initView();
	    }

	    public GameUI(Context context , AttributeSet attrs) {
	        super (context, attrs,0) ;
	        initView();
	    }

	    public GameUI(Context context , AttributeSet attrs, int defStyleAttr) {
	        super (context, attrs , defStyleAttr);
	        initView();
	    }

	    private void initView(){
	        mHolder = getHolder();
	        mHolder .addCallback(this) ;
	        renderThread = new RenderThread();
	        setFocusable( true);
	        setFocusableInTouchMode( true);
	        this.setKeepScreenOn(true);	       
	    }
	    
	  

	    @Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
	    	Point touchPos = new Point((int) event.getX(), (int) event.getY());
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				animal = tree.createAnimal(BitmapFactory.decodeResource(getResources(),arrays[getRandom()]), touchPos);
				//getResources(),arrays[getRandom()]
				animals.add(animal);
				
				break;
			case MotionEvent.ACTION_MOVE:
				animal = tree.createAnimal(BitmapFactory.decodeResource(getResources(),arrays[getRandom()]), touchPos);
				animals.add(animal);
				break;
			}
		
			return true;
		}

	/*	@Override
	    public void run() {
	        while (mIsDrawing){
	            draw();
	           // x+=1;
	        }
	    }*/
		private class RenderThread extends Thread {
			@Override
			public void run() {
				while (mIsDrawing) {

					Canvas canvas = null;
					try {
						canvas = mHolder.lockCanvas();
						 drawUI(canvas);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (canvas != null) {
							mHolder.unlockCanvasAndPost(canvas);
						}
					}
				}
			}
		}
	   /* private void draw(){
	        try {
	            mCanvas = mHolder.lockCanvas() ; //���Canvas����
	            drawUI(mCanvas);
	        } catch (Exception e){
	           
	        }finally {
	        if (mCanvas != null){
	                mHolder.unlockCanvasAndPost( mCanvas); //�Ի������ݽ����ύ
	            }
	        }
	    }*/
	    public int getRandom(){
	    	Random random = new Random();
	    	return random.nextInt(7);
	    }
		private void drawUI(Canvas canvas) {
			// TODO Auto-generated method stub
			Paint p = new Paint();
			p.setColor(Color.GRAY);
			canvas.drawRect(0, 0, getWidth(),getHeight(), p);
			
			tree.draw(canvas);
			drawAnimal(canvas);
			
		}
		private void drawAnimal(Canvas canvas){
			for(Animal animal:animals){
				if(animal !=null){
					animal.draw(canvas);
					animal.move();
				}
				//if(animal.getPoint().x >getWidth()||animal.getPoint().x<0||animal.getPoint().y<0||animal.getPoint().y>getHeight()){
				if (animal.getPoint().x >= getWidth() || animal.getPoint().y >= getHeight()) {	
				
						animals.remove(animal);
				//	}
				}
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			 mIsDrawing = true;
			 Point point = new Point(getWidth()/2-120,getHeight()/2-120);
			/* Bitmap b =BitmapFactory.decodeResource(getResources(), R.drawable.bg);
			 b.setWidth(100);
			 b.setHeight(100);*/
			 tree = new Tree(BitmapFactory.decodeResource(getResources(), R.drawable.bg), point);
				renderThread.start();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			 mIsDrawing = false;
		}

}
