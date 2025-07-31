package interface_adapter.Sophia.today_goal;

import view.ViewModel;

public class TodaysGoalsViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Today's Goals";
    public static final String REFRESH_BUTTON_LABEL = "Refresh";

    private TodaysGoalsState state = new TodaysGoalsState();

    public TodaysGoalsViewModel() {
        super("today's goals");
    }

    public TodaysGoalsState getState() {
        return state;
    }

    public void setState(TodaysGoalsState state) {
        this.state = state;
    }
}