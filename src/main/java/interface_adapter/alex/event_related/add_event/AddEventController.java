package interface_adapter.alex.event_related.add_event;

import use_case.alex.event_related.add_event.AddEventInputBoundary;
import use_case.alex.event_related.add_event.AddEventInputData;
import java.time.LocalDate;

/**
 * Controller for the AddEvent use case.
 * Converts UI inputs into AddEventInputData and passes them to the interactor.
 */
public class AddEventController {

    /** The interactor for the AddEvent use case. */
    private final AddEventInputBoundary addEventUseCaseInteractor;

    /**
     * Constructs the AddEventController with the given interactor.
     *
     * @param interactor the use case interactor to handle adding events
     */
    public AddEventController(final AddEventInputBoundary interactor) {
        this.addEventUseCaseInteractor = interactor;
    }

    /**
     * Executes the AddEvent use case with the provided event name and due date.
     *
     * @param selectedName The name of the selected event.
     * @param dueDate      The due date for the event.
     */
    public void execute(final String selectedName,
                        final LocalDate dueDate) {
        AddEventInputData inputData =
                new AddEventInputData(selectedName, dueDate);
        addEventUseCaseInteractor.execute(inputData);
    }
}


