
import java.time.LocalDateTime;

public class task extends info {
    boolean isComplete;
    LocalDateTime completedDateTime;
    boolean overDue;
    priority taskPriority;

    enum priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public task(info info, LocalDateTime beginDate, LocalDateTime dueDate) {
        super(info.name, info.description, info.category);
        this.id = info.id;
        this.isComplete = false;
        this.completedDateTime = null;
        this.beginDate = beginDate;
        this.dueDate = LocalDate.now();
        this.overDue = false;
        this.taskPriority = priority.MEDIUM; // default priority
    }

    public void setOverDue() {
        if (dueDate != null && LocalDateTime.now().isAfter(dueDate) && !isComplete) {
            overDue = true;
        }
    }

    public void completeTask(LocalDateTime completionTime) {
        this.isComplete = true;
        this.completedDateTime = completionTime;
    }

    public void setPriority(priority p) {
        this.taskPriority = p;
    }

    public priority getPriority() {
        return this.taskPriority;
    }
}

