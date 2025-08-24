package interface_adapter.alex.event_related.add_event;

import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.alex.event_related.add_event.AddEventOutputBoundary;
import use_case.alex.event_related.add_event.AddEventOutputData;
import use_case.alex.event_related.add_event.AddEventDataAccessInterf;
import entity.alex.Event.EventInterf;

import java.util.List;

/**
 * Presenter for the AddEvent use case.
 * Updates the AddedEventViewModel and TodaysEventsViewModel based on success or failure.
 * Now fully decoupled from the concrete Event class using EventInterf.
 */
public class AddEventPresenter implements AddEventOutputBoundary {

    private final AddedEventViewModel addedEventViewModel;
    private final TodaysEventsViewModel todaysEventsViewModel;
    private final AddEventDataAccessInterf addEventDAO;
    private TodaySoFarController todaySoFarController;

    public AddEventPresenter(AddedEventViewModel addedEventViewModel,
                             TodaysEventsViewModel todaysEventsViewModel,
                             AddEventDataAccessInterf addEventDAO) {
        this.addedEventViewModel = addedEventViewModel;
        this.todaysEventsViewModel = todaysEventsViewModel;
        this.addEventDAO = addEventDAO;
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }

    @Override
    public void prepareSuccessView(AddEventOutputData outputData) {
        // ✅ 更新 Add 模块提示
        AddedEventState state = addedEventViewModel.getState();
        state.setErrorMessage(null);
        state.setSuccessMessage("Event \"" + outputData.getName() + "\" added successfully.");
        state.setSelectedName(outputData.getName());
        state.setDueDate(outputData.getDueDate());
        addedEventViewModel.setState(state);

        // ✅ 刷新 Today's Events 模块
        List<EventInterf> updatedEvents = addEventDAO.getTodaysEvents();  // 使用接口类型
        TodaysEventsState todaysState = new TodaysEventsState();
        todaysState.setTodaysEvents(updatedEvents);
        todaysEventsViewModel.setState(todaysState);
        
        // Refresh Today So Far panel if controller is available
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        } else {
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        AddedEventState state = addedEventViewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        addedEventViewModel.setState(state);
    }
}



