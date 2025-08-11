package interface_adapter.alex.event_related.todays_events_module.delete_todays_event;

import entity.Alex.Event.EventInterf;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventOutputBoundary;
import use_case.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the DeleteTodaysEvent use case.
 * Updates the TodaysEventsViewModel and DeleteTodaysEventViewModel after deletion.
 * Now fully decoupled from the concrete Event class using EventInterf.
 */
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
        List<EventInterf> updatedList = new ArrayList<>(currentState.getTodaysEvents());
        updatedList.removeIf(event -> event.getInfo() != null &&
                event.getInfo().getId().equals(outputData.getEventId()));
        currentState.setTodaysEvents(updatedList);
        todaysEventViewModel.setState(currentState);
        todaysEventViewModel.firePropertyChanged("state");

        // 这里如果要同步更新 AddedEventViewModel 下拉框数据，可以在此处添加逻辑
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


