package entity.Alex.Event;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDate;

/**
 * Represents an event that is added to today's task area.
 * It contains info about the event and its begin/due dates.
 */
public final class Event implements EventInterf {

    /**
     * The basic information of the event.
     */
    private Info info;

    /**
     * The begin and due dates associated with the event.
     */
    private BeginAndDueDates beginAndDueDates;

    /**
     * Whether this event is a one-time occurrence.
     */
    private boolean oneTime;

    /**
     * Constructs a new Event object using the provided builder.
     *
     * @param builder the Builder used to construct the Event
     */
    private Event(final Builder builder) {
        if (builder.info == null) {
            throw new IllegalArgumentException("Info is required to create an Event.");
        }
        if (builder.beginAndDueDates == null) {
            throw new IllegalArgumentException(
                    "BeginAndDueDates is required to create an Event.");
        }
        this.info = builder.info;
        this.beginAndDueDates = builder.beginAndDueDates;
        this.oneTime = builder.oneTime;
    }

    /**
     * Builder class for creating Event instances.
     */
    public static class Builder {

        /**
         * The event's information (required).
         */
        private final Info info;

        /**
         * The begin and due dates (required before build).
         */
        private BeginAndDueDates beginAndDueDates;

        /**
         * Whether this event is one-time.
         */
        private boolean oneTime = false;

        /**
         * Constructs a new Builder with the required info.
         *
         * @param infoParam the info object
         */
        public Builder(final Info infoParam) {
            if (infoParam == null) {
                throw new IllegalArgumentException("Info cannot be null.");
            }
            this.info = infoParam;
        }

        /**
         * Sets the begin and due dates.
         *
         * @param beginAndDueDatesParam the BeginAndDueDates object
         * @return the Builder instance
         */
        public Builder beginAndDueDates(final BeginAndDueDates beginAndDueDatesParam) {
            if (beginAndDueDatesParam == null) {
                throw new IllegalArgumentException(
                        "BeginAndDueDates cannot be null.");
            }
            this.beginAndDueDates = beginAndDueDatesParam;
            return this;
        }

        /**
         * Sets whether the event is one-time.
         *
         * @param oneTimeParam true if one-time, false otherwise
         * @return the Builder instance
         */
        public Builder oneTime(final boolean oneTimeParam) {
            this.oneTime = oneTimeParam;
            return this;
        }

        /**
         * Builds and returns the Event instance.
         *
         * @return the constructed Event
         */
        public Event build() {
            if (beginAndDueDates == null) {
                throw new IllegalStateException(
                        "BeginAndDueDates must be set before building the event.");
            }
            return new Event(this);
        }
    }

    /**
     * Returns the info of this event.
     *
     * @return the info object
     */
    public Info getInfo() {
        return info;
    }

    /**
     * Returns the begin and due dates of the event.
     *
     * @return the BeginAndDueDates object
     */
    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    /**
     * Returns whether the event is one-time.
     *
     * @return true if one-time, false otherwise
     */
    public boolean isOneTime() {
        return oneTime;
    }

    /**
     * Updates the info of this event.
     *
     * @param infoParam the new Info object
     */
    public void editInfo(final Info infoParam) {
        if (infoParam == null) {
            throw new IllegalArgumentException("Info cannot be null.");
        }
        this.info = infoParam;
    }

    /**
     * Updates the due date of the event.
     *
     * @param newDueDate the new due date
     */
    public void editDueDate(final LocalDate newDueDate) {
        if (newDueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null.");
        }
        if (beginAndDueDates == null) {
            throw new IllegalStateException("BeginAndDueDates is not initialized.");
        }
        LocalDate beginDate = beginAndDueDates.getBeginDate();
        if (beginDate != null && newDueDate.isBefore(beginDate)) {
            throw new IllegalArgumentException(
                    "Due date cannot be before begin date.");
        }
        this.beginAndDueDates.setDueDate(newDueDate);
    }

    /**
     * Sets whether this event is one-time.
     *
     * @param oneTimeParam true if one-time, false otherwise
     */
    public void setOneTime(final boolean oneTimeParam) {
        this.oneTime = oneTimeParam;
    }
}
