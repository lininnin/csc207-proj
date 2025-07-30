package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDateTime;

public interface TaskInterf {
    Info getInfo();

    Task.Priority getPriority();

    boolean getStatus();

    LocalDateTime getCompletedDateTimes();

    BeginAndDueDates getBeginAndDueDates();

    boolean isOverDue();
}
