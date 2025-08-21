package interface_adapter.Angela.view_history;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_utils.TestDataResetUtil;
import use_case.Angela.view_history.ViewHistoryOutputData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class ViewHistoryPresenterTest {

    private ViewHistoryViewModel mockViewModel;
    private ViewHistoryPresenter presenter;
    private ViewHistoryState mockState;

    @BeforeEach
    void setUp() {
        TestDataResetUtil.resetAllSharedData();
        mockViewModel = mock(ViewHistoryViewModel.class);
        mockState = mock(ViewHistoryState.class);
        when(mockViewModel.getState()).thenReturn(mockState);
        
        presenter = new ViewHistoryPresenter(mockViewModel);
    }

    @AfterEach
    void tearDown() {
        // Clear all mocks to ensure no state leakage
        reset(mockViewModel, mockState);
        presenter = null;
    }

    @Test
    void testPrepareSuccessView_validOutputData_updatesViewModel() {
        LocalDate date = LocalDate.of(2023, 12, 15);
        String error = null; // Success case

        ViewHistoryOutputData outputData = new ViewHistoryOutputData(date, error);

        presenter.prepareSuccessView(outputData);

        // Verify that a new state is created with the builder pattern and set to view model
        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPrepareSuccessView_nullDate_updatesViewModel() {
        LocalDate date = null;
        String error = null;

        ViewHistoryOutputData outputData = new ViewHistoryOutputData(date, error);

        presenter.prepareSuccessView(outputData);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPrepareSuccessView_todayDate_updatesViewModel() {
        LocalDate date = LocalDate.now();
        String error = null;

        ViewHistoryOutputData outputData = new ViewHistoryOutputData(date, error);

        presenter.prepareSuccessView(outputData);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPrepareSuccessView_pastDate_updatesViewModel() {
        LocalDate date = LocalDate.now().minusDays(10);
        String error = null;

        ViewHistoryOutputData outputData = new ViewHistoryOutputData(date, error);

        presenter.prepareSuccessView(outputData);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPrepareFailView_errorMessage_updatesViewModel() {
        String error = "Failed to load history data";

        presenter.prepareFailView(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPrepareFailView_emptyErrorMessage_updatesViewModel() {
        String error = "";

        presenter.prepareFailView(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPrepareFailView_nullErrorMessage_updatesViewModel() {
        String error = null;

        presenter.prepareFailView(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPrepareFailView_longErrorMessage_updatesViewModel() {
        String error = "This is a very long error message that contains detailed information about what went wrong while trying to load the history data for the specified date";

        presenter.prepareFailView(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentAvailableDates_validDates_updatesViewModel() {
        List<LocalDate> availableDates = List.of(
                LocalDate.of(2023, 12, 15),
                LocalDate.of(2023, 12, 14),
                LocalDate.of(2023, 12, 13)
        );

        presenter.presentAvailableDates(availableDates);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentAvailableDates_emptyList_updatesViewModel() {
        List<LocalDate> availableDates = new ArrayList<>();

        presenter.presentAvailableDates(availableDates);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentAvailableDates_nullList_updatesViewModel() {
        List<LocalDate> availableDates = null;

        presenter.presentAvailableDates(availableDates);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentAvailableDates_largeList_updatesViewModel() {
        List<LocalDate> availableDates = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            availableDates.add(LocalDate.now().minusDays(i));
        }

        presenter.presentAvailableDates(availableDates);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentExportSuccess_validFilePath_updatesViewModel() {
        String filePath = "/path/to/exported/file.txt";

        presenter.presentExportSuccess(filePath);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentExportSuccess_emptyFilePath_updatesViewModel() {
        String filePath = "";

        presenter.presentExportSuccess(filePath);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentExportSuccess_nullFilePath_updatesViewModel() {
        String filePath = null;

        presenter.presentExportSuccess(filePath);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentExportFailure_errorMessage_updatesViewModel() {
        String error = "Failed to write to file";

        presenter.presentExportFailure(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentExportFailure_emptyErrorMessage_updatesViewModel() {
        String error = "";

        presenter.presentExportFailure(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentExportFailure_nullErrorMessage_updatesViewModel() {
        String error = null;

        presenter.presentExportFailure(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testPresentExportFailure_longErrorMessage_updatesViewModel() {
        String error = "This is a very long error message that contains detailed information about what went wrong during the export process";

        presenter.presentExportFailure(error);

        verify(mockViewModel).setState(any(ViewHistoryState.class));
    }

    @Test
    void testMultiplePresentations_allUpdateViewModel() {
        // Test multiple presentation methods called in sequence
        presenter.presentAvailableDates(List.of(LocalDate.now()));
        presenter.prepareFailView("Test error");
        presenter.presentExportSuccess("/test/path");

        verify(mockViewModel, times(3)).setState(any(ViewHistoryState.class));
    }
}