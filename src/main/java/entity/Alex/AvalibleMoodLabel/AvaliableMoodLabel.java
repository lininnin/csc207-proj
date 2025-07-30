package entity.Alex.AvalibleMoodLabel;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores available mood labels separated by their type: Positive and Negative.
 */
public class AvaliableMoodLabel implements AvaliableMoodLabelInterf {

    private final List<String> positiveLabels;
    private final List<String> negativeLabels;

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
     * @param moodLabel A mood label to add (must not be null)
     */
    public void addLabel(MoodLabelInterf moodLabel) {
        if (moodLabel == null) {
            throw new IllegalArgumentException("MoodLabel cannot be null.");
        }

        String name = moodLabel.getName();
        MoodLabel.Type type = moodLabel.getType();

        if (type == MoodLabel.Type.Positive) {
            if (!positiveLabels.contains(name)) {
                positiveLabels.add(name);
            }
        } else if (type == MoodLabel.Type.Negative) {
            if (!negativeLabels.contains(name)) {
                negativeLabels.add(name);
            }
        }
    }

    /**
     * Returns the list of positive mood labels.
     */
    public List<String> getPositiveLabels() {
        return new ArrayList<>(positiveLabels);
    }

    /**
     * Returns the list of negative mood labels.
     */
    public List<String> getNegativeLabels() {
        return new ArrayList<>(negativeLabels);
    }

    /**
     * Removes a label by name from both lists (if present).
     *
     * @param name The label name to remove
     */
    public void removeLabelByName(String name) {
        positiveLabels.remove(name);
        negativeLabels.remove(name);
    }

    /**
     * Clears all stored mood labels.
     */
    public void clear() {
        positiveLabels.clear();
        negativeLabels.clear();
    }
}
