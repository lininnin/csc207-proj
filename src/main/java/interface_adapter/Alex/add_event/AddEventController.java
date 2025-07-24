package interface_adapter.Alex.add_event;

import use_case.Alex.add_event.AddEventInputBoundary;
import use_case.Alex.add_event.AddEventInputData;

import java.time.LocalDate;

/**
 * Controller for the AddEvent use case.
 * Converts UI inputs into AddEventInputData and passes them to the interactor.
 */
public class AddEventController {

    private final AddEventInputBoundary addEventUseCaseInteractor;

    public AddEventController(AddEventInputBoundary addEventUseCaseInteractor) {
        this.addEventUseCaseInteractor = addEventUseCaseInteractor;
    }

    /**
     * Called by the view when the user confirms adding an event.
     *
     * @param selectedName The name selected from available events.
     * @param dueDate      The optional due date (can be null).
     */
    public void execute(String selectedName, LocalDate dueDate) {
        AddEventInputData inputData = new AddEventInputData(selectedName, dueDate);
        addEventUseCaseInteractor.execute(inputData);
    }
}

