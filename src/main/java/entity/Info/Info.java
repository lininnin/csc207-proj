package entity;

import java.time.LocalDate;

/**
 * Base class for all trackable entities in the MindTrack system.
 * Contains common properties shared by Tasks, Events, and Goals.
 * Follows Single Responsibility Principle - only responsible for basic entity information.
 */
public class Info {
    private final String id;
    private final String name;
    private final String description;
    private final String category;
    private final LocalDate createdDate;

    /**
     * Constructs a new Info object.
     *
     * @param id Unique identifier (required)
     * @param name Name of the entity (required, max 20 chars)
     * @param description Optional description (max 100 chars)
     * @param category Category name (optional)
     * @param createdDate Date when the entity was created
     * @throws IllegalArgumentException if required fields are invalid
     */
    public Info(String id, String name, String description, String category, LocalDate createdDate) {
        validateId(id);
        validateName(name);
        validateDescription(description);

        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.createdDate = createdDate != null ? createdDate : LocalDate.now();
    }

    private void validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() > 20) {
            throw new IllegalArgumentException("Name cannot exceed 20 characters");
        }
    }

    private void validateDescription(String description) {
        if (description != null && description.length() > 100) {
            throw new IllegalArgumentException("Description cannot exceed 100 characters");
        }
    }

    // Getters
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return id.equals(info.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}