package use_case.Angela.category.delete;

/**
 * Output data for the delete category use case.
 */
public class DeleteCategoryOutputData {
    private final String deletedCategoryId;
    private final String message;

    public DeleteCategoryOutputData(String deletedCategoryId, String message) {
        this.deletedCategoryId = deletedCategoryId;
        this.message = message;
    }

    public String getDeletedCategoryId() {
        return deletedCategoryId;
    }

    public String getMessage() {
        return message;
    }
}