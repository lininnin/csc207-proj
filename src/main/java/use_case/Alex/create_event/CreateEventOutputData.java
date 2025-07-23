package use_case.Alex.create_event;

/**
 * Output data class for the Create Event use case.
 * Contains basic information used by the presenter to update the view.
 */
public class CreateEventOutputData {

    private final String name;
    private final boolean useCaseFailed;

    /**
     * Constructor for CreateEventOutputData.
     *
     * @param name           The name of the created event (for display)
     * @param useCaseFailed  Whether the use case failed (used to determine view logic)
     */
    public CreateEventOutputData(String name, boolean useCaseFailed) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null or empty");
        }
        this.name = name.trim();
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * @return The name of the created event.
     */
    public String getName() {
        return name;
    }

    /**
     * @return True if the use case failed, false otherwise.
     */
    public boolean getUseCaseFailed() {
        return useCaseFailed;
    }
}

