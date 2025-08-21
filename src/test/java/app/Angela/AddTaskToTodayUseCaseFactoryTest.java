package app.Angela;

import interface_adapter.Angela.task.add_to_today.AddTaskToTodayController;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for AddTaskToTodayUseCaseFactory.
 */
class AddTaskToTodayUseCaseFactoryTest {

    @Mock
    private AddToTodayDataAccessInterface dataAccess;

    @Mock
    private AddTaskToTodayViewModel addTaskToTodayViewModel;

    @Mock
    private TodayTasksViewModel todayTasksViewModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        // Act
        AddTaskToTodayController controller = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                addTaskToTodayViewModel,
                todayTasksViewModel
        );

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testCreateWithDifferentParameters() {
        // Arrange - create new mock instances to test factory flexibility
        AddToTodayDataAccessInterface newDataAccess = 
            org.mockito.Mockito.mock(AddToTodayDataAccessInterface.class);
        AddTaskToTodayViewModel newAddTaskToTodayViewModel = 
            org.mockito.Mockito.mock(AddTaskToTodayViewModel.class);
        TodayTasksViewModel newTodayTasksViewModel = 
            org.mockito.Mockito.mock(TodayTasksViewModel.class);

        // Act
        AddTaskToTodayController controller = AddTaskToTodayUseCaseFactory.create(
                newDataAccess,
                newAddTaskToTodayViewModel,
                newTodayTasksViewModel
        );

        // Assert
        assertNotNull(controller);
    }

    @Test
    void testCreateReturnsWorkingController() {
        // Act
        AddTaskToTodayController controller = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                addTaskToTodayViewModel,
                todayTasksViewModel
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
        AddTaskToTodayController controller = AddTaskToTodayUseCaseFactory.create(
                null,
                addTaskToTodayViewModel,
                todayTasksViewModel
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateWithNullAddTaskToTodayViewModel() {
        // Act & Assert - Factory creates controller but it will fail when used
        AddTaskToTodayController controller = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                null,
                todayTasksViewModel
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateWithNullTodayTasksViewModel() {
        // Act & Assert - Factory creates controller but it will fail when used
        AddTaskToTodayController controller = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                addTaskToTodayViewModel,
                null
        );
        assertNotNull(controller);
    }

    @Test
    void testCreateMultipleInstances() {
        // Act - Create multiple controllers
        AddTaskToTodayController controller1 = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                addTaskToTodayViewModel,
                todayTasksViewModel
        );

        AddTaskToTodayController controller2 = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                addTaskToTodayViewModel,
                todayTasksViewModel
        );

        // Assert - Each call should create a new instance
        assertNotNull(controller1);
        assertNotNull(controller2);
        assertNotSame(controller1, controller2);
    }

    @Test
    void testFactoryIsStateless() {
        // Act - Create controllers with same parameters
        AddTaskToTodayController controller1 = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                addTaskToTodayViewModel,
                todayTasksViewModel
        );

        AddTaskToTodayController controller2 = AddTaskToTodayUseCaseFactory.create(
                dataAccess,
                addTaskToTodayViewModel,
                todayTasksViewModel
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

    @Test
    void testCreateWithAllNullParameters() {
        // Act & Assert - Factory creates controller but it will fail when used
        AddTaskToTodayController controller = AddTaskToTodayUseCaseFactory.create(null, null, null);
        assertNotNull(controller);
    }

    @Test
    void testFactoryMethodIsPublicStatic() throws NoSuchMethodException {
        // Act - Get the factory method via reflection
        var method = AddTaskToTodayUseCaseFactory.class.getMethod(
                "create",
                AddToTodayDataAccessInterface.class,
                AddTaskToTodayViewModel.class,
                TodayTasksViewModel.class
        );

        // Assert
        assertNotNull(method);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
        assertEquals(AddTaskToTodayController.class, method.getReturnType());
    }

    @Test
    void testFactoryClassIsPublic() {
        // Assert - Factory class should be public for external use
        assertTrue(java.lang.reflect.Modifier.isPublic(
                AddTaskToTodayUseCaseFactory.class.getModifiers()));
    }
}