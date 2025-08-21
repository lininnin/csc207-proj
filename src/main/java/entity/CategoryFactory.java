package entity;

/**
 * Factory for creating categories.
 */
public interface CategoryFactory {
    /**
     * Creates a new Category.
     * @param id the unique identifier of the new category
     * @param name the name of the new category
     * @param color the color of the new category
     * @return the new category
     */
    Category create(String id, String name, String color);

    /**
     * Creates a new Category with default color.
     * @param id the unique identifier of the new category
     * @param name the name of the new category
     * @return the new category
     */
    Category create(String id, String name);
}