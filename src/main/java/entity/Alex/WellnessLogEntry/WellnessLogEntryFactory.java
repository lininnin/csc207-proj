package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;
import use_case.alex.wellness_log_related.todays_wellness_log_module.edit_wellnesslog.EditWellnessLogInputData;

import java.time.LocalDateTime;

/**
 * Factory class for creating {@link WellnessLogEntry} instances.
 * Implements the {@link WellnessLogEntryFactoryInterf} interface.
 */
public class WellnessLogEntryFactory implements WellnessLogEntryFactoryInterf {

    /**
     * Creates a new {@link WellnessLogEntry} with the specified parameters
     * using the Builder pattern.
     *
     * @param time         The date and time of the wellness log entry.
     * @param stressLevel  The stress level at the time of logging.
     * @param energyLevel  The energy level at the time of logging.
     * @param fatigueLevel The fatigue level at the time of logging.
     * @param moodLabel    The mood label associated with the log entry.
     * @param userNote     An optional user note for additional context.
     * @return A newly constructed {@link WellnessLogEntry} instance.
     */
    @Override
    public WellnessLogEntryInterf create(final LocalDateTime time,
                                   final Levels stressLevel,
                                   final Levels energyLevel,
                                   final Levels fatigueLevel,
                                   final MoodLabelInterf moodLabel,
                                   final String userNote) {

        return new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(stressLevel)
                .energyLevel(energyLevel)
                .fatigueLevel(fatigueLevel)
                .moodLabel(moodLabel)
                .userNote(userNote)
                .build();
    }

    public WellnessLogEntryInterf from(WellnessLogEntryInterf entry) {
        return WellnessLogEntry.Builder.from(entry).build();
    }
}


