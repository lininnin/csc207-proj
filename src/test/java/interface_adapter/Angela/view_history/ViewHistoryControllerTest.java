package interface_adapter.Angela.view_history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.Angela.view_history.ViewHistoryInputBoundary;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

class ViewHistoryControllerTest {

    private ViewHistoryInputBoundary mockInteractor;
    private ViewHistoryController controller;

    @BeforeEach
    void setUp() {
        mockInteractor = mock(ViewHistoryInputBoundary.class);
        controller = new ViewHistoryController(mockInteractor);
    }

    @Test
    void testLoadHistory_validDate_callsInteractor() {
        LocalDate date = LocalDate.of(2023, 12, 15);

        controller.loadHistory(date);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testLoadHistory_todayDate_callsInteractor() {
        LocalDate date = LocalDate.now();

        controller.loadHistory(date);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testLoadHistory_pastDate_callsInteractor() {
        LocalDate date = LocalDate.now().minusDays(10);

        controller.loadHistory(date);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testLoadHistory_futureDate_callsInteractor() {
        LocalDate date = LocalDate.now().plusDays(5);

        controller.loadHistory(date);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testLoadHistory_nullDate_callsInteractor() {
        LocalDate date = null;

        controller.loadHistory(date);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate() == null
        ));
    }

    @Test
    void testLoadHistory_veryOldDate_callsInteractor() {
        LocalDate date = LocalDate.of(2000, 1, 1);

        controller.loadHistory(date);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testLoadHistory_leapYearDate_callsInteractor() {
        LocalDate date = LocalDate.of(2024, 2, 29); // Leap year

        controller.loadHistory(date);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testLoadAvailableDates_callsInteractor() {
        controller.loadAvailableDates();

        verify(mockInteractor).loadAvailableDates();
    }

    @Test
    void testLoadAvailableDates_multipleCalls_callsInteractorMultipleTimes() {
        controller.loadAvailableDates();
        controller.loadAvailableDates();
        controller.loadAvailableDates();

        verify(mockInteractor, times(3)).loadAvailableDates();
    }

    @Test
    void testExportHistory_validDate_callsInteractor() {
        LocalDate date = LocalDate.of(2023, 12, 15);

        controller.exportHistory(date);

        verify(mockInteractor).exportHistory(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testExportHistory_todayDate_callsInteractor() {
        LocalDate date = LocalDate.now();

        controller.exportHistory(date);

        verify(mockInteractor).exportHistory(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testExportHistory_pastDate_callsInteractor() {
        LocalDate date = LocalDate.now().minusMonths(2);

        controller.exportHistory(date);

        verify(mockInteractor).exportHistory(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testExportHistory_nullDate_callsInteractor() {
        LocalDate date = null;

        controller.exportHistory(date);

        verify(mockInteractor).exportHistory(argThat(inputData ->
                inputData.getDate() == null
        ));
    }

    @Test
    void testExportHistory_futureDate_callsInteractor() {
        LocalDate date = LocalDate.now().plusWeeks(3);

        controller.exportHistory(date);

        verify(mockInteractor).exportHistory(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }

    @Test
    void testMultipleOperations_allCallInteractor() {
        LocalDate date1 = LocalDate.of(2023, 12, 15);
        LocalDate date2 = LocalDate.of(2023, 12, 16);

        controller.loadHistory(date1);
        controller.loadAvailableDates();
        controller.exportHistory(date2);

        verify(mockInteractor).execute(argThat(inputData ->
                inputData.getDate().equals(date1)
        ));
        verify(mockInteractor).loadAvailableDates();
        verify(mockInteractor).exportHistory(argThat(inputData ->
                inputData.getDate().equals(date2)
        ));
    }

    @Test
    void testLoadHistory_sameDate_multipleCallsWithSameInputData() {
        LocalDate date = LocalDate.of(2023, 12, 15);

        controller.loadHistory(date);
        controller.loadHistory(date);

        verify(mockInteractor, times(2)).execute(argThat(inputData ->
                inputData.getDate().equals(date)
        ));
    }
}