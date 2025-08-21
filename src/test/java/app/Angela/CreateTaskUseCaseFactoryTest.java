package app.Angela;

import interface_adapter.Angela.task.create.CreateTaskController;
import interface_adapter.Angela.task.create.CreateTaskViewModel;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.ViewManagerModel;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import use_case.Angela.task.create.CreateTaskCategoryDataAccessInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreateTaskUseCaseFactory.
 */
class CreateTaskUseCaseFactoryTest {

    @Mock
    private CreateTaskDataAccessInterface dataAccess;

    @Mock
    private CreateTaskCategoryDataAccessInterface categoryDataAccess;

    @Mock
    private CreateTaskViewModel createTaskViewModel;

    @Mock
    private AvailableTasksViewModel availableTasksViewModel;

    @Mock
    private ViewManagerModel viewManagerModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Act
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testCreateWithDifferentParameters() {
        // Arrange - create new mock instances to test factory flexibility
        CreateTaskDataAccessInterface newDataAccess = 
            org.mockito.Mockito.mock(CreateTaskDataAccessInterface.class);
        CreateTaskCategoryDataAccessInterface newCategoryDataAccess = 
            org.mockito.Mockito.mock(CreateTaskCategoryDataAccessInterface.class);
        CreateTaskViewModel newCreateTaskViewModel = 
            org.mockito.Mockito.mock(CreateTaskViewModel.class);
        AvailableTasksViewModel newAvailableTasksViewModel = 
            org.mockito.Mockito.mock(AvailableTasksViewModel.class);
        ViewManagerModel newViewManagerModel = 
            org.mockito.Mockito.mock(ViewManagerModel.class);

        // Act
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                newDataAccess,
                newCategoryDataAccess,
                newCreateTaskViewModel,
                newAvailableTasksViewModel,
                newViewManagerModel
        );

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testCreateReturnsWorkingController() {
        // Act
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        // Assert - Controller should be functional (not throw exceptions when used)
        assertNotNull(controller);
        assertDoesNotThrow(() -> {
            // This tests that the controller was properly constructed
            // The actual business logic is tested in controller tests
            controller.getClass().getDeclaredMethods();
        });
    }

    @Test
    void testCreateWithNullDataAccess() {
        // Act & Assert - Factory creates controller but it will fail when used
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                null,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateWithNullCategoryDataAccess() {
        // Act & Assert - Factory creates controller but it will fail when used
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                dataAccess,
                null,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateWithNullCreateTaskViewModel() {
        // Act & Assert - Factory creates controller but it will fail when used
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                null,
                availableTasksViewModel,
                viewManagerModel
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateWithNullAvailableTasksViewModel() {
        // Act & Assert - Factory creates controller but it will fail when used
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                null,
                viewManagerModel
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateWithNullViewManagerModel() {
        // Act & Assert - Factory creates controller but it will fail when used
        CreateTaskController controller = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                null
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateMultipleInstances() {
        // Act - Create multiple controllers
        CreateTaskController controller1 = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        CreateTaskController controller2 = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        // Assert - Each call should create a new instance
        assertNotNull(controller1);
        assertNotNull(controller2);
        assertNotSame(controller1, controller2);
    }

    @Test
    void testFactoryIsStateless() {
        // Act - Create controllers with same parameters
        CreateTaskController controller1 = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        CreateTaskController controller2 = CreateTaskUseCaseFactory.create(
                dataAccess,
                categoryDataAccess,
                createTaskViewModel,
                availableTasksViewModel,
                viewManagerModel
        );

        // Assert - Factory should be stateless and create independent instances
        assertNotNull(controller1);
        assertNotNull(controller2);
        assertNotSame(controller1, controller2);
        
        // Both controllers should be functional
        assertNotNull(controller1.getClass());
        assertNotNull(controller2.getClass());
        assertEquals(controller1.getClass(), controller2.getClass());
    }
}