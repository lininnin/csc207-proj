package interface_adapter.Angela.category;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_utils.TestDataResetUtil;
import use_case.Angela.category.create.CreateCategoryOutputData;
import use_case.Angela.category.delete.DeleteCategoryOutputData;
import use_case.Angela.category.edit.EditCategoryOutputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test for CategoryManagementPresenter facade.
 * Tests basic instantiation and method calls.
 */
class CategoryManagementPresenterTest {

    private CategoryManagementPresenter presenter;
    private AvailableTasksViewModel mockAvailableTasksViewModel;
    private TodayTasksViewModel mockTodayTasksViewModel;
    private OverdueTasksController mockOverdueTasksController;
    private TodaySoFarController mockTodaySoFarController;
    private AvailableEventViewModel mockAvailableEventViewModel;
    private TodaysEventsViewModel mockTodaysEventsViewModel;

    @BeforeEach
    void setUp() {
        TestDataResetUtil.resetAllSharedData();
        
        // Use real ViewModel to avoid complex mocking issues
        CategoryManagementViewModel viewModel = new CategoryManagementViewModel();
        
        // Create mock ViewModels with proper state setup
        mockAvailableTasksViewModel = mock(AvailableTasksViewModel.class);
        mockTodayTasksViewModel = mock(TodayTasksViewModel.class);
        mockOverdueTasksController = mock(OverdueTasksController.class);
        mockTodaySoFarController = mock(TodaySoFarController.class);
        mockAvailableEventViewModel = mock(AvailableEventViewModel.class);
        mockTodaysEventsViewModel = mock(TodaysEventsViewModel.class);
        
        // Setup mock states to avoid NPE
        if (mockAvailableTasksViewModel != null) {
            when(mockAvailableTasksViewModel.getState()).thenReturn(mock(interface_adapter.Angela.task.available.AvailableTasksState.class));
        }
        if (mockTodayTasksViewModel != null) {
            when(mockTodayTasksViewModel.getState()).thenReturn(mock(interface_adapter.Angela.task.today.TodayTasksState.class));
        }
        
        presenter = new CategoryManagementPresenter(viewModel);
    }

    @AfterEach
    void tearDown() {
        // Clear all mocks to ensure no state leakage
        reset(mockAvailableTasksViewModel, mockTodayTasksViewModel, 
              mockOverdueTasksController, mockTodaySoFarController,
              mockAvailableEventViewModel, mockTodaysEventsViewModel);
        presenter = null;
    }

    @Test
    void testConstructor() {
        CategoryManagementViewModel viewModel = new CategoryManagementViewModel();
        CategoryManagementPresenter newPresenter = new CategoryManagementPresenter(viewModel);
        assertNotNull(newPresenter);
    }

