package use_case.alex.notification_related;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.mockito.Mockito.*;

public class NotificationInteractorTest {

    private NotificationDataAccessObjectInterf mockDAO;
    private NotificationOutputBoundary mockPresenter;
    private NotificationInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDAO = mock(NotificationDataAccessObjectInterf.class);
        mockPresenter = mock(NotificationOutputBoundary.class);
        interactor = new NotificationInteractor(mockDAO, mockPresenter);
    }

    @Test
    void testExecute_currentTimeMatchesReminder1_shouldTriggerReminder() {
        LocalTime now = LocalTime.of(8, 0);
        when(mockDAO.getReminder1()).thenReturn(now);
        when(mockDAO.getReminder2()).thenReturn(LocalTime.of(12, 0));
        when(mockDAO.getReminder3()).thenReturn(LocalTime.of(20, 0));

        NotificationInputData inputData = new NotificationInputData(now);
        interactor.execute(inputData);

        verify(mockPresenter).prepareReminderView(
                argThat(output -> output.getReminderMessage().contains("fill your wellness log"))
        );
    }

    @Test
    void testExecute_currentTimeMatchesReminder2_shouldTriggerReminder() {
        LocalTime now = LocalTime.of(12, 0);
        when(mockDAO.getReminder1()).thenReturn(LocalTime.of(8, 0));
        when(mockDAO.getReminder2()).thenReturn(now);
        when(mockDAO.getReminder3()).thenReturn(LocalTime.of(20, 0));

        NotificationInputData inputData = new NotificationInputData(now);
        interactor.execute(inputData);

        verify(mockPresenter).prepareReminderView(any(NotificationOutputData.class));
    }

    @Test
    void testExecute_currentTimeDoesNotMatchAny_shouldDoNothing() {
        LocalTime now = LocalTime.of(9, 30);
        when(mockDAO.getReminder1()).thenReturn(LocalTime.of(8, 0));
        when(mockDAO.getReminder2()).thenReturn(LocalTime.of(12, 0));
        when(mockDAO.getReminder3()).thenReturn(LocalTime.of(20, 0));

        NotificationInputData inputData = new NotificationInputData(now);
        interactor.execute(inputData);

        verify(mockPresenter, never()).prepareReminderView(any());
    }
}

