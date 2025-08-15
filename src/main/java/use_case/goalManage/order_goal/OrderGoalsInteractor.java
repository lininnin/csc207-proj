package use_case.goalManage.order_goal;

import data_access.GoalRepository;
import entity.Sophia.Goal;
import use_case.goalManage.available_goals.AvailableGoalsOutputBoundary;
import use_case.goalManage.available_goals.AvailableGoalsOutputData;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderGoalsInteractor implements OrderGoalsInputBoundary {
    private final GoalRepository goalRepository;
    private final OrderGoalsOutputBoundary presenter;
    private final AvailableGoalsOutputBoundary availableGoalsPresenter;

    public OrderGoalsInteractor(GoalRepository goalRepository,
                                OrderGoalsOutputBoundary presenter,
                                AvailableGoalsOutputBoundary availableGoalsPresenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
        this.availableGoalsPresenter = availableGoalsPresenter;
    }

    @Override
    public void execute(OrderGoalsInputData inputData) {
        String criteria = inputData.getOrderBy();
        boolean reverse = inputData.isReverse();
        List<Goal> allGoals = goalRepository.findAvailableGoals();

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
        }

        if (comparator != null) {
            if (reverse) {
                Collections.sort(allGoals, Collections.reverseOrder(comparator));
            } else {
                Collections.sort(allGoals, comparator);
            }
        }

        // After sorting, update the goals in the repository
        goalRepository.saveGoals();

        // Tell the presenter to update the view with the newly sorted list
        AvailableGoalsOutputData outputData = new AvailableGoalsOutputData(allGoals);
        availableGoalsPresenter.presentAvailableGoals(outputData);
    }
}