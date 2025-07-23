package entity.Alex.MoodLabel;

/**
 * Represents a mood label with a name and a type (positive or negative).
 * Used in wellness tracking to categorize emotional states.
 */
public class MoodLabel {

    /**
     * Type of the mood: Positive or Negative.
     */
    public enum Type {
        Positive, Negative
    }

    private Type type;
    private String name;

    /**
     * Private constructor used by the Builder.
     * Fields are validated and assigned via the Builder.
     *
     * @param builder Builder instance containing the fields.
     */
    private MoodLabel(Builder builder) {
        this.type = builder.type;
        this.name = builder.name;
    }

    /**
     * Builder for creating immutable MoodLabel instances.
     */
    public static class Builder {
        private Type type;
        private String name;

        /**
         * Builder constructor with required name.
         *
         * @param name The name of the mood label (e.g., "Happy", "Stressed")
         * @throws IllegalArgumentException if name is null or empty
         */
        public Builder(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            this.name = name.trim();
        }

        /**
         * Sets the mood type (Positive or Negative).
         *
         * @param type The type of the mood
         * @return The Builder itself
         * @throws IllegalArgumentException if type is null
         */
        public Builder type(Type type) {
            if (type == null) {
                throw new IllegalArgumentException("Type cannot be null");
            }
            this.type = type;
            return this;
        }

        /**
         * Builds and returns a new MoodLabel object.
         *
         * @return A new MoodLabel instance
         */
        public MoodLabel build() {
            return new MoodLabel(this);
        }
    }

    // ------------------ Getters ------------------

    /**
     * @return The name of this mood label
     */
    public String getName() {
        return name;
    }

    /**
     * @return The type (Positive or Negative) of this mood label
     */
    public Type getType() {
        return type;
    }

    // ------------------ Setters ------------------

    /**
     * Sets the name of this mood label.
     *
     * @param name The new name
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
    }

    /**
     * Sets the type of this mood label.
     *
     * @param type The new mood type
     * @throws IllegalArgumentException if type is null
     */
    public void setType(Type type) {
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        this.type = type;
    }
}

