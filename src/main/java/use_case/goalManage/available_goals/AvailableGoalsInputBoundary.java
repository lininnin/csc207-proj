package use_case.goalManage.available_goals;

/**
 * Input boundary interface for the Available Goals use case.
 * Defines the operations that can be performed on available goals.
 */
public interface AvailableGoalsInputBoundary {
    /**
     * Executes the default operation to fetch available goals.
     */
    void execute();

    /**
     * Executes an operation with a specific command.
     * @param command The command string specifying the operation
     */
    void execute(String command);
}
