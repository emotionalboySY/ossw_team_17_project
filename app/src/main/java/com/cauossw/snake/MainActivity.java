package com.cauossw.snake;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import android.view.View;

import com.cauossw.snake.databinding.ActivityGameBinding;
import com.cauossw.snake.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private TextView tv_start, tv_resume, tv_ranking;

    GameThread gameThread;

    private static String TAG = "MainActivity";

    private ActivityMainBinding activityMainBinding = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_start = (TextView) findViewById(R.id.main_bt_start);
        tv_resume = (TextView) findViewById(R.id.main_bt_resume);
        tv_ranking = (TextView) findViewById(R.id.main_bt_ranking);

        tv_ranking.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, RankingActivity.class);
            startActivity(intent);
        });
        /*
        FileOutputStream fos;
        String strFileContents = "Sam,30,10min 59sec";
        try {
            fos = openFileOutput("data.txt", MODE_PRIVATE);

            fos.write(strFileContents.getBytes());

            fos.close();
        } catch (IOException e) {
            Log.e("MAINACTIVITY", e.toString());
        }
        */

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.mainBtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });
    }
}
