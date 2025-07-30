package use_case.Angela.category.edit;

/**
 * Input data for editing a category.
 */
public class EditCategoryInputData {
    private final String categoryId;
    private final String newCategoryName;

    public EditCategoryInputData(String categoryId, String newCategoryName) {
        this.categoryId = categoryId;
        this.newCategoryName = newCategoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getNewCategoryName() {
        return newCategoryName;
    }
}