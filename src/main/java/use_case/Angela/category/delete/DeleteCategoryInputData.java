package use_case.Angela.category.delete;

/**
 * Input data for deleting a category.
 */
public class DeleteCategoryInputData {
    private final String categoryId;

    public DeleteCategoryInputData(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }
}