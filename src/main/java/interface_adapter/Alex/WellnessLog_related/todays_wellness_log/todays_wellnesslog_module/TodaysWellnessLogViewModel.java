package interface_adapter.Alex.WellnessLog_related.todays_wellness_log.todays_wellnesslog_module;

import interface_adapter.ViewModel;

public class TodaysWellnessLogViewModel extends ViewModel<TodaysWellnessLogState> {

    public static final String TODAYS_WELLNESS_LOG_PROPERTY = "todaysWellnessLog";

    public TodaysWellnessLogViewModel() {
        super("TodaysWellnessLogView");
        this.setState(new TodaysWellnessLogState());
    }
}
