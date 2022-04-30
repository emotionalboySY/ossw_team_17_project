package com.cauossw.snake;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private final static Logger LOG = Logger.getGlobal();

    Button up;
    Button down;
    Button left;
    Button right;
    Button start;
    Button restart;
    Button resume;
    Button pause;
    String str;

    static Handler handler;
    GameThread gameThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // test code
        up = findViewById(R.id.upButton);
        down = findViewById(R.id.downButton);
        left = findViewById(R.id.leftButton);
        right = findViewById(R.id.rightButton);
        start = findViewById(R.id.startButton);
        restart = findViewById(R.id.restartButton);
        resume= findViewById(R.id.resumeButton);
        pause = findViewById(R.id.pauseButton);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                LOG.info((String)msg.obj);
            }
        };

        gameThread = new GameThread(handler);

        // start, pause, restart 리스너 연결
        // snake 이동 관련 유저 인풋 리스너 연결
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameThread.setSnakeDir(Direction.UP);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameThread.setSnakeDir(Direction.DOWN);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameThread.setSnakeDir(Direction.LEFT);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameThread.setSnakeDir(Direction.RIGHT);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameThread.checkIsAtFirst()){
                    gameThread.start();
                }
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameThread.pause();
                gameThread = new GameThread(handler);
                gameThread.start();
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameThread.checkIsPaused() && !gameThread.checkIsLost()) {
                    gameThread = new GameThread(handler, str);
                    gameThread.start();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = gameThread.pause();
            }
        });
    }
}
