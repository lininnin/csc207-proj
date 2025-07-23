package interface_adapter.Alex.delete_event;

import entity.Info.Info;
import interface_adapter.Alex.available_event.AvailableEventState;
import interface_adapter.Alex.available_event.AvailableEventViewModel;
import use_case.Alex.delete_event.DeleteEventOutputBoundary;
import use_case.Alex.delete_event.DeleteEventOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the DeleteAvailableEvent use case.
 * Converts output data into view model state and notifies the view via the ViewModel.
 */
public class DeleteEventPresenter implements DeleteEventOutputBoundary {

    private final DeletedEventViewModel deletedEventViewModel;
    private final AvailableEventViewModel availableEventViewModel;

    public DeleteEventPresenter(DeletedEventViewModel deletedEventViewModel,
                                AvailableEventViewModel availableEventViewModel) {
        this.deletedEventViewModel = deletedEventViewModel;
        this.availableEventViewModel = availableEventViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteEventOutputData outputData) {
        // 1. 更新删除结果 ViewModel
        DeletedEventState newState = new DeletedEventState();
        newState.setDeletedEventId(outputData.getEventId());
        newState.setDeletedEventName(outputData.getEventName());
        newState.setDeletedSuccessfully(true);
        newState.setDeleteError(null);
        deletedEventViewModel.setState(newState);

        // 2. 从 AvailableEventState 中移除已删除的事件
        AvailableEventState currentState = availableEventViewModel.getState();
        List<Info> updatedList = new ArrayList<>(currentState.getAvailableEvents());
        updatedList.removeIf(info -> info.getId().equals(outputData.getEventId()));
        currentState.setAvailableEvents(updatedList);
        availableEventViewModel.setState(currentState);
        availableEventViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(DeleteEventOutputData outputData) {
        DeletedEventState newState = new DeletedEventState();
        newState.setDeletedEventId(outputData.getEventId());
        newState.setDeletedSuccessfully(false);
        newState.setDeleteError(outputData.getErrorMessage());
        deletedEventViewModel.setState(newState);
    }
}

