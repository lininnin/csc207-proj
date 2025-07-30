package entity.Alex.Event;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Info.Info;

import java.time.LocalDate;

/**
 * Represents an event that are added to Today's tasks area,
 * info: Info — info of this task
 * beginAndDueDates:  BeginAndDueDates — Begin and due dates of this task
 */
public class Event implements EventInterf {

    private Info info;
    private BeginAndDueDates beginAndDueDates;

    /**
     * Constructs a new Info object with optional description and category.
     * @param builder the builder of the item
     */

    private Event(Builder builder) {
        if (builder.info == null) {
            throw new IllegalArgumentException("Info is required to create a Task");
        }
        if (builder.beginAndDueDates == null) {
            throw new IllegalArgumentException("BeginAndDueDates is required to create a Task");
        }
        this.info = builder.info; //pass the info
        this.beginAndDueDates = builder.beginAndDueDates; //pass the beginning and due dates

    }

    public static class Builder {
        private final Info info;
        private BeginAndDueDates beginAndDueDates;

        public Builder(Info info) {
            if (info == null) {
                throw new IllegalArgumentException("Info cannot be null");
            }
            this.info = info;
        }

        public Builder beginAndDueDates(BeginAndDueDates beginAndDueDates) {
            if (beginAndDueDates == null) {
                throw new IllegalArgumentException("BeginAndDueDates cannot be null");
            }
            this.beginAndDueDates = beginAndDueDates;
            return this;
        }

        public Event build() {
            if (beginAndDueDates == null) {
                throw new IllegalStateException("BeginAndDueDates must be set before building the task");
            }
            return new Event(this);
        }
    }

// ----------------- Getters -----------------

    public Info getInfo() {
        return info;
    }

    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }


// ----------------- Editors with validation -----------------

    public void editInfo(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        this.info = info;
    }

    public void editDueDate(LocalDate newDueDate) {
        if (newDueDate == null) {
            throw new IllegalArgumentException("Due date cannot be null");
        }
        if (beginAndDueDates == null) {
            throw new IllegalStateException("BeginAndDueDates is not initialized");
        }
        LocalDate beginDate = beginAndDueDates.getBeginDate();
        if (beginDate != null && newDueDate.isBefore(beginDate)) {
            throw new IllegalArgumentException("Due date cannot be before begin date");
        }
        this.beginAndDueDates.setDueDate(newDueDate);
    }

}
