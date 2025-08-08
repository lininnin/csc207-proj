package use_case.alex.wellness_log_related.moodlabel_related.delete_moodLabel;

/**
 * Input data class for the DeleteMoodLabel use case.
 * Contains only the name of the mood label to be deleted.
 */
public class DeleteMoodLabelInputData {

    private final String moodLabelName;

    /**
     * Constructor for DeleteMoodLabelInputData.
     *
     * @param moodLabelName The name of the mood label to be deleted (required)
     */
    public DeleteMoodLabelInputData(String moodLabelName) {
        if (moodLabelName == null || moodLabelName.trim().isEmpty()) {
            throw new IllegalArgumentException("moodLabelName cannot be null or empty");
        }
        this.moodLabelName = moodLabelName.trim();
    }

    /**
     * Returns the name of the mood label to delete.
     */
    public String getMoodLabelName() {
        return moodLabelName;
    }
}

