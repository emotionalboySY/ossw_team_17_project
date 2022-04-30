package com.cauossw.snake;

public class GameThread extends Thread {
    private Snake snake;
    private Apple apple;

    private int score;
    private boolean isPaused,
            isAtFirst,
            isLost;

    GameThread() {
//        initData(); // 멤버 변수 초기화

        // @ start, pause, restart 리스너 연결
        // @ snake 이동 관련 유저 인풋 리스너 연결
    }

    @Override
    public void run() { // 주기적으로 반복되는 부분
//        while(!isPaused) {
//            try {
//                Thread.sleep(snake.getSpeed());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            snake.addHead(); // 이후 apple 먹지 않을 경우 꼬리 제거 필요
//
//            if (snake.isDead()) lose();
//            if (snake.canEat(apple)) {
//                score++;
//
//                // 새 apple 생성
//                while(true) { // snake가 바로 apple 먹을 수 있는 경우, 또는 snake body와 겹치는 경우 재생성
//                    apple = new Apple(Coordinate.random());
//                    if (!(snake.canEat(apple) || snake.overlaps(apple.getPosition()))) break;
//                }
//            } else snake.delTail();
//
//            // *** draw
//        }
    }

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
//        // 일시중지된 상태에서만 게임 재시작 가능
//        if (!isPaused) return;
//
//        initData();
//        start();
//    }
//
//                    private void initData() {
//        snake = new Snake(new Coordinate());
//
//        while(true) { // snake가 바로 apple 먹을 수 있는 경우, snake body와 겹치는 경우 다시 생성 필요
//            apple = new Apple(Coordinate.random());
//            if (!(snake.canEat(apple) || snake.overlaps(apple.getPosition()))) break;
//        }
//
//        score = 0;
//        isPaused = true;
//        isAtFirst = true;
//        isLost = false;
    }

    public void recordScore() {
        // ***
    }

    public void saveGameStatus() {
        // ***
    }
}
