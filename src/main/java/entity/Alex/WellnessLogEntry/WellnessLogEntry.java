package entity.Alex.WellnessLogEntry;

import entity.Alex.MoodLabel.MoodLabel;

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
    private int stressLevel;
    private int energyLevel;
    private int fatigueLevel;
    private MoodLabel moodLabel;
    private String userNote;

    /**
     * Private constructor used by the Builder.
     *
     * @param builder the builder object
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
        private int stressLevel;
        private int energyLevel;
        private int fatigueLevel;
        private MoodLabel moodLabel;
        private String userNote;

        /**
         * Sets the time of the wellness log entry.
         *
         * @param time LocalDateTime object (required)
         * @return the builder itself
         * @throws IllegalArgumentException if time is null
         */
        public Builder time(LocalDateTime time) {
            if (time == null) {
                throw new IllegalArgumentException("Time cannot be null");
            }
            this.time = time;
            return this;
        }

        /**
         * Sets the stress level (1-10).
         *
         * @param level stress level
         * @return the builder itself
         * @throws IllegalArgumentException if not in [1, 10]
         */
        public Builder stressLevel(int level) {
            if (level < 1 || level > 10) {
                throw new IllegalArgumentException("Stress level must be between 1 and 10");
            }
            this.stressLevel = level;
            return this;
        }

        /**
         * Sets the energy level (1-10).
         *
         * @param level energy level
         * @return the builder itself
         * @throws IllegalArgumentException if not in [1, 10]
         */
        public Builder energyLevel(int level) {
            if (level < 1 || level > 10) {
                throw new IllegalArgumentException("Energy level must be between 1 and 10");
            }
            this.energyLevel = level;
            return this;
        }

        /**
         * Sets the fatigue level (1-10).
         *
         * @param level fatigue level
         * @return the builder itself
         * @throws IllegalArgumentException if not in [1, 10]
         */
        public Builder fatigueLevel(int level) {
            if (level < 1 || level > 10) {
                throw new IllegalArgumentException("Fatigue level must be between 1 and 10");
            }
            this.fatigueLevel = level;
            return this;
        }

        /**
         * Sets the mood label.
         *
         * @param mood MoodLabel instance (required)
         * @return the builder itself
         * @throws IllegalArgumentException if moodLabel is null
         */
        public Builder moodLabel(MoodLabel mood) {
            if (mood == null) {
                throw new IllegalArgumentException("Mood label cannot be null");
            }
            this.moodLabel = mood;
            return this;
        }

        /**
         * Sets an optional user note.
         *
         * @param note user note
         * @return the builder itself
         */
        public Builder userNote(String note) {
            if (note != null) {
                String trimmed = note.trim();
                if (!trimmed.isEmpty()) {
                    this.userNote = trimmed;
                }
            }
            return this;
        }

        /**
         * Builds the WellnessLogEntry object after validating all required fields.
         *
         * @return new WellnessLogEntry object
         * @throws IllegalStateException if any required field is missing
         */
        public WellnessLogEntry build() {
            if (time == null) {
                throw new IllegalStateException("Time must be provided");
            }
            if (moodLabel == null) {
                throw new IllegalStateException("MoodLabel must be provided");
            }
            return new WellnessLogEntry(this);
        }
    }

    // ------------------ Getters ------------------

    /** @return Unique identifier of this wellness log entry */
    public String getId() {
        return id;
    }

    /** @return Timestamp of the log */
    public LocalDateTime getTime() {
        return time;
    }

    /** @return Stress level (1–10) */
    public int getStressLevel() {
        return stressLevel;
    }

    /** @return Energy level (1–10) */
    public int getEnergyLevel() {
        return energyLevel;
    }

    /** @return Fatigue level (1–10) */
    public int getFatigueLevel() {
        return fatigueLevel;
    }

    /** @return Mood at the time of log */
    public MoodLabel getMoodLabel() {
        return moodLabel;
    }

    /** @return Optional user note */
    public String getUserNote() {
        return userNote;
    }

    // ------------------ Getters ------------------

    /**
     * Updates the stress level.
     *
     * @param level the new stress level (1–10)
     * @throws IllegalArgumentException if level is not in [1, 10]
     */
    public void setStressLevel(int level) {
        if (level < 1 || level > 10) {
            throw new IllegalArgumentException("Stress level must be between 1 and 10");
        }
        this.stressLevel = level;
    }

    /**
     * Updates the energy level.
     *
     * @param level the new energy level (1–10)
     * @throws IllegalArgumentException if level is not in [1, 10]
     */
    public void setEnergyLevel(int level) {
        if (level < 1 || level > 10) {
            throw new IllegalArgumentException("Energy level must be between 1 and 10");
        }
        this.energyLevel = level;
    }

    /**
     * Updates the fatigue level.
     *
     * @param level the new fatigue level (1–10)
     * @throws IllegalArgumentException if level is not in [1, 10]
     */
    public void setFatigueLevel(int level) {
        if (level < 1 || level > 10) {
            throw new IllegalArgumentException("Fatigue level must be between 1 and 10");
        }
        this.fatigueLevel = level;
    }

    /**
     * Updates the mood label.
     *
     * @param mood the new mood label
     * @throws IllegalArgumentException if mood is null
     */
    public void setMoodLabel(MoodLabel mood) {
        if (mood == null) {
            throw new IllegalArgumentException("Mood label cannot be null");
        }
        this.moodLabel = mood;
    }

    /**
     * Updates the user note.
     *
     * @param note the new user note
     * @throws IllegalArgumentException if note is non-null but blank
     */
    public void setUserNote(String note) {
        if (note == null) {
            this.userNote = null;
        } else {
            String trimmed = note.trim();
            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("User note cannot be empty or blank if provided");
            }
            this.userNote = trimmed;
        }
    }

}

