package use_case.Category_related.create;

/**
 * Input data for creating a category.
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