package entity.Sophia;

import entity.BeginAndDueDates.BeginAndDueDates;

public interface GoalFactoryInterface {
    Goal createGoal(GoalInfo goalInfo, BeginAndDueDates dates, goalInterface.TimePeriod timePeriod, int frequency);
}
