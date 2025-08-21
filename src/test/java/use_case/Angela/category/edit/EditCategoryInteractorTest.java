package use_case.Angela.category.edit;

import data_access.InMemoryCategoryDataAccessObject;
import data_access.InMemoryTaskGateway;
import entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for EditCategoryInteractor following Clean Architecture principles.
 */
class EditCategoryInteractorTest {

    private InMemoryCategoryDataAccessObject categoryDataAccess;
    private InMemoryTaskGateway taskGateway;
    private TestEditCategoryPresenter testPresenter;
    private EditCategoryInteractor interactor;

    @BeforeEach
    void setUp() {
        categoryDataAccess = new InMemoryCategoryDataAccessObject();
        taskGateway = new InMemoryTaskGateway();
        testPresenter = new TestEditCategoryPresenter();
        interactor = new EditCategoryInteractor(categoryDataAccess, taskGateway, testPresenter, new entity.CommonCategoryFactory());
    }

    @Test
    void testSuccessfulEdit() {
        // Create a category to edit
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.save(category);

        // Edit the category
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "Updated Work"
        );

        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(categoryId, testPresenter.lastOutputData.getCategoryId());
        assertEquals("Updated Work", testPresenter.lastOutputData.getNewName());
        assertNull(testPresenter.lastError);

        // Verify category was updated
        Category updatedCategory = categoryDataAccess.getCategoryById(categoryId);
        assertEquals("Updated Work", updatedCategory.getName());
    }

    @Test
    void testEditNameOnly() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Original", "#0000FF");
        categoryDataAccess.save(category);

        // Edit only the name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "New Name"
        );

        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("New Name", testPresenter.lastOutputData.getNewName());
    }

    @Test
    void testFailureCategoryNotFound() {
        // Try to edit non-existent category
        EditCategoryInputData inputData = new EditCategoryInputData(
                "non-existent-id",
                "Updated Name"
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category not found", testPresenter.lastError);
    }

    @Test
    void testFailureEmptyCategoryName() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.save(category);

        // Try to edit with empty name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                ""
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);

        // Verify category was not changed
        Category unchangedCategory = categoryDataAccess.getCategoryById(categoryId);
        assertEquals("Work", unchangedCategory.getName());
    }

    @Test
    void testFailureNullCategoryName() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.save(category);

        // Try to edit with null name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                null
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);
    }

    @Test
    void testFailureDuplicateName() {
        // Create two categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        
        categoryDataAccess.save(new Category(categoryId1, "Work", "#0000FF"));
        categoryDataAccess.save(new Category(categoryId2, "Personal", "#00FF00"));

        // Try to rename Personal to Work (duplicate)
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId2,
                "Work"  // Duplicate name
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The category name already exists", testPresenter.lastError);

        // Verify category was not changed
        Category unchangedCategory = categoryDataAccess.getCategoryById(categoryId2);
        assertEquals("Personal", unchangedCategory.getName());
    }

    @Test
    void testFailureNullCategoryId() {
        // Try to edit with null ID
        EditCategoryInputData inputData = new EditCategoryInputData(
                null,
                "Updated Name"
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category ID is required", testPresenter.lastError);
    }

    @Test
    void testFailureEmptyCategoryId() {
        // Try to edit with empty ID
        EditCategoryInputData inputData = new EditCategoryInputData(
                "",
                "Updated Name"
        );

        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category ID is required", testPresenter.lastError);
    }

    @Test
    void testEditToSameName() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Work", "#0000FF");
        categoryDataAccess.save(category);

        // Edit to same name (should succeed)
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "Work"  // Same name
        );

        interactor.execute(inputData);

        // Verify success (editing to same name is allowed)
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Work", testPresenter.lastOutputData.getNewName());
    }

    @Test
    void testCaseInsensitiveDuplicateCheck() {
        // Create two categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        
        categoryDataAccess.save(new Category(categoryId1, "Work", "#0000FF"));
        categoryDataAccess.save(new Category(categoryId2, "Personal", "#00FF00"));

        // Try to rename Personal to WORK (different case)
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId2,
                "WORK"  // Different case of existing name
        );

        interactor.execute(inputData);

        // Verify failure (case-insensitive duplicate check)
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The category name already exists", testPresenter.lastError);
    }

    @Test
    void testNameTrimming() {
        // Create a category
        String categoryId = UUID.randomUUID().toString();
        Category category = new Category(categoryId, "Original", "#0000FF");
        categoryDataAccess.save(category);

        // Edit with whitespace around name
        EditCategoryInputData inputData = new EditCategoryInputData(
                categoryId,
                "  Updated Name  "
        );

        interactor.execute(inputData);

        // Verify success - name is NOT trimmed
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("  Updated Name  ", testPresenter.lastOutputData.getNewName());

        Category updatedCategory = categoryDataAccess.getCategoryById(categoryId);
        assertEquals("  Updated Name  ", updatedCategory.getName());
    }

    @Test
    void testEditMultipleCategories() {
        // Create multiple categories
        String categoryId1 = UUID.randomUUID().toString();
        String categoryId2 = UUID.randomUUID().toString();
        String categoryId3 = UUID.randomUUID().toString();
        
        categoryDataAccess.save(new Category(categoryId1, "Work", "#0000FF"));
        categoryDataAccess.save(new Category(categoryId2, "Personal", "#00FF00"));
        categoryDataAccess.save(new Category(categoryId3, "Urgent", "#FF0000"));

        // Edit first category
        EditCategoryInputData inputData1 = new EditCategoryInputData(
                categoryId1,
                "Business"
        );
        interactor.execute(inputData1);

        // Edit second category
        testPresenter = new TestEditCategoryPresenter();
        interactor = new EditCategoryInteractor(categoryDataAccess, taskGateway, testPresenter, new entity.CommonCategoryFactory());
        
        EditCategoryInputData inputData2 = new EditCategoryInputData(
                categoryId2,
                "Home"
        );
        interactor.execute(inputData2);

        // Verify both edits succeeded
        Category cat1 = categoryDataAccess.getCategoryById(categoryId1);
        Category cat2 = categoryDataAccess.getCategoryById(categoryId2);
        Category cat3 = categoryDataAccess.getCategoryById(categoryId3);

        assertEquals("Business", cat1.getName());
        assertEquals("Home", cat2.getName());
        assertEquals("Urgent", cat3.getName());  // Unchanged
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestEditCategoryPresenter implements EditCategoryOutputBoundary {
        EditCategoryOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(EditCategoryOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }
    }
}