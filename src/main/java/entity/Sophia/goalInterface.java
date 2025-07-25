package entity.Sophia;


import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Info.Info;

import java.time.LocalDateTime;

public interface goalInterface {
    public enum TimePeriod {
        WEEK,
        MONTH
    }

    GoalInfo getGoalInfo();

    BeginAndDueDates getBeginAndDueDates();

    TimePeriod getTimePeriod();

    int getCurrentProgress();

    boolean isCompleted();

    Info getInfo();

    Info getTargetTaskInfo();

    int getFrequency();

    LocalDateTime getCompletedDateTime();
}
