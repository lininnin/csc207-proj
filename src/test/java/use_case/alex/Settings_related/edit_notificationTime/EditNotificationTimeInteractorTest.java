package use_case.alex.Settings_related.edit_notificationTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.mockito.Mockito.*;

public class EditNotificationTimeInteractorTest {

    private EditNotificationTimeDataAccessInterf mockDAO;
    private EditNotificationTimeOutputBoundary mockPresenter;
    private EditNotificationTimeInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(EditNotificationTimeDataAccessInterf.class);
        mockPresenter = mock(EditNotificationTimeOutputBoundary.class);
        interactor = new EditNotificationTimeInteractor(mockDAO, mockPresenter);
    }

    @Test
    void testExecute_withValidTimes_shouldSucceed() {
        EditNotificationTimeInputData input = new EditNotificationTimeInputData(
                "08:00", "12:30", "20:45");

        interactor.execute(input);

        verify(mockDAO).updateNotificationTimes(
                LocalTime.of(8, 0),
                LocalTime.of(12, 30),
                LocalTime.of(20, 45));

        verify(mockPresenter).prepareSuccessView(
                argThat(output -> output.getReminder1().equals("08:00")
                        && output.getReminder2().equals("12:30")
                        && output.getReminder3().equals("20:45")
                        && !output.isUseCaseFailed()));
    }

    @Test
    void testExecute_withInvalidTimeFormat_shouldFail() {
        EditNotificationTimeInputData input = new EditNotificationTimeInputData(
                "8 AM", "noon", "night");

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(msg -> msg.contains("Invalid time format"))
        );
        verifyNoInteractions(mockDAO);
    }

    @Test
    void testExecute_withDAOThrowsException_shouldFailGracefully() {
        EditNotificationTimeInputData input = new EditNotificationTimeInputData(
                "10:00", "14:00", "22:00");

        doThrow(new RuntimeException("DB error")).when(mockDAO)
                .updateNotificationTimes(any(), any(), any());

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(msg -> msg.contains("Unexpected error"))
        );
    }
}

