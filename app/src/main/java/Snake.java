import java.util.ArrayList;

public class Snake {
    private ArrayList<Coordinate> body = new ArrayList<Coordinate>();
    private int speed;
    private Direction dir;

    Snake(Coordinate headPosition){
        this(headPosition, DefaultConst.SNAKE_LENGTH, DefaultConst.SNAKE_SPEED, DefaultConst.SNAKE_DIR);
    }

    Snake(Coordinate headPosition, int length, int speed, Direction dir) {
        this.speed = speed;
        this.dir = dir;

        int i;
        for (i = 0; i < length; i++) body.add(headPosition.getMovedPosition(dir, i));
    }

    public ArrayList<Coordinate> getPositions() {
        ArrayList<Coordinate> clonedBody = new ArrayList<>();

        int i;
        for (i = 0; i < body.size(); i++) clonedBody.add(body.get(i).clone());

        return clonedBody;
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
        body.remove(body.size());
    }
}