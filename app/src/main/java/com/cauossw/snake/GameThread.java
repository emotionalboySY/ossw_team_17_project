package com.cauossw.snake;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

public class GameThread extends Thread {
    private final String TAG = "GameThread";
    private ArrayList<Snake> snakes = new ArrayList<Snake>();
    private ArrayList<Apple> apples = new ArrayList<Apple>();
    private int score;
    private PlayMode mode;
    private int sp=1;

    private boolean isPaused = false,
            isLost = false,
            isStart = false;

    private Handler handler;

    GameThread(Handler handler, PlayMode mode) {
        if (mode == PlayMode.Dual) { // dual
            snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_DUAL_1_X, DefaultConst.SNAKE_DUAL_1_Y, mode),
                    DefaultConst.SNAKE_DUAL_1_DIR));
            snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_DUAL_2_X, DefaultConst.SNAKE_DUAL_2_Y, mode),
                    DefaultConst.SNAKE_DUAL_2_DIR));

            mkApples(DefaultConst.SNAKE_DUAL_APPLE_NUM);
        } else { // single / auto
            if (mode == PlayMode.Single)
                snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_SINGLE_X, DefaultConst.SNAKE_SINGLE_Y, mode),
                        DefaultConst.SNAKE_SINGLE_DIR));
            else
                snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_SINGLE_X, DefaultConst.SNAKE_SINGLE_Y, mode),
                        DefaultConst.SNAKE_AUTO_SPEED, DefaultConst.SNAKE_SINGLE_DIR));

            mkApples(DefaultConst.SNAKE_SINGLE_APPLE_NUM);
        }

        this.score = 0;
        this.handler = handler;
        this.mode = mode;
    }

    GameThread(Handler handler, String gameInfo) {
        // 파싱
        this.handler = handler;

        String[] infoArray = gameInfo.split(" ");
        String[] snakeInfoArray = infoArray[0].split("#");
        String[] appleInfoArray = infoArray[1].split("#");

        this.score =9+ Integer.parseInt(infoArray[2]);
        this.mode = PlayMode.valueOf(infoArray[3]);

        int i;
        for (i = 0; i < snakeInfoArray.length; i++) this.snakes.add(new Snake(snakeInfoArray[i], mode));
        for (i = 0; i < appleInfoArray.length; i++) this.apples.add(new Apple(appleInfoArray[i], mode));
    }

    @Override
    public void run() { // 주기적으로 반복되는 부분
        while(!isPaused) {
            Log.i(TAG, getStatusStr());
            sendPositionMsg();
            // gameView.invalidate();

            // 처음 시작 전 1초 delay
            try {
                if (!isStart) {
                    Thread.sleep(1000);
                    isStart = true;
                } else {
                    Thread.sleep(snakes.get(0).getSpeed());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (mode == PlayMode.Auto) {
                // auto일 경우, 자동으로 change dir
                snakes.get(0).setDir(snakes.get(0).autoFindDir(apples.get(0)));
            }

            int i, eatenAppleNum = 0;
            for (i = 0; i < snakes.size(); i++) {
                if (moveSnakeAndTryEatApple(i)) eatenAppleNum++;
            }

            // 먹은 Apple 개수만큼 새 apple 생성
            mkApples(eatenAppleNum);

            // check dead condition
            if (mode == PlayMode.Dual) {
                if (checkDead(0) && checkDead(1)) { // draw
                    handleDead(-1);
                    break;
                } else if (checkDead(0)) {
                    handleDead(0);
                    break;
                } else if (checkDead(1)) {
                    handleDead(1);
                    break;
                }
            } else if (checkDead(0)) { // single / auto dead condition
                handleDead(0);
                break;
            }
        }
    }

    public void setSnakeDir(int index, Direction dir) {
        snakes.get(index).setDir(dir);
    }

    public void speedDown(int index) {
        sp=1;
        snakes.get(index).speedDown(); }

    public void speedUp(int index) {
        sp=0;
        snakes.get(index).speedUp(); }
    // snakes.set(0,DefaultConst.SNAKE_SPEED);
    // snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_SINGLE_X, DefaultConst.SNAKE_SINGLE_Y, mode),
    //         DefaultConst.SNAKE_SINGLE_DIR));
    // DefaultConst.SNAKE_SPEED=DefaultConst.SNAKE_SPEED+300;

    //}

    public String pause() {
        isPaused = true;
        return getStatusStr();
    }

    public int getScore() {
        return score;
    }

    public ArrayList<ArrayList<Coordinate>> getSnakesPositions() {
        ArrayList<ArrayList<Coordinate>> snakesPositions = new ArrayList<ArrayList<Coordinate>>();

        int i;
        for (i = 0; i < snakes.size(); i++) snakesPositions.add(snakes.get(i).getPositions());
        return snakesPositions;
    }

    public ArrayList<Coordinate> getApplesPosition() {
        ArrayList<Coordinate> applesPositions = new ArrayList<Coordinate>();

        int i;
        for (i = 0; i < apples.size(); i++) applesPositions.add(apples.get(i).getPosition());
        return applesPositions;
    }

    public String getStatusStr() {
        String snakesStr = "", applesStr = "";

        int i;
        for (i = 0; i < snakes.size(); i++) {
            snakesStr += snakes.get(i).getStatusStr();
            if (i != snakes.size() - 1) snakesStr += "#";
        }
        for (i = 0; i < apples.size(); i++) {
            applesStr += apples.get(i).getPositionStr();
            if (i != apples.size() - 1) applesStr += "#";
        }

        return snakesStr + " " + applesStr + " " + this.score + " " + this.mode.toString();
    }

    public boolean checkIsPaused() {
        return isPaused;
    }

    public boolean checkIsLost() {
        return isLost;
    }

    private boolean moveSnakeAndTryEatApple(int snakeIndex) {
        boolean isAppleEaten = false;

        snakes.get(snakeIndex).addHead(); // 이후 apple 먹지 않을 경우 꼬리 제거 필요

        int eatableAppleIndex = getEatableAppleIndex(snakeIndex);
        if (eatableAppleIndex == -1) { // apple 못 먹은 경우, 꼬리 제거

            snakes.get(snakeIndex).delTail();
            Log.i(TAG, "꼬리 원복");
        } else { // 특정 index의 apple 먹을 수 있는 경우
            Log.i(TAG,(snakeIndex + 1) + " eat apple");

            if (mode != PlayMode.Dual){
                if(sp==0) {
                    score=score+2;}else score++; }//nnnnnnnnnnnnnnbjhjgcvnfxdzsdxcfvgb

            // apple 먹음
            apples.remove(eatableAppleIndex);
            isAppleEaten = true;
        }

        return isAppleEaten;
    }

    private boolean checkDead(int snakeIndex) {
        boolean shouldBreak = false;

        if (snakes.get(snakeIndex).isDead()
                || (mode == PlayMode.Dual && snakes.get(snakeIndex).canEat(snakes.get((snakeIndex + 1) % 2)))) {
            Log.i(TAG,(snakeIndex + 1) + " is Dead");
            shouldBreak = true;
        }

        return shouldBreak;
    }

    private void handleDead(int snakeIndex) {
        sendDeadMsg(snakeIndex);

        isLost = true;
        isPaused = true;
    }

    private void sendPositionMsg() {
        Message msg = handler.obtainMessage();
        Log.i(TAG,"position - 메세지 생성");
        Bundle bundle = new Bundle();
        bundle.putSerializable("snakes", getSnakesPositions());
        bundle.putSerializable("apples", getApplesPosition());

        if (mode != PlayMode.Dual)
            bundle.putSerializable("score", getScore());

        msg.setData(bundle);
        Log.i(TAG,"메세지에 번들 삽입");
        handler.sendMessage(msg);
        Log.i(TAG,"Bundle 전달");
    }

    private void sendDeadMsg(int snakeIndex) {
        Message deadMsg = handler.obtainMessage();
        Log.i(TAG,"dead - 메세지 생성");
        Bundle deadBundle = new Bundle();
        deadBundle.putInt("dead", 1);
        Log.i(TAG,"Bundle 전달");

        if (mode == PlayMode.Dual) {
            if (snakeIndex == -1) {
                // 두 뱀 동시에 죽는 경우
                deadBundle.putInt("winnerNum", -1);
            } else deadBundle.putInt("winnerNum", (snakeIndex + 1) % 2 + 1);
        } else
            deadBundle.putInt("score", getScore());

        deadMsg.setData(deadBundle);
        Log.i(TAG,"메세지에 번들 삽입");
        handler.sendMessage(deadMsg);
        Log.i(TAG,"Bundle 전달");
    }

    private int getEatableAppleIndex(int snakeIndex) {
        int i, appleIndex = -1;

        for (i = 0; i < apples.size(); i++) {
            appleIndex = snakes.get(snakeIndex).canEat(apples.get(i)) ? i : appleIndex;
            if (appleIndex != -1) break;
        }

        return appleIndex;
    }

    private void mkApple() {
        boolean isOkay = true;
        Apple apple;

        do { // snake 가 바로 apple 먹을 수 있는 경우, 다른 apple과 겹치는 경우, snake body 와 겹치는 경우 다시 생성 필요
            isOkay = true;
            apple = new Apple(Coordinate.random(mode));

            // 다른 apple과 position 겹치는 경우 check
            int i;
            for (i = 0; i < apples.size(); i++) {
                isOkay = isOkay && !apple.getPosition().equals(apples.get(i).getPosition());
            }

            // snake가 바로 먹을 수 있는 경우, snake body와 겹치는 경우 check
            for (i = 0; i < snakes.size(); i++) {
                isOkay = isOkay && !((snakes.get(i).canEat(apple))
                        || snakes.get(i).overlaps(apple.getPosition()));
            }
        } while(!isOkay);

        apples.add(apple);
    }

    private void mkApples(int appleNum) {
        int i;
        for (i = 0; i < appleNum; i++) mkApple();
    }
}
