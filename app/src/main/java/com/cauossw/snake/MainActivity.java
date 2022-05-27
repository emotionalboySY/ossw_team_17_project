package com.cauossw.snake;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cauossw.snake.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private String str;
    private final String TAG = "MainActivity";

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        str = pref.getString("data", "");
        Log.i(TAG + " onStart", str);
        Log.i(TAG + " onStart", String.valueOf(str.isEmpty()));

        if(str.isEmpty()) {
            activityMainBinding.mainBtLoad.setVisibility(View.GONE);
        } else {
            activityMainBinding.mainBtLoad.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        str = pref.getString("data", "");
        Log.i(TAG, str);
        Log.i(TAG, String.valueOf(str.isEmpty()));
        if(str.isEmpty()) {
            activityMainBinding.mainBtLoad.setVisibility(View.GONE);
        } else {
            activityMainBinding.mainBtLoad.setVisibility(View.VISIBLE);
        }

        activityMainBinding.singlePlay.setOnClickListener(view -> {
            Intent startIntent = new Intent(MainActivity.this, GameSingleActivity.class);
            startIntent.putExtra("data", "");
            startActivity(startIntent);
        });

        activityMainBinding.dualPlay.setOnClickListener(view -> {
            Intent startIntent = new Intent(MainActivity.this, GameDualActivity.class);
            startIntent.putExtra("data", "");
            startActivity(startIntent);
        });

        activityMainBinding.autoPlay.setOnClickListener(view -> {
//            Intent startIntent = new Intent(MainActivity.this, GameAutoActivity.class);
//            startIntent.putExtra("data", "");
//            startActivity(startIntent);
        });

        activityMainBinding.mainBtLoad.setOnClickListener(view -> {
            Intent loadIntent = new Intent(MainActivity.this, GameSingleActivity.class);
            SharedPreferences.Editor ed = pref.edit();
            ed.remove("data");
            ed.apply();
            loadIntent.putExtra("data", str);
            startActivity(loadIntent);
        });
        activityMainBinding.mainBtRanking.setOnClickListener(view -> {
            Intent rankingIntent = new Intent(MainActivity.this, RankingActivity.class);
            startActivity(rankingIntent);
        });
        activityMainBinding.mainBtExit.setOnClickListener(view -> {
            moveTaskToBack(true);
            finishAndRemoveTask();

            System.exit(0);
        });
    }
}
