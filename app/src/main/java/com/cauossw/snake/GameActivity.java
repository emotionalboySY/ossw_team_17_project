package com.cauossw.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cauossw.snake.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private static String TAG = "GameActivity";
    private ActivityGameBinding gameActivity;
    private static ThreadNakyoung thread = null;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);
        setContentView(gameView.getRootView()); 

        //create thread object
        thread = new ThreadNakyoung();
        thread.setName("Thread #"+ThreadNakyoung.getThreadNum());
    }



    @Override
    protected void onStart() {
        super.onStart();


        if(thread == null){
            thread = new ThreadNakyoung();
            thread.setName("Thread #"+ThreadNakyoung.getThreadNum());

        }

        thread.start();
    }
}