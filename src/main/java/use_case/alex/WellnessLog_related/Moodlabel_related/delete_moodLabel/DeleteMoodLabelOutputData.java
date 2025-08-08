package use_case.alex.WellnessLog_related.Moodlabel_related.delete_moodLabel;

/**
 * Output data class for the DeleteMoodLabel use case.
 * Contains the result of the deletion operation and relevant label info.
 */
public class DeleteMoodLabelOutputData {

    private final String moodLabelName;
    private final boolean deletionSuccess;
    private final String errorMessage;

    /**
     * Constructor for successful deletion.
     *
     * @param moodLabelName Name of the deleted mood label
     */
    public DeleteMoodLabelOutputData(String moodLabelName) {
        this.moodLabelName = moodLabelName;
        this.deletionSuccess = true;
        this.errorMessage = null;
    }

    /**
     * Constructor for failed deletion.
     *
     * @param moodLabelName Name of the attempted mood label
     * @param errorMessage  Reason why deletion failed
     */
    public DeleteMoodLabelOutputData(String moodLabelName, String errorMessage) {
        this.moodLabelName = moodLabelName;
        this.deletionSuccess = false;
        this.errorMessage = errorMessage;
    }

    public String getMoodLabelName() {
        return moodLabelName;
    }

    public boolean isDeletionSuccess() {
        return deletionSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

