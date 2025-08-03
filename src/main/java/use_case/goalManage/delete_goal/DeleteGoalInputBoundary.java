package use_case.goalManage.delete_goal;

/**
 * Input boundary for goal deletion
 */
public interface DeleteGoalInputBoundary {
    void execute(DeleteGoalInputData inputData);
}