    @Test
    void testSetDependencies() {
        // Test all setter methods - they should not throw exceptions
        assertDoesNotThrow(() -> presenter.setAvailableTasksViewModel(mockAvailableTasksViewModel));
        assertDoesNotThrow(() -> presenter.setTodayTasksViewModel(mockTodayTasksViewModel));
        assertDoesNotThrow(() -> presenter.setOverdueTasksController(mockOverdueTasksController));
        assertDoesNotThrow(() -> presenter.setTodaySoFarController(mockTodaySoFarController));
        assertDoesNotThrow(() -> presenter.setAvailableEventViewModel(mockAvailableEventViewModel));
        assertDoesNotThrow(() -> presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel));
    }

    @Test
    void testSetDependenciesWithNull() {
        // Test setter methods with null values - should not throw exceptions
        assertDoesNotThrow(() -> presenter.setAvailableTasksViewModel(null));
        assertDoesNotThrow(() -> presenter.setTodayTasksViewModel(null));
        assertDoesNotThrow(() -> presenter.setOverdueTasksController(null));
        assertDoesNotThrow(() -> presenter.setTodaySoFarController(null));
        assertDoesNotThrow(() -> presenter.setAvailableEventViewModel(null));
        assertDoesNotThrow(() -> presenter.setTodaysEventsViewModel(null));
    }

    @Test
    void testCreateCategoryPrepareSuccessView() {
        CreateCategoryOutputData outputData = new CreateCategoryOutputData("cat123", "Work", true);
        
        // This should delegate to CreateCategoryPresenter
        assertDoesNotThrow(() -> presenter.prepareSuccessView(outputData));
    }

    @Test
    void testCreateCategoryPrepareFailView() {
        String errorMessage = "Category already exists";
        
        // This should delegate to CreateCategoryPresenter
        assertDoesNotThrow(() -> presenter.prepareFailView(errorMessage));
    }

    @Test
    void testCreateCategoryPrepareFailViewWithNull() {
        String errorMessage = null;
        
        // This should delegate to CreateCategoryPresenter
        assertDoesNotThrow(() -> presenter.prepareFailView(errorMessage));
    }

    @Test
    void testDeleteCategoryPrepareSuccessView() {
        DeleteCategoryOutputData outputData = new DeleteCategoryOutputData("cat123", "Category 'Work' deleted successfully");
        
        // This should delegate to DeleteCategoryPresenter
        assertDoesNotThrow(() -> presenter.prepareSuccessView(outputData));
    }

    @Test
    void testEditCategoryPrepareSuccessView() {
        EditCategoryOutputData outputData = new EditCategoryOutputData("cat123", "OldName", "NewName");
        
        // This should delegate to EditCategoryPresenter
        assertDoesNotThrow(() -> presenter.prepareSuccessView(outputData));
    }

    @Test
    void testCompleteWorkflow() {
        // Set all dependencies first
        presenter.setAvailableTasksViewModel(mockAvailableTasksViewModel);
        presenter.setTodayTasksViewModel(mockTodayTasksViewModel);
        presenter.setOverdueTasksController(mockOverdueTasksController);
        presenter.setTodaySoFarController(mockTodaySoFarController);
        presenter.setAvailableEventViewModel(mockAvailableEventViewModel);
        presenter.setTodaysEventsViewModel(mockTodaysEventsViewModel);

        // Test create category success
        CreateCategoryOutputData createData = new CreateCategoryOutputData("cat1", "Work", true);
        assertDoesNotThrow(() -> presenter.prepareSuccessView(createData));

        // Test create category fail
        assertDoesNotThrow(() -> presenter.prepareFailView("Create failed"));

        // Test delete category
        DeleteCategoryOutputData deleteData = new DeleteCategoryOutputData("cat1", "Category 'Work' deleted successfully");
        assertDoesNotThrow(() -> presenter.prepareSuccessView(deleteData));

        // Test edit category
        EditCategoryOutputData editData = new EditCategoryOutputData("cat1", "Work", "Business");
        assertDoesNotThrow(() -> presenter.prepareSuccessView(editData));
    }

    @Test
    void testMultipleCategoryOperations() {
        // Test multiple operations in sequence
        CreateCategoryOutputData createData1 = new CreateCategoryOutputData("cat1", "Work", true);
        CreateCategoryOutputData createData2 = new CreateCategoryOutputData("cat2", "Personal", true);
        
        assertDoesNotThrow(() -> presenter.prepareSuccessView(createData1));
        assertDoesNotThrow(() -> presenter.prepareSuccessView(createData2));
        
        EditCategoryOutputData editData = new EditCategoryOutputData("cat1", "Work", "Business");
        assertDoesNotThrow(() -> presenter.prepareSuccessView(editData));
        
        DeleteCategoryOutputData deleteData = new DeleteCategoryOutputData("cat2", "Category 'Personal' deleted successfully");
        assertDoesNotThrow(() -> presenter.prepareSuccessView(deleteData));
    }

    @Test
    void testErrorHandling() {
        // Test various error scenarios
        assertDoesNotThrow(() -> presenter.prepareFailView(""));
        assertDoesNotThrow(() -> presenter.prepareFailView("Category name too long"));
        assertDoesNotThrow(() -> presenter.prepareFailView("Invalid characters in category name"));
        assertDoesNotThrow(() -> presenter.prepareFailView("Database connection failed"));
    }
}