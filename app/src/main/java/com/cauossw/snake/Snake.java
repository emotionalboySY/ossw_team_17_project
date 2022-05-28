package com.cauossw.snake;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class Snake {

    private static final String TAG = "Snake";

    private ArrayList<Coordinate> body = new ArrayList<Coordinate>();
    private int speed;
    private Direction dir;
    private Direction lastMovedDir = null;

    Snake(Coordinate tailPosition, Direction dir){
        this(tailPosition, DefaultConst.SNAKE_LENGTH, DefaultConst.SNAKE_SPEED, dir);
    }

    Snake(Coordinate tailPosition, int length, int speed, Direction dir) {
        this.speed = speed;
        this.dir = dir;

        // 꼬리에서부터, 현재 진행방향으로 나머지 몸 생성
        int i;
        for (i = 0; i < length; i++) body.add(i, tailPosition.getMovedPosition(dir, i));
        Collections.reverse(body);
    }

    Snake(String snakeInfo, PlayMode mode) {
        // 파싱
        String[] infoArray = snakeInfo.split("/");

        String[] positionArray = infoArray[0].split("-");
        int i;
        for (i = 0; i < positionArray.length; i++) body.add(new Coordinate(positionArray[i], mode));

        this.speed = Integer.parseInt(infoArray[1]);
        this.dir = Direction.valueOf(infoArray[2]);
    }

    public void setDir(Direction dir) {
        // 방향 전환 불가능한 경우 check
        if ((this.lastMovedDir == Direction.UP && dir == Direction.DOWN)
            || (this.lastMovedDir == Direction.DOWN && dir == Direction.UP)
            || (this.lastMovedDir == Direction.LEFT && dir == Direction.RIGHT)
            || (this.lastMovedDir == Direction.RIGHT && dir == Direction.LEFT))
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

    public boolean canEat(Snake another) {
        return another.overlaps(this.body.get(0)) || this.body.get(0).equals(another.getPositions().get(0));
    }

    public boolean overlaps(Coordinate position) { // (head 를 뺀) body 에 겹치는지 check
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
        // head 가 body 와 겹치는지, 또는 bound 벗어나는지 check
        return overlaps(body.get(0)) || body.get(0).isOutOfBound();
    }

    public int getSpeed() {
        return this.speed;
    }

    public Direction autoFindDir(Apple apple) {
        Direction dir = this.dir;

        // implement

        return dir;
    }

    protected void addHead() { // 현재 dir 방향으로 움직인 snake head 생성, body 맨 앞에 추가 (apple 먹지 않는다면 delTail() 호출 필요)
        body.add(0, body.get(0).getMovedPosition(dir));
        lastMovedDir = dir;
    }

    protected void delTail() {
        body.remove(body.size() - 1);
    }
}