package use_case.alex.wellness_log_related.moodlabel_related.add_moodLabel;

/**
 * Input data for the AddMoodLabel use case.
 * Carries the name and type of the new mood label to be added.
 */
public class AddMoodLabelInputData {

    private final String moodName;
    private final String moodType; // e.g. "Positive" or "Negative"

    public AddMoodLabelInputData(String moodName, String moodType) {
        this.moodName = moodName;
        this.moodType = moodType;
    }

    public String getMoodName() {
        return moodName;
    }

    public String getMoodType() {
        return moodType;
    }
}

