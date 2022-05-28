package com.cauossw.snake;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cauossw.snake.databinding.ActivityGameAutoBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class GameAutoActivity extends AppCompatActivity {

    private static final String TAG = "GameSingleActivity";
    private EditText edT;

    static Handler handler;
    static GameView gameView;


    private ActivityGameAutoBinding activityGameAutoBinding;
    private GameThread thread = null;
    private Bundle bundle;

    private String str = "";

    @SuppressLint({"HandlerLeak", "WrongCall"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameAutoBinding = ActivityGameAutoBinding.inflate(getLayoutInflater());
        setContentView(activityGameAutoBinding.getRoot());

        gameView = activityGameAutoBinding.GameView;
        gameView.setSize(DefaultConst.SINGLE_WIDTH, DefaultConst.SINGLE_HEIGHT);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handler, get message");

                bundle = new Bundle();
                bundle = msg.getData();

                Log.i(TAG, "dead int " + bundle.getInt("dead") + "");
                if(bundle.getInt("dead") == 1){
                    showDeadDialog();
                } else { // 죽지 않은 경우 invalidate
                    if(bundle.getInt("score") != 0) {
                        activityGameAutoBinding.score.setText("" + bundle.getInt("score"));
                    }
                    gameView.setBundle(bundle);
                    Log.i(TAG, gameView.toString());
                    gameView.invalidate();
                }
            }
        };

        //버튼 리스너 연결

        //etc button
        activityGameAutoBinding.inGamePause.setOnClickListener(v -> {
            activityGameAutoBinding.inGamePausePopup.setVisibility(View.VISIBLE);
            activityGameAutoBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0.3f);
            str = thread.pause();
            Log.i(TAG,"Button PAUSE");
        });
        activityGameAutoBinding.inGamePausePopupResume.setOnClickListener(v -> {
            activityGameAutoBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameAutoBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            if (thread.checkIsPaused() && !thread.checkIsLost()) {
                thread = new GameThread(handler, gameView, str);
                thread.start();
                Log.i(TAG,"Button RESUME");
            }
        });

        activityGameAutoBinding.inGamePausePopupRestart.setOnClickListener(v -> {
            activityGameAutoBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameAutoBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            thread = new GameThread(handler, gameView, PlayMode.Single);
            thread.start();
            activityGameAutoBinding.score.setText(String.valueOf(0));
            Log.i(TAG,"Button RESTART");
        });

        activityGameAutoBinding.inGamePausePopupExit.setOnClickListener(view -> {
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
            if(str.isEmpty()) {
                thread = new GameThread(handler, gameView, PlayMode.Single);
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

    private void showDeadDialog(){
        activityGameAutoBinding.inGameDeadPopup.setVisibility(View.VISIBLE);
        activityGameAutoBinding.inGameDeadPopup.bringToFront();
        activityGameAutoBinding.gameViewBlack.setAlpha(0.3f);
        activityGameAutoBinding.inGameDeadPopupScoreContent.setText(String.valueOf(bundle.getInt("score")));

        activityGameAutoBinding.inGameDeadPopupRestart.setOnClickListener(view -> {
            activityGameAutoBinding.inGameDeadPopup.setVisibility(View.GONE);
            activityGameAutoBinding.inGameDeadPopup.bringToFront();
            activityGameAutoBinding.gameViewBlack.setAlpha(0f);
            thread = new GameThread(handler, gameView, PlayMode.Single);
            thread.start();
            activityGameAutoBinding.score.setText(String.valueOf(0));
            Log.i(TAG, "Restart After Death");
        });

        activityGameAutoBinding.inGameDeadPopupExit.setOnClickListener(view -> {
            str = null;
            finish();
        });
    }
}
