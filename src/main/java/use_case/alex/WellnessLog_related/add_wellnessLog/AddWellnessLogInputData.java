package use_case.alex.WellnessLog_related.add_wellnessLog;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;

import java.time.LocalDateTime;

public class AddWellnessLogInputData {

    private final LocalDateTime time;
    private final Levels stressLevel;
    private final Levels energyLevel;
    private final Levels fatigueLevel;
    private final MoodLabel moodLabel;
    private final String userNote;

    public AddWellnessLogInputData(LocalDateTime time,
                                   Levels stressLevel,
                                   Levels energyLevel,
                                   Levels fatigueLevel,
                                   MoodLabel moodLabel,
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

    public MoodLabel getMoodLabel() {
        return moodLabel;
    }

    public String getUserNote() {
        return userNote;
    }
}

