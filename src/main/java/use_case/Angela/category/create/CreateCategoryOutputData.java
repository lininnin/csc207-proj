package use_case.Angela.category.create;

/**
 * Output data for the create category use case.
 */
public class CreateCategoryOutputData {
    private final String categoryId;
    private final String categoryName;
    private final boolean useCaseFailed;

    public CreateCategoryOutputData(String categoryId, String categoryName, boolean useCaseFailed) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.useCaseFailed = useCaseFailed;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}