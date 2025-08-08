package use_case.alex.event_related.create_event;

import java.time.LocalDate;

/**
 * Input data class for the Create Event use case.
 */
public class CreateEventInputData {

    private final String id;
    private final String name;
    private final String description;
    private final String category;
    private final LocalDate createdDate;

    /**
     * Constructor for CreateEventInputData.
     *
     * @param id           Unique identifier for the event (required)
     * @param name         Name of the event
     * @param description  Description of the event
     * @param category     Category of the event
     * @param createdDate  Date the event was created (required)
     */
    public CreateEventInputData(String id, String name, String description, String category, LocalDate createdDate) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        if (createdDate == null) {
            throw new IllegalArgumentException("createdDate cannot be null");
        }
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.createdDate = createdDate;
    }

    // ---------- Getters ----------

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    // ---------- Optional Setters (if mutability needed) ----------

}

