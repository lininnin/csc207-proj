package interface_adapter.alex.event_related.create_event;

import entity.info.InfoInterf;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.alex.event_related.create_event.CreateEventOutputBoundary;
import use_case.alex.event_related.create_event.CreateEventOutputData;
import use_case.alex.event_related.create_event.CreateEventDataAccessInterface;
import entity.info.Info;

import javax.swing.*;
import java.util.List;

/**
 * Presenter for the CreateEvent use case.
 * Updates both CreatedEventViewModel and AvailableEventViewModel.
 */
public class CreateEventPresenter implements CreateEventOutputBoundary {

    private final CreatedEventViewModel createdEventViewModel;
    private final AvailableEventViewModel availableEventViewModel;
    private final CreateEventDataAccessInterface dataAccess;
    private TodaySoFarController todaySoFarController;

    public CreateEventPresenter(CreatedEventViewModel createdEventViewModel,
                                AvailableEventViewModel availableEventViewModel,
                                CreateEventDataAccessInterface dataAccess) {
        this.createdEventViewModel = createdEventViewModel;
        this.availableEventViewModel = availableEventViewModel;
        this.dataAccess = dataAccess;
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }

    @Override
    public void prepareSuccessView(CreateEventOutputData outputData) {
        // 1. 更新创建结果视图
        CreatedEventState createdState = new CreatedEventState();
        createdState.setName("");
        createdState.setCategory("");
        createdState.setDescription("");
        createdEventViewModel.setState(createdState);
        createdEventViewModel.firePropertyChanged(CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY);

        createdEventViewModel.setState(createdState);
        createdEventViewModel.firePropertyChanged(CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY);


        // 2. 获取完整事件列表并更新 AvailableEventViewModel
        List<InfoInterf> updatedList = dataAccess.getAllEvents();
        AvailableEventState newState = new AvailableEventState();
        availableEventViewModel.setState(newState);
        newState.setAvailableEvents(updatedList);
        availableEventViewModel.setState(newState);
        availableEventViewModel.firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);
        System.out.println("CreateEventPresenter triggered");
        
        // Refresh Today So Far panel if controller is available
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
    }


    @Override
    public void prepareFailView(String errorMessage) {
        CreatedEventState state = createdEventViewModel.getState();
        state.setNameError(errorMessage); // 添加
        createdEventViewModel.setState(state);
        //JOptionPane.showMessageDialog(null, errorMessage, "Create Event Failed", JOptionPane.ERROR_MESSAGE);
    }

}


