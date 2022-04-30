package com.cauossw.snake;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;

public class GameThread extends Thread {
    private Snake snake;
    private Apple apple;

    private int score;
    private boolean isPaused = false,
            isLost = false,
            isAtFirst = true;

    private Handler handler;

    GameThread(Handler handler) {
        // 멤버 변수 초기화
        snake = new Snake(new Coordinate());

        while(true) { // snake가 바로 apple 먹을 수 있는 경우, snake body와 겹치는 경우 다시 생성 필요
            apple = new Apple(Coordinate.random());
            if (!(snake.canEat(apple) || snake.overlaps(apple.getPosition()))) break;
        }

        this.score = 0;

        this.handler = handler;
    }

    GameThread(Handler handler, String gameInfo) {
        // 파싱
        this.handler = handler;

        String[] infoArray = gameInfo.split(" ");

        this.snake = new Snake(infoArray[0]);
        this.apple = new Apple(infoArray[1]);
        this.score = Integer.parseInt(infoArray[2]);
    }

    @Override
    public void run() { // 주기적으로 반복되는 부분
        while(!isPaused) {
            // *** main thread로 message 보내 canvas 출력 필요
            Message message3 = handler.obtainMessage();
            message3.what = 0;
            message3.obj = getStatusStr();
            handler.sendMessage(message3);

            try {
                Thread.sleep(snake.getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            snake.addHead(); // 이후 apple 먹지 않을 경우 꼬리 제거 필요
            // log
//            Message msgAddHead = handler.obtainMessage();
//            msgAddHead.what = 0;
//            msgAddHead.obj = "add head";
//            handler.sendMessage(msgAddHead);

            if (snake.canEat(apple)) {
                score++;

                // 새 apple 생성
                while(true) { // snake가 바로 apple 먹을 수 있는 경우, 또는 snake body와 겹치는 경우 재생성
                    apple = new Apple(Coordinate.random());
                    if (!(snake.canEat(apple) || snake.overlaps(apple.getPosition()))) break;
                }

                // log
//                Message msgEatApple = handler.obtainMessage();
//                msgEatApple.what = 0;
//                msgEatApple.obj = "eat apple";
//                handler.sendMessage(msgEatApple);
            } else {
                snake.delTail();

                // log
//                Message msgDelTail = handler.obtainMessage();
//                msgDelTail.what = 0;
//                msgDelTail.obj = "don't eat apple";
//                handler.sendMessage(msgDelTail);
            }

            if (snake.isDead()) {
                lose();

                // log
//                Message msgLose = handler.obtainMessage();
//                msgLose.what = 0;
//                msgLose.obj = "dead";
//                handler.sendMessage(msgLose);
            }
        }
    }

    public void setSnakeDir(Direction dir) {
        snake.setDir(dir);
    }

    public void start() {
        isAtFirst = false;
        super.start();
    }

    public String pause() {
        isPaused = true;
        return getStatusStr();
    }

    public int getScore() {
        return score;
    }

    public ArrayList<Coordinate> getSnakePositions() {
        return snake.getPositions();
    }

    public Coordinate getApplePosition() {
        return apple.getPosition();
    }

    public String getStatusStr() {
        return snake.getStatusStr() + " " + apple.getPositionStr() + " " + this.score;
    }

    public boolean checkIsPaused() {
        return isPaused;
    }

    public boolean checkIsAtFirst() {
        return isAtFirst;
    }

    public boolean checkIsLost() {
        return isLost;
    }

    private void lose() {
        isLost = true;
        isPaused = true;
    }
}
