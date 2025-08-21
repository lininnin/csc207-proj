package entity.alex.MoodLabel;

/**
 * Interface for MoodLabel entity.
 * Defines the contract for accessing and modifying mood label data.
 */
public interface MoodLabelInterf {

    /**
     * Gets the name of the mood label.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets the name of the mood label.
     *
     * @param name the new name
     * @throws IllegalArgumentException if name is null or empty
     */
    void setName(String name);

    /**
     * Gets the type of the mood label (Positive or Negative).
     *
     * @return the type
     */
    Type getType();

    /**
     * Sets the type of the mood label.
     *
     * @param type the new type
     * @throws IllegalArgumentException if type is null
     */
    void setType(Type type);
}

