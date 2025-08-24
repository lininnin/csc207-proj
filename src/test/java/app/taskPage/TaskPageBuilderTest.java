package app.taskPage;

import app.AppDataAccessFactory;
import data_access.InMemoryTaskDataAccessObject;
import data_access.InMemoryCategoryDataAccessObject;
import interface_adapter.Angela.category.CategoryManagementViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for TaskPageBuilder.
 * Tests builder pattern implementation and dependency injection.
 * Note: GUI creation tests are adapted for headless testing environment.
 */
class TaskPageBuilderTest {

    private TaskPageBuilder builder;
    private AppDataAccessFactory mockDataAccessFactory;
    private InMemoryTaskDataAccessObject mockTaskGateway;
    private InMemoryCategoryDataAccessObject mockCategoryDataAccess;
    private CategoryManagementViewModel mockCategoryViewModel;

    @BeforeEach
    void setUp() {
        // Set headless mode to avoid GUI issues in testing
        System.setProperty("java.awt.headless", "true");

        mockDataAccessFactory = mock(AppDataAccessFactory.class);
        mockTaskGateway = mock(InMemoryTaskDataAccessObject.class);
        mockCategoryDataAccess = mock(InMemoryCategoryDataAccessObject.class);
        mockCategoryViewModel = mock(CategoryManagementViewModel.class);

        when(mockDataAccessFactory.getTaskGateway()).thenReturn(mockTaskGateway);
        when(mockDataAccessFactory.getCategoryDataAccess()).thenReturn(mockCategoryDataAccess);
        when(mockDataAccessFactory.getCategoryManagementViewModel()).thenReturn(mockCategoryViewModel);
        when(mockDataAccessFactory.getGoalRepository()).thenReturn(mock(data_access.files.FileGoalRepository.class));
    }

