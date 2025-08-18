package use_case.goalManage.today_goal;

import java.util.List;
import java.util.Optional;

import data_access.GoalRepository;
import entity.Sophia.Goal;

/**
 * The interactor for the today's goals use case.
 * It contains the core business logic for retrieving, adding, removing, and updating goals for the day.
 */
public class TodayGoalInteractor implements TodayGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final TodayGoalOutputBoundary outputBoundary;

    /**
     * Constructs a {@code TodayGoalInteractor} with the required dependencies.
     *
     * @param goalRepository The data access object for goals.
     * @param outputBoundary The presenter to prepare the view for success or failure.
     */
    public TodayGoalInteractor(GoalRepository goalRepository,
                               TodayGoalOutputBoundary outputBoundary) {
        this.goalRepository = goalRepository;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Retrieves the list of today's goals and presents them to the output boundary.
     * This method no longer catches exceptions, allowing them to propagate up the call stack.
     */
    @Override
    public void execute() {
        final List<Goal> todayGoals = goalRepository.getTodayGoals();
        final TodayGoalOutputData outputData = new TodayGoalOutputData(todayGoals);
        outputBoundary.prepareSuccessView(outputData);
    }

    /**
     * Adds a new goal to the list of today's goals, preventing duplicates.
     * This method no longer catches exceptions, allowing them to be handled by a higher layer.
     *
     * @param inputData The data identifying the goal to add.
     */
    @Override
    public void addToToday(TodayGoalInputData inputData) {
        final String goalName = inputData.getGoalName();

        // Check if goal already in today's goals to avoid duplicates
        final List<Goal> todayGoals = goalRepository.getTodayGoals();
        final boolean alreadyAdded = todayGoals.stream()
                .anyMatch(goal -> goal.getGoalInfo().getInfo().getName().equals(goalName));

        if (!alreadyAdded) {
            goalRepository.addGoalToToday(goalName);
        }
        execute();
    }

    /**
     * Removes a goal from the list of today's goals.
     * This method no longer catches exceptions, allowing them to be handled by a higher layer.
     *
     * @param inputData The data identifying the goal to remove.
     */
    @Override
    public void removeFromToday(TodayGoalInputData inputData) {
        goalRepository.removeGoalFromToday(inputData.getGoalName());
        execute();
    }

    /**
     * Updates the progress of an existing goal.
     * It handles both integer and double input for progress, rounding to the nearest integer for storage.
     *
     * @param inputData The data identifying the goal and its new progress amount.
     */
    @Override
    public void updateProgress(TodayGoalInputData inputData) {
        final Optional<Goal> optGoal = goalRepository.findByName(inputData.getGoalName());
        if (optGoal.isEmpty()) {
            throw new GoalNotFoundException("Goal not found");
        }
        final Goal goal = optGoal.get();

        final double newAmount = inputData.getNewAmount();
        if (newAmount % 1 == 0) {
            goal.setCurrentProgress((int) newAmount);
        }
        else {
            goal.setCurrentProgress((int) Math.round(newAmount));
        }

        goalRepository.save(goal);
        execute();
    }

    /**
     * Custom exception for when a goal is not found.
     * This class is a private static nested class to keep it within the same file.
     */
    private static class GoalNotFoundException extends RuntimeException {
        GoalNotFoundException(String message) {
            super(message);
        }
    }
}
