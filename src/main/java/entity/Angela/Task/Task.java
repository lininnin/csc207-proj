package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a Task that are added to Today's tasks area,
 * info: Info — info of this task
 * priority: enum— Optional Task Priority level (HIGH, MEDIUM, LOW)
 * isCompleted: boolean — Whether the task is currently marked as done
 * completedDateTime: LocalDateTime — Optional completed date for the instance
 * beginAndDueDates:  BeginAndDueDates — Begin and due dates of this task
 * overDue: boolean — The Task is overdue or not
 */
public class Task implements TaskInterf {
    public enum Priority {
        Low,
        Medium,
        High
    }

    private Info info;
    private Priority priority;
    private boolean isCompleted;
    private LocalDateTime completedDateTimes;
    private BeginAndDueDates beginAndDueDates;
    private boolean overDue;

    /**
     * Constructs a new Info object with optional description and category.
      * @param builder the builder of the item
     */

    private Task(Builder builder) {
        if (builder.info == null) {
            throw new IllegalArgumentException("Info is required to create a Task");
        }
        if (builder.beginAndDueDates == null) {
            throw new IllegalArgumentException("BeginAndDueDates is required to create a Task");
        }
        this.info = builder.info; //pass the info
        this.priority = builder.priority; //pass the priority
        this.isCompleted = false; //the status of a new task is always not completed
        this.completedDateTimes = null; //do not have information of completed date & time for a new task
        this.beginAndDueDates = builder.beginAndDueDates; //pass the beginning and due dates
        this.overDue = false; //a new task is always not overdue
    }

    public static class Builder {
        private final Info info;
        private Priority priority = null;
        private BeginAndDueDates beginAndDueDates;

        public Builder(Info info) {
            if (info == null) {
                throw new IllegalArgumentException("Info cannot be null");
            }
            this.info = info;
        }

        public Builder priority(Priority priority) {
            if (priority == null) {
                throw new IllegalArgumentException("Priority cannot be null");
            }
            this.priority = priority;
            return this;
        }

        public Builder beginAndDueDates(BeginAndDueDates beginAndDueDates) {
            if (beginAndDueDates == null) {
                throw new IllegalArgumentException("BeginAndDueDates cannot be null");
            }
            this.beginAndDueDates = beginAndDueDates;
            return this;
        }

        public Task build() {
            if (beginAndDueDates == null) {
                throw new IllegalStateException("BeginAndDueDates must be set before building the task");
            }
            return new Task(this);
        }
    }

// ----------------- Getters -----------------

    public Info getInfo() {
        return info;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean getStatus() {
        return isCompleted;
    }

    public LocalDateTime getCompletedDateTimes() {
        return completedDateTimes;
    }

    public BeginAndDueDates getBeginAndDueDates() {
        return beginAndDueDates;
    }

    public boolean isOverDue() {
        return overDue;
    }


// ----------------- Editors with validation -----------------

    public void editInfo(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        this.info = info;
    }


    public void editPriority(Priority newPriority) {
        if (newPriority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = newPriority;
    }

    public void editStatus(boolean status) {
        this.isCompleted = status;
        if (status) {
            this.completedDateTimes = LocalDateTime.now();
        } else {
            this.completedDateTimes = null;
        }
    }

    public void editCompletedDateTimes(LocalDateTime completedDateTime) {
        if (completedDateTime == null) {
            throw new IllegalArgumentException("CompletedDateTimes cannot be null");
        }
        this.completedDateTimes = completedDateTime;
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
