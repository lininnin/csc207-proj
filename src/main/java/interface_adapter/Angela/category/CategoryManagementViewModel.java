package interface_adapter.Angela.category;

import interface_adapter.ViewModel;

/**
 * View model for category management popup.
 */
public class CategoryManagementViewModel extends ViewModel<CategoryManagementState> {
    public static final String CATEGORY_STATE_PROPERTY = "categoryState";

    public CategoryManagementViewModel() {
        super("category management");
        setState(new CategoryManagementState());
    }
}