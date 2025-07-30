package interface_adapter.view_model;

import interface_adapter.ViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * View model for category management.
 * Holds the state for the category dialog and list.
 */
public class CategoryViewModel extends ViewModel<CategoryViewModel.CategoryState> {

    public static final String TITLE_LABEL = "Categories";
    public static final String CREATE_BUTTON_LABEL = "Create New";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";
    public static final String SAVE_BUTTON_LABEL = "Save";
    public static final String DELETE_BUTTON_LABEL = "Delete";
    public static final String EDIT_BUTTON_LABEL = "Edit";

    public CategoryViewModel() {
        super("category");
        setState(new CategoryState());
    }

    /**
     * State class for category view model.
     */
    public static class CategoryState {
        private final List<CategoryItem> categories = new ArrayList<>();
        private String newCategoryName = "";
        private String errorMessage = "";
        private String successMessage = "";
        private boolean isCreatingNew = false;
        private String editingCategoryId = null;

        public List<CategoryItem> getCategories() {
            return new ArrayList<>(categories);
        }

        public String getNewCategoryName() {
            return newCategoryName;
        }

        public void setNewCategoryName(String name) {
            this.newCategoryName = name;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getSuccessMessage() {
            return successMessage;
        }

        public void setSuccessMessage(String successMessage) {
            this.successMessage = successMessage;
        }

        public boolean isCreatingNew() {
            return isCreatingNew;
        }

        public void setCreatingNew(boolean creatingNew) {
            this.isCreatingNew = creatingNew;
        }

        public String getEditingCategoryId() {
            return editingCategoryId;
        }

        public void setEditingCategoryId(String editingCategoryId) {
            this.editingCategoryId = editingCategoryId;
        }
    }

    /**
     * Represents a category item in the list.
     */
    public static class CategoryItem {
        private final String id;
        private String name;
        private final String color;

        public CategoryItem(String id, String name, String color) {
            this.id = id;
            this.name = name;
            this.color = color != null ? color : "#808080";
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public String getColor() { return color; }
        public void setName(String name) { this.name = name; }
    }

    // Convenience methods that modify state

    public void addCategory(String id, String name) {
        CategoryState state = getState();
        state.categories.add(new CategoryItem(id, name, null));
        state.setNewCategoryName("");
        state.setCreatingNew(false);
        firePropertyChanged();
    }

    public void removeCategory(String id) {
        CategoryState state = getState();
        state.categories.removeIf(cat -> cat.getId().equals(id));
        firePropertyChanged();
    }

    public void updateCategoryName(String id, String newName) {
        CategoryState state = getState();
        state.categories.stream()
                .filter(cat -> cat.getId().equals(id))
                .findFirst()
                .ifPresent(cat -> cat.setName(newName));
        firePropertyChanged();
    }

    public void setNewCategoryName(String name) {
        getState().setNewCategoryName(name);
    }

    public void clearNewCategoryName() {
        getState().setNewCategoryName("");
    }

    public void setError(String error) {
        CategoryState state = getState();
        state.setErrorMessage(error);
        state.setSuccessMessage("");
        firePropertyChanged();
    }

    public void clearError() {
        getState().setErrorMessage("");
    }

    public void setSuccessMessage(String message) {
        CategoryState state = getState();
        state.setSuccessMessage(message);
        state.setErrorMessage("");
        firePropertyChanged();
    }

    public void setCreatingNew(boolean creatingNew) {
        CategoryState state = getState();
        state.setCreatingNew(creatingNew);
        if (creatingNew) {
            state.setNewCategoryName("");
            state.setErrorMessage("");
        }
    }

    public void setEditingCategoryId(String categoryId) {
        getState().setEditingCategoryId(categoryId);
    }

    public boolean isEditing(String categoryId) {
        return categoryId.equals(getState().getEditingCategoryId());
    }

    public void clearEditingState() {
        getState().setEditingCategoryId(null);
    }

    // Controller references
    private interface_adapter.controller.CreateCategoryController createController;
    private interface_adapter.controller.DeleteCategoryController deleteController;
    private interface_adapter.controller.EditCategoryController editController;

    public void setCreateController(interface_adapter.controller.CreateCategoryController controller) {
        this.createController = controller;
    }

    public void setDeleteController(interface_adapter.controller.DeleteCategoryController controller) {
        this.deleteController = controller;
    }

    public void setEditController(interface_adapter.controller.EditCategoryController controller) {
        this.editController = controller;
    }
}