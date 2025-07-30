package use_case.Alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog;

/**
 * Input boundary interface for the EditWellnessLog use case.
 * Defines the method that must be implemented by the interactor.
 */
public interface EditWellnessLogInputBoundary {

    /**
     * Executes the use case with the provided input data.
     *
     * @param inputData The input data required to edit a wellness log.
     */
    void execute(EditWellnessLogInputData inputData);
}

