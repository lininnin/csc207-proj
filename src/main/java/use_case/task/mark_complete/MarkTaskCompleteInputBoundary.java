package use_case.task.mark_complete;

/**
 * Input boundary for marking tasks complete/incomplete.
 */
public interface MarkTaskCompleteInputBoundary {
    /**
     * Marks a task as complete.
     *
     * @param inputData The input data containing task ID
     */
    void markComplete(MarkTaskCompleteInputData inputData);

    /**
     * Marks a task as incomplete.
     *
     * @param inputData The input data containing task ID
     */
    void markIncomplete(MarkTaskCompleteInputData inputData);
}