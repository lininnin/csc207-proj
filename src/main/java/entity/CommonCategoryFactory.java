package entity;

/**
 * Factory for creating Category objects.
 */
public class CommonCategoryFactory implements CategoryFactory {

    @Override
    public Category create(String id, String name, String color) {
        return new Category(id, name, color);
    }

    @Override
    public Category create(String id, String name) {
        return new Category(id, name, null);
    }
}