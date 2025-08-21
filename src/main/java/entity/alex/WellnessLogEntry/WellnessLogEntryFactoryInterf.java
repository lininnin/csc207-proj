package entity.alex.WellnessLogEntry;

import entity.alex.MoodLabel.MoodLabelInterf;

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
    WellnessLogEntryInterf create(LocalDateTime time,
                            Levels stressLevel,
                            Levels energyLevel,
                            Levels fatigueLevel,
                            MoodLabelInterf moodLabel,
                            String userNote);

    /**
     * Creates a copy of an existing WellnessLogEntryInterf instance.
     * Useful for cloning or rebuilding an entry.
     *
     * @param entry the entry to clone
     * @return a new copy of the entry
     */
    WellnessLogEntryInterf from(WellnessLogEntryInterf entry);
}

