package com.cauossw.snake;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public class Snake {

    private static final String TAG = "Snake";

    private ArrayList<Coordinate> body = new ArrayList<Coordinate>();
    private int speed;
    private Direction dir;

    Snake(Coordinate headPosition){
        this(headPosition, DefaultConst.SNAKE_LENGTH, DefaultConst.SNAKE_SPEED, DefaultConst.SNAKE_DIR);
    }

    Snake(Coordinate headPosition, int length, int speed, Direction dir) {
        this.speed = speed;
        this.dir = dir;

        Direction dirForBody; // 현재 진행방향의 반대 방향으로 나머지 몸통 생성
        if (dir == Direction.UP) dirForBody = Direction.DOWN;
        else if (dir == Direction.DOWN) dirForBody = Direction.UP;
        else if (dir == Direction.LEFT) dirForBody = Direction.RIGHT;
        else dirForBody = Direction.LEFT;

        int i;
        for (i = 1; i < length + 1; i++) body.add(headPosition.getMovedPosition(dirForBody, i));
    }

    Snake(String snakeInfo) {
        // 파싱
        String[] infoArray = snakeInfo.split("/");

        String[] positionArray = infoArray[0].split("-");
        int i;
        for (i = 0; i < positionArray.length; i++) body.add(new Coordinate(positionArray[i]));

        this.speed = Integer.parseInt(infoArray[1]);
        this.dir = Direction.valueOf(infoArray[2]);
    }

    public void setDir(Direction dir) {
        // 방향 전환 불가능한 경우 check
        if ((this.dir == Direction.UP && dir == Direction.DOWN)
            || (this.dir == Direction.DOWN && dir == Direction.UP)
            || (this.dir == Direction.LEFT && dir == Direction.RIGHT)
            || (this.dir == Direction.RIGHT && dir == Direction.LEFT))
            return;

        this.dir = dir;
    }

    public ArrayList<Coordinate> getPositions() {
        ArrayList<Coordinate> clonedBody = new ArrayList<>();

        int i;
        for (i = 0; i < body.size(); i++) clonedBody.add(body.get(i).clone());

        return clonedBody;
    }

    public String getStatusStr() {
        return getPositionsStr() + "/" + this.speed + "/" + this.dir.toString();
    }

    public String getPositionsStr() {
        String result = "";
        int i;

        for (i = 0; i < body.size(); i++) {
            result += body.get(i).getStr();
            if (i != body.size() - 1) result += "-";
        }

        return result;
    }

    public int getLength(){
        Log.i(TAG, "Snake Body Size: "+ body.size());
        return body.size();
    }

    public boolean canEat(Apple apple) {
        return this.body.get(0).equals(apple.getPosition());
    }

    public boolean overlaps(Coordinate position) { // (head를 뺀) body에 겹치는지 check
        boolean isInBody = false;

        int i;
        for (i = 1; i < body.size(); i++) {
            if (position.equals(body.get(i))) {
                isInBody = true;
                break;
            }
        }

        return isInBody;
    }

    public boolean isDead() {
        // head가 body와 겹치는지, 또는 bound 벗어나는지 check
        return overlaps(body.get(0)) || body.get(0).isOutOfBound();
    }

    public int getSpeed() {
        return this.speed;
    }

    protected void addHead() { // 현재 dir 방향으로 움직인 snake head 생성, body 맨 앞에 추가 (apple 먹지 않는다면 delTail() 호출 필요)
        body.add(0, body.get(0).getMovedPosition(dir));
    }

    protected void delTail() {
        body.remove(body.size() - 1);
    }
}