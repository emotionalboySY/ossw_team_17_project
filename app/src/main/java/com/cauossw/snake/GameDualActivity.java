package com.cauossw.snake;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.cauossw.snake.databinding.ActivityGameDualBinding;


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

        gameView = activityGameDualBinding.GameView;
        gameView.setSize(DefaultConst.DUAL_WIDTH, DefaultConst.DUAL_HEIGHT);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
            Log.i(TAG, "handler, get message");

            bundle = new Bundle();
            bundle = msg.getData();

            Log.i(TAG, "dead int " + bundle.getInt("dead") + "");
            if (bundle.getInt("dead") == 1) {
                if (bundle.getInt("winnerNum") == -1) Log.i(TAG, "draw");
                else Log.i(TAG, "handler " + bundle.getInt("winnerNum") + " is win");
                showDeadDialog();
            } else { // 죽지 않은 경우 invalidate
                gameView.setBundle(bundle);
                Log.i(TAG, gameView.toString());
                gameView.invalidate();
            }
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
            Log.i(TAG,"Button RESTART");
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
        String endStr;
        activityGameDualBinding.inGameDeadPopup.setVisibility(View.VISIBLE);
        activityGameDualBinding.inGameDeadPopup.bringToFront();
        activityGameDualBinding.gameViewBlack.setAlpha(0.3f);

        if (bundle.getInt("winnerNum") == -1) endStr = "DRAW";
        else endStr = bundle.getInt("winnerNum") + " IS WIN";
        activityGameDualBinding.inGameDeadPopupScoreWinner.setText(endStr);

        activityGameDualBinding.inGameDeadPopupRestart.setOnClickListener(view -> {
            activityGameDualBinding.inGameDeadPopup.setVisibility(View.GONE);
            activityGameDualBinding.inGameDeadPopup.bringToFront();
            activityGameDualBinding.gameViewBlack.setAlpha(0f);
            thread = new GameThread(handler, gameView, PlayMode.Dual);
            thread.start();
            Log.i(TAG, "Restart After Death");
        });

        activityGameDualBinding.inGameDeadPopupExit.setOnClickListener(view -> {
            str = null;
            finish();
        });
    }
}
