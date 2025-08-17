package interface_adapter.alex.event_related.available_event_module.delete_event;

import entity.info.InfoInterf; // ✅ 改为依赖接口
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.alex.event_related.available_events_module.delete_event.DeleteEventOutputBoundary;
import use_case.alex.event_related.available_events_module.delete_event.DeleteEventOutputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the DeleteAvailableEvent use case.
 * Converts output data into view model state and notifies the view via the ViewModel.
 * Now decoupled from concrete Info class using InfoInterf.
 */
public class DeleteEventPresenter implements DeleteEventOutputBoundary {

    private final DeletedEventViewModel deletedEventViewModel;
    private final AvailableEventViewModel availableEventViewModel;
    private final AddedEventViewModel addedEventViewModel;
    private TodaySoFarController todaySoFarController;

    public DeleteEventPresenter(DeletedEventViewModel deletedEventViewModel,
                                AvailableEventViewModel availableEventViewModel,
                                AddedEventViewModel addedEventViewModel) {
        this.deletedEventViewModel = deletedEventViewModel;
        this.availableEventViewModel = availableEventViewModel;
        this.addedEventViewModel = addedEventViewModel;
    }
    
    public void setTodaySoFarController(TodaySoFarController todaySoFarController) {
        this.todaySoFarController = todaySoFarController;
    }

    @Override
    public void prepareSuccessView(DeleteEventOutputData outputData) {
        // ✅ 更新删除结果 ViewModel
        DeletedEventState newState = new DeletedEventState();
        newState.setDeletedEventId(outputData.getEventId());
        newState.setDeletedEventName(outputData.getEventName());
        newState.setDeletedSuccessfully(true);
        newState.setDeleteError(null);
        deletedEventViewModel.setState(newState);

        // ✅ 更新 AvailableEventViewModel
        AvailableEventState currentState = availableEventViewModel.getState();
        List<InfoInterf> updatedList = new ArrayList<>(currentState.getAvailableEvents());
        updatedList.removeIf(info -> info.getId().equals(outputData.getEventId()));
        currentState.setAvailableEvents(updatedList);
        availableEventViewModel.firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);

        // ✅ 同步更新 AddedEventViewModel 中的下拉框 name 列表
        List<String> names = new ArrayList<>();
        for (InfoInterf info : updatedList) {
            names.add(info.getName());
        }

        AddedEventState addedState = addedEventViewModel.getState();
        addedState.setAvailableNames(names);
        addedEventViewModel.setState(addedState);
        addedEventViewModel.firePropertyChanged(AddedEventViewModel.ADD_EVENT_STATE_PROPERTY);
        
        // ✅ Refresh Today So Far panel to remove deleted event if it was displayed
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
            System.out.println("DEBUG: Triggered Today So Far refresh after event delete");
        }
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

