package boxgame.javafx.model;

public enum Direction {
    RIGHT(2),
    LEFT(-2),
    RIGHT1(3),
    LEFT1(-3),
    RIGHT2(4),
    LEFT2(-4),
    RIGHT3(5),
    LEFT3(-5),
    RIGHT4(6),
    LEFT4(-6),
    RIGHT5(7),
    LEFT5(-7),
    RIGHT6(8),
    LEFT6(-8),
    RIGHT7(9),
    LEFT7(-9),
    RIGHT8(10),
    LEFT8(-10),
    RIGHT9(11),
    LEFT9(-11),
    RIGHT10(12),
    LEFT10(-12),
    RIGHT11(13),
    LEFT11(-13);

    private final int colChange;

    Direction(int colChange) {
        this.colChange = colChange;
    }

    int getColChange() {
        return colChange;
    }

    public static Direction of(int colChange) {
        for (var direction : values()) {
            if (direction.colChange == colChange) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }

}
