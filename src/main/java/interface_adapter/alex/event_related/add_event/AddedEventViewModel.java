package interface_adapter.alex.event_related.add_event;

import interface_adapter.ViewModel;

public class AddedEventViewModel extends ViewModel<AddedEventState> {

    public static final String ADD_EVENT_STATE_PROPERTY = "addEventState";

    // ✅ 添加标签常量
    public static final String TITLE_LABEL = "Add Today's Event";
    public static final String NAME_LABEL = "Select Event Name:";
    public static final String DUE_DATE_LABEL = "Due Date (optional, yyyy-mm-dd):";
    public static final String ADD_BUTTON_LABEL = "Add";

    public AddedEventViewModel() {
        super("add event");
        this.setState(new AddedEventState());  // 初始化 state
    }

    @Override
    public void setState(AddedEventState state) {
        super.setState(state);
        this.firePropertyChanged(ADD_EVENT_STATE_PROPERTY); // 通知 UI 更新
    }
}



