import java.util.Random;

public class Coordinate implements Cloneable {
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

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean equals(Object another) {
        return (another instanceof Coordinate
                && this.x == ((Coordinate)another).x && this.y == ((Coordinate)another).y);
    }

    public Coordinate clone() {
        return new Coordinate(this.x, this.y);
    }

    public static Coordinate random() {
        // WIDTH, HEIGHT 제한 내에서 새 좌표 랜덤하게 생성
        Random random = new Random();
        return new Coordinate((int)(random.nextInt() * WIDTH), (int)(random.nextInt() * HEIGHT));
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
