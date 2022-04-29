public class Apple {
    private Coordinate position;

    Apple(Coordinate position) {
        this.position = position;
    }

    public Coordinate getPosition() {
        return this.position.clone();
    }
}
