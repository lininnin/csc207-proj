package use_case.goalManage.available_goals;

import java.util.List;

import use_case.repository.GoalRepository;
import entity.Sophia.Goal;

/**
 * Interactor implementation for the Available Goals use case.
 * Orchestrates the business logic for managing available goals.
 */
public class AvailableGoalsInteractor implements AvailableGoalsInputBoundary {
    private final GoalRepository goalRepository;
    private final AvailableGoalsOutputBoundary presenter;

    /**
     * Constructs an AvailableGoalsInteractor with dependencies.
     * @param goalRepository The goal data repository
     * @param presenter The output boundary presenter
     */
    public AvailableGoalsInteractor(GoalRepository goalRepository,
                                    AvailableGoalsOutputBoundary presenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
    }

    /**
     * Fetches and presents all available goals.
     */
    @Override
    public void execute() {
        final List<Goal> availableGoals = goalRepository.findAvailableGoals();
        final AvailableGoalsOutputData outputData = new AvailableGoalsOutputData(availableGoals);
        presenter.presentAvailableGoals(outputData);
    }

    /**
     * Executes a command-specific operation.
     * Currently only supports "refresh" command.
     * @param command The operation command
     */
    @Override
    public void execute(String command) {
        if ("refresh".equalsIgnoreCase(command)) {
            execute();
        }
    }
}
