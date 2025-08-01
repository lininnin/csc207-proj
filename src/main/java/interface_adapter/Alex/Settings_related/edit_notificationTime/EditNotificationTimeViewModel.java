package interface_adapter.Alex.Settings_related.edit_notificationTime;

import interface_adapter.ViewModel;

public class EditNotificationTimeViewModel extends ViewModel<EditNotificationTimeState> {

    public static final String EDIT_NOTIFICATION_TIME_STATE_PROPERTY = "editNotificationTimeState";

    public EditNotificationTimeViewModel() {
        super("edit notification time view");
        this.setState(new EditNotificationTimeState());
    }
}

