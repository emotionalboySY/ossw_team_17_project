package com.cauossw.snake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv_start, tv_resume, tv_ranking;
    private FragmentManager fragmentManager;
    private RankingFragment rankingFragment;
    private MainFragment mainFragment;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        tv_start = (TextView) findViewById(R.id.main_bt_start);
        tv_resume = (TextView) findViewById(R.id.main_bt_resume);
        tv_ranking = (TextView) findViewById(R.id.main_bt_ranking);

        rankingFragment = new RankingFragment();
        mainFragment = new MainFragment();

        transaction = fragmentManager.beginTransaction();

        tv_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(2);
            }
        });
    }

    public void changeFragment(int index) {
        switch(index) {
            case 1:
                transaction.replace(R.id.main_fragment, mainFragment).addToBackStack(null).commit();
                break;
            case 2:
                transaction.replace(R.id.main_fragment, rankingFragment).addToBackStack(null).commit();
                break;
        }
    }

    public interface onKeyBackPressedListener {
        void onBackKey();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener) {
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBackKey();
        } else {
            if(fragmentManager.getBackStackEntryCount() == 0) {
                Toast.makeText(getApplicationContext(), "Press back again to exit application.", Toast.LENGTH_SHORT).show();
            } else {
                super.onBackPressed();
            }
        }
    }
}