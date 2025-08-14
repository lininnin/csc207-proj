package entity.Sophia;

import entity.BeginAndDueDates.BeginAndDueDates;

/**
 * A factory class for creating instances of the {@link Goal} class.
 * This factory implements the {@link GoalFactoryInterface} to provide a standardized
 * way of instantiating new goals with their required attributes.
 */
public class GoalFactory implements GoalFactoryInterface {

    /**
     * Creates a new {@link Goal} object with the specified parameters.
     *
     * @param goalInfo The metadata and information for the goal.
     * @param dates The beginning and due dates for the goal.
     * @param timePeriod The time period (e.g., WEEK, MONTH) for the goal.
     * @param frequency The required number of task completions to meet the goal.
     * @return A new {@link Goal} instance.
     */
    @Override
    public Goal createGoal(GoalInfo goalInfo, BeginAndDueDates dates,
                           GoalInterface.TimePeriod timePeriod, int frequency) {
        return new Goal(goalInfo, dates, timePeriod, frequency);
    }
}
