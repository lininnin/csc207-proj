package use_case.Angela.category.create;

import data_access.InMemoryCategoryDataAccessObject;
import entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreateCategoryInteractor following Clean Architecture principles.
 */
class CreateCategoryInteractorTest {

    private InMemoryCategoryDataAccessObject categoryDataAccess;
    private TestCreateCategoryPresenter testPresenter;
    private CreateCategoryInteractor interactor;

    @BeforeEach
    void setUp() {
        categoryDataAccess = new InMemoryCategoryDataAccessObject();
        testPresenter = new TestCreateCategoryPresenter();
        interactor = new CreateCategoryInteractor(categoryDataAccess, testPresenter, new entity.CommonCategoryFactory());
    }

    @Test
    void testSuccessfulCategoryCreation() {
        // Create input data for a new category
        CreateCategoryInputData inputData = new CreateCategoryInputData(
                "Work"
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Work", testPresenter.lastOutputData.getCategoryName());
        assertNull(testPresenter.lastError);

        // Verify category was saved
        List<Category> categories = categoryDataAccess.getAllCategories();
        assertEquals(1, categories.size());
        Category savedCategory = categories.get(0);
        assertEquals("Work", savedCategory.getName());
        assertNotNull(savedCategory.getColor());  // Has default color
    }

    @Test
    void testCreateMultipleCategories() {
        // Create first category
        CreateCategoryInputData inputData1 = new CreateCategoryInputData("Work");
        interactor.execute(inputData1);

        // Create second category
        CreateCategoryInputData inputData2 = new CreateCategoryInputData("Personal");
        interactor.execute(inputData2);

        // Create third category
        CreateCategoryInputData inputData3 = new CreateCategoryInputData("Urgent");
        interactor.execute(inputData3);

        // Verify all categories were created
        List<Category> categories = categoryDataAccess.getAllCategories();
        assertTrue(categories.size() >= 3);
    }

    @Test
    void testCreateCategoryWithDefaultColor() {
        // Create category (always uses default color)
        CreateCategoryInputData inputData = new CreateCategoryInputData(
                "Default Color"
        );

        // Execute the interactor
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("Default Color", testPresenter.lastOutputData.getCategoryName());
    }

    @Test
    void testFailureDuplicateCategoryName() {
        // Create first category
        CreateCategoryInputData inputData1 = new CreateCategoryInputData("Work");
        interactor.execute(inputData1);

        // Reset presenter
        testPresenter = new TestCreateCategoryPresenter();
        interactor = new CreateCategoryInteractor(categoryDataAccess, testPresenter, new entity.CommonCategoryFactory());

        // Try to create category with same name (case-insensitive)
        CreateCategoryInputData inputData2 = new CreateCategoryInputData("work");
        interactor.execute(inputData2);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The category name already exists", testPresenter.lastError);

        // Verify still has categories from previous tests
        assertTrue(categoryDataAccess.getAllCategories().size() >= 1);
    }

    @Test
    void testFailureEmptyCategoryName() {
        // Try to create category with empty name
        CreateCategoryInputData inputData = new CreateCategoryInputData("");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);

        // Verify no category was created
        assertTrue(categoryDataAccess.getAllCategories().isEmpty());
    }

    @Test
    void testFailureNullCategoryName() {
        // Try to create category with null name
        CreateCategoryInputData inputData = new CreateCategoryInputData(null);
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);

