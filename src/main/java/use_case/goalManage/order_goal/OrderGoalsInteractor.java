package use_case.goalManage.order_goal;

import entity.Sophia.Goal;
import data_access.GoalRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderGoalsInteractor implements OrderGoalsInputBoundary {
    private final GoalRepository goalRepository;
    private final OrderGoalsOutputBoundary presenter;

    public OrderGoalsInteractor(GoalRepository goalRepository,
                                OrderGoalsOutputBoundary presenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(OrderGoalsInputData inputData) {
        List<Goal> goals = goalRepository.getAllGoals();
        String orderBy = inputData.getOrderBy().toLowerCase();

        switch (orderBy) {
            case "name":
                goals.sort(Comparator.comparing(g -> g.getInfo().getName()));
                break;
            case "deadline":
                goals.sort(Comparator.comparing(g -> g.getBeginAndDueDates().getDueDate()));
                break;
            case "period":
                goals.sort(Comparator.comparing(Goal::getTimePeriod));
                break;
            default:
                break;
        }

        if (inputData.isReverse()) {
            Collections.reverse(goals);
        }

        presenter.present(new OrderGoalsOutputData(goals));
    }
}