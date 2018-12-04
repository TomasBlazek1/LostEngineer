package com.example.erisdar.semestralka;
import com.example.erisdar.semestralka.GameArea;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_window);
        GameArea view = findViewById(R.id.view);

    }

    @Override
    protected void onResume()
    {
        GameArea view = findViewById(R.id.view);
        view.gameLoopThread.setRunning(true);
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        GameArea view = findViewById(R.id.view);
        view.gameLoopThread.setRunning(false);
        super.onPause();
    }
}
