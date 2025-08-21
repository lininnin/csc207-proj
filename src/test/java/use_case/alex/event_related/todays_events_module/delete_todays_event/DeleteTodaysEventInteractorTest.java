package use_case.alex.event_related.todays_events_module.delete_todays_event;

import entity.alex.Event.EventInterf;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DeleteTodaysEventInteractorTest {

    private DeleteTodaysEventDataAccessInterf mockDataAccess;
    private DeleteTodaysEventOutputBoundary mockPresenter;
    private DeleteTodaysEventInteractor interactor;
    private EventInterf mockEvent;
    private InfoInterf mockInfo;

    @BeforeEach
    public void setUp() {
        mockDataAccess = mock(DeleteTodaysEventDataAccessInterf.class);
        mockPresenter = mock(DeleteTodaysEventOutputBoundary.class);
        interactor = new DeleteTodaysEventInteractor(mockDataAccess, mockPresenter);

        mockEvent = mock(EventInterf.class);
        mockInfo = mock(InfoInterf.class);
    }

    @Test
    public void testDeleteSuccess() {
        String eventId = "event123";
        when(mockInfo.getId()).thenReturn(eventId);
        when(mockInfo.getName()).thenReturn("Sample Event");
        when(mockEvent.getInfo()).thenReturn(mockInfo);

        when(mockDataAccess.getEventById(eventId)).thenReturn(mockEvent);
        when(mockDataAccess.contains(mockEvent)).thenReturn(true);
        when(mockDataAccess.remove(mockEvent)).thenReturn(true);

        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockPresenter).prepareSuccessView(argThat(output ->
                output.getEventId().equals(eventId) &&
                        output.getEventName().equals("Sample Event") &&
                        output.isDeletionSuccess()
        ));
    }

    @Test
    public void testEventNotFound() {
        String eventId = "missingEvent";
        when(mockDataAccess.getEventById(eventId)).thenReturn(null);

        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(argThat(output ->
                output.getEventId().equals(eventId) &&
                        "Event not found or already deleted".equals(output.getErrorMessage()) &&
                        !output.isDeletionSuccess()
        ));
    }

    @Test
    public void testDeleteFailsDueToDataAccess() {
        String eventId = "event456";
        when(mockInfo.getId()).thenReturn(eventId);
        when(mockEvent.getInfo()).thenReturn(mockInfo);

        when(mockDataAccess.getEventById(eventId)).thenReturn(mockEvent);
        when(mockDataAccess.contains(mockEvent)).thenReturn(true);
        when(mockDataAccess.remove(mockEvent)).thenReturn(false); // deletion fails

        DeleteTodaysEventInputData inputData = new DeleteTodaysEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(argThat(output ->
                output.getEventId().equals(eventId) &&
                        "Deletion failed due to unknown error".equals(output.getErrorMessage()) &&
                        !output.isDeletionSuccess()
        ));
    }
}
