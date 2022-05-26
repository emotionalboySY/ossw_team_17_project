package com.cauossw.snake;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Random;

public class Coordinate implements Cloneable, Serializable  {
    private int x;
    private int y;
    private PlayMode mode;

    Coordinate(int x, int y, PlayMode mode) {
        this.x = x;
        this.y = y;
        this.mode = mode;
    }

    Coordinate(String positionInfo, PlayMode mode) {
        // 파싱
        String[] position = positionInfo.split(",|<|>");
        this.x = Integer.parseInt(position[1]);
        this.y = Integer.parseInt(position[2]);
        this.mode = mode;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @SuppressLint("DefaultLocale")
    public String getStr() {
        return String.format("<%d,%d>", this.x, this.y);
    }

    public boolean equals(Object another) {
        return (another instanceof Coordinate
                && this.x == ((Coordinate)another).x && this.y == ((Coordinate)another).y);
    }

    @NonNull
    public Coordinate clone() {
        return new Coordinate(this.x, this.y, this.mode);
    }

    public static Coordinate random(PlayMode mode) {
        // WIDTH, HEIGHT 제한 내에서 새 좌표 랜덤하게 생성
        Random random = new Random();
        return new Coordinate((random.nextInt(DefaultConst.getWidth(mode))), (random.nextInt(DefaultConst.getHeight(mode))), mode);
    }

    public boolean isOutOfBound() {
        return (this.x < 0 || this.x >= DefaultConst.getWidth(mode) || this.y < 0 || this.y >= DefaultConst.getHeight(mode));
    }

    protected Coordinate getMovedPosition(Direction dir) {
        return getMovedPosition(dir, 1);
    }

    protected Coordinate getMovedPosition(Direction dir, int step) {
        Coordinate newPosition = this.clone();
        newPosition.move(dir, step);

        return newPosition;
    }

    private void move(Direction dir, int step) {
        if (dir == Direction.UP) this.y = this.y - step;
        else if (dir == Direction.DOWN) this.y = this.y + step;
        else if (dir == Direction.LEFT) this.x = this.x - step;
        else if (dir == Direction.RIGHT) this.x = this.x + step;
    }
}
