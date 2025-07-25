package use_case.Alex.avaliable_events_module.edit_event;

public interface EditEventInputBoundary {
    /**
     * Executes the edit event use case with the provided input data.
     *
     * @param inputData The data needed to perform the edit operation.
     */
    void execute(EditEventInputData inputData);
}

