package use_case.alex.event_related.add_event;

/**
 * OutputBoundary for the AddEvent use case.
 * Defines how the system should present the result of adding an event.
 */
public interface AddEventOutputBoundary {

    /**
     * Prepares the view when the event is successfully added.
     *
     * @param outputData The data to present.
     */
    void prepareSuccessView(AddEventOutputData outputData);

    /**
     * Prepares the view when the event addition fails (e.g., name not found).
     *
     * @param errorMessage The error message to present.
     */
    void prepareFailView(String errorMessage);
}

