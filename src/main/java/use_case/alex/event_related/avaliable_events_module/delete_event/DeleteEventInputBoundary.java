package use_case.alex.event_related.avaliable_events_module.delete_event;

/**
 * Input boundary for the DeleteAvailableEvent use case.
 * Defines the input method that the controller can call.
 */
public interface DeleteEventInputBoundary {

    /**
     * Executes the delete use case with the provided input data.
     *
     * @param inputData The input data containing event ID (and possibly more info).
     */
    void execute(DeleteEventInputData inputData);
}

