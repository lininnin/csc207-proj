package entity.Sophia;

import entity.BeginAndDueDates.BeginAndDueDates;

/**
 * A factory interface for creating instances of the {@link Goal} class.
 * This interface defines a contract for any class that is responsible for
 * instantiating new goals with their required attributes, promoting
 * a standardized and decoupled creation process.
 */
public interface GoalFactoryInterface {

    /**
     * Creates a new {@link Goal} object with the specified parameters.
     *
     * @param goalInfo The metadata and information for the goal.
     * @param dates The beginning and due dates for the goal.
     * @param timePeriod The time period (e.g., WEEK, MONTH) for the goal.
     * @param frequency The required number of task completions to meet the goal.
     * @return A new {@link Goal} instance.
     */
    Goal createGoal(GoalInfo goalInfo, BeginAndDueDates dates, GoalInterface.TimePeriod timePeriod, int frequency);
}
