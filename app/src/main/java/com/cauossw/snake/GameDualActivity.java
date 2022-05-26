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

import com.cauossw.snake.databinding.ActivityGameDualBinding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class GameDualActivity extends AppCompatActivity {

    private static final String TAG = "GameDualActivity";
    private EditText edT;

    static Handler handler;
    static GameView gameView;


    private ActivityGameDualBinding activityGameDualBinding;
    private GameThread thread = null;
    private Bundle bundle;

    private String str = "";

    @SuppressLint({"HandlerLeak", "WrongCall"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGameDualBinding = ActivityGameDualBinding.inflate(getLayoutInflater());
        setContentView(activityGameDualBinding.getRoot());

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);

        gameView = activityGameDualBinding.GameView;

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
                    activityGameDualBinding.score.setText("" + bundle.getInt("score"));
                }
                gameView.setBundle(bundle);
                Log.i(TAG, gameView.toString());

            }
        };

        //버튼 리스너 연결
        //1P 상하좌우 반전
        activityGameDualBinding.upButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.UP);
            Log.i(TAG, "Button UP");
        });
        activityGameDualBinding.downButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.DOWN);
            Log.i(TAG, "Button DOWN");

        });
        activityGameDualBinding.leftButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.LEFT);
            Log.i(TAG, "Button LEFT");

        });
        activityGameDualBinding.rightButton1P.setOnClickListener(v -> {
            thread.setSnakeDir(0, Direction.RIGHT);
            Log.i(TAG, "Button RIGHT");
        });

        //2P
        activityGameDualBinding.upButton2P.setOnClickListener(v -> {
            thread.setSnakeDir(1, Direction.UP);
            Log.i(TAG, "Button UP");
        });
        activityGameDualBinding.downButton2P.setOnClickListener(v -> {
            thread.setSnakeDir(1, Direction.DOWN);
            Log.i(TAG, "Button DOWN");

        });
        activityGameDualBinding.leftButton2P.setOnClickListener(v -> {
            thread.setSnakeDir(1, Direction.LEFT);
            Log.i(TAG, "Button LEFT");

        });
        activityGameDualBinding.rightButton2P.setOnClickListener(v -> {
            thread.setSnakeDir(1, Direction.RIGHT);
            Log.i(TAG, "Button RIGHT");
        });

        //etc button
        activityGameDualBinding.inGamePause.setOnClickListener(v -> {
            activityGameDualBinding.inGamePausePopup.setVisibility(View.VISIBLE);
            activityGameDualBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0.3f);
            str = thread.pause();
            Log.i(TAG,"Button PAUSE");
        });
        activityGameDualBinding.inGamePausePopupResume.setOnClickListener(v -> {
            activityGameDualBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameDualBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            if (thread.checkIsPaused() && !thread.checkIsLost()) {
                thread = new GameThread(handler, gameView, str);
                thread.start();
                Log.i(TAG,"Button RESUME");
            }
        });
        activityGameDualBinding.inGamePausePopupRestart.setOnClickListener(v -> {
            activityGameDualBinding.inGamePausePopup.setVisibility(View.GONE);
            activityGameDualBinding.inGamePausePopup.bringToFront();
            LinearLayout blackBG = findViewById(R.id.gameView_black);
            blackBG.setAlpha(0f);
            thread = new GameThread(handler, gameView, PlayMode.Dual);
            thread.start();
            activityGameDualBinding.score.setText(String.valueOf(0));
            Log.i(TAG,"Button RESTART");
        });
        activityGameDualBinding.inGamePausePopupSave.setOnClickListener(view -> {
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor ed = pref.edit();
            ed.putString("data", str);
            ed.apply();
            finish();
            Log.i(TAG,"Button EXIT");
        });
        activityGameDualBinding.inGamePausePopupExit.setOnClickListener(view -> {
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
                thread = new GameThread(handler, gameView, PlayMode.Dual);
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
        activityGameDualBinding.inGameDeadPopup.setVisibility(View.VISIBLE);
        activityGameDualBinding.inGameDeadPopup.bringToFront();
        activityGameDualBinding.gameViewBlack.setAlpha(0.3f);
        activityGameDualBinding.inGameDeadPopupScoreContent.setText(String.valueOf(bundle.getInt("score")));

        activityGameDualBinding.inGameDeadPopupRestart.setOnClickListener(view -> {
            activityGameDualBinding.inGameDeadPopup.setVisibility(View.GONE);
            activityGameDualBinding.inGameDeadPopup.bringToFront();
            activityGameDualBinding.gameViewBlack.setAlpha(0f);
            thread = new GameThread(handler, gameView, PlayMode.Dual);
            thread.start();
            activityGameDualBinding.inGameDeadPopupScoreContent.setText("0");
            Log.i(TAG, "Restart After Death");
        });

        activityGameDualBinding.inGameDeadPopupExit.setOnClickListener(view -> {
            str = null;
            finish();
        });
        activityGameDualBinding.inGameDeadPopupRanking.setOnClickListener(view -> {
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
