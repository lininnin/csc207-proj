package use_case.mark_task_complete;

/**
 * Input boundary for mark task complete use case.
 */
public interface MarkTaskCompleteInputBoundary {
    void execute(MarkTaskCompleteInputData inputData);
}