package use_case.Alex.WellnessLog_related.Moodlabel_related.delete_moodLabel;

import entity.Alex.MoodLabel.MoodLabel;

/**
 * Interface for data access operations related to deleting mood labels.
 * This interface is used only by the DeleteMoodLabel use case.
 */
public interface DeleteMoodLabelDataAccessInterf {

    /**
     * Removes the given MoodLabel from the available mood label pool.
     *
     * @param moodLabel The MoodLabel to remove.
     * @return true if removed successfully, false otherwise.
     */
    boolean remove(MoodLabel moodLabel);

    /**
     * Checks whether the given MoodLabel exists in the pool.
     *
     * @param moodLabel The MoodLabel to check.
     * @return true if present, false otherwise.
     */
    boolean contains(MoodLabel moodLabel);

    /**
     * Retrieves a MoodLabel by its name.
     * Useful when DeleteInteractor only receives the name but needs the full MoodLabel object.
     *
     * @param name The name of the MoodLabel.
     * @return The MoodLabel with the given name, or null if not found.
     */
    MoodLabel getByName(String name);
}

