package interface_adapter.Alex.create_event;

import interface_adapter.Alex.available_event.AvailableEventViewModel;
import interface_adapter.Alex.available_event.AvailableEventState;
import use_case.Alex.create_event.CreateEventOutputBoundary;
import use_case.Alex.create_event.CreateEventOutputData;
import use_case.Alex.create_event.CreateEventDataAccessInterface;
import entity.Info.Info;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the CreateEvent use case.
 * Updates both CreatedEventViewModel and AvailableEventViewModel.
 */
public class CreateEventPresenter implements CreateEventOutputBoundary {

    private final CreatedEventViewModel createdEventViewModel;
    private final AvailableEventViewModel availableEventViewModel;
    private final CreateEventDataAccessInterface dataAccess;

    public CreateEventPresenter(CreatedEventViewModel createdEventViewModel,
                                AvailableEventViewModel availableEventViewModel,
                                CreateEventDataAccessInterface dataAccess) {
        this.createdEventViewModel = createdEventViewModel;
        this.availableEventViewModel = availableEventViewModel;
        this.dataAccess = dataAccess;
    }

    @Override
    public void prepareSuccessView(CreateEventOutputData outputData) {
        // 1. 更新创建结果视图
        CreatedEventState createdState = new CreatedEventState();
        createdState.setName(outputData.getName());
        createdEventViewModel.setState(createdState);
        createdEventViewModel.firePropertyChanged(CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY);


        // 2. 获取完整事件列表并更新 AvailableEventViewModel
        List<Info> updatedList = dataAccess.getAllEvents();
        AvailableEventState newState = new AvailableEventState();
        newState.setAvailableEvents(updatedList);
        availableEventViewModel.setState(newState);
        availableEventViewModel.firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);
    }


    @Override
    public void prepareFailView(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Create Event Failed", JOptionPane.ERROR_MESSAGE);
    }

}


