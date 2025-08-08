package use_case.alex.WellnessLog_related.Moodlabel_related.add_moodLabel;

/**
 * Output data for the AddMoodLabel use case.
 * Carries the result of the creation process (e.g., name and type of the newly added label).
 */
public class AddMoodLabelOutputData {

    private final String moodName;
    private final String moodType;
    private final boolean success;

    public AddMoodLabelOutputData(String moodName, String moodType, boolean success) {
        this.moodName = moodName;
        this.moodType = moodType;
        this.success = success;
    }

    public String getMoodName() {
        return moodName;
    }

    public String getMoodType() {
        return moodType;
    }

    public boolean isSuccess() {
        return success;
    }
}

