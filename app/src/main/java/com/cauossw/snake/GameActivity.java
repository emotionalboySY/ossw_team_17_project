package com.cauossw.snake;


import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cauossw.snake.databinding.ActivityGameBinding;


public class GameActivity extends AppCompatActivity {

    private static String TAG = "GameActivity";
    private ActivityGameBinding activityGameBinding;
    private GameView gameView;
    private GameThread thread = null;


    private int displayWidth;
    private int displayHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(activityGameBinding.getRoot());

        thread = new GameThread();
        getDisplaySize(); //디스플레이 크기 얻어옴
        gameView = new GameView(GameActivity.this);
        gameView.measure(displayWidth, displayWidth);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(thread == null){
            thread = new GameThread();
        }


        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getDisplaySize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        displayWidth = size.x;
        displayHeight = size.y;

        Log.i(TAG, "Display Size width:"+displayWidth+" height:"+displayHeight);
    }
}