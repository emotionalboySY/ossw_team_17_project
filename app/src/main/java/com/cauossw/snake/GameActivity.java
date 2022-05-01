package com.cauossw.snake;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cauossw.snake.databinding.ActivityGameBinding;

import java.util.ArrayList;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";


    private ActivityGameBinding activityGameBinding;
    private GameView gameView;
    static Handler handler;
    private GameThread thread = null;

    private String str = "";
    private String status = "";


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameBinding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(activityGameBinding.getRoot());

        gameView = activityGameBinding.GameView;
        Log.i(TAG, "gameView 객체 생성, id:" + gameView.toString());

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handler, get message");

                Bundle bundle = new Bundle();
                bundle = msg.getData();
                gameView.setBundle(bundle);
                TextView scoreView = findViewById(R.id.score);
                scoreView.setText(bundle.getSerializable("score").toString());
                Log.i(TAG, gameView.toString());

            }
        };

        if (thread == null) {
            thread = new GameThread(handler, gameView);
        }

        //버튼 리스너 연결
        activityGameBinding.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.UP);
                Log.i(TAG, "Button UP");
            }
        });
        activityGameBinding.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.DOWN);
                Log.i(TAG, "Button DOWN");

            }
        });
        activityGameBinding.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.LEFT);
                Log.i(TAG, "Button LEFT");

            }
        });
        activityGameBinding.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.RIGHT);
                Log.i(TAG, "Button RIGHT");
            }
        });
        activityGameBinding.inGamePause.setOnClickListener(v -> {
            activityGameBinding.inGamePausePopup.setVisibility(View.VISIBLE);
            activityGameBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0.3f);
            str = thread.pause();
            Log.i(TAG,"Button PAUSE");
        });
        activityGameBinding.popupResume.setOnClickListener(v -> {
            if (thread.checkIsPaused() && !thread.checkIsLost()) {
                thread = new GameThread(handler, gameView, str);
                thread.start();
                Log.i(TAG,"Button RESUME");
            }
        });
        activityGameBinding.popupRestart.setOnClickListener(v -> {
            thread.pause();
            thread = new GameThread(handler, gameView);
            thread.start();
            Log.i(TAG,"Button RESTART");
        });
        activityGameBinding.popupSave.setOnClickListener(view -> {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("data", str);
            ed.apply();
            finish();
            Log.i(TAG,"Button EXIT");
        });
        activityGameBinding.popupExit.setOnClickListener(view -> {
            str = null;
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"START");

        Intent intent = getIntent();
        String str = intent.getStringExtra("data");

        if(thread == null) {
            thread = new GameThread(handler, gameView);
            if(str.isEmpty()) {
                thread = new GameThread(handler, gameView);
            } else {
                thread = new GameThread(handler, gameView, str);
            }
        }
        thread.start();
        Log.i(TAG,"스레드 시작");
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
