package entity.alex.WellnessLogEntry;

/**
 * Enum representing different levels used in wellness logs,
 * ranging from 1 to 10.
 * Each level is associated with an integer value.
 */
public enum Levels {
    /**
     * Level 1.
     */
    ONE(1),

    /**
     * Level 2.
     */
    TWO(2),

    /**
     * Level 3.
     */
    THREE(3),

    /**
     * Level 4.
     */
    FOUR(4),

    /**
     * Level 5.
     */
    FIVE(5),

    /**
     * Level 6.
     */
    SIX(6),

    /**
     * Level 7.
     */
    SEVEN(7),

    /**
     * Level 8.
     */
    EIGHT(8),

    /**
     * Level 9.
     */
    NINE(9),

    /**
     * Level 10.
     */
    TEN(10);

    /**
     * The integer value associated with the level.
     */
    private final int value;

    /**
     * Constructs a {@code Levels} enum with the specified integer value.
     *
     * @param valueParam The integer value of the level.
     */
    Levels(final int valueParam) {
        this.value = valueParam;
    }

    /**
     * Returns the integer value associated with this level.
     *
     * @return The integer value of the level.
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns the {@code Levels} enum corresponding to
     * the specified integer value.
     *
     * @param value The integer value to convert.
     * @return The corresponding {@code Levels} enum.
     * @throws IllegalArgumentException
     * if the value does not correspond to any defined level.
     */
    public static Levels fromInt(final int value) {
        for (Levels level : Levels.values()) {
            if (level.getValue() == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid level: " + value);
    }
}


