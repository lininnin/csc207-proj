package entity.Alex.AvalibleMoodLabel;

import entity.Alex.MoodLabel.MoodLabelInterf;

import java.util.List;

/**
 * Interface for AvaliableMoodLabel.
 * Exposes operations for managing categorized mood labels.
 */
public interface AvaliableMoodLabelInterf {

    /**
     * Adds a mood label to the appropriate category.
     *
     * @param moodLabel the mood label to add
     */
    void addLabel(MoodLabelInterf moodLabel);

    /**
     * Removes a mood label by name from both categories.
     *
     * @param name the name of the label to remove
     */
    void removeLabelByName(String name);

    /**
     * Returns all positive mood label names.
     *
     * @return list of positive label names
     */
    List<String> getPositiveLabels();

    /**
     * Returns all negative mood label names.
     *
     * @return list of negative label names
     */
    List<String> getNegativeLabels();

    /**
     * Clears all stored mood labels.
     */
    void clear();
}

