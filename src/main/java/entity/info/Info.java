package entity.info;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents metadata information shared by various domain entities,
 * such as Task, Event, Goal, etc.
 * Includes fields like name, description, category, and created date.
 */
public class Info implements InfoInterf {
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
         * @return new Info instance
         */
        public Info build() {
            return new Info(this);
        }
    }

    // Getters

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
    /** @return The date this Info was created */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    // Setters (mutable for compatibility)

    /**
     * Sets the name of this Info.
     * @deprecated Use {@link #withName(String)} for immutable updates
     * @param name The new name (required)
     * @throws IllegalArgumentException if name is null or empty
     */
    @Deprecated
    public void setName(String name) {
        // Name remains required, cannot be empty
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name.trim();
    }

    /**
     * Sets the description of this Info.
     * @deprecated Use {@link #withDescription(String)} for immutable updates
     * @param description The new description (optional)
     */
    @Deprecated
    public void setDescription(String description) {
        // Allow description to be null or empty, normalize to empty string
        this.description = (description == null || description.trim().isEmpty()) ? "" : description.trim();
    }

    /**
     * Sets the category of this Info.
     * @deprecated Use {@link #withCategory(String)} for immutable updates
     * @param category The new category (optional)
     */
    @Deprecated
    public void setCategory(String category) {
        // Allow category to be null or empty, normalize to empty string
        this.category = (category == null || category.trim().isEmpty()) ? "" : category.trim();
    }

    // Immutable update methods for Clean Architecture compliance
    
    /**
     * Creates a new Info instance with the specified name.
     * This follows the immutable entity pattern.
     * 
     * @param name The new name (required)
     * @return A new Info instance with the updated name
     * @throws IllegalArgumentException if name is null or empty
     */
    public Info withName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        
        Info newInfo = new Info.Builder(name.trim())
                .description(this.description)
                .category(this.category)
                .build();
        return newInfo;
    }
    
    /**
     * Creates a new Info instance with the specified description.
     * This follows the immutable entity pattern.
     * 
     * @param description The new description (optional)
     * @return A new Info instance with the updated description
     */
    public Info withDescription(String description) {
        String normalizedDescription = (description == null || description.trim().isEmpty()) ? "" : description.trim();
        
        Info newInfo = new Info.Builder(this.name)
                .description(normalizedDescription)
                .category(this.category)
                .build();
        return newInfo;
    }
    
    /**
     * Creates a new Info instance with the specified category.
     * This follows the immutable entity pattern.
     * 
     * @param category The new category (optional)
     * @return A new Info instance with the updated category
     */
    public Info withCategory(String category) {
        String normalizedCategory = (category == null || category.trim().isEmpty()) ? "" : category.trim();
        
        Info newInfo = new Info.Builder(this.name)
                .description(this.description)
                .category(normalizedCategory)
                .build();
        return newInfo;
    }
}