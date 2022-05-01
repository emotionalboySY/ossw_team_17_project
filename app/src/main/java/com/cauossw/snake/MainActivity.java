package com.cauossw.snake;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

 /*
import android.util.Log;
import java.io.FileOutputStream;
import java.io.IOException;
 */


public class MainActivity extends AppCompatActivity {

    private TextView tv_start, tv_load, tv_ranking, tv_exit;
    private String str;
    private final String TAG = "MAINACTIVITY";

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        str = pref.getString("data", "");
        Log.i(TAG + " onStart", str);
        Log.i(TAG + " onStart", String.valueOf(str.isEmpty()));

        if(str.isEmpty()) {
            tv_load.setVisibility(View.GONE);
        } else {
            tv_load.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_start = findViewById(R.id.main_bt_start);
        tv_load = findViewById(R.id.main_bt_load);
        tv_ranking = findViewById(R.id.main_bt_ranking);
        tv_exit = findViewById(R.id.main_bt_exit);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        str = pref.getString("data", "");
        Log.i(TAG, str);
        Log.i(TAG, String.valueOf(str.isEmpty()));
        if(str.isEmpty()) {
            tv_load.setVisibility(View.GONE);
        } else {
            tv_load.setVisibility(View.VISIBLE);
        }

        tv_start.setOnClickListener(view -> {
            Intent startIntent = new Intent(MainActivity.this, GameActivity.class);
            startIntent.putExtra("data", "");
            startActivity(startIntent);
        });
        tv_load.setOnClickListener(view -> {
            Intent loadIntent = new Intent(MainActivity.this, GameActivity.class);
            SharedPreferences.Editor ed = pref.edit();
            ed.remove("data");
            ed.apply();
            loadIntent.putExtra("data", str);
            startActivity(loadIntent);
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
