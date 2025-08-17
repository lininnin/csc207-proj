package interface_adapter.sophia.delete_goal;

import view.ViewModel;

/**
 * ViewModel for the Delete Goal feature.
 * Manages the state and UI labels for goal deletion.
 */
public class DeletedGoalViewModel extends ViewModel {
    public static final String TITLE_LABEL = "Delete Goal";
    public static final String CONFIRM_MESSAGE = "Confirm Deletion";
    private DeletedGoalState state = new DeletedGoalState();

    /**
     * Constructs the ViewModel with the specified view name.
     */
    public DeletedGoalViewModel() {
        super("delete goal");
    }

    /**
     * Gets the current state.
     * @return The current DeletedGoalState
     */
    public DeletedGoalState getState() {
        return state;
    }

    /**
     * Sets the current state.
     * @param state The state to set
     */
    public void setState(DeletedGoalState state) {
        this.state = state;
    }
}