package use_case.Angela.task.unmark_complete;

/**
 * Input boundary for the unmark task complete use case.
 */
public interface UnmarkTaskCompleteInputBoundary {
    /**
     * Unmarks a completed task with the given data.
     *
     * @param inputData The task unmark data
     */
    void execute(UnmarkTaskCompleteInputData inputData);
}