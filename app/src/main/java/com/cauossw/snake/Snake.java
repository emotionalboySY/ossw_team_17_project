package com.cauossw.snake;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Snake {

    private static final String TAG = "Snake";

    private ArrayList<Coordinate> body = new ArrayList<Coordinate>();
    private int speed;
    private Direction dir;
    private Direction lastMovedDir = null;
    private ArrayList<Direction> auto_Route = new ArrayList<Direction>();
    private int autoDirIndex = 0;
    private boolean isAutoDirChanged = false;

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

    public void setRouteToApple(Apple apple, boolean isStart) {

        auto_Route.clear();
        isAutoDirChanged = true;
        Direction dir = this.dir;
        Coordinate applePos = apple.getPosition();
        Coordinate snakePos = body.get(0);
        ApplePosition applePos4 = ApplePosition.Top;

        if (!isStart) {
            switch (dir) {
                case UP:
                    if (autoCheckUp()) {
                        setRouteUp(body.get(0));
                    } else if (autoCheckRight()) {
                        setRouteRight(body.get(0));
                    } else if (autoCheckDown()) {
                        setRouteDown(body.get(0));
                    } else if (autoCheckLeft()) {
                        setRouteLeft(body.get(0));
                    }
                    break;
                case RIGHT:
                    if (autoCheckRight()) {
                        setRouteRight(body.get(0));
                    } else if (autoCheckDown()) {
                        setRouteDown(body.get(0));
                    } else if (autoCheckLeft()) {
                        setRouteLeft(body.get(0));
                    } else if (autoCheckUp()) {
                        setRouteUp(body.get(0));
                    }
                    break;
                case DOWN:
                    if (autoCheckDown()) {
                        setRouteDown(body.get(0));
                    } else if (autoCheckLeft()) {
                        setRouteLeft(body.get(0));
                    } else if (autoCheckUp()) {
                        setRouteUp(body.get(0));
                    } else if (autoCheckRight()) {
                        setRouteRight(body.get(0));
                    }
                    break;
                case LEFT:
                    if (autoCheckLeft()) {
                        setRouteLeft(body.get(0));
                    } else if (autoCheckUp()) {
                        setRouteUp(body.get(0));
                    } else if (autoCheckRight()) {
                        setRouteRight(body.get(0));
                    } else if (autoCheckDown()) {
                        setRouteDown(body.get(0));
                    }
                    break;
            }
        }

        if(applePos.getX() > snakePos.getX() && applePos.getY() < snakePos.getY()) applePos4 = ApplePosition.RightTop;
        else if(applePos.getX() > snakePos.getX() && applePos.getY() > snakePos.getY()) applePos4 = ApplePosition.RightBottom;
        else if(applePos.getX() < snakePos.getX() && applePos.getY() > snakePos.getY()) applePos4 = ApplePosition.LeftBottom;
        else if(applePos.getX() < snakePos.getX() && applePos.getY() < snakePos.getY()) applePos4 = ApplePosition.LeftTop;
        else if(applePos.getX() == snakePos.getX() && applePos.getY() > snakePos.getY()) applePos4 = ApplePosition.Bottom;
        else if(applePos.getX() > snakePos.getX() && applePos.getY() == snakePos.getY()) applePos4 = ApplePosition.Right;
        else if(applePos.getX() < snakePos.getX() && applePos.getY() == snakePos.getY()) applePos4 = ApplePosition.Left;

        int i;
        int xDiff = Math.abs(applePos.getX() - snakePos.getX());
        int yDiff = Math.abs(applePos.getY() - snakePos.getY());
        int turnPos = (int)((Math.random()*10000)%xDiff);

        switch(applePos4) {
            case Top:
                for(i = snakePos.getY() + 1; i <= applePos.getY(); i++) {
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
                for(i = snakePos.getX() + 1; i <= applePos.getX(); i++) {
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
                for(i = snakePos.getY() - 1; i >= applePos.getY(); i--) {
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
                for(i = snakePos.getX() - 1; i >= applePos.getY(); i--) {
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

    public Direction autoFindDir(Apple apple) {
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

    public boolean autoCheckUp() {
        Coordinate limit;
        int dist = 0;
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
        int dist = 0;
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
        int dist = 0;
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
        Coordinate limit;
        int dist = 0;
        int distMin = 40;
        int distMinIndex = -1;
        int leftBodyLength;

        limit = new Coordinate(0, body.get(0).getY(), PlayMode.Auto);
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

    public void setRouteUp(Coordinate pos) {
        int i;
        for (i = pos.getY() - 1; i >= 0; i--) {
            auto_Route.add(Direction.UP);
        }
        for (i = pos.getX() + 1; i < DefaultConst.SINGLE_WIDTH; i++) {
            auto_Route.add(Direction.RIGHT);
        }
        for (i = 0; i < DefaultConst.SINGLE_HEIGHT - 1; i++) {
            auto_Route.add(Direction.DOWN);
        }
        for (i = DefaultConst.SINGLE_WIDTH - 2; i >= 0; i--) {
            auto_Route.add(Direction.LEFT);
        }
        for (i = DefaultConst.SINGLE_HEIGHT - 2; i >= 0; i--) {
            auto_Route.add(Direction.UP);
        }
        for (i = 0; i < pos.getX(); i++) {
            auto_Route.add(Direction.RIGHT);
        }
        auto_Route.add(Direction.DOWN);
    }

    public void setRouteRight(Coordinate pos) {
        int i;
        for (i = pos.getX() + 1; i < DefaultConst.SINGLE_WIDTH; i++) {
            auto_Route.add(Direction.RIGHT);
        }
        for (i = pos.getY() + 1; i < DefaultConst.SINGLE_HEIGHT; i++) {
            auto_Route.add(Direction.DOWN);
        }
        for (i = DefaultConst.SINGLE_WIDTH - 2; i >= 0; i--) {
            auto_Route.add(Direction.LEFT);
        }
        for (i = DefaultConst.SINGLE_HEIGHT - 2; i >= 0; i--) {
            auto_Route.add(Direction.UP);
        }
        for (i = 0; i < DefaultConst.SINGLE_WIDTH - 1; i++) {
            auto_Route.add(Direction.RIGHT);
        }
        for (i = 0; i < pos.getY(); i++) {
            auto_Route.add(Direction.DOWN);
        }
        auto_Route.add(Direction.LEFT);
    }

    public void setRouteDown(Coordinate pos) {
        int i;
        for (i = pos.getY() + 1; i < DefaultConst.SINGLE_HEIGHT; i++) {
            auto_Route.add(Direction.DOWN);
        }
        for (i = pos.getX() - 1; i >= 0; i--) {
            auto_Route.add(Direction.LEFT);
        }
        for (i = DefaultConst.SINGLE_HEIGHT - 2; i >= 0; i--) {
            auto_Route.add(Direction.UP);
        }
        for (i = 0; i < DefaultConst.SINGLE_WIDTH - 1; i++) {
            auto_Route.add(Direction.RIGHT);
        }
        for (i = 0; i < DefaultConst.SINGLE_HEIGHT - 1; i++) {
            auto_Route.add(Direction.DOWN);
        }
        for (i = DefaultConst.SINGLE_WIDTH - 1; i > pos.getX(); i--) {
            auto_Route.add(Direction.LEFT);
        }
        auto_Route.add(Direction.UP);
    }

    public void setRouteLeft(Coordinate pos) {
        int i;
        for (i = pos.getX() - 1; i >= 0; i--) {
            auto_Route.add(Direction.LEFT);
        }
        for (i = pos.getY() - 1; i >= 0; i--) {
            auto_Route.add(Direction.UP);
        }
        for (i = 0; i < DefaultConst.SINGLE_WIDTH - 1; i++) {
            auto_Route.add(Direction.RIGHT);
        }
        for (i = 0; i < DefaultConst.SINGLE_HEIGHT - 1; i++) {
            auto_Route.add(Direction.DOWN);
        }
        for (i = DefaultConst.SINGLE_WIDTH - 2; i >= 0; i--) {
            auto_Route.add(Direction.LEFT);
        }
        for (i = DefaultConst.SINGLE_HEIGHT - 1; i > pos.getY(); i--) {
            auto_Route.add(Direction.UP);
        }
        auto_Route.add(Direction.RIGHT);
    }

    protected void addHead() { // 현재 dir 방향으로 움직인 snake head 생성, body 맨 앞에 추가 (apple 먹지 않는다면 delTail() 호출 필요)
        body.add(0, body.get(0).getMovedPosition(dir));
        lastMovedDir = dir;
    }

    protected void delTail() {
        body.remove(body.size() - 1);
    }
}