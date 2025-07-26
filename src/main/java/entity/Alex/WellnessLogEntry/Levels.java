package entity.Alex.WellnessLogEntry;

public enum Levels {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10);

    private final int value;

    Levels(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Levels fromInt(int value) {
        for (Levels level : Levels.values()) {
            if (level.getValue() == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid level: " + value);
    }
}

