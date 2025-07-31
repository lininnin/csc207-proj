package use_case.create_goal;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Info.Info;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import use_case.repository.GoalRepository;

import java.time.LocalDate;

public class CreateGoalUseCase implements CreateGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final CreateGoalOutputBoundary outputBoundary;

    public CreateGoalUseCase(GoalRepository goalRepository,
                             CreateGoalOutputBoundary outputBoundary) {
        this.goalRepository = goalRepository;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CreateGoalInputData inputData) {
        try {
            validateInputs(inputData);

            Info goalInfo = buildGoalInfo(inputData);
            Info targetTaskInfo = buildTargetTaskInfo(inputData);
            BeginAndDueDates dates = buildDates(inputData);

            Goal newGoal = new Goal(
                    new GoalInfo(goalInfo, targetTaskInfo),
                    dates,
                    inputData.getTimePeriod(),
                    inputData.getFrequency()
            );

            goalRepository.save(newGoal);
            outputBoundary.presentSuccess(new CreateGoalOutputData(
                    newGoal.getInfo().getName(),
                    "Successfully created goal: " + inputData.getName()
            ));

        } catch (IllegalArgumentException e) {
            outputBoundary.presentError(e.getMessage());
        } catch (Exception e) {
            outputBoundary.presentError("Goal creation failed: " + e.getMessage());
        }
    }

    private void validateInputs(CreateGoalInputData inputData) {
        if (goalRepository.existsByName(inputData.getName())) {
            throw new IllegalArgumentException("Goal name already exists");
        }
        if (inputData.getName().length() > 20) {
            throw new IllegalArgumentException("Goal name exceeds 20 characters");
        }
        if (inputData.getDescription() != null && inputData.getDescription().length() > 100) {
            throw new IllegalArgumentException("Description exceeds 100 characters");
        }
        if (inputData.getCategory() != null && inputData.getCategory().length() > 20) {
            throw new IllegalArgumentException("Category name exceeds 20 characters");
        }
        if (inputData.getFrequency() <= 0) {
            throw new IllegalArgumentException("Frequency must be positive");
        }
    }

    private Info buildGoalInfo(CreateGoalInputData inputData) {
        return new Info.Builder(inputData.getName())
                .description(inputData.getDescription())
                .category(inputData.getCategory())
                .build();
    }

    private Info buildTargetTaskInfo(CreateGoalInputData inputData) {
        return new Info.Builder(inputData.getTargetTask()).build();
    }

    private BeginAndDueDates buildDates(CreateGoalInputData inputData) {
        LocalDate beginDate = LocalDate.now();
        LocalDate dueDate = inputData.getTimePeriod() == Goal.TimePeriod.WEEK
                ? beginDate.plusWeeks(1)
                : beginDate.plusMonths(1);
        return new BeginAndDueDates(beginDate, dueDate);
    }
}