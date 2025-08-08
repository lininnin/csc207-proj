package use_case.alex.event_related.available_event_module;

import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.event_related.avaliable_events_module.delete_event.*;

import static org.mockito.Mockito.*;

public class DeleteAvaliableEventInteractorTest {

    private DeleteEventDataAccessInterf mockDataAccess;
    private DeleteEventOutputBoundary mockOutputBoundary;
    private DeleteEventInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDataAccess = mock(DeleteEventDataAccessInterf.class);
        mockOutputBoundary = mock(DeleteEventOutputBoundary.class);
        interactor = new DeleteEventInteractor(mockDataAccess, mockOutputBoundary);
    }

    @Test
    void testExecute_eventNotFound_shouldFail() {
        String eventId = "123";
        when(mockDataAccess.getEventById(eventId)).thenReturn(null);

        DeleteEventInputData inputData = new DeleteEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockOutputBoundary).prepareFailView(
                argThat(output ->
                        output.getEventId().equals("123") &&
                                output.getErrorMessage().equals("Event not found or already deleted") &&
                                !output.isDeletionSuccess())
        );
        verify(mockDataAccess, never()).remove(any());
    }

    @Test
    void testExecute_eventNotContained_shouldFail() {
        String eventId = "456";
        Info mockInfo = new Info.Builder("Sample Event").build();
        when(mockDataAccess.getEventById(eventId)).thenReturn(mockInfo);
        when(mockDataAccess.contains(mockInfo)).thenReturn(false);

        DeleteEventInputData inputData = new DeleteEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockOutputBoundary).prepareFailView(
                argThat(output ->
                        output.getEventId().equals("456") &&
                                output.getErrorMessage().equals("Event not found or already deleted") &&
                                !output.isDeletionSuccess())
        );
        verify(mockDataAccess, never()).remove(any());
    }

    @Test
    void testExecute_deletionFails_shouldFail() {
        String eventId = "789";
        Info mockInfo = new Info.Builder("Failing Event").build();
        when(mockDataAccess.getEventById(eventId)).thenReturn(mockInfo);
        when(mockDataAccess.contains(mockInfo)).thenReturn(true);
        when(mockDataAccess.remove(mockInfo)).thenReturn(false);

        DeleteEventInputData inputData = new DeleteEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockOutputBoundary).prepareFailView(
                argThat(output ->
                        output.getEventId().equals(mockInfo.getId()) &&
                                output.getErrorMessage().equals("Deletion failed due to unknown error") &&
                                !output.isDeletionSuccess())
        );
    }

    @Test
    void testExecute_successfulDeletion_shouldSucceed() {
        String eventId = "abc";
        Info mockInfo = new Info.Builder("Success Event").build();
        when(mockDataAccess.getEventById(eventId)).thenReturn(mockInfo);
        when(mockDataAccess.contains(mockInfo)).thenReturn(true);
        when(mockDataAccess.remove(mockInfo)).thenReturn(true);

        DeleteEventInputData inputData = new DeleteEventInputData(eventId);
        interactor.execute(inputData);

        verify(mockOutputBoundary).prepareSuccessView(
                argThat(output ->
                        output.getEventId().equals(mockInfo.getId()) &&
                                output.getEventName().equals("Success Event") &&
                                output.isDeletionSuccess())
        );
    }
}
