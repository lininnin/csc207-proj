package entity.Sophia;

import entity.BeginAndDueDates.BeginAndDueDates;

public class goalFactory implements GoalFactoryInterface {

    @Override
    public Goal createGoal(GoalInfo goalInfo, BeginAndDueDates dates, goalInterface.TimePeriod timePeriod, int frequency) {
        return new Goal(goalInfo, dates, timePeriod, frequency);
    }
}
