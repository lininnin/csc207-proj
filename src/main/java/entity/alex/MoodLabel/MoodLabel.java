package entity.alex.MoodLabel;

/**
 * Represents a mood label with a name and a type (positive or negative).
 * Used in wellness tracking to categorize emotional states.
 */
public final class MoodLabel implements MoodLabelInterf {


    /**
     * The type of this mood label (positive/negative).
     */
    private Type type;

    /**
     * The name of this mood label (e.g., "Happy", "Stressed").
     */
    private String name;

    /**
     * Private constructor used by the Builder.
     * Fields are validated and assigned via the Builder.
     *
     * @param builder Builder instance containing the fields
     */
    private MoodLabel(final Builder builder) {
        this.type = builder.type;
        this.name = builder.name;
    }

    /**
     * Builder for creating immutable MoodLabel instances.
     */
    public static class Builder {

        /**
         * The mood type to assign.
         */
        private Type type;

        /**
         * The name of the mood label.
         */
        private String name;

        /**
         * Builder constructor with required name.
         *
         * @param nameParam the name of the mood label (e.g., "Happy")
         * @throws IllegalArgumentException if name is null or empty
         */
        public Builder(final String nameParam) {
            if (nameParam == null || nameParam.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty.");
            }
            this.name = nameParam.trim();
        }

        /**
         * Sets the mood type (Positive or Negative).
         *
         * @param typeParam the type of the mood
         * @return the Builder itself
         * @throws IllegalArgumentException if type is null
         */
        public Builder type(final Type typeParam) {
            if (typeParam == null) {
                throw new IllegalArgumentException("Type cannot be null.");
            }
            this.type = typeParam;
            return this;
        }

        /**
         * Builds and returns a new MoodLabel object.
         *
         * @return a new MoodLabel instance
         */
        public MoodLabel build() {
            return new MoodLabel(this);
        }
    }

    // ------------------ Getters ------------------

    /**
     * Returns the name of this mood label.
     *
     * @return the name string
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this mood label.
     *
     * @return the type (positive or negative)
     */
    public Type getType() {
        return type;
    }

    // ------------------ Setters ------------------

    /**
     * Sets the name of this mood label.
     *
     * @param nameParam the new name
     * @throws IllegalArgumentException if name is null or empty
     */
    public void setName(final String nameParam) {
        if (nameParam == null || nameParam.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = nameParam.trim();
    }

    /**
     * Sets the type of this mood label.
     *
     * @param typeParam the new mood type
     * @throws IllegalArgumentException if type is null
     */
    public void setType(final Type typeParam) {
        if (typeParam == null) {
            throw new IllegalArgumentException("Type cannot be null.");
        }
        this.type = typeParam;
    }

    // ------------------ Display for JComboBox ------------------

    /**
     * Returns a string representation of this mood label.
     *
     * @return the name of the label
     */
    @Override
    public String toString() {
        return name; // Or: return name + " (" + type + ")";
    }
}
