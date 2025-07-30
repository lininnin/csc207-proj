package use_case.goal;

import entity.Sophia.Goal;
import entity.Sophia.GoalFactoryInterface;
import entity.Sophia.GoalInfo;
import entity.Sophia.goalInterface;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDate;
import java.util.Set;

public class CreateGoalInteractor implements CreateGoalInputBoundary {
    private final CreateGoalOutputBoundary presenter;
    private final GoalFactoryInterface factory;
    private final Set<String> existingGoalNames;
    private final Set<String> existingCategories;

    public CreateGoalInteractor(CreateGoalOutputBoundary presenter,
                                GoalFactoryInterface factory,
                                Set<String> existingGoalNames,
                                Set<String> existingCategories) {
        this.presenter = presenter;
        this.factory = factory;
        this.existingGoalNames = existingGoalNames;
        this.existingCategories = existingCategories;
    }

    @Override
    public void createGoal(CreateGoalInputData inputData) {
        // Validate inputs as before...

        goalInterface.TimePeriod period = goalInterface.TimePeriod.valueOf(inputData.timePeriod);

        Info goalMeta = new Info.Builder(inputData.name)
                .description(inputData.description)
                .category(inputData.category)
                .build();

        Info targetTaskMeta = new Info.Builder(inputData.targetTask).build();

        GoalInfo goalInfo = new GoalInfo(goalMeta, targetTaskMeta);

        // Calculate begin and due dates based on period
        LocalDate beginDate = LocalDate.now();
        LocalDate dueDate;
        if (period == goalInterface.TimePeriod.WEEK) {
            dueDate = beginDate.plusWeeks(1);
        } else { // MONTH
            dueDate = beginDate.plusMonths(1);
        }

        BeginAndDueDates dates = new BeginAndDueDates(beginDate, dueDate);

        Goal goal = factory.createGoal(goalInfo, dates, period, inputData.frequency);
        existingGoalNames.add(inputData.name);

        presenter.presentSuccess(new CreateGoalOutputData(goalInfo.getInfo().getName()));
    }

}
