import java.time.LocalDate;
import java.time.LocalDateTime;

public class beginAndDueDates {
    protected LocalDateTime beginDate;
    protected LocalDateTime dueDate;

    public LocalDateTime setBeginDate() {
        this.beginDate = LocalDateTime.now();
        return this.beginDate;
    }

    public LocalDateTime setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        return this.dueDate;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }
}