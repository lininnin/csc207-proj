package entity.Alex.AvalibleMoodLabel;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.MoodLabel.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores available mood labels separated by their type: Positive and Negative.
 * Now fully decoupled from the concrete MoodLabel class.
 */
public class AvaliableMoodLabel implements AvaliableMoodLabelInterf {

    private final List<MoodLabelInterf> positiveLabels;
    private final List<MoodLabelInterf> negativeLabels;

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
    @Override
    public void addLabel(MoodLabelInterf moodLabel) {
        if (moodLabel == null) {
            throw new IllegalArgumentException("MoodLabel cannot be null.");
        }

        String name = moodLabel.getName();
        Type type = moodLabel.getType();

        if (containsName(name)) {
            return; // 避免重复
        }

        if (type == Type.Positive) {
            positiveLabels.add(moodLabel);
        } else if (type == Type.Negative) {
            negativeLabels.add(moodLabel);
        }
    }

    /**
     * Removes a label by name from both lists (if present).
     *
     * @param name The label name to remove
     */
    @Override
    public void removeLabelByName(String name) {
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
     */
    @Override
    public List<String> getPositiveLabels() {
        List<String> names = new ArrayList<>();
        for (MoodLabelInterf label : positiveLabels) {
            names.add(label.getName());
        }
        return names;
    }

    /**
     * Returns a list of negative mood label names.
     */
    @Override
    public List<String> getNegativeLabels() {
        List<String> names = new ArrayList<>();
        for (MoodLabelInterf label : negativeLabels) {
            names.add(label.getName());
        }
        return names;
    }

    /**
     * Returns all positive MoodLabel objects.
     */
    @Override
    public List<MoodLabelInterf> getPositiveLabelObjects() {
        return new ArrayList<>(positiveLabels);
    }

    /**
     * Returns all negative MoodLabel objects.
     */
    @Override
    public List<MoodLabelInterf> getNegativeLabelObjects() {
        return new ArrayList<>(negativeLabels);
    }

    /**
     * Checks whether a label name exists in either list.
     */
    private boolean containsName(String name) {
        return positiveLabels.stream().anyMatch(l -> l.getName().equals(name))
                || negativeLabels.stream().anyMatch(l -> l.getName().equals(name));
    }
}
