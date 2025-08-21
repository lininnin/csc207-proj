package interface_adapter.Angela.today_so_far;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.today_so_far.TodaySoFarInputBoundary;

import static org.mockito.Mockito.*;

class TodaySoFarControllerTest {

    private TodaySoFarInputBoundary mockInputBoundary;
    private TodaySoFarController controller;

    @BeforeEach
    void setUp() {
        mockInputBoundary = mock(TodaySoFarInputBoundary.class);
        controller = new TodaySoFarController(mockInputBoundary);
    }

    @Test
    void testRefresh_callsInputBoundary() {
        controller.refresh();

        verify(mockInputBoundary).refreshTodaySoFar();
    }

    @Test
    void testRefresh_multipleCallsCallsInputBoundaryMultipleTimes() {
        controller.refresh();
        controller.refresh();
        controller.refresh();

        verify(mockInputBoundary, times(3)).refreshTodaySoFar();
    }

    @Test
    void testConstructor_storesInputBoundary() {
        // Verify that the constructor stores the input boundary correctly
        // by testing that refresh works after construction
        
        controller.refresh();

        verify(mockInputBoundary).refreshTodaySoFar();
    }
}