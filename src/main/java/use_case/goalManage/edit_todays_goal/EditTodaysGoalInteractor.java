package use_case.goalManage.edit_todays_goal;

import entity.Sophia.Goal;
import entity.BeginAndDueDates.BeginAndDueDates;
import use_case.repository.GoalRepository;
import java.time.LocalDate;

public class EditTodaysGoalInteractor implements EditTodaysGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final EditTodaysGoalOutputBoundary presenter;

    public EditTodaysGoalInteractor(GoalRepository goalRepository,
                                    EditTodaysGoalOutputBoundary presenter) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditTodaysGoalInputData inputData) {
        try {
            Goal goal = goalRepository.findByName(inputData.getGoalName())
                    .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

            if (!inputData.isConfirmed()) {
                presenter.prepareConfirmationView(
                        new EditTodaysGoalOutputData(
                                inputData.getGoalName(),
                                "Update due date to " + inputData.getNewDueDate() + "?"
                        )
                );
                return;
            }

            BeginAndDueDates newDates = new BeginAndDueDates(
                    goal.getBeginAndDueDates().getBeginDate(),
                    inputData.getNewDueDate()
            );

            Goal updatedGoal = new Goal(
                    goal.getGoalInfo(),
                    newDates,
                    goal.getTimePeriod(),
                    goal.getFrequency()
            );

            // Maintain progress
            for (int i = 0; i < goal.getCurrentProgress(); i++) {
                updatedGoal.recordCompletion();
            }

            goalRepository.save(updatedGoal);
            presenter.prepareSuccessView(
                    new EditTodaysGoalOutputData(
                            inputData.getGoalName(),
                            "Due date updated successfully"
                    )
            );

        } catch (Exception e) {
            presenter.prepareFailView("Failed to edit goal: " + e.getMessage());
        }
    }
}