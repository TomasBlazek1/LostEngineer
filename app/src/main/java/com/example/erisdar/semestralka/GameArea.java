package com.example.erisdar.semestralka;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ScaleGestureDetector;



public class GameArea extends SurfaceView implements GestureDetector.OnGestureListener
{
    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.0f;

    private SurfaceHolder holder;
    public GameLoopThread gameLoopThread;

    private int xDrift;
    private int yDrift;

    private int canvasWidth, canvasHeight;

    private int xDest,yDest;

    private Paint p;
    Bitmap b;
    World world;

    public GameArea(Context c, AttributeSet attrs)
    {
        super(c, attrs);
        mDetector = new GestureDetectorCompat(getContext(),this);
        world = new World(this);
        xDrift = 0;
        yDrift = 0;

        xDest = world.player.x;
        yDest = world.player.y;

        mScaleDetector = new ScaleGestureDetector(c, new ScaleListener());
        b = BitmapFactory.decodeResource(getResources(),R.drawable.dudeguy);
        setWillNotDraw(true);
        p = new Paint();
        p.setColor(Color.RED);
        p.setAntiAlias(true);
        p.setStrokeWidth(1);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStrokeCap(Paint.Cap.ROUND);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
                canvasHeight = height;
                canvasWidth = width;
            }
        });
    }

    public void Save(Bundle b)
    {


    }

    public void Update(long frame_time)
    {
        world.onScale(mScaleFactor);
        if(world.player.move_progress == 0)
        {
            world.player.xDest = xDest;
            world.player.yDest = yDest;
        }
        else
        {
            xDrift = (int)Math.floor(world.player.xScreen * world.tiledim) - xDrift;
            yDrift = (int)Math.floor(world.player.yScreen * world.tiledim) - yDrift;
        }
        world.player.Move(frame_time);

        world.Update();

    }

    public void onDraw(Canvas canvas, long frame_time) {
        long startTime = System.nanoTime();

        Update(frame_time);
        world.onDraw(canvas, xDrift, yDrift);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        canvas.drawText(Long.toString(duration/1000000),0,100, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);
        if (this.mDetector.onTouchEvent(ev)) {
            return true;
        }
        return true;
    }
    @Override
    public boolean onDown(MotionEvent event) {
        //Log.d(DEBUG_TAG,"onDown: " + event.toString());
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        //Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        //Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        int temp1 = Math.round((event.getX() - canvasWidth / 2.0f) / (float)world.tiledim);
        int temp2 =  Math.round((event.getY() - canvasHeight / 2.0f) / (float)world.tiledim);

        xDest = world.player.x + temp1;
        yDest = world.player.y + temp2;
        Log.d(DEBUG_TAG, "Move: " + xDest + ", "+ yDest);
        return true;
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.5f, Math.min(mScaleFactor, 10.0f));
            Log.d(DEBUG_TAG, "onScale: " + mScaleFactor);
            return true;
        }
    }
}

