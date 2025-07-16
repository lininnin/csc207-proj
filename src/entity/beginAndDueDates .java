import java.time.LocalDate;

public class BeginAndDueDates {
    private LocalDate beginDate;
    private LocalDate dueDate;

    public BeginAndDueDates(LocalDate beginDate, LocalDate dueDate) {
        this.beginDate = beginDate;
        this.dueDate = dueDate;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
