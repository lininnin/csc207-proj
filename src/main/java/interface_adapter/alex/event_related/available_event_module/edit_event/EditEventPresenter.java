package interface_adapter.alex.event_related.available_event_module.edit_event;

import entity.info.InfoInterf;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventState;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import use_case.alex.event_related.available_events_module.edit_event.EditEventOutputBoundary;
import use_case.alex.event_related.available_events_module.edit_event.EditEventOutputData;

import java.util.List;

/**
 * Presenter for the EditAvailableEvent use case.
 * Updates the edited Info instance in-place and notifies the view.
 */
public class EditEventPresenter implements EditEventOutputBoundary {

    private final EditedEventViewModel editedEventViewModel;
    private final AvailableEventViewModel availableEventViewModel;
    private TodaysEventsViewModel todaysEventsViewModel;
    private TodaySoFarController todaySoFarController;

    public EditEventPresenter(EditedEventViewModel editedEventViewModel,
                              AvailableEventViewModel availableEventViewModel) {
        this.editedEventViewModel = editedEventViewModel;
        this.availableEventViewModel = availableEventViewModel;
    }
    
    public void setTodaysEventsViewModel(TodaysEventsViewModel todaysEventsViewModel) {
        this.todaysEventsViewModel = todaysEventsViewModel;
    }
    
    public void setTodaySoFarController(TodaySoFarController todaySoFarController) {
        this.todaySoFarController = todaySoFarController;
    }

    @Override
    public void prepareSuccessView(EditEventOutputData outputData) {
        // 1. 更新 EditedEventViewModel 状态（用于弹窗提示）
        EditedEventState newState = new EditedEventState();
        newState.setEventId(outputData.getId());
        newState.setName(outputData.getName());
        newState.setCategory(outputData.getCategory());
        newState.setDescription(outputData.getDescription());
        editedEventViewModel.updateState(newState);

        // 2. 修改 Info 并用新 AvailableEventState 更新 ViewModel
        AvailableEventState oldState = availableEventViewModel.getState();
        List<InfoInterf> eventList = oldState.getAvailableEvents();

        for (InfoInterf info : eventList) {
            if (info.getId().equals(outputData.getId())) {
                info.setName(outputData.getName());
                info.setCategory(outputData.getCategory());
                info.setDescription(outputData.getDescription());
                break;
            }
        }

        // 3. 用新 state 包装后更新 ViewModel ⇒ 触发 UI 监听
        AvailableEventState newStateForView = new AvailableEventState();
        newStateForView.setAvailableEvents(eventList); // 原列表也行
        availableEventViewModel.setState(newStateForView);
        availableEventViewModel.firePropertyChanged(AvailableEventViewModel.AVAILABLE_EVENTS_PROPERTY);
        
        // 4. Also trigger refresh of today's events view since they share the same Info objects
        if (todaysEventsViewModel != null) {
            // Fire property change to refresh the today's events display
            todaysEventsViewModel.firePropertyChanged("state");
        }
        
        // 5. Also refresh Today So Far panel to show updated event names/categories
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
            System.out.println("DEBUG: Triggered Today So Far refresh after event edit");
        }

    }



    @Override
    public void prepareFailView(EditEventOutputData outputData) {
        EditedEventState newState = new EditedEventState();
        newState.setEventId(outputData.getId());
        newState.setEditError(outputData.getErrorMessage()); // ✅ 使用精确错误信息
        editedEventViewModel.updateState(newState);
        editedEventViewModel.firePropertyChanged("state");   // ✅ 通知视图
    }
}
