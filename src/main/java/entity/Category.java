package entity;

/**
 * Represents a category that can be assigned to tasks, events, and goals.
 * Categories help organize and filter items in the application.
 */
public class Category {
    private final String id;
    private String name;
    private final String color; // Optional color for UI display

    /**
     * Constructs a new Category with the given name.
     *
     * @param id Unique identifier for the category
     * @param name Name of the category (max 20 chars)
     * @param color Optional color code for UI display
     * @throws IllegalArgumentException if name is null, empty, or exceeds 20 characters
     */
    public Category(String id, String name, String color) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (name.length() > 20) {
            throw new IllegalArgumentException("Category name cannot exceed 20 characters");
        }

        this.id = id;
        this.name = name.trim();
        this.color = color != null ? color : "#808080"; // Default gray
    }

    /**
     * Gets the unique identifier of this category.
     *
     * @return The category ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of this category.
     *
     * @return The category name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for this category.
     *
     * @param name The new name (max 20 chars)
     * @throws IllegalArgumentException if name is invalid
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        if (name.length() > 20) {
            throw new IllegalArgumentException("Category name cannot exceed 20 characters");
        }
        this.name = name.trim();
    }

    /**
     * Gets the color code for this category.
     *
     * @return The color code (hex format)
     */
    public String getColor() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Category category = (Category) obj;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}