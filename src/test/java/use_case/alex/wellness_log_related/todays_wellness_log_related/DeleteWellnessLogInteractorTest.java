package use_case.alex.wellness_log_related.todays_wellness_log_related;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.wellness_log_related.todays_wellness_log_module.delete_wellnesslog.*;

import static org.mockito.Mockito.*;

public class DeleteWellnessLogInteractorTest {

    private DeleteWellnessLogDataAccessInterf mockDAO;
    private DeleteWellnessLogOutputBoundary mockPresenter;
    private DeleteWellnessLogInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(DeleteWellnessLogDataAccessInterf.class);
        mockPresenter = mock(DeleteWellnessLogOutputBoundary.class);
        interactor = new DeleteWellnessLogInteractor(mockDAO, mockPresenter);
    }

    @Test
    void testExecute_successfulDeletion_shouldCallSuccessView() {
        String logId = "log-123";
        when(mockDAO.deleteById(logId)).thenReturn(true);

        DeleteWellnessLogInputData input = new DeleteWellnessLogInputData(logId);
        interactor.execute(input);

        verify(mockDAO).deleteById(logId);
        verify(mockPresenter).prepareSuccessView(argThat(output ->
                output.getDeletedLogId().equals(logId) && output.isSuccess()));
    }

    @Test
    void testExecute_failedDeletion_shouldCallFailView() {
        String logId = "nonexistent-log";
        when(mockDAO.deleteById(logId)).thenReturn(false);

        DeleteWellnessLogInputData input = new DeleteWellnessLogInputData(logId);
        interactor.execute(input);

        verify(mockDAO).deleteById(logId);
        verify(mockPresenter).prepareFailView("Deletion failed: Log ID " + logId + " not found.");
    }
}

