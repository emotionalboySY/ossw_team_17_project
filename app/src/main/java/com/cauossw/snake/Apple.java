package com.cauossw.snake;

public class Apple {
    private static final String TAG = "Apple";

    private Coordinate position;

    Apple(Coordinate position) {
        this.position = position;
    }

    Apple(String positionStr) {
        // 파싱
        this.position = new Coordinate(positionStr);
    }

    public Coordinate getPosition() {
        return this.position.clone();
    }

    public String getPositionStr() {
        return this.position.getStr();
    }
}
