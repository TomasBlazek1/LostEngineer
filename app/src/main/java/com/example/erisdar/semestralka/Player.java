package com.example.erisdar.semestralka;

import android.graphics.Bitmap;

public class Player {
    private final int MS_PER_TURN = 500;
    public int x, y, xDest,yDest;
    public float move_progress, xScreen, yScreen;
    public Bitmap src_player;

    public Player(int x, int y, Bitmap src_player)
    {
        this.src_player = src_player;
        this.x = x;
        this.y = y;
        xScreen = x;
        yScreen = y;
        move_progress = 0;
    }

    public void Move(long frame_time)
    {
        if(xDest == x && yDest == y)
            return;

        move_progress += 0.05;

        if (xDest < x) {
            if(move_progress > 1) {
                move_progress = 0;
                x--;
            }
            xScreen = x - move_progress;
        } else if (xDest > x) {
            if(move_progress > 1) {
                move_progress = 0;
                x++;
            }
            xScreen = x + move_progress;
        } else if (yDest < y) {
            if(move_progress > 1) {
                move_progress = 0;
                y--;
            }
            yScreen = y - move_progress;
        } else if (yDest > y) {
            if(move_progress > 1) {
                move_progress = 0;
                y++;
            }
            yScreen = y + move_progress;
        }

    }
}
