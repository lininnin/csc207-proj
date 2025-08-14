package use_case.alex.event_related.available_event_module;

import entity.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.event_related.avaliable_events_module.edit_event.*;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class EditAvailableEventInteractorTest {

    private EditEventDataAccessInterf mockDataAccess;
    private EditEventOutputBoundary mockPresenter;
    private EditEventInputData input;
    private EditEventInteractor interactor;

    @BeforeEach
    void setUp() {
        mockDataAccess = mock(EditEventDataAccessInterf.class);
        mockPresenter = mock(EditEventOutputBoundary.class);
        interactor = new EditEventInteractor(mockDataAccess, mockPresenter);
    }

    @Test
    void testEmptyName_shouldFail() {
        input = new EditEventInputData("id123", "", "Work", "desc");
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> "Event name cannot be empty.".equals(out.getErrorMessage())
                        && out.isUseCaseFailed())
        );

        verifyNoInteractions(mockDataAccess);
    }

    @Test
    void testNameTooLong_shouldFail() {
        String longName = "ThisNameIsWayTooLongToBeValid";
        input = new EditEventInputData("id123", longName, "Work", "desc");
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getErrorMessage().contains("Event name too long"))
        );
    }

    @Test
    void testCategoryTooLong_shouldFail() {
        String longCategory = "CategoryThatIsDefinitelyOverTwentyChars";
        input = new EditEventInputData("id123", "Valid Name", longCategory, "desc");
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getErrorMessage().contains("Category too long"))
        );
    }

    @Test
    void testDescriptionTooLong_shouldFail() {
        String longDesc = "d".repeat(101);
        input = new EditEventInputData("id123", "Valid Name", "Work", longDesc);
        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getErrorMessage().contains("Description too long"))
        );
    }

    @Test
    void testEventNotFound_shouldFail() {
        input = new EditEventInputData("not_found_id", "Name", "Cat", "Desc");
        when(mockDataAccess.getEventById("not_found_id")).thenReturn(null);

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getErrorMessage().contains("Original event not found"))
        );
    }

    @Test
    void testNameConflict_shouldFail() {
        Info original = new Info.Builder("Old Name").build();
        original.setCategory("Cat");
        original.setDescription("Desc");

        Info other = new Info.Builder("New Name").build();  // name conflict

        input = new EditEventInputData(original.getId(), "New Name", "Cat", "Desc");

        when(mockDataAccess.getEventById(original.getId())).thenReturn(original);
        when(mockDataAccess.getAllEvents()).thenReturn(Arrays.asList(original, other));

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getErrorMessage().contains("Event name already exists"))
        );
    }

    @Test
    void testUpdateFails_shouldFail() {
        Info event = new Info.Builder("Old Name").build();
        event.setCategory("Cat");
        event.setDescription("Old Desc");

        input = new EditEventInputData(event.getId(), "New Name", "New Cat", "New Desc");

        when(mockDataAccess.getEventById(event.getId())).thenReturn(event);
        when(mockDataAccess.getAllEvents()).thenReturn(Collections.singletonList(event));
        when(mockDataAccess.update(event)).thenReturn(false);  // DB update fails

        interactor.execute(input);

        verify(mockPresenter).prepareFailView(
                argThat(out -> out.getErrorMessage().contains("Database update failed"))
        );
    }

    @Test
    void testValidEdit_shouldSucceed() {
        Info event = new Info.Builder("Old Name").build();
        event.setCategory("Old Cat");
        event.setDescription("Old Desc");

        input = new EditEventInputData(event.getId(), "Updated Name", "New Cat", "New Desc");

        when(mockDataAccess.getEventById(event.getId())).thenReturn(event);
        when(mockDataAccess.getAllEvents()).thenReturn(Collections.singletonList(event));
        when(mockDataAccess.update(event)).thenReturn(true);

        interactor.execute(input);

        verify(mockPresenter).prepareSuccessView(
                argThat(out -> out.getName().equals("Updated Name")
                        && out.getCategory().equals("New Cat")
                        && out.getDescription().equals("New Desc")
                        && !out.isUseCaseFailed())
        );
    }
}

