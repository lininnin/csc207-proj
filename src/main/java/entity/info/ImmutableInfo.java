package entity.info;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable wrapper for Info entity.
 * Provides immutable alternative to mutable Info for Clean Architecture compliance.
 * 
 * Strategy: Adapter pattern to maintain backward compatibility with Event module.
 * This allows Task module to use immutable entities while Event module continues using mutable Info.
 */
public class ImmutableInfo implements InfoInterf {
    private final String id;
    private final String name;
    private final String description;
    private final String category;
    private final LocalDate createdDate;

    /**
     * Creates an immutable Info from existing Info instance.
     * Used to convert mutable Info to immutable version.
     *
     * @param info The Info instance to wrap
     * @throws IllegalArgumentException if info is null
     */
    public ImmutableInfo(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        
        this.id = info.getId();
        this.name = info.getName();
        this.description = info.getDescription();
        this.category = info.getCategory();
        this.createdDate = info.getCreatedDate();
    }

    /**
     * Creates an immutable Info with specified values.
     * Used for creating new immutable instances.
     *
     * @param id The unique identifier
     * @param name The name (required)
     * @param description The description (optional)
     * @param category The category (optional)
     * @param createdDate The creation date
     * @throws IllegalArgumentException if required fields are null/empty
     */
    public ImmutableInfo(String id, String name, String description, String category, LocalDate createdDate) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (createdDate == null) {
            throw new IllegalArgumentException("Created date cannot be null");
        }

        this.id = id.trim();
        this.name = name.trim();
        this.description = (description == null || description.trim().isEmpty()) ? "" : description.trim();
        this.category = (category == null || category.trim().isEmpty()) ? "" : category.trim();
        this.createdDate = createdDate;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setName(String name) {
        // No-op for immutable implementation - use withName() instead
    }

    @Override
    public void setDescription(String description) {
        // No-op for immutable implementation - use withDescription() instead
    }

    @Override
    public void setCategory(String category) {
        // No-op for immutable implementation - use withCategory() instead
    }

    // Immutable update methods (return new instances)

    /**
     * Creates a new ImmutableInfo with updated name.
     *
     * @param name The new name (required)
     * @return A new ImmutableInfo with updated name
     * @throws IllegalArgumentException if name is null or empty
     */
    public ImmutableInfo withName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return new ImmutableInfo(this.id, name.trim(), this.description, this.category, this.createdDate);
    }

    /**
     * Creates a new ImmutableInfo with updated description.
     *
     * @param description The new description (optional)
     * @return A new ImmutableInfo with updated description
     */
    public ImmutableInfo withDescription(String description) {
        String normalizedDescription = (description == null || description.trim().isEmpty()) ? "" : description.trim();
        return new ImmutableInfo(this.id, this.name, normalizedDescription, this.category, this.createdDate);
    }

    /**
     * Creates a new ImmutableInfo with updated category.
     *
     * @param category The new category (optional)
     * @return A new ImmutableInfo with updated category
     */
    public ImmutableInfo withCategory(String category) {
        String normalizedCategory = (category == null || category.trim().isEmpty()) ? "" : category.trim();
        return new ImmutableInfo(this.id, this.name, this.description, normalizedCategory, this.createdDate);
    }

    /**
     * Converts this immutable Info to a mutable Info instance.
     * Used for backward compatibility with Event module.
     *
     * @return A new mutable Info with the same data
     */
    public Info toMutableInfo() {
        Info mutableInfo = new Info.Builder(this.name)
                .description(this.description)
                .category(this.category)
                .build();
        
        // Note: The mutable Info will get a new ID and creation date
        // This is acceptable since the conversion is primarily for compatibility
        return mutableInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableInfo that = (ImmutableInfo) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(category, that.category) &&
                Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, category, createdDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ImmutableInfo{");
        sb.append("id='").append(id).append("'");
        sb.append(", name='").append(name).append("'");
        if (description != null && !description.isEmpty()) {
            sb.append(", description='").append(description).append("'");
        }
        if (category != null && !category.isEmpty()) {
            sb.append(", category='").append(category).append("'");
        }
        sb.append(", createdDate=").append(createdDate);
        sb.append("}");
        return sb.toString();
    }
}