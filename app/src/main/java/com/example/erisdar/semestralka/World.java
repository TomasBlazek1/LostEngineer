package com.example.erisdar.semestralka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

enum Tile
{
    GROUND, STONE
}



public class World {
    public Player player;
    private int TILE_DIM = 16;
    public double xCenter, yCenter;
    int tiledim = 16;
    private Tile[] map;
    private Bitmap src_ground;
    private Bitmap src_stone;
    private Bitmap ground;
    private Bitmap stone;
    private Bitmap playerGFX;
    private Paint black;
    private Paint marker;
    private Paint gray;
    public int draw_left, draw_top;
    float[] dilutedHeightMap;

    public World(GameArea area)
    {
        Bitmap src_player = BitmapFactory.decodeResource(area.getResources(),R.drawable.dudeguy);
        player = new Player(500,500,src_player);
        xCenter = player.x;
        yCenter = player.y;
        black = new Paint();
        black.setColor(Color.BLACK);
        marker = new Paint();
        marker.setColor(0x18FFFFFF);
        gray = new Paint();
        gray.setColor(Color.GRAY);
        src_ground = BitmapFactory.decodeResource(area.getResources(),R.drawable.ground);
        src_stone = BitmapFactory.decodeResource(area.getResources(),R.drawable.stone);
        ground = src_ground;
        stone = src_stone;
        playerGFX = src_player;

        Random rand = new Random(System.nanoTime());
        float[] heightMap = new float[1000000];
        for( int i = 0; i < 1000000; i++)
        {
            heightMap[i] = rand.nextFloat();
        }
        dilutedHeightMap = new float[1000000];
        for(int i = 0 ; i < 15; i++) {
            for (int x = 1; x < 999; x++) {
                for (int y = 1; y < 999; y++) {
                    dilutedHeightMap[x + y * 1000] = heightMap[(x) + (y) * 1000] / 2 +
                            (heightMap[(x + 1) + (y) * 1000] +
                                    heightMap[(x - 1) + (y) * 1000] +
                                    heightMap[(x) + (y + 1) * 1000] +
                                    heightMap[(x) + (y - 1) * 1000]) / 8;

                }
            }
            for(int ind = 0; ind < 1000000; ind++)
            {
                heightMap[ind] = dilutedHeightMap[ind];
            }
        }



        map = new Tile[1000000];
        for(int i = 0; i < 1000000; i++)
        {
            if(dilutedHeightMap[i] > 0.52)
            {
                map[i] = Tile.GROUND;
            }
            else
            {
                map[i] = Tile.STONE;
            }
        }
    }

    public void Update()
    {
        if(xCenter != player.xScreen || yCenter != player.yScreen)
        {
            double distance = Math.sqrt((xCenter - player.xScreen) * (xCenter - player.xScreen) + (yCenter - player.yScreen) * (yCenter - player.yScreen));
            xCenter -= (xCenter - player.xScreen) * (distance/15);
            yCenter -= (yCenter - player.yScreen) * (distance/15);
        }
    }

    public void onDraw(Canvas c, int xDrift, int yDrift)
    {
        float w = ((float)c.getWidth())/tiledim;
        float h = ((float)c.getHeight())/tiledim;

        int xstart = (int)Math.ceil((int)xCenter - Math.ceil(Math.ceil(w)/2));
        int xend = (int)Math.ceil((int)xCenter + Math.ceil(Math.ceil(w)/2));
        int ystart = (int)Math.ceil((int)yCenter - Math.ceil(Math.ceil(h)/2));
        int yend = (int)Math.ceil((int)yCenter + Math.ceil(Math.ceil(h)/2));

        draw_left = -((xend - xstart) * tiledim - c.getWidth() + tiledim)/2 - (int)((xCenter%1) * tiledim);
        draw_top = -((yend - ystart) * tiledim - c.getHeight() + tiledim)/2 - (int)((yCenter%1) * tiledim);

        if(-draw_left > tiledim)
        {
            draw_left += tiledim;
            xstart++;
            xend++;
        }
        if(-draw_top > tiledim)
        {
            draw_top += tiledim;
            ystart++;
            yend++;
        }


        if(tiledim < 32) {
            for (int x = xstart; x <= xend; x++) {
                for (int y = ystart; y <= yend; y++) {

                    if (map[x + y * 1000] == Tile.STONE) {
                        c.drawRect(draw_left + tiledim * (x - xstart), draw_top + tiledim * (y - ystart), draw_left + tiledim * (x - xstart) + tiledim, draw_top + tiledim * (y - ystart) + tiledim, black);
                    } else {
                        c.drawRect(draw_left + tiledim * (x - xstart), draw_top + tiledim * (y - ystart), draw_left + tiledim * (x - xstart) + tiledim, draw_top + tiledim * (y - ystart) + tiledim, gray);
                    }
                }
            }
        }
        else
        {
            for (int x = xstart; x <= xend; x++) {
                for (int y = ystart; y <= yend; y++) {
                    if (map[x + y * 1000] == Tile.STONE) {
                        c.drawBitmap(stone,draw_left + tiledim * (x-xstart),draw_top + tiledim * (y - ystart), null);
                    } else {
                        c.drawBitmap(ground,draw_left + tiledim * (x-xstart),draw_top + tiledim * (y - ystart), null);
                    }
                }
            }

        }
        c.drawBitmap(playerGFX, draw_left + tiledim * (player.xScreen - xstart),
                                draw_top + tiledim * (player.yScreen - ystart),
                                null);
        c.drawRect( draw_left + tiledim * (player.xDest - xstart),
                    draw_top + tiledim * (player.yDest - ystart),
                    draw_left + tiledim * (player.xDest - xstart) + tiledim,
                    draw_top + tiledim * (player.yDest - ystart) + tiledim, marker);
    }

    public void onScale(float scale)
    {
        tiledim = (int)(TILE_DIM * scale);
        playerGFX = Bitmap.createScaledBitmap(player.src_player, tiledim, tiledim, false);
        if(scale >= 2)
        {

            stone = Bitmap.createScaledBitmap(src_stone, tiledim, tiledim, false);
            ground = Bitmap.createScaledBitmap(src_ground, tiledim, tiledim, false);
        }
    }
}
