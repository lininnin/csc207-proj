package use_case.alex.WellnessLog_related.Moodlabel_related.add_moodLabel;

import entity.Alex.MoodLabel.MoodLabel;

import java.util.List;

/**
 * Data access interface for adding mood labels.
 * Provides methods to save and retrieve mood labels.
 */
public interface AddMoodLabelDataAccessInterf {

    /**
     * Saves the given mood label to the available labels store.
     *
     * @param moodLabel The mood label to save.
     */
    void save(MoodLabel moodLabel);

    /**
     * Retrieves all existing mood labels.
     *
     * @return List of all mood labels.
     */
    List<MoodLabel> getAllLabels();
}

