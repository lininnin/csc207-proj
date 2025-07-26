package use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog;

import entity.Alex.MoodLabel.MoodLabel;

/**
 * Input data for the EditWellnessLog use case.
 * Contains all the fields required to update a wellness log entry.
 */
public class EditWellnessLogInputData {

    private final String logId;
    private final int energyLevel;
    private final int stressLevel;
    private final int fatigueLevel;
    private final String moodName;
    private final MoodLabel.Type moodType;
    private final String note;

    public EditWellnessLogInputData(String logId,
                                    int energyLevel,
                                    int stressLevel,
                                    int fatigueLevel,
                                    String moodName,
                                    MoodLabel.Type moodType,
                                    String note) {
        this.logId = logId;
        this.energyLevel = energyLevel;
        this.stressLevel = stressLevel;
        this.fatigueLevel = fatigueLevel;
        this.moodName = moodName;
        this.moodType = moodType;
        this.note = note;
    }

    public String getLogId() {
        return logId;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public int getStressLevel() {
        return stressLevel;
    }

    public int getFatigueLevel() {
        return fatigueLevel;
    }

    public String getMoodName() {
        return moodName;
    }

    public MoodLabel.Type getMoodType() {
        return moodType;
    }

    public String getNote() {
        return note;
    }
}


