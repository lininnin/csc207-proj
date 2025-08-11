package interface_adapter.alex.WellnessLog_related.todays_wellnesslog_module.todays_wellness_log;

import interface_adapter.ViewModel;

public class TodaysWellnessLogViewModel extends ViewModel<TodaysWellnessLogState> {

    public static final String TODAYS_WELLNESS_LOG_PROPERTY = "todaysWellnessLog";

    public TodaysWellnessLogViewModel() {
        super("TodaysWellnessLogView");
        this.setState(new TodaysWellnessLogState());
    }

    /**
     * Notifies the view that the state has changed.
     */
    public void fireStateChanged() {
        firePropertyChanged(TODAYS_WELLNESS_LOG_PROPERTY);
    }
}

