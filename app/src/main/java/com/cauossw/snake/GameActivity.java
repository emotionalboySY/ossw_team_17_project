package com.cauossw.snake;


import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.cauossw.snake.databinding.ActivityGameBinding;


public class GameActivity extends AppCompatActivity {

    private static String TAG = "GameActivity";

    //핸들러 내부 클래스
    private static class MyHandler extends Handler{
        public MyHandler(){}

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

        }
    }
    private static MyHandler handler;

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

        getDisplaySize(); //디스플레이 크기 얻어옴, 게임 뷰 크기를 정하기 위함
        gameView = new GameView(GameActivity.this);
        gameView.measure(displayWidth, displayWidth);

        handler = new MyHandler();
        if(thread == null) {
            thread = new GameThread(handler);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(thread == null) {
            thread = new GameThread(handler);
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
