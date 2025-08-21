package use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel;

import entity.alex.MoodLabel.MoodLabelInterf;

/**
 * Interface for data access operations related to deleting mood labels.
 * This interface is used only by the DeleteMoodLabel use case.
 * Now fully decoupled from the concrete MoodLabel class by using the MoodLabelInterf interface.
 */
public interface DeleteMoodLabelDataAccessInterf {

    /**
     * Removes the given MoodLabel from the available mood label pool.
     *
     * @param moodLabel The MoodLabel to remove.
     * @return true if removed successfully, false otherwise.
     */
    boolean remove(MoodLabelInterf moodLabel);

    /**
     * Checks whether the given MoodLabel exists in the pool.
     *
     * @param moodLabel The MoodLabel to check.
     * @return true if present, false otherwise.
     */
    boolean contains(MoodLabelInterf moodLabel);

    /**
     * Retrieves a MoodLabel by its name.
     * Useful when DeleteInteractor only receives the name but needs the full MoodLabel object.
     *
     * @param name The name of the MoodLabel.
     * @return The MoodLabel with the given name, or null if not found.
     */
    MoodLabelInterf getByName(String name);
}


