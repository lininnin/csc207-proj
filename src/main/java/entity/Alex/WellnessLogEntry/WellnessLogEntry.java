package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;
import entity.Alex.MoodLabel.MoodLabelInterf;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a wellness log entry containing stress,
 * energy, fatigue levels and mood information.
 * <p>
 * This class uses the Builder pattern
 * for controlled construction and input validation.
 */
public final class WellnessLogEntry {

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
    private WellnessLogEntry(final Builder builder) {
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
        public static Builder from(final WellnessLogEntryInterf entry) {
            Builder builder = new Builder();
            builder.id = entry.getId();
            builder.time = entry.getTime();
            builder.stressLevel = entry.getStressLevel();
            builder.energyLevel = entry.getEnergyLevel();
            builder.fatigueLevel = entry.getFatigueLevel();
            builder.moodLabel = entry.getMoodLabel();
            builder.userNote = entry.getUserNote();
            return builder;
        }

        public Builder time(final LocalDateTime timeParam) {
            this.time = timeParam;
            return this;
        }

        public Builder stressLevel(final Levels levelParam) {
            this.stressLevel = levelParam;
            return this;
        }

        public Builder energyLevel(final Levels levelParam) {
            this.energyLevel = levelParam;
            return this;
        }

        public Builder fatigueLevel(final Levels levelParam) {
            this.fatigueLevel = levelParam;
            return this;
        }

        public Builder moodLabel(final MoodLabelInterf moodParam) {
            this.moodLabel = moodParam;
            return this;
        }

        public Builder userNote(final String noteParam) {
            if (noteParam != null) {
                String trimmed = noteParam.trim();
                if (!trimmed.isEmpty()) {
                    this.userNote = trimmed;
                }
            }
            return this;
        }

        public WellnessLogEntry build() {
            if (time == null || moodLabel == null
                    || stressLevel == null || energyLevel == null
                    || fatigueLevel == null) {
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

    public void setStressLevel(final Levels levelParam) {
        if (levelParam == null) {
            throw new IllegalArgumentException("Stress level cannot be null");
        }
        this.stressLevel = levelParam;
    }

    public void setEnergyLevel(final Levels levelParam) {
        if (levelParam == null) {
            throw new IllegalArgumentException("Energy level cannot be null");
        }
        this.energyLevel = levelParam;
    }

    public void setFatigueLevel(final Levels levelParam) {
        if (levelParam == null) {
            throw new IllegalArgumentException("Fatigue level cannot be null");
        }
        this.fatigueLevel = levelParam;
    }

    public void setMoodLabel(final MoodLabelInterf moodParam) {
        if (moodParam == null) {
            throw new IllegalArgumentException("Mood label cannot be null");
        }
        this.moodLabel = moodParam;
    }

    public void setUserNote(final String noteParam) {
        if (noteParam == null) {
            this.userNote = null;
        } else {
            String trimmed = noteParam.trim();
            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("User note cannot be blank");
            }
            this.userNote = trimmed;
        }
    }
}


