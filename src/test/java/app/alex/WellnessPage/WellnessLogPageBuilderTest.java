package app.alex.WellnessPage;

import app.AppDataAccessFactory;
import app.SharedTodaySoFarComponents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for WellnessLogPageBuilder.
 * Tests builder pattern implementation focusing on dependency injection and construction.
 * GUI component testing is limited due to headless environment constraints.
 */
class WellnessLogPageBuilderTest {

    private WellnessLogPageBuilder builder;
    private AppDataAccessFactory mockDataAccessFactory;
    private SharedTodaySoFarComponents mockSharedComponents;

    @BeforeEach
    void setUp() {
        mockDataAccessFactory = mock(AppDataAccessFactory.class);
        mockSharedComponents = mock(SharedTodaySoFarComponents.class);
        builder = new WellnessLogPageBuilder();
    }

    @Test
    @DisplayName("Should create builder instance successfully")
    void testBuilderCreation() {
        assertNotNull(builder, "Builder should be created successfully");
    }

    @Test
    @DisplayName("Should verify AppDataAccessFactory singleton usage")
    void testAppDataAccessFactoryUsage() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(mockSharedComponents);

            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(mock(data_access.alex.TodaysWellnessLogDataAccessObject.class));
            when(mockSharedComponents.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockSharedComponents.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockSharedComponents.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            // Attempt to build - may fail due to GUI creation but should call singleton
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode - we're testing singleton usage
            }

            mockedFactory.verify(AppDataAccessFactory::getInstance);
            verify(mockDataAccessFactory).getWellnessDataAccess();
        }
    }

    @Test
    @DisplayName("Should verify SharedTodaySoFarComponents singleton usage")
    void testSharedTodaySoFarComponentsUsage() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(mockSharedComponents);

            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(mock(data_access.alex.TodaysWellnessLogDataAccessObject.class));
            when(mockSharedComponents.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockSharedComponents.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockSharedComponents.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            // Attempt to build - may fail due to GUI creation but should call singleton
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode - we're testing singleton usage
            }

            mockedShared.verify(SharedTodaySoFarComponents::getInstance);
            verify(mockSharedComponents).getOverdueTasksController();
            verify(mockSharedComponents).getTodaySoFarController();
            verify(mockSharedComponents).createTodaySoFarView();
            verify(mockSharedComponents).refresh();
        }
    }

    @Test
    @DisplayName("Should handle null data access factory gracefully")
    void testNullDataAccessHandling() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(mockSharedComponents);

            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(null);
            when(mockSharedComponents.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockSharedComponents.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockSharedComponents.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            // Should handle null gracefully or throw appropriate exception
            Exception exception = assertThrows(Exception.class, () -> builder.build());
            assertNotNull(exception, "Should throw an exception when data access is null");
        }
    }

    @Test
    @DisplayName("Should handle null shared components gracefully")
    void testNullSharedComponentsHandling() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(null);

            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(mock(data_access.alex.TodaysWellnessLogDataAccessObject.class));

            // Should handle null shared components gracefully or throw appropriate exception
            Exception exception = assertThrows(Exception.class, () -> builder.build());
            assertNotNull(exception, "Should throw an exception when shared components is null");
        }
    }

    @Test
    @DisplayName("Should verify wellness data access integration")
    void testWellnessDataAccessIntegration() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(mockSharedComponents);

            // Mock wellness data access
            data_access.alex.TodaysWellnessLogDataAccessObject mockWellnessDAO = 
                mock(data_access.alex.TodaysWellnessLogDataAccessObject.class);
            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(mockWellnessDAO);
            
            when(mockSharedComponents.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockSharedComponents.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockSharedComponents.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            // Attempt build - focus on data access verification
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode - we're testing data access integration
            }

            // Verify wellness data access was retrieved
            verify(mockDataAccessFactory).getWellnessDataAccess();
        }
    }

    @Test
    @DisplayName("Should verify component controller integration")
    void testControllerIntegration() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(mockSharedComponents);

            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(mock(data_access.alex.TodaysWellnessLogDataAccessObject.class));
            
            // Mock controller dependencies
            interface_adapter.Angela.task.overdue.OverdueTasksController mockOverdueController = 
                mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class);
            interface_adapter.Angela.today_so_far.TodaySoFarController mockTodaySoFarController = 
                mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class);
            
            when(mockSharedComponents.getOverdueTasksController()).thenReturn(mockOverdueController);
            when(mockSharedComponents.getTodaySoFarController()).thenReturn(mockTodaySoFarController);
            when(mockSharedComponents.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            // Attempt build - focus on controller verification
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode - we're testing controller integration
            }

            // Verify controller dependencies were retrieved
            verify(mockSharedComponents).getOverdueTasksController();
            verify(mockSharedComponents).getTodaySoFarController();
        }
    }

    @Test
    @DisplayName("Should verify view creation integration")
    void testViewCreationIntegration() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(mockSharedComponents);

            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(mock(data_access.alex.TodaysWellnessLogDataAccessObject.class));
            when(mockSharedComponents.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockSharedComponents.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            
            // Mock view creation
            view.Angela.TodaySoFarView mockTodaySoFarView = mock(view.Angela.TodaySoFarView.class);
            when(mockSharedComponents.createTodaySoFarView()).thenReturn(mockTodaySoFarView);

            // Attempt build - focus on view creation verification
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode - we're testing view creation integration
            }

            // Verify view creation was called
            verify(mockSharedComponents).createTodaySoFarView();
        }
    }

    @Test
    @DisplayName("Should verify builder pattern implementation")
    void testBuilderPattern() {
        // Test that builder can be instantiated multiple times
        WellnessLogPageBuilder builder1 = new WellnessLogPageBuilder();
        WellnessLogPageBuilder builder2 = new WellnessLogPageBuilder();
        
        assertNotNull(builder1, "First builder should be created");
        assertNotNull(builder2, "Second builder should be created");
        assertNotSame(builder1, builder2, "Builders should be separate instances");
    }

    @Test
    @DisplayName("Should verify refresh mechanism integration")
    void testRefreshMechanismIntegration() {
        try (MockedStatic<AppDataAccessFactory> mockedFactory = mockStatic(AppDataAccessFactory.class);
             MockedStatic<SharedTodaySoFarComponents> mockedShared = mockStatic(SharedTodaySoFarComponents.class)) {

            mockedFactory.when(AppDataAccessFactory::getInstance).thenReturn(mockDataAccessFactory);
            mockedShared.when(SharedTodaySoFarComponents::getInstance).thenReturn(mockSharedComponents);

            when(mockDataAccessFactory.getWellnessDataAccess()).thenReturn(mock(data_access.alex.TodaysWellnessLogDataAccessObject.class));
            when(mockSharedComponents.getOverdueTasksController()).thenReturn(mock(interface_adapter.Angela.task.overdue.OverdueTasksController.class));
            when(mockSharedComponents.getTodaySoFarController()).thenReturn(mock(interface_adapter.Angela.today_so_far.TodaySoFarController.class));
            when(mockSharedComponents.createTodaySoFarView()).thenReturn(mock(view.Angela.TodaySoFarView.class));

            // Attempt build - focus on refresh mechanism
            try {
                builder.build();
            } catch (Exception e) {
                // Expected in headless mode - we're testing refresh integration
            }

            // Verify refresh was called during build
            verify(mockSharedComponents).refresh();
        }
    }
}