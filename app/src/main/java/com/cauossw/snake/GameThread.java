package com.cauossw.snake;

import android.os.Handler;
import android.os.Message;

public class GameThread extends Thread {
    private Snake snake;
    private Apple apple;

    private int score;
    private boolean isPaused,
            isAtFirst,
            isLost;

    private Handler handler;

    GameThread(Handler handler) {
        initData(); // 멤버 변수 초기화
        this.handler = handler;
    }

    @Override
    public void run() { // 주기적으로 반복되는 부분
        while(!isPaused) {
            try {
                Thread.sleep(snake.getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            snake.addHead(); // 이후 apple 먹지 않을 경우 꼬리 제거 필요
            Message message1 = handler.obtainMessage();
            message1.what = 0;
            message1.obj = "add head";
            handler.sendMessage(message1);

            if (snake.isDead()) {
                Message message2 = handler.obtainMessage();
                message2.what = 0;
                message2.obj = "dead";
                handler.sendMessage(message2);

                lose();
            }
            if (snake.canEat(apple)) {
                Message message2 = handler.obtainMessage();
                message2.what = 0;
                message2.obj = "eat apple";
                handler.sendMessage(message2);

                score++;

                // 새 apple 생성
                while(true) { // snake가 바로 apple 먹을 수 있는 경우, 또는 snake body와 겹치는 경우 재생성
                    apple = new Apple(Coordinate.random());
                    if (!(snake.canEat(apple) || snake.overlaps(apple.getPosition()))) break;
                }
            } else {
                Message message2 = handler.obtainMessage();
                message2.what = 0;
                message2.obj = "don't eat apple";
                handler.sendMessage(message2);

                snake.delTail();
            }

            // draw func call message를 main thread로 전송
            Message message3 = handler.obtainMessage();
            message3.what = 0;
            message3.obj = "APPLE : " + snake.getPositionsStr() + " APPLE : " + apple.getPositionStr();
            handler.sendMessage(message3);
        }
    }

    public void setSnakeDir(Direction dir) {
        snake.setDir(dir);
    }

    @Override
    public void start() {
        if (isLost) return;
        if (isAtFirst) super.start();

        isAtFirst = false;
        isPaused = false;
    }

    public void pause() {
        isPaused = true;
    }

    public void lose() {
        isLost = true;
        isPaused = true;
    }

    public void restart() {
        // 일시중지된 상태에서만 게임 재시작 가능
        if (!isPaused) return;

        initData();
        start();
    }

    private void initData() {
        snake = new Snake(new Coordinate());

        while(true) { // snake가 바로 apple 먹을 수 있는 경우, snake body와 겹치는 경우 다시 생성 필요
            apple = new Apple(Coordinate.random());
            if (!(snake.canEat(apple) || snake.overlaps(apple.getPosition()))) break;
        }

        score = 0;
        isPaused = true;
        isAtFirst = true;
        isLost = false;
    }

    public int getScore() {
        return score;
    }

    public void saveGameStatus() {
        // ***
    }
}
