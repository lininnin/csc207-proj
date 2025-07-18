package entity;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents metadata information shared by various domain entities,
 * such as Task, Event, Goal, etc.
 * Includes fields like name, description, category, and created date.
 */
public class Info {
    private final String id;
    private final String name;
    private final String description;
    private final String category;
    private final LocalDate createdDate;

    /**
     * Constructs a new Info object with optional description and category.
     *
     * @param name        The name of the item (required)
     * @param description Optional description
     * @param category    Optional category (e.g., "work", "personal", "academic")
     * @throws IllegalArgumentException if name is null or empty
     */
    public Info(String name, String description, String category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name.trim();
        this.description = (description != null && !description.trim().isEmpty()) ? description.trim() : null;
        this.category = (category != null && !category.trim().isEmpty()) ? category.trim() : null;
        this.createdDate = LocalDate.now();
    }

    /** @return The unique ID of this Info */
    public String getId() {
        return id;
    }

    /** @return The name of this Info */
    public String getName() {
        return name;
    }

    /** @return The description, if available
     *
     * TODO: Will be used in task detail views and tooltips
     */
    public String getDescription() {
        return description;
    }

    /** @return The category, if available */
    public String getCategory() {
        return category;
    }

    /** @return The date this Info was created
     *
     * TODO: Will be used for audit logs and sorting tasks by creation date
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public String getInfo(){
        return "Name: " + name +
                (description != null ? "\nDescription: " + description : "") +
                (category != null ? "\nCategory: " + category : "") +
                "\nCreated on: " + createdDate;

    }
}