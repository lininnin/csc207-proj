package use_case.orderGoal;

import entity.Goal;
import interface_adapter.GoalRepository;

import java.util.Comparator;
import java.util.List;

public class OrderGoalsUseCase implements OrderGoalsInputBoundary {
    private final GoalRepository goalRepository;
    private final OrderGoalsOutputBoundary presenter;

    public OrderGoalsUseCase(GoalRepository goalRepository, OrderGoalsOutputBoundary presenter) {
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
                // Optional
                break;
        }
        presenter.present(new OrderGoalsOutputData(goals));
    }
}
