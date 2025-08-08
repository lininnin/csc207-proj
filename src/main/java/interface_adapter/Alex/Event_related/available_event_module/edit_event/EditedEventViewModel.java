package interface_adapter.Alex.Event_related.available_event_module.edit_event;

import interface_adapter.ViewModel;

public class EditedEventViewModel extends ViewModel<EditedEventState> {

    public static final String EDITED_EVENT_STATE_PROPERTY = "editedEventState";

    public EditedEventViewModel() {
        super("Edited Event View");
        this.setState(new EditedEventState());
    }

    /**
     * Updates the state and notifies listeners.
     */
    public void updateState(EditedEventState newState) {
        this.setState(newState);
        this.firePropertyChanged(EDITED_EVENT_STATE_PROPERTY);
    }

    public void clearError() {
        EditedEventState cleared = new EditedEventState();
        // 保留当前 id，如果需要
        cleared.setEventId(this.getState().getEventId());
        cleared.setEditError(null); // 清空错误
        this.setState(cleared);
        this.firePropertyChanged("state"); // ✅ 通知视图更新
    }

}

