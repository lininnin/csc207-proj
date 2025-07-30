package interface_adapter.view_model;

import interface_adapter.ViewModel;
import java.util.ArrayList;
import java.util.List;

/**
 * View model for category management.
 * Holds the state for the category dialog and list.
 */
public class CategoryViewModel extends ViewModel {

    public static final String TITLE_LABEL = "Categories";
    public static final String CREATE_BUTTON_LABEL = "Create New";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";
    public static final String SAVE_BUTTON_LABEL = "Save";
    public static final String DELETE_BUTTON_LABEL = "Delete";
    public static final String EDIT_BUTTON_LABEL = "Edit";

    private final List<CategoryItem> categories;
    private String newCategoryName = "";
    private String errorMessage = "";
    private String successMessage = "";
    private boolean isCreatingNew = false;
    private String editingCategoryId = null;

    public CategoryViewModel() {
        super("category");
        this.categories = new ArrayList<>();
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

    // State management methods

    public void addCategory(String id, String name) {
        categories.add(new CategoryItem(id, name, null));
        clearNewCategoryName();
        setCreatingNew(false);
    }

    public void removeCategory(String id) {
        categories.removeIf(cat -> cat.getId().equals(id));
    }

    public void updateCategoryName(String id, String newName) {
        categories.stream()
                .filter(cat -> cat.getId().equals(id))
                .findFirst()
                .ifPresent(cat -> cat.setName(newName));
    }

    // Getters and setters

    public List<CategoryItem> getCategories() {
        return new ArrayList<>(categories);
    }

    public String getNewCategoryName() {
        return newCategoryName;
    }

    public void setNewCategoryName(String name) {
        this.newCategoryName = name;
    }

    public void clearNewCategoryName() {
        this.newCategoryName = "";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setError(String error) {
        this.errorMessage = error;
        this.successMessage = "";
    }

    public void clearError() {
        this.errorMessage = "";
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String message) {
        this.successMessage = message;
        this.errorMessage = "";
    }

    public boolean isCreatingNew() {
        return isCreatingNew;
    }

    public void setCreatingNew(boolean creatingNew) {
        this.isCreatingNew = creatingNew;
        if (creatingNew) {
            clearNewCategoryName();
            clearError();
        }
    }

    public String getEditingCategoryId() {
        return editingCategoryId;
    }

    public void setEditingCategoryId(String categoryId) {
        this.editingCategoryId = categoryId;
    }

    public boolean isEditing(String categoryId) {
        return categoryId.equals(editingCategoryId);
    }

    public void clearEditingState() {
        this.editingCategoryId = null;
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