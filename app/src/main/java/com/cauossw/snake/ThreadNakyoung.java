package com.cauossw.snake;

import android.util.Log;

public class ThreadNakyoung extends Thread{

    static String TAG = "Threads";
    static int ThreadNum = 0;

    private GameView gameView = null;

    public ThreadNakyoung() {
        super();
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        int delay = 500;

        while(true) {
            Log.i(TAG,threadName + " has been started");

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i(TAG, " has been ended");
        }

    }

    public static int getThreadNum(){
        ThreadNum += 1;
        return ThreadNum;
    }

}
