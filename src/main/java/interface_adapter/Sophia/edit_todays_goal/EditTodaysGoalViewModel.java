package interface_adapter.Sophia.edit_todays_goal;

import view.ViewModel;

public class EditTodaysGoalViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Edit Today's Goal";
    public static final String DUE_DATE_LABEL = "New Due Date";

    private EditTodaysGoalState state = new EditTodaysGoalState();

    public EditTodaysGoalViewModel() {
        super("edit today's goal");
    }

    public EditTodaysGoalState getState() {
        return state;
    }

    public void setState(EditTodaysGoalState state) {
        this.state = state;
    }
}