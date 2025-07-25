package interface_adapter.Alex.edit_event;

import entity.Info.Info;
import interface_adapter.Alex.available_event.AvailableEventState;
import interface_adapter.Alex.available_event.AvailableEventViewModel;
import use_case.Alex.avaliable_events_module.edit_event.EditEventOutputBoundary;
import use_case.Alex.avaliable_events_module.edit_event.EditEventOutputData;

import java.util.List;

/**
 * Presenter for the EditAvailableEvent use case.
 * Updates the edited Info instance in-place and notifies the view.
 */
public class EditEventPresenter implements EditEventOutputBoundary {

    private final EditedEventViewModel editedEventViewModel;
    private final AvailableEventViewModel availableEventViewModel;

    public EditEventPresenter(EditedEventViewModel editedEventViewModel,
                              AvailableEventViewModel availableEventViewModel) {
        this.editedEventViewModel = editedEventViewModel;
        this.availableEventViewModel = availableEventViewModel;
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
        List<Info> eventList = oldState.getAvailableEvents();

        for (Info info : eventList) {
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

    }



    @Override
    public void prepareFailView(EditEventOutputData outputData) {
        EditedEventState newState = new EditedEventState();
        newState.setEventId(outputData.getId());
        newState.setEditError("Edit failed: input may be invalid or event not found.");

        // ✅ 触发弹窗提示错误
        editedEventViewModel.updateState(newState);
    }
}
