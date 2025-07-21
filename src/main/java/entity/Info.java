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
    private String name;
    private String description;
    private String category;
    private final LocalDate createdDate;

    /**
     * Private constructor used by the Builder.
     * Fields are validated and trimmed inside Builder.
     *
     * @param builder Builder instance containing the fields.
     */
    private Info(Builder builder) {
        this.id = UUID.randomUUID().toString();
        this.name = builder.name;
        this.description = builder.description;
        this.category = builder.category;
        this.createdDate = LocalDate.now();
    }

    public static class Builder {
        private final String name;
        private String description;
        private String category;

        /**
         * Builder constructor with required field.
         *
         * @param name The name of the item (required)
         * @throws IllegalArgumentException if name is null or empty
         */
        public Builder(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            this.name = name.trim();
        }

        /**
         * @param description Optional description
         * @return the Builder itself
         */
        public Builder description(String description) {
            if (description != null && !description.trim().isEmpty()) {
                this.description = description.trim();
            }
            return this;
        }

        /**
         * @param category Optional category (e.g., "work", "personal", "academic")
         * @return the Builder itself
         */
        public Builder category(String category) {
            if (category != null && !category.trim().isEmpty()) {
                this.category = category.trim();
            }
            return this;
        }

        /**
         * Builds and returns a new Info object.
         */
        public Info build() {
            return new Info(this);
        }
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

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        this.category = category.trim();
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        this.description = description.trim();
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
    }
}
