package interface_adapter.Alex.add_event;

import use_case.Alex.add_event.AddEventOutputBoundary;
import use_case.Alex.add_event.AddEventOutputData;

/**
 * Presenter for the AddEvent use case.
 * Updates the AddEventViewModel based on success or failure of the use case.
 */
public class AddEventPresenter implements AddEventOutputBoundary {

    private final AddedEventViewModel addedEventViewModel;

    public AddEventPresenter(AddedEventViewModel addedEventViewModel) {
        this.addedEventViewModel = addedEventViewModel;
    }

    @Override
    public void prepareSuccessView(AddEventOutputData outputData) {
        AddedEventState state = addedEventViewModel.getState();
        state.setErrorMessage(null); // Clear any previous error
        state.setSuccessMessage("Event \"" + outputData.getName() + "\" added successfully.");
        state.setSelectedName(outputData.getName());
        state.setDueDate(outputData.getDueDate());
        addedEventViewModel.setState(state);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        AddedEventState state = addedEventViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        addedEventViewModel.setState(state);
    }
}

