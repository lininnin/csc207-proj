package data_access;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;
import entity.Alex.AvalibleMoodLabel.AvaliableMoodLabel;

import java.util.Optional;

/**
 * In-memory DAO for managing available mood labels.
 * Internally uses an AvaliableMoodLabel object to store categorized labels.
 */
public class MoodAvailableDataAccessObject {

    private final AvaliableMoodLabel availableLabels;

    /**
     * Initializes the DAO with some default labels.
     */
    public MoodAvailableDataAccessObject() {
        this.availableLabels = new AvaliableMoodLabel();
        // 初始化默认正向
        availableLabels.addLabel(new MoodLabel.Builder("Happy").type(MoodLabel.Type.Positive).build());
        availableLabels.addLabel(new MoodLabel.Builder("Calm").type(MoodLabel.Type.Positive).build());
        // 初始化默认负向
        availableLabels.addLabel(new MoodLabel.Builder("Anxious").type(MoodLabel.Type.Negative).build());
        availableLabels.addLabel(new MoodLabel.Builder("Stressed").type(MoodLabel.Type.Negative).build());
    }

    /**
     * Adds a mood label to the appropriate category.
     *
     * @param moodLabel mood label to add
     */
    public void add(MoodLabelInterf moodLabel) {
        if (contains(moodLabel.getName())) {
            throw new IllegalArgumentException("Label with the same name already exists");
        }
        availableLabels.addLabel(moodLabel);
    }

    /**
     * Removes a label by name from both positive and negative lists.
     *
     * @param name label name
     * @return true if removed, false otherwise
     */
    public boolean removeByName(String name) {
        boolean before = contains(name);
        availableLabels.removeLabelByName(name);
        return before;
    }

    /**
     * Returns a categorized view of available mood labels.
     *
     * @return AvaliableMoodLabel object
     */
    public AvaliableMoodLabel getCategorized() {
        return availableLabels;
    }

    /**
     * Checks if a label name exists in either category.
     *
     * @param name label name
     * @return true if present
     */
    public boolean contains(String name) {
        return availableLabels.getPositiveLabels().contains(name)
                || availableLabels.getNegativeLabels().contains(name);
    }

    /**
     * Updates a label by replacing the name and type.
     *
     * @param oldName  label to update
     * @param newLabel new version
     * @return true if successful
     */
    public boolean update(String oldName, MoodLabelInterf newLabel) {
        if (!contains(oldName)) return false;
        availableLabels.removeLabelByName(oldName);
        availableLabels.addLabel(newLabel);
        return true;
    }

    /**
     * Clears all stored labels.
     */
    public void clearAll() {
        availableLabels.clear();
    }
}


