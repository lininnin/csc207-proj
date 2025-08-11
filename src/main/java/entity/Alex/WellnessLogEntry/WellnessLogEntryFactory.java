package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;

import java.time.LocalDateTime;

public class WellnessLogEntryFactory implements WellnessLogEntryFactoryInterf {

    @Override
    public WellnessLogEntryInterf create(LocalDateTime time,
                                   Levels stressLevel,
                                   Levels energyLevel,
                                   Levels fatigueLevel,
                                   MoodLabelInterf moodLabel,
                                   String userNote) {

        return new WellnessLogEntry.Builder()
                .time(time)
                .stressLevel(stressLevel)
                .energyLevel(energyLevel)
                .fatigueLevel(fatigueLevel)
                .moodLabel(moodLabel)
                .userNote(userNote)
                .build();
    }
}

