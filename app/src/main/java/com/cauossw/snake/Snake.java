package com.cauossw.snake;

import android.util.Log;

import java.sql.Array;
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

    public boolean isDead(Direction dir) { //auto모드를 위한 오버로딩 입력받은 포지션으로 갈 시 죽는지를 체크한다.
        // head 가 body 와 겹치는지, 또는 bound 벗어나는지 check
        return overlaps(body.get(0).getMovedPosition(dir)) || body.get(0).getMovedPosition(dir).isOutOfBound();
    }

    public boolean isDead(Coordinate coord) { //auto모드를 위한 오버로딩 입력받은 포지션으로 갈 시 죽는지를 체크한다.
        // head 가 body 와 겹치는지, 또는 bound 벗어나는지 check
        return overlaps(coord) || coord.isOutOfBound();
    }

    public int getSpeed() {
        return this.speed;
    }

    public Direction autoFindDir(Apple apple) {
        Direction dir = this.dir;
        ArrayList<Direction> dieDirection = new ArrayList<>();
        ArrayList<Direction> liveDirection = new ArrayList<>();

        int apple_x = apple.getPosition().getX();
        int apple_y = apple.getPosition().getY();

        int head_x = this.body.get(0).getX();
        int head_y = this.body.get(0).getY();
//        Log.i(TAG, "apple: "+apple_x+","+apple_y+" head: "+head_x+","+head_y);

        for (Direction direction: Direction.values()) {
            if(isDead(direction)){ dieDirection.add(direction);}
            else{liveDirection.add(direction);}
        }

        Log.i(TAG, dieDirection.toString());
        Log.i(TAG, liveDirection.toString());
        //1. 사과 위치에따른 방향설정
        if(Math.abs(apple_x - head_x)>=Math.abs(apple_y-head_y)){ //x방향이 가까움
            if(apple_x - head_x > 0){ dir = Direction.RIGHT;}
            else{dir = Direction.LEFT;}
        }else{ //y방향이 가까움
            if(apple_y - head_y >0){dir = Direction.DOWN;}
            else{dir = Direction.UP;}
        }

        if(liveDirection.contains(dir)){ //해당 방향으로 가면 장애물이 없는 경우
        }else{ //장애물이 있는경우
//            int min = 9999;
//            for(Direction direction: liveDirection){
//                //TODO
//                //장애물이 없는 방향으로 진행하되, 남은 방향중에 최선을 선택하는 알고리즘 구현
//                Coordinate temp = body.get(0).getMovedPosition(direction);
//                int tempMin = Math.min(Math.abs(temp.getX()-apple_x),Math.abs(temp.getY()-apple_y));
//                if(tempMin<min){
//                    min =tempMin;
//                    dir = direction;
//                }
//            }

            for(Direction direction: liveDirection){
                ArrayList<Direction> temp = new ArrayList<>();
                Coordinate coord = body.get(0).getMovedPosition(direction);
                for(Direction direct : Direction.values()){
                    if(!isDead(coord.getMovedPosition(direct))){
                        temp.add(direct);
                    }
                }
                if (temp.size() != 0){
                    dir = direction;
                }
            }
        }
        Log.i(TAG,"snake auto dir:"+dir);
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