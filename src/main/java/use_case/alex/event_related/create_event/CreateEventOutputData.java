package use_case.alex.event_related.create_event;

/**
 * Output data class for the Create Event use case.
 * Contains basic information used by the presenter to update the view.
 */
public class CreateEventOutputData {

    private final String name;
    private final String description;
    private final String category;
    private final boolean useCaseFailed;

    /**
     * Constructor for CreateEventOutputData.
     *
     * @param name           The name of the created event
     * @param description    The description of the created event
     * @param category       The category of the created event
     * @param useCaseFailed  Whether the use case failed
     */
    public CreateEventOutputData(String name, String description, String category, boolean useCaseFailed) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null or empty");
        }
        this.name = name.trim();
        this.description = (description == null) ? "" : description.trim();
        this.category = (category == null) ? "" : category.trim();
        this.useCaseFailed = useCaseFailed;
    }

    /**
     * @return The name of the created event.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of the created event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The category of the created event.
     */
    public String getCategory() {
        return category;
    }

    /**
     * @return True if the use case failed, false otherwise.
     */
    public boolean getUseCaseFailed() {
        return useCaseFailed;
    }

    /**
     * Convenience method for presenter to check failure.
     */
    public boolean isFailed() {
        return useCaseFailed;
    }
}
