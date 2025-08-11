package use_case.Angela.category.delete;

/**
 * Output data for delete category use case.
 */
public class DeleteCategoryOutputData {
    private final String categoryId;
    private final String message;

    public DeleteCategoryOutputData(String categoryId, String message) {
        this.categoryId = categoryId;
        this.message = message;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getMessage() {
        return message;
    }
}