        // Verify no category was created
        assertTrue(categoryDataAccess.getAllCategories().isEmpty());
    }

    @Test
    void testFailureWhitespaceCategoryName() {
        // Try to create category with whitespace name
        CreateCategoryInputData inputData = new CreateCategoryInputData("   ");
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("Category name cannot be empty", testPresenter.lastError);

        // Verify no category was created
        assertTrue(categoryDataAccess.getAllCategories().isEmpty());
    }

    @Test
    void testCategoryNameTrimming() {
        // Create category with whitespace around name
        CreateCategoryInputData inputData = new CreateCategoryInputData("  Work  ");
        interactor.execute(inputData);

        // Verify success - name is NOT trimmed
        assertNotNull(testPresenter.lastOutputData);
        assertEquals("  Work  ", testPresenter.lastOutputData.getCategoryName());

        Category savedCategory = categoryDataAccess.getCategoryByName("  Work  ");
        assertNotNull(savedCategory);
        assertEquals("  Work  ", savedCategory.getName());
    }

    // Test removed - color not part of input data

    @Test
    void testCaseInsensitiveDuplicateCheck() {
        // Create category with mixed case
        CreateCategoryInputData inputData1 = new CreateCategoryInputData("WoRk");
        interactor.execute(inputData1);

        testPresenter = new TestCreateCategoryPresenter();
        interactor = new CreateCategoryInteractor(categoryDataAccess, testPresenter, new entity.CommonCategoryFactory());

        // Try different case variations
        CreateCategoryInputData inputData2 = new CreateCategoryInputData("WORK");
        interactor.execute(inputData2);
        assertNotNull(testPresenter.lastError);

        testPresenter = new TestCreateCategoryPresenter();
        interactor = new CreateCategoryInteractor(categoryDataAccess, testPresenter, new entity.CommonCategoryFactory());

        CreateCategoryInputData inputData3 = new CreateCategoryInputData("work");
        interactor.execute(inputData3);
        assertNotNull(testPresenter.lastError);

        // Should still only have one category
        int categoryCount = 0;
        for (Category cat : categoryDataAccess.getAllCategories()) {
            if (cat.getName().equalsIgnoreCase("work")) {
                categoryCount++;
            }
        }
        assertEquals(1, categoryCount);
    }
    
    @Test
    void testFailureCategoryNameExceeds20Characters() {
        // Try to create category with name > 20 characters
        String longName = "ThisIsAVeryLongCategoryNameThatExceeds20Characters";
        CreateCategoryInputData inputData = new CreateCategoryInputData(longName);
        interactor.execute(inputData);

        // Verify failure
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertEquals("The name of category cannot exceed 20 letters", testPresenter.lastError);

        // Verify no category was created
        assertTrue(categoryDataAccess.getAllCategories().isEmpty());
    }
    
    @Test
    void testFailureCategoryNameExactly20Characters() {
        // Create category with exactly 20 characters (should succeed)
        String exactName = "12345678901234567890"; // exactly 20 chars
        CreateCategoryInputData inputData = new CreateCategoryInputData(exactName);
        interactor.execute(inputData);

        // Verify success
        assertNotNull(testPresenter.lastOutputData);
        assertEquals(exactName, testPresenter.lastOutputData.getCategoryName());
        assertNull(testPresenter.lastError);
    }
    
    @Test
    void testExceptionHandlingDuringSave() {
        // Create a gateway that throws exception on save
        TestExceptionCategoryGateway exceptionGateway = new TestExceptionCategoryGateway();
        testPresenter = new TestCreateCategoryPresenter();
        interactor = new CreateCategoryInteractor(exceptionGateway, testPresenter, new entity.CommonCategoryFactory());
        
        CreateCategoryInputData inputData = new CreateCategoryInputData("TestCategory");
        interactor.execute(inputData);
        
        // Verify failure due to exception
        assertNull(testPresenter.lastOutputData);
        assertNotNull(testPresenter.lastError);
        assertTrue(testPresenter.lastError.contains("Failed to create category"));
    }

    /**
     * Test presenter implementation that captures output for verification.
     */
    private static class TestCreateCategoryPresenter implements CreateCategoryOutputBoundary {
        CreateCategoryOutputData lastOutputData;
        String lastError;

        @Override
        public void prepareSuccessView(CreateCategoryOutputData outputData) {
            this.lastOutputData = outputData;
            this.lastError = null;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastError = error;
            this.lastOutputData = null;
        }
    }
    
    /**
     * Test gateway that throws exception on save to test error handling.
     */
    private static class TestExceptionCategoryGateway implements CreateCategoryDataAccessInterface {
        @Override
        public void save(Category category) {
            throw new RuntimeException("Database connection failed");
        }
        
        @Override
        public boolean existsByName(String name) {
            return false;
        }
        
        @Override
        public List<Category> getAllCategories() {
            return new ArrayList<>();
        }
        
        @Override
        public int getCategoryCount() {
            return 0;
        }
    }
}