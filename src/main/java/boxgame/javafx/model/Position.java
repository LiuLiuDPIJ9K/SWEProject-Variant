package boxgame.javafx.model;

public record Position (int col) {

    public Position moveTo(Direction direction) {
        return new Position(col + direction.getColChange());
    }

    public String toString() {
        return String.format("%d", col);
    }

}
