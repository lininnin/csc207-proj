package use_case.goalManage.order_goal;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import use_case.repository.GoalRepository;
import entity.Sophia.Goal;
import use_case.goalManage.available_goals.AvailableGoalsOutputBoundary;
import use_case.goalManage.available_goals.AvailableGoalsOutputData;

/**
 * The interactor for the order goals use case.
 * It handles the business logic of retrieving, sorting, and presenting goals.
 */
public class OrderGoalsInteractor implements OrderGoalsInputBoundary {
    private final GoalRepository goalRepository;
    private final OrderGoalsOutputBoundary presenter;
    private final AvailableGoalsOutputBoundary availableGoalsPresenter;

    /**
     * Constructs an {@code OrderGoalsInteractor} with the required dependencies.
     *
     * @param goalRepository The repository for accessing goal data.
     * @param presenter The output boundary for the order goals use case, presenting the sorted data.
     * @param availableGoalsPresenter The output boundary for the available goals use case
     */
    public OrderGoalsInteractor(GoalRepository goalRepository,
                                OrderGoalsOutputBoundary presenter,
                                AvailableGoalsOutputBoundary availableGoalsPresenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
        this.availableGoalsPresenter = availableGoalsPresenter;
    }

    /**
     * Executes the main logic for ordering goals.
     * It retrieves all goals, sorts them based on the criteria in the input data, and then presents the sorted list.
     *
     * @param inputData The data containing the sorting criteria (field name and direction).
     */
    @Override
    public void execute(OrderGoalsInputData inputData) {
        final String criteria = inputData.getOrderBy();
        final boolean reverse = inputData.isReverse();
        final List<Goal> allGoals = goalRepository.findAvailableGoals();

        // Implement the sorting logic based on the criteria
        Comparator<Goal> comparator = null;
        switch (criteria.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(goal -> goal.getGoalInfo().getInfo().getName());
                break;
            case "deadline":
                comparator = Comparator.comparing(goal -> goal.getBeginAndDueDates().getDueDate());
                break;
            case "period":
                comparator = Comparator.comparing(goal -> goal.getTimePeriod().toString());
                break;
            default:
                // Handle invalid input, e.g., throw an exception
                throw new IllegalArgumentException("Invalid sorting criteria: " + criteria);
        }

        if (comparator != null) {
            if (reverse) {
                Collections.sort(allGoals, Collections.reverseOrder(comparator));
            }
            else {
                Collections.sort(allGoals, comparator);
            }
        }

        // After sorting, update the goals in the repository
        goalRepository.saveGoals();

        // Tell the presenter to update the view with the newly sorted list
        final AvailableGoalsOutputData outputData = new AvailableGoalsOutputData(allGoals);
        availableGoalsPresenter.presentAvailableGoals(outputData);
    }
}
