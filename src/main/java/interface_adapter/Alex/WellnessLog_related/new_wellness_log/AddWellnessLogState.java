package interface_adapter.Alex.WellnessLog_related.new_wellness_log;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;

import java.util.ArrayList;
import java.util.List;

public class AddWellnessLogState {

    private MoodLabel moodLabel = null;
    private Levels stressLevel = null;
    private Levels energyLevel = null;
    private Levels fatigueLevel = null;

    private String userNote = "";

    /** 用于下拉框选择情绪标签 */
    private List<MoodLabel> availableMoodLabels = new ArrayList<>();

    /** 错误与成功信息 */
    private String errorMessage = null;
    private String successMessage = null;

    // ---------------- Getters ----------------

    public MoodLabel getMoodLabel() {
        return moodLabel;
    }

    public Levels getStressLevel() {
        return stressLevel;
    }

    public Levels getEnergyLevel() {
        return energyLevel;
    }

    public Levels getFatigueLevel() {
        return fatigueLevel;
    }

    public String getUserNote() {
        return userNote;
    }

    public List<MoodLabel> getAvailableMoodLabels() {
        return availableMoodLabels;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    // ---------------- Setters ----------------

    public void setMoodLabel(MoodLabel moodLabel) {
        this.moodLabel = moodLabel;
    }

    public void setStressLevel(Levels stressLevel) {
        this.stressLevel = stressLevel;
    }

    public void setEnergyLevel(Levels energyLevel) {
        this.energyLevel = energyLevel;
    }

    public void setFatigueLevel(Levels fatigueLevel) {
        this.fatigueLevel = fatigueLevel;
    }

    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    public void setAvailableMoodLabels(List<MoodLabel> availableMoodLabels) {
        this.availableMoodLabels = availableMoodLabels;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}

