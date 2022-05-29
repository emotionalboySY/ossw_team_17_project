package com.cauossw.snake;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Snake {

    private static final String TAG = "Snake";

    private ArrayList<Coordinate> body = new ArrayList<>();
    private int speed;
    private Direction dir;
    private Direction lastMovedDir = null;
    private ArrayList<Direction> auto_Route = new ArrayList<>();
    private int autoDirIndex = 0;
    private boolean isAutoDirChanged = false,
                    isCircled = false;
    private Direction[] dirClock = new Direction[] { Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT };
    private Direction[] dirCounterClock = new Direction[] { Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT };
    private ArrayList<Coordinate> simulatorBody = new ArrayList<>();

    Snake(Coordinate tailPosition, Direction dir) {
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

    public int getLength() {
        Log.i(TAG, "Snake Body Size: " + body.size());
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

    public boolean isAtEdge() {
        return body.get(0).getX() == 0 || body.get(0).getX() == DefaultConst.SINGLE_WIDTH - 1
                || body.get(0).getY() == 0 || body.get(0).getY() == DefaultConst.SINGLE_HEIGHT - 1;
    }

    public void setRouteToApple(Apple apple, boolean isStart) {

        auto_Route.clear();
        simulatorBody.clear();
        isAutoDirChanged = true;
        Direction dir = this.dir;
        Coordinate applePos = apple.getPosition();
        Coordinate snakePos = body.get(0);
        ApplePosition applePos4 = ApplePosition.Top;
        for(int i = body.size() - 1; i >= 0; i--) {
            simulatorBody.add(body.get(i));
        }

        if (!isStart) {
            isCircled = false;
            if(!isAtEdge()) {
                switch (dir) {
                    case UP:
                        if (autoCheckUp()) {
                            setRouteToEdge(Direction.UP);
                        } else if (autoCheckRight()) {
                            setRouteToEdge(Direction.RIGHT);
                        } else if (autoCheckLeft()) {
                            setRouteToEdge(Direction.LEFT);
                        }
                        break;
                    case RIGHT:
                        if (autoCheckRight()) {
                            setRouteToEdge(Direction.RIGHT);
                        } else if (autoCheckDown()) {
                            setRouteToEdge(Direction.DOWN);
                        } else if (autoCheckUp()) {
                            setRouteToEdge(Direction.UP);
                        }
                        break;
                    case DOWN:
                        if (autoCheckDown()) {
                            setRouteToEdge(Direction.DOWN);
                        } else if (autoCheckLeft()) {
                            setRouteToEdge(Direction.LEFT);
                        } else if (autoCheckRight()) {
                            setRouteToEdge(Direction.RIGHT);
                        }
                        break;
                    case LEFT:
                        if (autoCheckLeft()) {
                            setRouteToEdge(Direction.LEFT);
                        } else if (autoCheckUp()) {
                            setRouteToEdge(Direction.UP);
                        } else if (autoCheckDown()) {
                            setRouteToEdge(Direction.DOWN);
                        }
                        break;
                }
            }
            clockOrCounterClock();
            snakePos = simulatorBody.get(simulatorBody.size() - 1);
        }

        if(applePos.getX() > snakePos.getX() && applePos.getY() < snakePos.getY()) applePos4 = ApplePosition.RightTop;
        else if(applePos.getX() > snakePos.getX() && applePos.getY() > snakePos.getY()) applePos4 = ApplePosition.RightBottom;
        else if(applePos.getX() < snakePos.getX() && applePos.getY() > snakePos.getY()) applePos4 = ApplePosition.LeftBottom;
        else if(applePos.getX() < snakePos.getX() && applePos.getY() < snakePos.getY()) applePos4 = ApplePosition.LeftTop;
        else if(applePos.getX() == snakePos.getX() && applePos.getY() > snakePos.getY()) applePos4 = ApplePosition.Bottom;
        else if(applePos.getX() > snakePos.getX() && applePos.getY() == snakePos.getY()) applePos4 = ApplePosition.Right;
        else if(applePos.getX() < snakePos.getX() && applePos.getY() == snakePos.getY()) applePos4 = ApplePosition.Left;

        Log.d("********************NEXT APPLE POSITION********************", applePos4.name());

        int i;
        int xDiff = Math.abs(applePos.getX() - snakePos.getX());
        int yDiff = Math.abs(applePos.getY() - snakePos.getY());
        int turnPos = (int)((Math.random()*10000)%xDiff);

        switch(applePos4) {
            case Top:
                for(i = 0; i < yDiff; i++) {
                    auto_Route.add(Direction.UP);
                }
                break;
            case RightTop:
                for(i = 0; i < turnPos; i++) {
                    auto_Route.add(Direction.RIGHT);
                }
                for(i = 0; i < yDiff; i++) {
                    auto_Route.add(Direction.UP);
                }
                for(i = turnPos; i < xDiff; i++) {
                    auto_Route.add(Direction.RIGHT);
                }
                break;
            case Right:
                for(i = 0; i < xDiff; i++) {
                    auto_Route.add(Direction.RIGHT);
                }
                break;
            case RightBottom:
                if(turnPos == 0) {
                    turnPos = 1;
                }
                for(i = 0; i < turnPos; i++) {
                    auto_Route.add(Direction.RIGHT);
                }
                for(i = 0; i < yDiff; i++) {
                    auto_Route.add(Direction.DOWN);
                }
                for(i = turnPos; i < xDiff; i++) {
                    auto_Route.add(Direction.RIGHT);
                }
                break;
            case Bottom:
                for(i = 0; i < yDiff; i++) {
                    auto_Route.add(Direction.DOWN);
                }
                break;
            case LeftBottom:
                if(turnPos == 0) {
                    turnPos = 1;
                }
                for(i = 0; i < turnPos; i++) {
                    auto_Route.add(Direction.LEFT);
                }
                for(i = 0; i < yDiff; i++) {
                    auto_Route.add(Direction.DOWN);
                }
                for(i = turnPos; i < xDiff; i++) {
                    auto_Route.add(Direction.LEFT);
                }
                break;
            case Left:
                for(i = 0; i < xDiff; i++) {
                    auto_Route.add(Direction.LEFT);
                }
                break;
            case LeftTop:
                for(i = 0; i < turnPos; i++) {
                    auto_Route.add(Direction.LEFT);
                }
                for(i = 0; i < yDiff; i++) {
                    auto_Route.add(Direction.UP);
                }
                for(i = turnPos; i < xDiff; i++) {
                    auto_Route.add(Direction.LEFT);
                }
                break;
        }

        String[] auto_Route_Arr = new String[auto_Route.size()];
        int k;
        for(k = 0; k < auto_Route.size(); k++) {
            auto_Route_Arr[k] = auto_Route.get(k).name();
        }

        Log.d("********************SETROUTEFORAUTOMODE********************", Arrays.toString(auto_Route_Arr));

    }

    public Direction autoFindDir() {
        if(isAutoDirChanged) {
            autoDirIndex = 0;
            isAutoDirChanged = false;
        }
        Direction dir = this.dir;

        if (autoDirIndex < auto_Route.size()) {
            dir = auto_Route.get(autoDirIndex);
            autoDirIndex++;
        }

        return dir;
    }
    // 진행 방향으로 edge까지 갈 수 있는지 체크
    public boolean autoCheckUp() {
        Coordinate limit;
        int dist;
        int distMin = 40;
        int distMinIndex = -1;
        int leftBodyLength;

        limit = new Coordinate(body.get(0).getX(), 0, PlayMode.Auto);
        int i;
        for (i = 1; i < body.size(); i++) {
            if ((body.get(i).getX() == body.get(0).getX()) && (body.get(i).getY() >= limit.getY() && body.get(i).getY() < body.get(0).getY())) {
                dist = body.get(0).getY() - body.get(i).getY();
                if (distMin > dist) {
                    distMin = dist;
                    distMinIndex = i;
                }
            }
        }
        if(distMinIndex == -1) {
            return true;
        }
        else {
            dist = body.get(0).getY() - body.get(distMinIndex).getY();
            leftBodyLength = body.size() - distMinIndex;

            return leftBodyLength <= dist;
        }
    }

    public boolean autoCheckRight() {
        Coordinate limit;
        int dist;
        int distMin = 40;
        int distMinIndex = -1;
        int leftBodyLength;

        limit = new Coordinate(DefaultConst.SINGLE_WIDTH - 1, body.get(0).getY(), PlayMode.Auto);
        int i;
        for (i = 1; i < body.size(); i++) {
            if ((body.get(i).getY() == body.get(0).getY()) && (body.get(i).getX() <= limit.getX() && body.get(i).getX() > body.get(0).getX())) {
                dist = body.get(i).getX() - body.get(0).getX();
                if (distMin > dist) {
                    distMin = dist;
                    distMinIndex = i;
                }
            }
        }
        if(distMinIndex == -1) {
            return true;
        }
        else {
            dist = body.get(distMinIndex).getX() - body.get(0).getX();
            leftBodyLength = body.size() - distMinIndex;

            return leftBodyLength <= dist;
        }
    }

    public boolean autoCheckDown() {
        Coordinate limit;
        int dist;
        int distMin = 40;
        int distMinIndex = -1;
        int leftBodyLength;

        limit = new Coordinate(body.get(0).getX(), DefaultConst.SINGLE_HEIGHT - 1, PlayMode.Auto);
        int i;
        for (i = 1; i < body.size(); i++) {
            if ((body.get(i).getX() == body.get(0).getX()) && (body.get(i).getY() < limit.getY() && body.get(i).getY() > body.get(0).getY())) {
                dist = body.get(i).getY() - body.get(0).getY();
                if (distMin > dist) {
                    distMin = dist;
                    distMinIndex = i;
                }
            }
        }
        if(distMinIndex == -1) {
            return true;
        }
        else {
            dist = body.get(distMinIndex).getY() - body.get(0).getY();
            leftBodyLength = body.size() - distMinIndex;

            return leftBodyLength <= dist;
        }
    }

    public boolean autoCheckLeft() {
        int dist;
        int distMin = 40;
        int distMinIndex = -1;
        int leftBodyLength;

        int i;
        for (i = 1; i < body.size(); i++) {
            if ((body.get(i).getY() == body.get(0).getY()) && (body.get(i).getX() >= 0 && body.get(i).getX() < body.get(0).getX())) {
                dist = body.get(0).getX() - body.get(i).getX();
                if (distMin > dist) {
                    distMin = dist;
                    distMinIndex = i;
                }
            }
        }
        if(distMinIndex == -1) {
            return true;
        }
        else {
            dist = body.get(0).getX() - body.get(distMinIndex).getX();
            leftBodyLength = body.size() - distMinIndex;

            return leftBodyLength <= dist;
        }
    }

    // 빈 방항으로 edge까지 직진
    public void setRouteToEdge(Direction dir) {
        Coordinate temp;

        do {
            auto_Route.add(dir);
            switch (dir) {
                case UP:
                    temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX(), simulatorBody.get(simulatorBody.size() - 1).getY() - 1, PlayMode.Auto);
                    simulatorBody.add(temp);
                    break;
                case DOWN:
                    temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX(), simulatorBody.get(simulatorBody.size() - 1).getY() + 1, PlayMode.Auto);
                    simulatorBody.add(temp);
                    break;
                case LEFT:
                    temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX() - 1, simulatorBody.get(simulatorBody.size() - 1).getY(), PlayMode.Auto);
                    simulatorBody.add(temp);
                    break;
                case RIGHT:
                    temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX() + 1, simulatorBody.get(simulatorBody.size() - 1).getY(), PlayMode.Auto);
                    simulatorBody.add(temp);
                    break;
            }
        } while (simulatorBody.get(simulatorBody.size() - 1).getX() != 0
                && simulatorBody.get(simulatorBody.size() - 1).getX() != DefaultConst.SINGLE_WIDTH - 1
                && simulatorBody.get(simulatorBody.size() - 1).getY() != 0
                && simulatorBody.get(simulatorBody.size() - 1).getY() != DefaultConst.SINGLE_HEIGHT - 1);
    }

    // Edge 도착 후, 시계방향 반시계방향 결정
    public void clockOrCounterClock() {

        Direction dir = this.dir;
        int i;
        if(simulatorBody.get(simulatorBody.size() - 1).getX() == 0) {
            if(simulatorBody.get(simulatorBody.size() - 1).getY() == 0) {
                if(isAtEdge()) {
                    if(dir == Direction.LEFT) setRouteCounterClockWise(Direction.DOWN);
                    else setRouteClockWise(Direction.RIGHT);
                }
                else {
                    if (auto_Route.get(auto_Route.size() - 1) == Direction.LEFT)
                        setRouteCounterClockWise(Direction.DOWN);
                    else setRouteClockWise(Direction.RIGHT);
                }
            }
            else if(simulatorBody.get(simulatorBody.size() - 1).getY() == DefaultConst.SINGLE_HEIGHT - 1) {
                if(isAtEdge()) {
                    if(dir == Direction.LEFT) setRouteClockWise(Direction.UP);
                    else setRouteCounterClockWise(Direction.RIGHT);
                }
                else {
                    if (auto_Route.get(auto_Route.size() - 1) == Direction.LEFT)
                        setRouteClockWise(Direction.UP);
                    else setRouteCounterClockWise(Direction.RIGHT);
                }
            }
            else {
                for(i = 1; i < body.size(); i++) {
                    if(simulatorBody.get(simulatorBody.size() - (i + 1)).getX() == simulatorBody.get(simulatorBody.size() - 1).getX()) {
                        if(simulatorBody.get(simulatorBody.size() - (i + 1)).getY() > simulatorBody.get(simulatorBody.size() - 1).getY()) {
                            setRouteClockWise(Direction.UP);
                        }
                        else setRouteCounterClockWise(Direction.DOWN);
                    }
                }
                if(!isCircled) setRouteClockWise(Direction.UP);
            }
        }
        else if(simulatorBody.get(simulatorBody.size() - 1).getX() == DefaultConst.SINGLE_WIDTH - 1) {
            if(simulatorBody.get(simulatorBody.size() - 1).getY() == 0) {
                if(isAtEdge()) {
                    if(dir == Direction.UP) setRouteCounterClockWise(Direction.LEFT);
                    else setRouteClockWise(Direction.DOWN);
                }
                else {
                    if (auto_Route.get(auto_Route.size() - 1) == Direction.UP)
                        setRouteCounterClockWise(Direction.LEFT);
                    else setRouteClockWise(Direction.DOWN);
                }
            }
            else if(simulatorBody.get(simulatorBody.size() - 1).getY() == DefaultConst.SINGLE_HEIGHT - 1) {
                if(isAtEdge()) {
                    if(dir == Direction.RIGHT) setRouteCounterClockWise(Direction.UP);
                    else setRouteClockWise(Direction.LEFT);
                }
                else {
                    if (auto_Route.get(auto_Route.size() - 1) == Direction.RIGHT)
                        setRouteCounterClockWise(Direction.UP);
                    else setRouteClockWise(Direction.LEFT);
                }
            }
            else {
                for(i = 1; i < body.size(); i++) {
                    if(simulatorBody.get(simulatorBody.size() - (i + 1)).getX() == simulatorBody.get(simulatorBody.size() - 1).getX()) {
                        if(simulatorBody.get(simulatorBody.size() - (i + 1)).getY() < simulatorBody.get(simulatorBody.size() - 1).getY()) {
                            setRouteClockWise(Direction.DOWN);
                        }
                        else setRouteCounterClockWise(Direction.UP);
                    }
                }
                if(!isCircled) setRouteClockWise(Direction.DOWN);
            }
        }
        else if (simulatorBody.get(simulatorBody.size() - 1).getY() == 0) {
            for (i = 1; i < body.size(); i++) {
                if (simulatorBody.get(simulatorBody.size() - (i + 1)).getY() == simulatorBody.get(simulatorBody.size() - 1).getY()) {
                    if (simulatorBody.get(simulatorBody.size() - (i + 1)).getX() > simulatorBody.get(simulatorBody.size() - 1).getX()) {
                        setRouteCounterClockWise(Direction.LEFT);
                    } else setRouteClockWise(Direction.RIGHT);
                }
            }
            if(!isCircled) setRouteClockWise(Direction.RIGHT);
        }
        else if(simulatorBody.get(simulatorBody.size() - 1).getY() == DefaultConst.SINGLE_HEIGHT - 1) {
            for (i = 1; i < body.size(); i++) {
                if (simulatorBody.get(simulatorBody.size() - (i + 1)).getY() == simulatorBody.get(simulatorBody.size() - 1).getY()) {
                    if (simulatorBody.get(simulatorBody.size() - (i + 1)).getX() > simulatorBody.get(simulatorBody.size() - 1).getX()) {
                        setRouteClockWise(Direction.LEFT);
                    } else setRouteCounterClockWise(Direction.RIGHT);
                }
            }
            if(!isCircled) setRouteClockWise(Direction.LEFT);
        }
    }

    public Direction[] shiftDirQueue(Direction[] dir, int num) {
        Direction[] queue = dir.clone();

        int i, j;
        for(i = 1; i <= num; i++) {
            Direction temp = queue[0];
            for(j = 1; j <= 3; j++) {
                queue[j - 1] = queue[j];
            }
            queue[3] = temp;
        }

        return queue;
    }

    public void simulAdd(Direction dir) {
        Coordinate temp;
        switch(dir) {
            case UP:
                temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX(), simulatorBody.get(simulatorBody.size() - 1).getY() - 1, PlayMode.Auto);
                simulatorBody.add(temp);
                break;
            case DOWN:
                temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX(), simulatorBody.get(simulatorBody.size() - 1).getY() + 1, PlayMode.Auto);
                simulatorBody.add(temp);
                break;
            case RIGHT:
                temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX() + 1, simulatorBody.get(simulatorBody.size() - 1).getY(), PlayMode.Auto);
                simulatorBody.add(temp);
                break;
            case LEFT:
                temp = new Coordinate(simulatorBody.get(simulatorBody.size() - 1).getX() - 1, simulatorBody.get(simulatorBody.size() - 1).getY(), PlayMode.Auto);
                simulatorBody.add(temp);
                break;
        }
    }

    public void setRouteClockWise(Direction startDir) {
        isCircled = true;
        Direction[] queue;
        int index = 0;
        switch (startDir) {
            case DOWN:
                queue = shiftDirQueue(dirClock, 2);
                break;
            case LEFT:
                queue = shiftDirQueue(dirClock, 3);
                break;
            case RIGHT:
                queue = shiftDirQueue(dirClock, 1);
                break;
            default:
                queue = dirClock.clone();
                break;
        }
        Direction curDir;
        for(int i = 1; i <= DefaultConst.AUTO_CIRCLE_STEPS; i++) {
            curDir = queue[index];
            auto_Route.add(curDir);
            simulAdd(curDir);
            if((simulatorBody.get(simulatorBody.size() - 1).getX() == 0 && simulatorBody.get(simulatorBody.size() - 1).getY() == 0)
                    || (simulatorBody.get(simulatorBody.size() - 1).getX() == 0 && simulatorBody.get(simulatorBody.size() - 1).getY() == DefaultConst.SINGLE_HEIGHT - 1)
                    || (simulatorBody.get(simulatorBody.size() - 1).getX() == DefaultConst.SINGLE_WIDTH - 1 && simulatorBody.get(simulatorBody.size() - 1).getY() == 0)
                    || (simulatorBody.get(simulatorBody.size() - 1).getX() == DefaultConst.SINGLE_WIDTH - 1 && simulatorBody.get(simulatorBody.size() - 1).getY() == DefaultConst.SINGLE_HEIGHT - 1)) {
                index++;
                if(index > 3) {
                    index = 0;
                }
            }
        }

        curDir = queue[index];
        auto_Route.add(curDir);
        simulAdd(curDir);
    }

    public void setRouteCounterClockWise(Direction startDir) {
        isCircled = true;
        Direction[] queue;
        int index = 0;
        switch (startDir) {
            case DOWN:
                queue = shiftDirQueue(dirCounterClock, 2);
                break;
            case LEFT:
                queue = shiftDirQueue(dirCounterClock, 1);
                break;
            case RIGHT:
                queue = shiftDirQueue(dirCounterClock, 3);
                break;
            default:
                queue = dirCounterClock.clone();
                break;
        }
        Direction curDir;
        for(int i = 1; i <= DefaultConst.AUTO_CIRCLE_STEPS; i++) {
            curDir = queue[index];
            auto_Route.add(curDir);
            simulAdd(curDir);
            if((simulatorBody.get(simulatorBody.size() - 1).getX() == 0 && simulatorBody.get(simulatorBody.size() - 1).getY() == 0)
                    || (simulatorBody.get(simulatorBody.size() - 1).getX() == 0 && simulatorBody.get(simulatorBody.size() - 1).getY() == DefaultConst.SINGLE_HEIGHT - 1)
                    || (simulatorBody.get(simulatorBody.size() - 1).getX() == DefaultConst.SINGLE_WIDTH - 1 && simulatorBody.get(simulatorBody.size() - 1).getY() == 0)
                    || (simulatorBody.get(simulatorBody.size() - 1).getX() == DefaultConst.SINGLE_WIDTH - 1 && simulatorBody.get(simulatorBody.size() - 1).getY() == DefaultConst.SINGLE_HEIGHT - 1)) {
                index++;
                if(index > 3) {
                    index = 0;
                }
            }
        }

        curDir = queue[index];
        auto_Route.add(curDir);
        simulAdd(curDir);
    }

    protected void addHead() { // 현재 dir 방향으로 움직인 snake head 생성, body 맨 앞에 추가 (apple 먹지 않는다면 delTail() 호출 필요)
        body.add(0, body.get(0).getMovedPosition(dir));
        lastMovedDir = dir;
    }

    protected void delTail() {
        body.remove(body.size() - 1);
    }
}