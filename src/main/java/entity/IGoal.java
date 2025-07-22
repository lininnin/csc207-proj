package entity;

import java.time.LocalDateTime;

public interface IGoal {
    enum TimePeriod {
        WEEK,
        MONTH
    }

    void recordCompletion();

    void minusCurrentProgress();

    GoalInfo getGoalInfo();

    BeginAndDueDates getBeginAndDueDates();

    TimePeriod getTimePeriod();

    int getFrequency();

    int getCurrentProgress();

    boolean isCompleted();

    LocalDateTime getCompletedDateTime();

    Info getInfo();

    Info getTargetTaskInfo();
}