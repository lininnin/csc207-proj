package interface_adapter.Alex.edit_todays_event;

import entity.Alex.Event.Event;
import interface_adapter.Alex.todays_events.TodaysEventsState;
import interface_adapter.Alex.todays_events.TodaysEventsViewModel;
import use_case.Alex.todays_events.edit_todays_event.EditTodaysEventOutputBoundary;
import use_case.Alex.todays_events.edit_todays_event.EditTodaysEventOutputData;

import java.time.LocalDate;
import java.util.List;

/**
 * Presenter for the EditTodaysEvent use case.
 * Updates the Event's due date and refreshes the TodaysEventsViewModel.
 */
public class EditTodaysEventPresenter implements EditTodaysEventOutputBoundary {

    private final EditTodaysEventViewModel editTodaysEventViewModel;
    private final TodaysEventsViewModel todaysEventsViewModel;

    public EditTodaysEventPresenter(EditTodaysEventViewModel editTodaysEventViewModel,
                                    TodaysEventsViewModel todaysEventsViewModel) {
        this.editTodaysEventViewModel = editTodaysEventViewModel;
        this.todaysEventsViewModel = todaysEventsViewModel;
    }

    @Override
    public void prepareSuccessView(EditTodaysEventOutputData outputData) {
        // 1. 更新 EditTodaysEventViewModel 状态
        EditTodaysEventState newState = new EditTodaysEventState();
        newState.setEventId(outputData.getId());
        newState.setDueDate(outputData.getDueDate());
        editTodaysEventViewModel.setState(newState);

        // 2. 查找对应的 Event 并更新 dueDate
        TodaysEventsState oldState = todaysEventsViewModel.getState();
        List<Event> eventList = oldState.getTodaysEvents(); // List<Event> 而非 List<Info>

        for (Event event : eventList) {
            if (event.getInfo().getId().equals(outputData.getId())) {
                event.editDueDate(LocalDate.parse(outputData.getDueDate()));
                break;
            }
        }

        // 3. 触发 UI 刷新
        TodaysEventsState newStateForView = new TodaysEventsState();
        newStateForView.setTodaysEvents(eventList);
        todaysEventsViewModel.setState(newStateForView);
        todaysEventsViewModel.firePropertyChanged(TodaysEventsViewModel.TODAYS_EVENTS_PROPERTY);
    }

    @Override
    public void prepareFailView(EditTodaysEventOutputData outputData) {
        EditTodaysEventState newState = new EditTodaysEventState();
        newState.setEventId(outputData.getId());
        newState.setEditError("Edit failed: invalid due date or event not found.");
        editTodaysEventViewModel.setState(newState);
    }
}


