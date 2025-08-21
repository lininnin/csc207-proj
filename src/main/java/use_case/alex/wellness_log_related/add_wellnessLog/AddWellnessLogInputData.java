package use_case.alex.wellness_log_related.add_wellnessLog;

import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.WellnessLogEntry.Levels;

import java.time.LocalDateTime;

public class AddWellnessLogInputData {

    private final LocalDateTime time;
    private final Levels stressLevel;
    private final Levels energyLevel;
    private final Levels fatigueLevel;
    private final MoodLabelInterf moodLabel;
    private final String userNote;

    public AddWellnessLogInputData(LocalDateTime time,
                                   Levels stressLevel,
                                   Levels energyLevel,
                                   Levels fatigueLevel,
                                   MoodLabelInterf moodLabel,
                                   String userNote) {
        this.time = time;
        this.stressLevel = stressLevel;
        this.energyLevel = energyLevel;
        this.fatigueLevel = fatigueLevel;
        this.moodLabel = moodLabel;
        this.userNote = userNote;
    }

    public LocalDateTime getTime() {
        return time;
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

    public MoodLabelInterf getMoodLabel() {
        return moodLabel;
    }

    public String getUserNote() {
        return userNote;
    }
}
