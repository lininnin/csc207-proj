package use_case.task.mark_complete;

/**
 * Input boundary for marking tasks complete/incomplete.
 */
public interface MarkTaskCompleteInputBoundary {
    /**
     * Marks a task as complete.
     *
     * @param taskId The ID of the task to mark complete
     */
    void markComplete(String taskId);

    /**
     * Marks a task as incomplete.
     *
     * @param taskId The ID of the task to mark incomplete
     */
    void markIncomplete(String taskId);
}