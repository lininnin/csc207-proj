package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;

import java.time.LocalDateTime;

public interface WellnessLogEntryFactoryInterf {

    /**
     * Creates a new WellnessLogEntry with required fields.
     *
     * @param time         the timestamp of the log
     * @param stressLevel  the stress level (non-null)
     * @param energyLevel  the energy level (non-null)
     * @param fatigueLevel the fatigue level (non-null)
     * @param moodLabel    the mood label (non-null)
     * @param userNote     optional user note (nullable or blank)
     * @return a new WellnessLogEntry
     */
    WellnessLogEntry create(LocalDateTime time,
                            Levels stressLevel,
                            Levels energyLevel,
                            Levels fatigueLevel,
                            MoodLabel moodLabel,
                            String userNote);
}

