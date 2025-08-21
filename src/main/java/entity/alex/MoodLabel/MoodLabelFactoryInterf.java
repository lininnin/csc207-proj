package entity.alex.MoodLabel;

/**
 * Factory interface for creating MoodLabel entities.
 */
public interface MoodLabelFactoryInterf {

    /**
     * Creates a new MoodLabel instance with the specified name and type.
     *
     * @param name The name of the mood label (e.g., "Happy", "Stressed")
     * @param type The type of the mood label (Positive or Negative)
     * @return A new MoodLabelInterf instance
     * @throws IllegalArgumentException if name is null/empty or type is null
     */
    MoodLabelInterf create(String name, Type type);
}

