package use_case.alex.WellnessLog_related.Moodlabel_related.edit_moodLabel;

import entity.Alex.MoodLabel.MoodLabel;

/**
 * Interface for data access operations related to editing mood labels.
 * This interface is used only by the EditMoodLabel use case.
 */
public interface EditMoodLabelDataAccessInterface {

    /**
     * Updates an existing mood label in the available mood label pool.
     *
     * @param updatedLabel The updated MoodLabel object.
     * @return true if the update is successful, false otherwise.
     */
    boolean update(MoodLabel updatedLabel);

    /**
     * Retrieves a MoodLabel object by its ID or name.
     * Useful when EditInteractor receives an identifier but needs the original label.
     *
     * @param name The name of the MoodLabel.
     * @return The MoodLabel with the given name, or null if not found.
     */
    MoodLabel getByName(String name);

    /**
     * Checks whether a mood label with the given name exists.
     *
     * @param name The name to check.
     * @return true if the label exists, false otherwise.
     */
    boolean existsByName(String name);
}

