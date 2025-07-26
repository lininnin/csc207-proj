package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;

import java.time.LocalDateTime;

public class WellnessLogEntryFactory implements WellnessLogEntryFactoryInterf {

    @Override
    public WellnessLogEntry create(LocalDateTime time,
                                   Levels stressLevel,
                                   Levels energyLevel,
                                   Levels fatigueLevel,
                                   MoodLabel moodLabel,
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

