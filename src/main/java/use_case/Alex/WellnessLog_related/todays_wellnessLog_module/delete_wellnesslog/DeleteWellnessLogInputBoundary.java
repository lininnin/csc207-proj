package use_case.Alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog;

/**
 * Input boundary for the DeleteWellnessLog use case.
 * Defines the method that must be implemented by the interactor.
 */
public interface DeleteWellnessLogInputBoundary {

    /**
     * Executes the delete operation for the given input data.
     *
     * @param inputData the data containing the ID of the log to delete
     */
    void execute(DeleteWellnessLogInputData inputData);
}

