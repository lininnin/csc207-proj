package use_case.Angela.task.mark_complete;

/**
 * Input boundary for marking a task complete.
 */
public interface MarkTaskCompleteInputBoundary {
    /**
     * Marks a task as complete.
     *
     * @param inputData The input data
     */
    void execute(MarkTaskCompleteInputData inputData);
}