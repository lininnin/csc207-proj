package interface_adapter.Alex;

import use_case.Alex.create_event.CreateEventOutputBoundary;
import use_case.Alex.create_event.CreateEventOutputData;

/**
 * The Presenter for the Change Password Use Case.
 */
public class CreateEventPresenter implements CreateEventOutputBoundary {

    private final CreatedEventViewModel createdEventViewModel;

    public CreateEventPresenter(CreatedEventViewModel createdEventViewModel) {
        this.createdEventViewModel = createdEventViewModel;
    }

    @Override
    public void prepareSuccessView(CreateEventOutputData outputData) {
        // currently there isn't anything to change based on the output data,
        // since the output data only contains the username, which remains the same.
        // We still fire the property changed event, but just to let the view know that
        // it can alert the user that their password was changed successfully..
        createdEventViewModel.firePropertyChanged("password");

    }

    @Override
    public void prepareFailView(String error) {
        // note: this use case currently can't fail
    }
}
