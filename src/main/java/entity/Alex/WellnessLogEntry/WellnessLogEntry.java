package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.WellnessLogEntry.Levels;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a wellness log entry containing stress, energy, fatigue levels and mood information.
 * <p>
 * This class uses the Builder pattern for controlled construction and input validation.
 */
public class WellnessLogEntry {

    private final String id;
    private final LocalDateTime time;
    private Levels stressLevel;
    private Levels energyLevel;
    private Levels fatigueLevel;
    private MoodLabel moodLabel;
    private String userNote;

    /**
     * Private constructor used by the Builder.
     */
    private WellnessLogEntry(Builder builder) {
        this.id = UUID.randomUUID().toString();
        this.time = builder.time;
        this.stressLevel = builder.stressLevel;
        this.energyLevel = builder.energyLevel;
        this.fatigueLevel = builder.fatigueLevel;
        this.moodLabel = builder.moodLabel;
        this.userNote = builder.userNote;
    }

    /**
     * Builder class for WellnessLogEntry.
     */
    public static class Builder {
        private LocalDateTime time;
        private Levels stressLevel;
        private Levels energyLevel;
        private Levels fatigueLevel;
        private MoodLabel moodLabel;
        private String userNote;

        public Builder time(LocalDateTime time) {
            if (time == null) {
                throw new IllegalArgumentException("Time cannot be null");
            }
            this.time = time;
            return this;
        }

        public Builder stressLevel(Levels level) {
            if (level == null) {
                throw new IllegalArgumentException("Stress level cannot be null");
            }
            this.stressLevel = level;
            return this;
        }

        public Builder energyLevel(Levels level) {
            if (level == null) {
                throw new IllegalArgumentException("Energy level cannot be null");
            }
            this.energyLevel = level;
            return this;
        }

        public Builder fatigueLevel(Levels level) {
            if (level == null) {
                throw new IllegalArgumentException("Fatigue level cannot be null");
            }
            this.fatigueLevel = level;
            return this;
        }

        public Builder moodLabel(MoodLabel mood) {
            if (mood == null) {
                throw new IllegalArgumentException("Mood label cannot be null");
            }
            this.moodLabel = mood;
            return this;
        }

        public Builder userNote(String note) {
            if (note != null) {
                String trimmed = note.trim();
                if (!trimmed.isEmpty()) {
                    this.userNote = trimmed;
                }
            }
            return this;
        }

        public WellnessLogEntry build() {
            if (time == null) {
                throw new IllegalStateException("Time must be provided");
            }
            if (moodLabel == null) {
                throw new IllegalStateException("MoodLabel must be provided");
            }
            if (stressLevel == null || energyLevel == null || fatigueLevel == null) {
                throw new IllegalStateException("All Levels must be provided");
            }
            return new WellnessLogEntry(this);
        }
    }

    // ------------------ Getters ------------------

    public String getId() {
        return id;
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

    // ------------------ Setters ------------------

    public void setStressLevel(Levels level) {
        if (level == null) {
            throw new IllegalArgumentException("Stress level cannot be null");
        }
        this.stressLevel = level;
    }

    public void setEnergyLevel(Levels level) {
        if (level == null) {
            throw new IllegalArgumentException("Energy level cannot be null");
        }
        this.energyLevel = level;
    }

    public void setFatigueLevel(Levels level) {
        if (level == null) {
            throw new IllegalArgumentException("Fatigue level cannot be null");
        }
        this.fatigueLevel = level;
    }

    public void setMoodLabel(MoodLabel mood) {
        if (mood == null) {
            throw new IllegalArgumentException("Mood label cannot be null");
        }
        this.moodLabel = mood;
    }

    public void setUserNote(String note) {
        if (note == null) {
            this.userNote = null;
        } else {
            String trimmed = note.trim();
            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("User note cannot be blank");
            }
            this.userNote = trimmed;
        }
    }
}


