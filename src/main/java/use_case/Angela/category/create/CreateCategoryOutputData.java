package use_case.Angela.category.create;

/**
 * Output data for create category use case.
 */
public class CreateCategoryOutputData {
    private final String categoryId;
    private final String categoryName;
    private final boolean closeDialog;

    public CreateCategoryOutputData(String categoryId, String categoryName, boolean closeDialog) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.closeDialog = closeDialog;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean shouldCloseDialog() {
        return closeDialog;
    }
}