    @Test
    @DisplayName("Should create builder with injected data access factory")
    void testConstructorWithDataAccessFactory() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class)) {
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);

            // When
            builder = new TaskPageBuilder(mockDataAccessFactory);

            // Then
            assertNotNull(builder);
            mockedStatic.verify(AppDataAccessFactory::getInstance, times(1));
        }
    }

    @Test
    @DisplayName("Should create builder with default constructor using singleton")
    void testDefaultConstructor() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class)) {
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);

            // When
            builder = new TaskPageBuilder();

            // Then
            assertNotNull(builder);
            mockedStatic.verify(AppDataAccessFactory::getInstance, times(2)); // Called twice - once in each constructor
        }
    }

    @Test
    @DisplayName("Should access data access factory dependencies during construction")
    void testDataAccessFactoryDependencies() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class)) {
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);

            // When
            builder = new TaskPageBuilder();

            // Then - Verify dependencies were accessed during construction
            verify(mockDataAccessFactory).getTaskGateway();
            verify(mockDataAccessFactory).getCategoryDataAccess();
            verify(mockDataAccessFactory).getCategoryManagementViewModel();
        }
    }

    @Test
    @DisplayName("Should handle refresh views with null views gracefully")
    void testRefreshViewsWithNullViews() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class)) {
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);

            builder = new TaskPageBuilder();

            // When & Then - should not throw exception
            assertDoesNotThrow(() -> builder.refreshViews());
        }
    }

    @Test
    @DisplayName("Should handle build method dependency setup")
    void testBuildDependencySetup() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class);
             MockedStatic<app.SharedTodaySoFarComponents> sharedMock = mockStatic(app.SharedTodaySoFarComponents.class)) {
            
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            
            // Mock SharedTodaySoFarComponents
            app.SharedTodaySoFarComponents mockShared = mock(app.SharedTodaySoFarComponents.class);
            sharedMock.when(app.SharedTodaySoFarComponents::getInstance).thenReturn(mockShared);
            when(mockShared.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockShared.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockShared.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            builder = new TaskPageBuilder();

            // When & Then - Should access dependencies during build setup
            try {
                builder.build();
            } catch (Exception e) {
                // GUI creation may fail in headless mode, but dependency setup should work
            }

            // Verify that data access objects were accessed during build
            verify(mockDataAccessFactory, atLeastOnce()).getGoalRepository();
        }
    }

    @Test
    @DisplayName("Should initialize shared components correctly")
    void testInitializesSharedComponents() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class);
             MockedStatic<app.SharedTodaySoFarComponents> sharedMock = mockStatic(app.SharedTodaySoFarComponents.class)) {
            
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            
            // Mock SharedTodaySoFarComponents
            app.SharedTodaySoFarComponents mockShared = mock(app.SharedTodaySoFarComponents.class);
            sharedMock.when(app.SharedTodaySoFarComponents::getInstance).thenReturn(mockShared);
            when(mockShared.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockShared.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockShared.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            builder = new TaskPageBuilder();

            // When - This will trigger shared component initialization
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode, but shared components should still be accessed
            }

            // Then - Verify shared components were accessed
            verify(mockShared).getOverdueTasksController();
            verify(mockShared).getTodaySoFarController();
            verify(mockShared).createTodaySoFarView();
            verify(mockShared).refresh();
        }
    }

    @Test
    @DisplayName("Should create builder instances with proper initialization")
    void testBuilderInstantiation() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class)) {
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);

            // Test both constructor variants
            TaskPageBuilder builder1 = new TaskPageBuilder();
            TaskPageBuilder builder2 = new TaskPageBuilder(mockDataAccessFactory);

            // Both should be created successfully
            assertNotNull(builder1);
            assertNotNull(builder2);
            
            // Both should have accessed the factory
            verify(mockDataAccessFactory, atLeastOnce()).getTaskGateway();
            verify(mockDataAccessFactory, atLeastOnce()).getCategoryDataAccess();
            verify(mockDataAccessFactory, atLeastOnce()).getCategoryManagementViewModel();
        }
    }

    @Test
    @DisplayName("Should handle shared components singleton pattern")
    void testSharedComponentsSingleton() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class);
             MockedStatic<app.SharedTodaySoFarComponents> sharedMock = mockStatic(app.SharedTodaySoFarComponents.class)) {
            
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            
            app.SharedTodaySoFarComponents mockShared = mock(app.SharedTodaySoFarComponents.class);
            sharedMock.when(app.SharedTodaySoFarComponents::getInstance).thenReturn(mockShared);
            when(mockShared.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockShared.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockShared.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            builder = new TaskPageBuilder();

            // When - Attempt build operation
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode
            }

            // Then - Verify singleton access
            sharedMock.verify(app.SharedTodaySoFarComponents::getInstance, atLeastOnce());
        }
    }

    @Test
    @DisplayName("Should access all required mock dependencies during build")
    void testBuildAccessesAllDependencies() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class);
             MockedStatic<app.SharedTodaySoFarComponents> sharedMock = mockStatic(app.SharedTodaySoFarComponents.class)) {
            
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            
            app.SharedTodaySoFarComponents mockShared = mock(app.SharedTodaySoFarComponents.class);
            sharedMock.when(app.SharedTodaySoFarComponents::getInstance).thenReturn(mockShared);
            when(mockShared.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockShared.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockShared.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            builder = new TaskPageBuilder();

            // When - Try to build (may fail due to GUI but should access dependencies)
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode
            }

            // Then - Verify all key dependencies were accessed
            verify(mockDataAccessFactory, atLeastOnce()).getTaskGateway();
            verify(mockDataAccessFactory, atLeastOnce()).getCategoryDataAccess();
            verify(mockDataAccessFactory, atLeastOnce()).getCategoryManagementViewModel();
            verify(mockDataAccessFactory, atLeastOnce()).getGoalRepository();
        }
    }

    @Test
    @DisplayName("Should handle refresh views after partial build initialization")
    void testRefreshViewsAfterPartialBuild() {
        try (MockedStatic<AppDataAccessFactory> mockedStatic = mockStatic(AppDataAccessFactory.class);
             MockedStatic<app.SharedTodaySoFarComponents> sharedMock = mockStatic(app.SharedTodaySoFarComponents.class)) {
            
            mockedStatic.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            
            app.SharedTodaySoFarComponents mockShared = mock(app.SharedTodaySoFarComponents.class);
            sharedMock.when(app.SharedTodaySoFarComponents::getInstance).thenReturn(mockShared);
            when(mockShared.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockShared.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockShared.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            builder = new TaskPageBuilder();

            // Attempt build (may fail but should set up some components)
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode
            }

            // When & Then - should not throw exception
            assertDoesNotThrow(() -> builder.refreshViews());
        }
    }
}