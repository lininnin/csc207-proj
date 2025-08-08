package use_case.alex.event_related.add_event;

/**
 * Input boundary for the AddEvent use case.
 * Defines the method to be implemented by the interactor that handles
 * event creation logic based on input data from the controller.
 */
public interface AddEventInputBoundary {

    /**
     * Executes the AddEvent use case with the provided input data.
     *
     * @param inputData The input data containing event details such as name and due date.
     */
    void execute(AddEventInputData inputData);
}


