package use_case.Angela.category.create;

/**
 * Input data for creating a new category.
 */
public class CreateCategoryInputData {
    private final String categoryName;

    public CreateCategoryInputData(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}