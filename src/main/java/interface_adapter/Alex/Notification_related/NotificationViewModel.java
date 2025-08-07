package interface_adapter.Alex.Notification_related;

import interface_adapter.ViewModel;

public class NotificationViewModel extends ViewModel<NotificationState> {

    public static final String NOTIFICATION_STATE_PROPERTY = "notificationState";

    public NotificationViewModel() {
        super("notification view");
        this.setState(new NotificationState());
    }
}