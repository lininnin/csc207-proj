package entity.Alex.MoodLabel;

/**
 * Concrete factory for creating MoodLabel entities using the MoodLabel.Builder.
 */
public class MoodLabelFactory implements MoodLabelFactoryInterf {

    /**
     * Creates a new MoodLabel instance with the specified name and type.
     *
     * @param name The name of the mood label (e.g., "Happy", "Stressed")
     * @param type The type of the mood label (Positive or Negative)
     * @return A new MoodLabelInterf instance
     * @throws IllegalArgumentException if name is null/empty or type is null
     */
    @Override
    public MoodLabel create(final String name, final Type type) {
        return new MoodLabel.Builder(name)
                .type(type)
                .build();
    }
}


