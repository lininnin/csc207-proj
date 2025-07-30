package interface_adapter.Alex.Event_related.create_event;

import use_case.Alex.Event_related.create_event.CreateEventInputBoundary;
import use_case.Alex.Event_related.create_event.CreateEventInputData;

import java.time.LocalDate;


/**
 * Controller for the Change Password Use Case.
 */
public class CreateEventController {

private final CreateEventInputBoundary userCreateEventUseCaseInteractor;

public CreateEventController(CreateEventInputBoundary userCreateEventUseCaseInteractor) {
    this.userCreateEventUseCaseInteractor = userCreateEventUseCaseInteractor;
}

/**
 * Executes the Change Password Use Case.
 * @param password the new password
 * @param username the user whose password to change
 */
public void execute(String id, String name, String description, String category, LocalDate createdDate) {
    final CreateEventInputData createEventInputData = new CreateEventInputData(id, name, description, category, createdDate);

    userCreateEventUseCaseInteractor.execute(createEventInputData);
}
}
