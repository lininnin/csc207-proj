package use_case.goalManage.create_goal;

import use_case.repository.GoalRepository;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.Sophia.Goal;
import entity.Sophia.GoalFactory;
import entity.Sophia.GoalInfo;
import entity.info.Info;

/**
 * Interactor implementation for the Create Goal use case.
 * Handles the business logic of creating new goals.
 */
public class CreateGoalInteractor implements CreateGoalInputBoundary {
    private final GoalRepository goalRepository;
    private final CreateGoalOutputBoundary presenter;
    private final GoalFactory goalFactory;

    /**
     * Constructs a CreateGoalInteractor with required dependencies.
     * @param goalRepository Repository for saving goals
     * @param presenter Presenter for handling output
     * @param goalFactory Factory for creating goal instances
     */
    public CreateGoalInteractor(GoalRepository goalRepository,
                                CreateGoalOutputBoundary presenter,
                                GoalFactory goalFactory) {
        this.goalRepository = goalRepository;
        this.presenter = presenter;
        this.goalFactory = goalFactory;
    }

    /**
     * Executes the goal creation process using the provided input data.
     * @param inputData Contains all parameters needed for goal creation
     */
    @Override
    public void execute(CreateGoalInputData inputData) {
        try {
            // Create Info objects using the Builder pattern
            final Info mainInfo = new Info.Builder(inputData.getGoalName())
                    .description(inputData.getGoalDescription())
                    .build();

            // Use the actual target task's info if provided
            final Info targetTaskInfo;
            if (inputData.getTargetTask() != null && inputData.getTargetTask().getInfo() != null) {
                // Use the actual task's info
                targetTaskInfo = (Info) inputData.getTargetTask().getInfo();
                System.out.println("DEBUG: CreateGoalInteractor - Using target task: " + targetTaskInfo.getName());
            }
            else {
                // Fallback to default if no target task provided
                targetTaskInfo = new Info.Builder("TargetTask")
                        .description("Default target task")
                        .build();
                System.out.println("DEBUG: CreateGoalInteractor - No target task provided, using default");
            }

            // Create GoalInfo and BeginAndDueDates
            final GoalInfo goalInfo = new GoalInfo(mainInfo, targetTaskInfo);
            final BeginAndDueDates dates = new BeginAndDueDates(
                    inputData.getStartDate(),
                    inputData.getEndDate()
            );

            // Create the goal using the factory
            final Goal goal = goalFactory.createGoal(
                    goalInfo,
                    dates,
                    inputData.getTimePeriod(),
                    inputData.getFrequency()
            );

            // Goal starts with 0 progress by default

            // Save and present results
            goalRepository.save(goal);
            presenter.presentSuccess(new CreateGoalOutputData(
                    goal.getGoalInfo().getInfo().getName(),
                    "Goal created successfully"
            ));
        }
        catch (IllegalArgumentException ex) {
            presenter.presentError("Invalid goal parameters: " + ex.getMessage());
        }
        catch (Exception ex) {
            presenter.presentError("Failed to create goal: " + ex.getMessage());
        }
    }
}
