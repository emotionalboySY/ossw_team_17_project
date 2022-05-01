package com.cauossw.snake;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.cauossw.snake.databinding.ActivityGameBinding;


public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";

    static Handler handler;
    static GameView gameView;


    private ActivityGameBinding activityGameBinding;
    private GameThread thread = null;

    private PopupDialog popupDialog;

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
    }


    public void pause(View v){
        status = thread.pause();
        popupDialog = new PopupDialog(GameActivity.this, status,thread);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //투명배경
        popupDialog.show();
        Log.i(TAG,"Button PAUSE");
    }
    public void resume(View v){
        if (thread.checkIsPaused() && !thread.checkIsLost()) {
            thread = new GameThread(handler, gameView, status);
            thread.start();
            Log.i(TAG,"Button RESUME");
        }
        if(popupDialog.isShowing()){
            popupDialog.dismiss();
        }
    }

    public void restart(View v){
        thread.pause();
        thread = new GameThread(handler, gameView);
        thread.start();
        Log.i(TAG,"Button RESTART");

        if(popupDialog.isShowing()){
            popupDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"START");
        if(thread == null) {
            thread = new GameThread(handler, gameView);
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
