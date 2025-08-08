package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.edit_wellnessLog;

import entity.Alex.WellnessLogEntry.WellnessLogEntry;

/**
 * State object for editing a wellness log entry.
 * Stores the current fields being edited and any validation errors.
 */
public class EditWellnessLogState {

    private String logId = ""; // Unique identifier of the log entry being edited
    private String moodLabel;
    private int energyLevel;
    private int stressLevel;
    private int fatigueLevel;
    private String note = "";

    private String errorMessage = "";

    public EditWellnessLogState() {
    }

    public EditWellnessLogState(WellnessLogEntry entry) {
        if (entry != null) {
            this.logId = entry.getId();
            this.moodLabel = entry.getMoodLabel().toString();
            this.energyLevel = entry.getEnergyLevel().getValue();
            this.stressLevel = entry.getStressLevel().getValue();
            this.fatigueLevel = entry.getFatigueLevel().getValue();
            this.note = entry.getUserNote();
        }
    }

    // ---------------- Getters ----------------

    public String getLogId() {
        return logId;
    }

    public String getMoodLabel() {
        return moodLabel;
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

    public String getNote() {
        return note;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // ---------------- Setters ----------------

    public void setLogId(String logId) {
        this.logId = logId != null ? logId.trim() : "";
    }

    public void setMoodLevel(String moodLabel) {
        this.moodLabel = moodLabel;
    }

    public void setEnergyLevel(int energyLevel) {
        this.energyLevel = energyLevel;
    }

    public void setStressLevel(int stressLevel) {
        this.stressLevel = stressLevel;
    }

    public void setFatigueLevel(int fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public void setNote(String note) {
        this.note = note != null ? note.trim() : "";
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage != null ? errorMessage.trim() : "";
    }
}

