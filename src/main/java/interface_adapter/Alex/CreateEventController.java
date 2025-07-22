package interface_adapter.Alex;

import use_case.Alex.create_event.CreateEventInputBoundary;
import use_case.Alex.create_event.CreateEventInputData;


/**
 * Controller for the Change Password Use Case.
 */
public class CreateEventController {
}

    private final CreateEventInputBoundary userCreateEventUseCaseInteractor;

    public CreateEventController(CreateEventInputBoundary userCreateEventUseCaseInteractor) {
        this.userCreateEventUseCaseInteractor = userCreateEventUseCaseInteractor;
    }

    /**
     * Executes the Change Password Use Case.
     * @param password the new password
     * @param username the user whose password to change
     */
    public void execute(String password, String username) {
        final CreateEventInputData changePasswordInputData = new CreateEventInputData(username, password);

        userCreateEventUseCaseInteractor.execute(changePasswordInputData);
    }
}