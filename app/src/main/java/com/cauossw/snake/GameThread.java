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
    private GameView gameView;
    private int score;
    private PlayMode mode;
    private boolean isPaused = false,
            isLost = false,
            isStart = false;

    private Handler handler;

    GameThread(Handler handler, GameView gameView, PlayMode mode) {
        if (mode == PlayMode.Dual) { // dual
            snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_DUAL_1_X, DefaultConst.SNAKE_DUAL_1_Y),
                    DefaultConst.SNAKE_DUAL_1_DIR));
            snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_DUAL_2_X, DefaultConst.SNAKE_DUAL_2_Y),
                    DefaultConst.SNAKE_DUAL_2_DIR));

            mkApples(DefaultConst.SNAKE_DUAL_APPLE_NUM);
        } else { // single / auto
            snakes.add(new Snake(new Coordinate(DefaultConst.SNAKE_SINGLE_X, DefaultConst.SNAKE_SINGLE_Y),
                    DefaultConst.SNAKE_SINGLE_DIR));

            mkApples(DefaultConst.SNAKE_SINGLE_APPLE_NUM);
        }

        this.score = 0;
        this.handler = handler;
        this.gameView = gameView;
        this.mode = mode;
    }

    GameThread(Handler handler, GameView gameView, String gameInfo) {
        // 파싱
        this.handler = handler;

        String[] infoArray = gameInfo.split(" ");
        String[] snakeInfoArray = infoArray[0].split("#");
        String[] appleInfoArray = infoArray[1].split("#");

        int i;
        for (i = 0; i < snakeInfoArray.length; i++) this.snakes.add(new Snake(snakeInfoArray[i]));
        for (i = 0; i < appleInfoArray.length; i++) this.apples.add(new Apple(appleInfoArray[i]));

        this.gameView = gameView;
        this.score = Integer.parseInt(infoArray[2]);
        this.mode = PlayMode.valueOf(infoArray[3]);
    }

    @Override
    public void run() { // 주기적으로 반복되는 부분
        while(!isPaused) {
            Log.i(TAG, getStatusStr());

            Message Message = handler.obtainMessage();
            Log.i(TAG,"메세지 생성");
            Bundle bundle = new Bundle();
            bundle.putSerializable("snake_1P", getSnakePositions(0));
            bundle.putSerializable("snake_2P", getSnakePositions(1));
            bundle.putSerializable("apple", getApplePosition(0));
            bundle.putSerializable("score", getScore());
            Message.setData(bundle);
            Log.i(TAG,"메세지에 번들 삽입");
            handler.sendMessage(Message);
            Log.i(TAG,"Bundle 전달");

            try {
                if (!isStart) {
                    Thread.sleep(3000);
                    isStart = true;
                } else {
                    Thread.sleep(snakes.get(0).getSpeed());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int snakeIndex, eatableAppleIndex;
            for (snakeIndex = 0; snakeIndex < snakes.size(); snakeIndex++) {
                snakes.get(snakeIndex).addHead(); // 이후 apple 먹지 않을 경우 꼬리 제거 필요

                eatableAppleIndex = getEatableAppleIndex(snakeIndex);
                if (eatableAppleIndex != -1) { // 특정 index의 apple 먹을 수 있는 경우
                    Log.i(TAG,(snakeIndex + 1) + " eat apple");
                    score++;

                    Message upScore = handler.obtainMessage();
                    Log.i(TAG,"메세지 생성");
                    Bundle upScoreBundle = new Bundle();
                    upScoreBundle.putInt("score", getScore());
                    Message.setData(upScoreBundle);
                    Log.i(TAG,"메세지에 번들 삽입");
                    handler.sendMessage(upScore);
                    Log.i(TAG,"Bundle 전달");

                    // 삭제 후 새 apple 생성
                    apples.remove(eatableAppleIndex);
                    mkApple(eatableAppleIndex);
                } else {
                    snakes.get(snakeIndex).delTail();
                    Log.i(TAG, "꼬리 원복");
                }

                // 뱀이 죽거나, dual mode에서 다른 뱀을 먹은 경우
                if (snakes.get(snakeIndex).isDead()
                    || (mode == PlayMode.Dual && snakes.get(snakeIndex).canEat(snakes.get((snakeIndex + 1) % 2)))) {
                    Log.i(TAG,(snakeIndex + 1) + " is Dead");

                    Message dead = handler.obtainMessage();
                    Log.i(TAG,"메세지 생성");
                    Bundle deadBundle = new Bundle();
                    deadBundle.putInt("dead", 1);
                    deadBundle.putInt("score", getScore());
                    Message.setData(deadBundle);
                    Log.i(TAG,"메세지에 번들 삽입");
                    handler.sendMessage(dead);
                    Log.i(TAG,"Bundle 전달");

                    lose();
                    break;
                }
            }

            //위치 절대 옮기면 안됨!
            gameView.invalidate();
        }
    }

    public void setSnakeDir(int index, Direction dir) {
        snakes.get(index).setDir(dir);
    }

    public String pause() {
        isPaused = true;
        return getStatusStr();
    }

    public int getScore() {
        return score;
    }

    public ArrayList<Coordinate> getSnakePositions(int snakeIndex) {
        return snakes.get(snakeIndex).getPositions();
    }

    public Coordinate getApplePosition(int appleIndex) {
        return apples.get(appleIndex).getPosition();
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

    private void lose() {
        isLost = true;
        isPaused = true;
    }

   private int getEatableAppleIndex(int snakeIndex) {
        int i, appleIndex = -1;

        for (i = 0; i < apples.size(); i++) {
            appleIndex = snakes.get(snakeIndex).canEat(apples.get(i)) ? i : appleIndex;
            if (appleIndex != -1) break;
        }

        return appleIndex;
    }

    private void mkApple(int appleIndex) {
        boolean isOkay = true;

        do { // snake 가 바로 apple 먹을 수 있는 경우, 다른 apple과 겹치는 경우, snake body 와 겹치는 경우 다시 생성 필요
            isOkay = true;
            apples.add(appleIndex, new Apple(Coordinate.random()));

            // 다른 apple과 position 겹치는 경우 check
            int i;
            for (i = 0; i < apples.size(); i++) {
                if (i != appleIndex) isOkay = isOkay
                        && !apples.get(appleIndex).getPosition().equals(apples.get(i).getPosition());
            }

            // snake가 바로 먹을 수 있는 경우, snake body와 겹치는 경우 check
            for (i = 0; i < snakes.size(); i++) {
                isOkay = isOkay && !((snakes.get(i).canEat(apples.get(appleIndex))
                        || snakes.get(i).overlaps(apples.get(appleIndex).getPosition())));
            }
        } while(!isOkay);
    }

    private void mkApples(int appleNum) {
        int i;
        for (i = 0; i < appleNum; i++) mkApple(i);
    }
}
