package interface_adapter.Alex.delete_todays_event;

import entity.Alex.Event.Event;
import interface_adapter.Alex.add_event.AddedEventViewModel;
import interface_adapter.Alex.todays_events.TodaysEventsState;
import interface_adapter.Alex.todays_events.TodaysEventsViewModel;
import use_case.Alex.todays_events.delete_todays_event.DeleteTodaysEventOutputBoundary;
import use_case.Alex.todays_events.delete_todays_event.DeleteTodaysEventOutputData;

import java.util.ArrayList;
import java.util.List;

public class DeleteTodaysEventPresenter implements DeleteTodaysEventOutputBoundary {

    private final DeleteTodaysEventViewModel deleteTodaysEventViewModel;
    private final TodaysEventsViewModel todaysEventViewModel;
    private final AddedEventViewModel addedEventViewModel;

    public DeleteTodaysEventPresenter(DeleteTodaysEventViewModel deleteTodaysEventViewModel,
                                      TodaysEventsViewModel todaysEventViewModel,
                                      AddedEventViewModel addedEventViewModel) {
        this.deleteTodaysEventViewModel = deleteTodaysEventViewModel;
        this.todaysEventViewModel = todaysEventViewModel;
        this.addedEventViewModel = addedEventViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteTodaysEventOutputData outputData) {
        DeleteTodaysEventState newState = new DeleteTodaysEventState();
        newState.setDeletedEventId(outputData.getEventId());
        newState.setDeletedEventName(outputData.getEventName());
        newState.setDeletedSuccessfully(true);
        newState.setDeleteError(null);
        deleteTodaysEventViewModel.setState(newState);
        deleteTodaysEventViewModel.firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);

        TodaysEventsState currentState = todaysEventViewModel.getState();
        List<Event> updatedList = new ArrayList<>(currentState.getTodaysEvents());
        updatedList.removeIf(event -> event.getInfo().getId().equals(outputData.getEventId()));
        currentState.setTodaysEvents(updatedList);
        todaysEventViewModel.setState(currentState);
        todaysEventViewModel.firePropertyChanged(TodaysEventsViewModel.TODAYS_EVENTS_PROPERTY);

        // ✅ 已删除错误下拉框更新逻辑
    }


    @Override
    public void prepareFailView(DeleteTodaysEventOutputData outputData) {
        DeleteTodaysEventState newState = new DeleteTodaysEventState();
        newState.setDeletedEventId(outputData.getEventId());
        newState.setDeletedEventName(outputData.getEventName());
        newState.setDeletedSuccessfully(false);
        newState.setDeleteError(outputData.getErrorMessage());
        deleteTodaysEventViewModel.setState(newState);
        deleteTodaysEventViewModel.firePropertyChanged(DeleteTodaysEventViewModel.DELETE_TODAYS_EVENT_STATE_PROPERTY);
    }
}

