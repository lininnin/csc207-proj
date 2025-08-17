package interface_adapter.sophia.create_goal;

import view.ViewModel;

/**
 * ViewModel for the Create Goal view.
 * Manages the state and UI labels for goal creation.
 */
public class CreatedGoalViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Create Goal";
    public static final String NAME_LABEL = "Name";
    public static final String DESCRIPTION_LABEL = "Description";
    public static final String CREATE_BUTTON_LABEL = "Create Goal";
    private CreatedGoalState state = new CreatedGoalState();

    /**
     * Constructs the ViewModel with the specified view name.
     */
    public CreatedGoalViewModel() {
        super("create goal");
    }

    /**
     * Gets the current state.
     * @return The current CreatedGoalState
     */
    public CreatedGoalState getState() {
        return state;
    }

    /**
     * Sets the current state.
     * @param state The state to set
     */
    public void setState(CreatedGoalState state) {
        this.state = state;
    }
}
