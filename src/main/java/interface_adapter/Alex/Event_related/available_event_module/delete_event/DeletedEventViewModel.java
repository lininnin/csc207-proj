package interface_adapter.Alex.Event_related.available_event_module.delete_event;

import interface_adapter.ViewModel;

public class DeletedEventViewModel extends ViewModel<DeletedEventState> {

    public static final String DELETE_EVENT_STATE_PROPERTY = "deletedEventState";

    public DeletedEventViewModel() {
        super("deleted event view");
        this.setState(new DeletedEventState());
    }

    // 可选：封装触发事件（也可以用父类提供的 firePropertyChanged()）
    public void updateState(DeletedEventState newState) {
        this.setState(newState);
        this.firePropertyChanged(DELETE_EVENT_STATE_PROPERTY);
    }
}


