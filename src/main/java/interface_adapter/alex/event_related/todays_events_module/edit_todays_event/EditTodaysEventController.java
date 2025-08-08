package interface_adapter.alex.event_related.todays_events_module.edit_todays_event;

import use_case.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventInputBoundary;
import use_case.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventInputData;

/**
 * Controller for editing the due date of a today's event.
 * Called by the UI when the user submits an edit request.
 */
public class EditTodaysEventController {

    private final EditTodaysEventInputBoundary editTodaysEventInteractor;

    public EditTodaysEventController(EditTodaysEventInputBoundary editTodaysEventInteractor) {
        this.editTodaysEventInteractor = editTodaysEventInteractor;
    }

    /**
     * Executes the use case with given id and new due date.
     *
     * @param id       ID of the event to be edited
     * @param dueDate  New due date in ISO-8601 format (yyyy-MM-dd)
     */
    public void execute(String id, String dueDate) {
        EditTodaysEventInputData inputData = new EditTodaysEventInputData(id, dueDate);
        editTodaysEventInteractor.execute(inputData);
    }
}

