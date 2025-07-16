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
     * @param name        The name of the item
     * @param description Optional description
     * @param category    Optional category (e.g., "work", "personal")
     */
    public Info(String name, String description, String category) {
        this.id = UUID.randomUUID().toString();
        this.name = name != null && !name.isEmpty() ? name : null;
        this.description = description != null && !description.isEmpty() ? description : null;
        this.category = category != null && !category.isEmpty() ? category : null;
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

    /** @return The description, if available */
    public String getDescription() {
        return description;
    }

    /** @return The category, if available */
    public String getCategory() {
        return category;
    }

    /** @return The date this Info was created */
    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
