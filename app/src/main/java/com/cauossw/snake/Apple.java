package com.cauossw.snake;

public class Apple {

    private Coordinate position;

    Apple(Coordinate position) {
        this.position = position;
    }
    Apple(String positionStr, PlayMode mode) {
        // 파싱
        this.position = new Coordinate(positionStr, mode);
    }
    public Coordinate getPosition() {
        return this.position.clone();
    }
    public String getPositionStr() {
        return this.position.getStr();
    }
}
