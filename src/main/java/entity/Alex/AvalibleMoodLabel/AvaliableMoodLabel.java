package entity.Alex.AvalibleMoodLabel;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores available mood labels separated by their type: Positive and Negative.
 */
public class AvaliableMoodLabel implements AvaliableMoodLabelInterf {

    /** List of positive mood labels. */
    private final List<MoodLabel> positiveLabels;

    /** List of negative mood labels. */
    private final List<MoodLabel> negativeLabels;

    /**
     * Default constructor initializes empty lists.
     */
    public AvaliableMoodLabel() {
        this.positiveLabels = new ArrayList<>();
        this.negativeLabels = new ArrayList<>();
    }

    /**
     * Adds a mood label to the appropriate list based on its type.
     *
     * @param moodLabel the mood label to add (must not be null)
     */
    @Override
    public void addLabel(final MoodLabelInterf moodLabel) {
        if (moodLabel == null) {
            throw new IllegalArgumentException("MoodLabel cannot be null.");
        }

        final String name = moodLabel.getName();
        final MoodLabel.Type type = moodLabel.getType();

        if (containsName(name)) {
            return; // Avoid duplication
        }

        if (type == MoodLabel.Type.Positive) {
            positiveLabels.add((MoodLabel) moodLabel);
        } else if (type == MoodLabel.Type.Negative) {
            negativeLabels.add((MoodLabel) moodLabel);
        }
    }

    /**
     * Removes a label by name from both lists (if present).
     *
     * @param name the label name to remove
     */
    @Override
    public void removeLabelByName(final String name) {
        positiveLabels.removeIf(label -> label.getName().equals(name));
        negativeLabels.removeIf(label -> label.getName().equals(name));
    }

    /**
     * Clears all stored mood labels.
     */
    @Override
    public void clear() {
        positiveLabels.clear();
        negativeLabels.clear();
    }

    /**
     * Returns a list of positive mood label names.
     *
     * @return a list of names of positive mood labels
     */
    @Override
    public List<String> getPositiveLabels() {
        final List<String> names = new ArrayList<>();
        for (MoodLabel label : positiveLabels) {
            names.add(label.getName());
        }
        return names;
    }

    /**
     * Returns a list of negative mood label names.
     *
     * @return a list of names of negative mood labels
     */
    @Override
    public List<String> getNegativeLabels() {
        final List<String> names = new ArrayList<>();
        for (MoodLabel label : negativeLabels) {
            names.add(label.getName());
        }
        return names;
    }

    /**
     * Returns all positive MoodLabel objects.
     *
     * @return a list of positive MoodLabel objects
     */
    @Override
    public List<MoodLabel> getPositiveLabelObjects() {
        return new ArrayList<>(positiveLabels);
    }

    /**
     * Returns all negative MoodLabel objects.
     *
     * @return a list of negative MoodLabel objects
     */
    @Override
    public List<MoodLabel> getNegativeLabelObjects() {
        return new ArrayList<>(negativeLabels);
    }

    /**
     * Checks whether a label name exists in either list.
     *
     * @param name the label name to check
     * @return true if the name exists, false otherwise
     */
    private boolean containsName(final String name) {
        return positiveLabels.stream().anyMatch(l -> l.getName().equals(name))
                || negativeLabels.stream().anyMatch(l -> l.getName().equals(name));
    }
}
