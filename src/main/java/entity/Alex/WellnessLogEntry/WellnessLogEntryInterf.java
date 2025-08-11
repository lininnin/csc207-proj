package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;

import java.time.LocalDateTime;

/**
 * Interface for a wellness log entry that tracks mental and physical states.
 */
public interface WellnessLogEntryInterf {

    /**
     * @return The unique ID of the wellness log entry.
     */
    String getId();

    /**
     * @return The timestamp of the wellness log entry.
     */
    LocalDateTime getTime();

    /**
     * @return The stress level recorded.
     */
    Levels getStressLevel();

    /**
     * @return The energy level recorded.
     */
    Levels getEnergyLevel();

    /**
     * @return The fatigue level recorded.
     */
    Levels getFatigueLevel();

    /**
     * @return The mood label recorded.
     */
    MoodLabelInterf getMoodLabel();

    /**
     * @return The optional user note.
     */
    String getUserNote();

    /**
     * Sets the stress level.
     * @param levelParam The stress level.
     */
    void setStressLevel(Levels levelParam);

    /**
     * Sets the energy level.
     * @param levelParam The energy level.
     */
    void setEnergyLevel(Levels levelParam);

    /**
     * Sets the fatigue level.
     * @param levelParam The fatigue level.
     */
    void setFatigueLevel(Levels levelParam);

    /**
     * Sets the mood label.
     * @param moodParam The mood label.
     */
    void setMoodLabel(MoodLabel moodParam);

    /**
     * Sets the user note.
     * @param noteParam The note to attach.
     */
    void setUserNote(String noteParam);
}

