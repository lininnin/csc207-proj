package entity.Alex.AvalibleMoodLabel;

/**
 * Default factory that creates concrete instances of AvaliableMoodLabel.
 * Implements the AvaliableMoodlabelFactoryInterf for dependency inversion.
 */
public class AvaliableMoodLabelFactory implements AvaliableMoodlabelFactoryInterf {

    /**
     * Creates and returns a new AvaliableMoodLabel instance.
     *
     * @return AvaliableMoodLabelInterf instance
     */
    @Override
    public AvaliableMoodLabelInterf create() {
        return new AvaliableMoodLabel();
    }
}

