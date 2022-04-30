package com.cauossw.snake;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cauossw.snake.databinding.ActivityGameBinding;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";


    private ActivityGameBinding activityGameBinding;
    private GameView gameView;
    static Handler handler;
    private GameThread thread = null;

    private String str = "";




    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(activityGameBinding.getRoot());

        gameView = new GameView(GameActivity.this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handler, get message");
                if(msg.obj.getClass().getName().equals(Snake.class.getName())){
                    gameView.setSnake((Snake)msg.obj);
                }else if(msg.obj.getClass().getName().equals(Apple.class.getName())){
                    gameView.setApple((Apple)msg.obj);
                }
            }
        };

        if(thread == null){
            thread = new GameThread(handler);
        }

        //버튼 리스너 연결
        activityGameBinding.upButton.setOnClickListener(v -> thread.setSnakeDir(Direction.UP));

        activityGameBinding.downButton.setOnClickListener(v -> thread.setSnakeDir(Direction.DOWN));
        activityGameBinding.leftButton.setOnClickListener(v -> thread.setSnakeDir(Direction.LEFT));
        activityGameBinding.rightButton.setOnClickListener(v -> thread.setSnakeDir(Direction.RIGHT));

        activityGameBinding.inGamePause.setOnClickListener(v -> {
            activityGameBinding.inGamePausePopup.setVisibility(View.VISIBLE);
            str = thread.pause();
        });
        activityGameBinding.popupResume.setOnClickListener(v -> {
            if (thread.checkIsPaused() && !thread.checkIsLost()) {
                thread = new GameThread(handler, str);
                thread.start();
            }
        });
        activityGameBinding.popupRestart.setOnClickListener(v -> {
            thread.pause();
            thread = new GameThread(handler);
            thread.start();
        });
        activityGameBinding.popupSave.setOnClickListener(view -> {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("data", str);
            ed.apply();
            finish();
        });
        activityGameBinding.popupExit.setOnClickListener(view -> {
            str = null;
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String str = intent.getStringExtra("data");

        if(thread == null) {
            if(str.isEmpty()) {
                thread = new GameThread(handler);
            } else {
                thread = new GameThread(handler, str);
            }
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
