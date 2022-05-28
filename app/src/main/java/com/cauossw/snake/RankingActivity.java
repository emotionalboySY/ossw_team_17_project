package com.cauossw.snake;

import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;

public class RankingActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ranking);

        ImageButton backButton = findViewById(R.id.rank_bt_back);
        ArrayList<RankData> data = new ArrayList<>();
        RelativeLayout rankDataNotFound = findViewById(R.id.rank_layout_nodata);
        RecyclerView recyclerView = findViewById(R.id.rank_layout_content);

        backButton.setOnClickListener(view -> finish());

        try {
            FileInputStream fis = getApplicationContext().openFileInput("data.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    String[] lineData = line.split(",", -1);
                    Log.i("@@@@@@ RANKING ACTIVITY @@@@@@", line);
                    RankData singleData = new RankData();
                    if(lineData.length == 2) {
                        singleData.setName(lineData[0]);
                        singleData.setScore(lineData[1]);
                        data.add(singleData);
                        Log.d("RANKING DATA", singleData.getData());
                    }
                    line = reader.readLine();
                }

                Comparator<RankData> scoreDesc = (rankData, t1) -> Integer.compare(parseInt(t1.getScore()), parseInt(rankData.getScore()));

                data.sort(scoreDesc);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Data Loading Failed", Toast.LENGTH_SHORT).show();
                Log.e("Ranking", "Data Loading Failed");
            }

            fis.close();

            DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(decoration);

            recyclerView.setVisibility(View.VISIBLE);
            rankDataNotFound.setVisibility(View.GONE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            RankingViewAdapter adapter = new RankingViewAdapter(data);
            recyclerView.setAdapter(adapter);

        } catch (FileNotFoundException e) {
            Log.e("Ranking", "Data File Not Found");

            recyclerView.setVisibility(View.GONE);
            rankDataNotFound.setVisibility(View.VISIBLE);

            TextView restart = findViewById(R.id.rank_layout_nodata_startgame);

            restart.setOnClickListener(view -> {
                Intent startIntent = new Intent(RankingActivity.this, GameSingleActivity.class);
                startIntent.putExtra("data", "");
                startActivity(startIntent);
                finish();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
