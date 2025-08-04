package interface_adapter.Alex.Settings_related.notificationTime_module;

import interface_adapter.ViewModel;

public class NotificationTimeViewModel extends ViewModel<NotificationTimeState> {

    public static final String NOTIFICATION_TIME_STATE_PROPERTY = "notificationTimeState";

    public NotificationTimeViewModel() {
        super("notification time view");
        this.setState(new NotificationTimeState());
    }
}

