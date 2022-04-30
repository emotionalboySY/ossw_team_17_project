package com.cauossw.snake;


import android.annotation.SuppressLint;
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

        gameView = activityGameBinding.GameView;
        Log.i(TAG,"gameView 객체 생성, id:"+gameView.toString());

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.i(TAG, "handler, get message");
//                if(msg.obj.getClass().getName().equals(Snake.class.getName())){
//                    Log.i(TAG,"Message obj: "+Snake.class.getName());
//                    Log.i(TAG,"Snake length: "+((Snake) msg.obj).getLength());
//                    gameView.setSnake((Snake)msg.obj);
//
//                }else if(msg.obj.getClass().getName().equals(Apple.class.getName())){
//                    Log.i(TAG,"Message obj: "+Apple.class.getName());
//                    gameView.setApple((Apple)msg.obj);
//                }
                Bundle bundle = new Bundle();
                bundle = msg.getData();
                gameView.setBundle(bundle);
                Log.i(TAG,gameView.toString());

            }
        };

        if(thread == null){
            thread = new GameThread(handler,gameView);
        }

        //버튼 리스너 연결
        activityGameBinding.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.UP);
                Log.i(TAG,"Button UP");
            }
        });

        activityGameBinding.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.DOWN);
                Log.i(TAG,"Button DOWN");

            }
        });
        activityGameBinding.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.LEFT);
                Log.i(TAG,"Button LEFT");

            }
        });
        activityGameBinding.rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.setSnakeDir(Direction.RIGHT);
                Log.i(TAG,"Button RIGHT");
            }
        });

        activityGameBinding.restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thread.pause();
                thread = new GameThread(handler, gameView);
                thread.start();
                Log.i(TAG,"Button RESTART");
            }
        });
        activityGameBinding.resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thread.checkIsPaused() && !thread.checkIsLost()) {
                    thread = new GameThread(handler, gameView, str);
                    thread.start();
                    Log.i(TAG,"Button RESUME");
                }
            }
        });
        activityGameBinding.pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = thread.pause();
                Log.i(TAG,"Button PAUSE");
            }
        });
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
