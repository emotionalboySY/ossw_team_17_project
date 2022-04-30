package com.cauossw.snake;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cauossw.snake.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    //핸들러 내부 클래스
    private static class MyHandler extends Handler {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(activityGameBinding.getRoot());

        gameView = new GameView(GameActivity.this);

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

}
