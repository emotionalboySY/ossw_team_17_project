package com.cauossw.snake;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

 /*
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
 */


public class MainActivity extends AppCompatActivity {

    private TextView tv_start, tv_resume, tv_ranking, tv_exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_start = findViewById(R.id.main_bt_start);
        tv_resume = findViewById(R.id.main_bt_resume);
        tv_ranking = findViewById(R.id.main_bt_ranking);
        tv_exit = findViewById(R.id.main_bt_exit);

        tv_start.setOnClickListener(view -> {
            Intent startIntent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(startIntent);
        });

        tv_ranking.setOnClickListener(view -> {
            Intent rankingIntent = new Intent(MainActivity.this, RankingActivity.class);
            startActivity(rankingIntent);
        });
        tv_exit.setOnClickListener(view -> {
            moveTaskToBack(true);
            finishAndRemoveTask();

            System.exit(0);
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
    }
}
