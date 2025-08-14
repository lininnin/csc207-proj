package use_case.goalManage.create_goal;

import entity.Angela.Task.Task;
import entity.Sophia.Goal;
import entity.Sophia.GoalInfo;
import entity.Sophia.GoalFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import data_access.GoalRepository;

public class CreateGoalInteractor implements CreateGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final CreateGoalOutputBoundary presenter;
    private final GoalFactory goalFactory;

    public CreateGoalInteractor(GoalRepository goalRepository,
                                CreateGoalOutputBoundary presenter,
                                GoalFactory goalFactory) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
        this.goalFactory = goalFactory;
    }

    @Override
    public void execute(CreateGoalInputData inputData) {
        try {
            // Create Info objects using the Builder pattern
            Info mainInfo = new Info.Builder(inputData.getGoalName())
                    .description(inputData.getGoalDescription())
                    .build();

            // Use the actual target task's info if provided
            Info targetTaskInfo;
            if (inputData.getTargetTask() != null && inputData.getTargetTask().getInfo() != null) {
                // Use the actual task's info
                targetTaskInfo = inputData.getTargetTask().getInfo();
                System.out.println("DEBUG: CreateGoalInteractor - Using target task: " + targetTaskInfo.getName());
            } else {
                // Fallback to default if no target task provided
                targetTaskInfo = new Info.Builder("TargetTask")
                        .description(String.valueOf(inputData.getTargetAmount()))
                        .build();
                System.out.println("DEBUG: CreateGoalInteractor - No target task provided, using default");
            }

            // Create GoalInfo and BeginAndDueDates
            GoalInfo goalInfo = new GoalInfo(mainInfo, targetTaskInfo);
            BeginAndDueDates dates = new BeginAndDueDates(
                    inputData.getStartDate(),
                    inputData.getEndDate()
            );

            // Create the goal using the factory
            Goal goal = goalFactory.createGoal(
                    goalInfo,
                    dates,
                    inputData.getTimePeriod(),
                    inputData.getFrequency()
            );

            // Set current amount if provided
            if (inputData.getCurrentAmount() > 0) {
                goal.setCurrentProgress((int) inputData.getCurrentAmount());
            }

            // Save and present results
            goalRepository.save(goal);
            presenter.presentSuccess(new CreateGoalOutputData(
                    goal.getGoalInfo().getInfo().getName(),
                    "Goal created successfully"
            ));
        } catch (IllegalArgumentException e) {
            presenter.presentError("Invalid goal parameters: " + e.getMessage());
        } catch (Exception e) {
            presenter.presentError("Failed to create goal: " + e.getMessage());
        }
    }
}