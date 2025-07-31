package interface_adapter.Sophia.create_goal;

import view.ViewModel;

public class CreatedGoalViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Create Goal";
    public static final String NAME_LABEL = "Name";
    public static final String DESCRIPTION_LABEL = "Description";
    // ... other constant labels

    private CreatedGoalState state = new CreatedGoalState();

    public CreatedGoalViewModel() {
        super("create goal");
    }

    public CreatedGoalState getState() {
        return state;
    }

    public void setState(CreatedGoalState state) {
        this.state = state;
    }
}