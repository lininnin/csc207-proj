package use_case.alex.event_related.todays_events_module.delete_todays_event;

import entity.Alex.Event.EventInterf;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DeleteTodaysEventInteractorTest {

    private DeleteTodaysEventDataAccessInterf mockDataAccess;
    private DeleteTodaysEventOutputBoundary mockPresenter;
    private DeleteTodaysEventInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDataAccess = mock(DeleteTodaysEventDataAccessInterf.class);
        mockPresenter = mock(DeleteTodaysEventOutputBoundary.class);
        interactor = new DeleteTodaysEventInteractor(mockDataAccess, mockPresenter);
    }

    @Test
    void testDeleteSuccess_shouldCallSuccessView() {
        String eventId = "e123";

        InfoInterf mockInfo = mock(InfoInterf.class);
        when(mockInfo.getId()).thenReturn(eventId);
        when(mockInfo.getName()).thenReturn("Math Assignment");

        EventInterf mockEvent = mock(EventInterf.class);
        when(mockEvent.getInfo()).thenReturn(mockInfo);

        when(mockDataAccess.getEventById(eventId)).thenReturn(mockEvent);
        when(mockDataAccess.contains(mockEvent)).thenReturn(true);
        when(mockDataAccess.remove(mockEvent)).thenReturn(true);

        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockPresenter).prepareSuccessView(
                argThat(out -> out != null &&
                        "e123".equals(out.getEventId()) &&
                        "Math Assignment".equals(out.getEventName()) &&
                        out.isDeletionSuccess())
        );
    }

    @Test
    void testEventNotFound_shouldCallFailView() {
        String eventId = "nonexistent";

        when(mockDataAccess.getEventById(eventId)).thenReturn(null);

        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out != null &&
                        "nonexistent".equals(out.getEventId()) &&
                        out.getErrorMessage().contains("not found") &&
                        !out.isDeletionSuccess())
        );

        verify(mockDataAccess, never()).remove(any());
    }

    @Test
    void testRemoveFails_shouldCallFailView() {
        String eventId = "e123";

        InfoInterf mockInfo = mock(InfoInterf.class);
        when(mockInfo.getId()).thenReturn(eventId);
        when(mockInfo.getName()).thenReturn("Delete Me");

        EventInterf mockEvent = mock(EventInterf.class);
        when(mockEvent.getInfo()).thenReturn(mockInfo);

        when(mockDataAccess.getEventById(eventId)).thenReturn(mockEvent);
        when(mockDataAccess.contains(mockEvent)).thenReturn(true);
        when(mockDataAccess.remove(mockEvent)).thenReturn(false);

        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out != null &&
                        out.getErrorMessage().contains("Deletion failed") &&
                        !out.isDeletionSuccess())
        );
    }
}
