package com.cauossw.snake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    static String TAG = "MainActivity";
    static ThreadNakyoung thread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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