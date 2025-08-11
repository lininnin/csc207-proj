package use_case.alex.wellness_log_related.add_wellnessLog;

/**
 * Input boundary for the AddWellnessLog use case.
 * This interface is called by the controller.
 */
public interface AddWellnessLogInputBoundary {

    /**
     * Executes the use case for adding a new wellness log entry.
     *
     * @param inputData the input data required to construct the log
     */
    void execute(AddWellnessLogInputData inputData);
}

