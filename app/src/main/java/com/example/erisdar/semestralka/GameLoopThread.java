package com.example.erisdar.semestralka;

import android.graphics.Canvas;



public class GameLoopThread extends Thread
{
    static final long FPS = 60;
    private GameArea view;
    private boolean running = false;

    public GameLoopThread(GameArea view)
    {
        this.view = view;
    }

    public void setRunning(boolean run)
    {

        running = run;
    }

    @Override
    public void run()
    {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        long prew_time = 0;
        while (running)
        {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try
            {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder())
                {
                    view.onDraw(c, prew_time);
                }
            } finally
            {
                if (c != null)
                {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            prew_time = System.currentTimeMillis() - startTime;
            sleepTime = ticksPS - prew_time;
            try
            {
                if (sleepTime > 0)
                    sleep(sleepTime);
            } catch (Exception e) {}

        }

    }

}