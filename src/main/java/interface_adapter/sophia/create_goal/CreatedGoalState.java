package interface_adapter.sophia.create_goal;

/**
 * Represents the state of a created goal in the system.
 * Holds the message that will be displayed to the user after goal creation.
 */
public class CreatedGoalState {
    private String message = "";

    /**
     * Gets the current message state.
     * @return The current message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message state.
     * @param message The message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
