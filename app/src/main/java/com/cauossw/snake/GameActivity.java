package com.cauossw.snake;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cauossw.snake.databinding.ActivityGameBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    private EditText edT;

    static Handler handler;
    static GameView gameView;


    private ActivityGameBinding activityGameBinding;
    private GameThread thread = null;
    private Bundle bundle;

    private String str = "";

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

                bundle = new Bundle();
                bundle = msg.getData();

                if(bundle.getInt("dead") == 1){
                    Log.i(TAG, "handler, is dead");
                    showDeadDialog();
                }

                if(bundle.getInt("score")!=0) {
                    activityGameBinding.score.setText("" + bundle.getInt("score"));
                }
                gameView.setBundle(bundle);
                Log.i(TAG, gameView.toString());

            }
        };

        //버튼 리스너 연결
        activityGameBinding.upButton.setOnClickListener(v -> {
            thread.setSnakeDir(Direction.UP);
            Log.i(TAG, "Button UP");
        });
        activityGameBinding.downButton.setOnClickListener(v -> {
            thread.setSnakeDir(Direction.DOWN);
            Log.i(TAG, "Button DOWN");

        });
        activityGameBinding.leftButton.setOnClickListener(v -> {
            thread.setSnakeDir(Direction.LEFT);
            Log.i(TAG, "Button LEFT");

        });
        activityGameBinding.rightButton.setOnClickListener(v -> {
            thread.setSnakeDir(Direction.RIGHT);
            Log.i(TAG, "Button RIGHT");
        });
        activityGameBinding.inGamePause.setOnClickListener(v -> {
            activityGameBinding.inGamePausePopup.setVisibility(View.VISIBLE);
            activityGameBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0.3f);
            str = thread.pause();
            Log.i(TAG,"Button PAUSE");
        });
        activityGameBinding.inGamePausePopupResume.setOnClickListener(v -> {
            activityGameBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            if (thread.checkIsPaused() && !thread.checkIsLost()) {
                thread = new GameThread(handler, gameView, str);
                thread.start();
                Log.i(TAG,"Button RESUME");
            }
        });
        activityGameBinding.inGamePausePopupRestart.setOnClickListener(v -> {
            activityGameBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            thread = new GameThread(handler, gameView);
            thread.start();
            activityGameBinding.score.setText(String.valueOf(0));
            Log.i(TAG,"Button RESTART");
        });
        activityGameBinding.inGamePausePopupSave.setOnClickListener(view -> {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("data", str);
            ed.apply();
            finish();
            Log.i(TAG,"Button EXIT");
        });
        activityGameBinding.inGamePausePopupExit.setOnClickListener(view -> {
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

    private void showDeadDialog(){
        activityGameBinding.inGameDeadPopup.setVisibility(View.VISIBLE);
        activityGameBinding.inGameDeadPopup.bringToFront();
        activityGameBinding.gameViewBlack.setAlpha(0.3f);
        activityGameBinding.inGameDeadPopupScoreContent.setText(String.valueOf(bundle.getInt("score")));

        activityGameBinding.inGameDeadPopupRestart.setOnClickListener(view -> {
            activityGameBinding.inGameDeadPopup.setVisibility(View.GONE);
            activityGameBinding.inGameDeadPopup.bringToFront();
            activityGameBinding.gameViewBlack.setAlpha(0f);
            thread = new GameThread(handler, gameView);
            thread.start();
            activityGameBinding.inGameDeadPopupScoreContent.setText(0);
            Log.i(TAG, "Restart After Death");
        });

        activityGameBinding.inGameDeadPopupExit.setOnClickListener(view -> {
            str = null;
            finish();
        });
        activityGameBinding.inGameDeadPopupRanking.setOnClickListener(view -> {
            edT = new EditText(getApplicationContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Submit to Ranking")
                    .setMessage("Write your name for ranking page.")
                    .setCancelable(false)
                    .setView(edT)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
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
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
}
