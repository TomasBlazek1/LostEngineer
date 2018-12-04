package com.example.erisdar.semestralka;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_window);
    }

    public void onClick(View v)
    {
        Intent intent = new Intent(this, GameWindow.class);
        startActivity(intent);
    }

}
