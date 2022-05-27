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

import com.cauossw.snake.databinding.ActivityGameSingleBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class GameSingleActivity extends AppCompatActivity {

    private static final String TAG = "GameSingleActivity";
    private EditText edT;

    static Handler handler;
    static GameView gameView;


    private ActivityGameSingleBinding activityGameSingleBinding;
    private GameThread thread = null;
    private Bundle bundle;

    private String str = "";

    @SuppressLint({"HandlerLeak", "WrongCall"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameSingleBinding = ActivityGameSingleBinding.inflate(getLayoutInflater());
        setContentView(activityGameSingleBinding.getRoot());

        gameView = activityGameSingleBinding.GameView;
        gameView.setSize(DefaultConst.SINGLE_WIDTH, DefaultConst.SINGLE_HEIGHT);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handler, get message");

                bundle = new Bundle();
                bundle = msg.getData();

                Log.i(TAG, "daed int " + bundle.getInt("dead") + "");
                if(bundle.getInt("dead") == 1){
                    Log.i(TAG, "handler " + bundle.getInt("snakeIndex") + " is dead");
                    showDeadDialog();
                }

                if(bundle.getInt("score") != 0) {
                    activityGameSingleBinding.score.setText("" + bundle.getInt("score"));
                }
                gameView.setBundle(bundle);
                Log.i(TAG, gameView.toString());

            }
        };

        //버튼 리스너 연결
        //1P 상하좌우 반전
        activityGameSingleBinding.upButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.UP);
            Log.i(TAG, "Button UP");
        });
        activityGameSingleBinding.downButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.DOWN);
            Log.i(TAG, "Button DOWN");

        });
        activityGameSingleBinding.leftButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.LEFT);
            Log.i(TAG, "Button LEFT");

        });
        activityGameSingleBinding.rightButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.RIGHT);
            Log.i(TAG, "Button RIGHT");
        });


        //etc button
        activityGameSingleBinding.inGamePause.setOnClickListener(v -> {
            activityGameSingleBinding.inGamePausePopup.setVisibility(View.VISIBLE);
            activityGameSingleBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0.3f);
            str = thread.pause();
            Log.i(TAG,"Button PAUSE");
        });
        activityGameSingleBinding.inGamePausePopupResume.setOnClickListener(v -> {
            activityGameSingleBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameSingleBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            if (thread.checkIsPaused() && !thread.checkIsLost()) {
                thread = new GameThread(handler, gameView, str);
                thread.start();
                Log.i(TAG,"Button RESUME");
            }
        });

        activityGameSingleBinding.inGamePausePopupRestart.setOnClickListener(v -> {
            activityGameSingleBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameSingleBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            thread = new GameThread(handler, gameView, PlayMode.Single);
            thread.start();
            activityGameSingleBinding.score.setText(String.valueOf(0));
            Log.i(TAG,"Button RESTART");
        });
        activityGameSingleBinding.inGamePausePopupSave.setOnClickListener(view -> {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("data", str);
            ed.apply();
            finish();
            Log.i(TAG,"Button EXIT");
        });
        activityGameSingleBinding.inGamePausePopupExit.setOnClickListener(view -> {
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
        activityGameSingleBinding.inGameDeadPopup.setVisibility(View.VISIBLE);
        activityGameSingleBinding.inGameDeadPopup.bringToFront();
        activityGameSingleBinding.gameViewBlack.setAlpha(0.3f);
        activityGameSingleBinding.inGameDeadPopupScoreContent.setText(String.valueOf(bundle.getInt("score")));

        activityGameSingleBinding.inGameDeadPopupRestart.setOnClickListener(view -> {
            activityGameSingleBinding.inGameDeadPopup.setVisibility(View.GONE);
            activityGameSingleBinding.inGameDeadPopup.bringToFront();
            activityGameSingleBinding.gameViewBlack.setAlpha(0f);
            thread = new GameThread(handler, gameView, PlayMode.Single);
            thread.start();
            activityGameSingleBinding.score.setText(String.valueOf(0));
            Log.i(TAG, "Restart After Death");
        });

        activityGameSingleBinding.inGameDeadPopupExit.setOnClickListener(view -> {
            str = null;
            finish();
        });
        activityGameSingleBinding.inGameDeadPopupRanking.setOnClickListener(view -> {
            edT = new EditText(getApplicationContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submit to Ranking")
                    .setMessage("Write your name for ranking page.")
                    .setCancelable(false)
                    .setView(edT)
                    .setPositiveButton("Submit", (dialogInterface, i) -> {
                        String name = edT.getText().toString();
                        int score = bundle.getInt("score");
                        String scoreS = String.valueOf(score);
                        String inputData = name + "," + scoreS + "\n";
                        try {
                            File file = new File(getFilesDir(), "data.txt");
                            FileWriter fw = new FileWriter(file, true);
                            PrintWriter writer = new PrintWriter(fw, true);
                            writer.println(inputData);
                            Log.i(TAG, "Ranking Submitted: " + inputData);
                            writer.close();
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        finish();
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
}
