package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabelInterf;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a wellness log entry containing stress, energy, fatigue levels and mood information.
 * <p>
 * This class uses the Builder pattern for controlled construction and input validation.
 * Now decoupled from the concrete MoodLabel class by depending on the MoodLabelInterf interface.
 */
public class WellnessLogEntry implements WellnessLogEntryInterf {

    private final String id;
    private final LocalDateTime time;
    private Levels stressLevel;
    private Levels energyLevel;
    private Levels fatigueLevel;
    private MoodLabelInterf moodLabel;
    private String userNote;

    /**
     * Private constructor used by the Builder.
     */
    private WellnessLogEntry(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID().toString();
        this.time = builder.time;
        this.stressLevel = builder.stressLevel;
        this.energyLevel = builder.energyLevel;
        this.fatigueLevel = builder.fatigueLevel;
        this.moodLabel = builder.moodLabel;
        this.userNote = builder.userNote;
    }

    public static class Builder {
        private String id;
        private LocalDateTime time;
        private Levels stressLevel;
        private Levels energyLevel;
        private Levels fatigueLevel;
        private MoodLabelInterf moodLabel;
        private String userNote;

        // ✅ 新增：从已有 WellnessLogEntry 构建一个可变 Builder
        public static Builder from(WellnessLogEntry entry) {
            Builder builder = new Builder();
            builder.id = entry.id;
            builder.time = entry.time;
            builder.stressLevel = entry.stressLevel;
            builder.energyLevel = entry.energyLevel;
            builder.fatigueLevel = entry.fatigueLevel;
            builder.moodLabel = entry.moodLabel;
            builder.userNote = entry.userNote;
            return builder;
        }

        public Builder time(LocalDateTime time) {
            this.time = time;
            return this;
        }

        public Builder stressLevel(Levels level) {
            this.stressLevel = level;
            return this;
        }

        public Builder energyLevel(Levels level) {
            this.energyLevel = level;
            return this;
        }

        public Builder fatigueLevel(Levels level) {
            this.fatigueLevel = level;
            return this;
        }

        public Builder moodLabel(MoodLabelInterf mood) {
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
            if (time == null || moodLabel == null ||
                    stressLevel == null || energyLevel == null || fatigueLevel == null) {
                throw new IllegalStateException("All required fields must be set.");
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

    public MoodLabelInterf getMoodLabel() {
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

    public void setMoodLabel(MoodLabelInterf mood) {
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



