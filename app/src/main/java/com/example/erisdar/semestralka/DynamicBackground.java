package com.example.erisdar.semestralka;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class DynamicBackground extends View {
    private Bitmap model;
    private Canvas canvas;
    private Paint p;
    private Random rand;
    private Paint blur;
    private Bitmap title;
    public DynamicBackground(Context c, AttributeSet attr)
    {
        super(c, attr);
        model = null;
        p = new Paint();
        rand = new Random();
        setWillNotDraw(false);
        blur = new Paint();
        title = BitmapFactory.decodeResource(getResources(), R.drawable.title);
    }

    @Override
    public void onDraw(Canvas c)
    {
        if(model == null) {
            model = Bitmap.createBitmap(c.getWidth() / 20, c.getHeight() / 20, Bitmap.Config.ARGB_8888);
        }
        canvas = new Canvas(model);

        for(int x = 0; x < model.getWidth(); x++)
        {
            for(int y = 0; y < model.getHeight();y++)
            {
                int r = rand.nextInt(256);
                p.setColor(Color.rgb(r,r,r));
                canvas.drawPoint(x,y,p);
            }
        }


        c.drawBitmap(model,null, new Rect(0,0,c.getWidth(),c.getHeight()),null);
        title = Bitmap.createScaledBitmap(title, c.getWidth(), c.getWidth(), false);
        c.drawBitmap(title, null, new Rect(0,0,c.getWidth(),c.getWidth()), null);

    }
}
