import java.time.LocalDateTime;
import java.time.LocalTime;

public class task{
    info info;
    boolean isComplete;
    LocalDataTime completedDateTime;
    beginAndDueDates beginAndDueDate;
    boolean overDue;

    enum priority {
        LOW,
        MEDIUM,
        HIGH
    }

    public void creatTask(info info, LocalDateTime time){
        this.info = info;
        this.isComplete = false;
        completeData = null;
        beginAndDueDate.setBeginDate(time);
        overDue = false;
    }

    public void setOverDue(LocalDateTime time){
        if completedDateTime.isBefore(time){
            overDue = true;
        }
    }

    public void completeData(){ // waiting for input type
        this.isComplete = true;
    }




}
