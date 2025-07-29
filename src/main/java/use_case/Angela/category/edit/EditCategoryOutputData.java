package use_case.Angela.category.edit;

/**
 * Output data for the edit category use case.
 */
public class EditCategoryOutputData {
    private final String categoryId;
    private final String oldName;
    private final String newName;

    public EditCategoryOutputData(String categoryId, String oldName, String newName) {
        this.categoryId = categoryId;
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }
}