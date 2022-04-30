package com.cauossw.snake;

import android.annotation.SuppressLint;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Random;

public class Coordinate implements Cloneable, Serializable  {
    public static final int WIDTH = DefaultConst.WIDTH;
    public static final int HEIGHT = DefaultConst.HEIGHT;

    private int x;
    private int y;

    Coordinate() {
        this(WIDTH / 2, HEIGHT / 2);
    }

    Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate(String positionInfo) {
        // 파싱
        String[] position = positionInfo.split(",|<|>");
        this.x = Integer.parseInt(position[1]);
        this.y = Integer.parseInt(position[2]);
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
        return new Coordinate(this.x, this.y);
    }

    public static Coordinate random() {
        // WIDTH, HEIGHT 제한 내에서 새 좌표 랜덤하게 생성
        Random random = new Random();
        return new Coordinate((random.nextInt(WIDTH)), (random.nextInt(HEIGHT)));
    }

    public boolean isOutOfBound() {
        return (this.x < 0 || this.x >= WIDTH || this.y < 0 || this.y >= HEIGHT);
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
