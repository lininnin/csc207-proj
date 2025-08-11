package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabelInterf;
import java.time.LocalDateTime;

/**
 * Interface for WellnessLogEntry entity.
 * Provides read and write access to wellness log entry properties without depending on a concrete implementation.
 */
public interface WellnessLogEntryInterf {

    // ---------- Getters ----------
    String getId();

    LocalDateTime getTime();

    Levels getStressLevel();

    Levels getEnergyLevel();

    Levels getFatigueLevel();

    MoodLabelInterf getMoodLabel();

    String getUserNote();

    // ---------- Setters ----------
    void setStressLevel(Levels level);

    void setEnergyLevel(Levels level);

    void setFatigueLevel(Levels level);

    void setMoodLabel(MoodLabelInterf mood);

    void setUserNote(String note);
}
