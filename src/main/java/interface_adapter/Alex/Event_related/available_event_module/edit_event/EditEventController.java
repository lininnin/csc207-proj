package interface_adapter.Alex.Event_related.available_event_module.edit_event;

import use_case.Alex.Event_related.avaliable_events_module.edit_event.EditEventInputBoundary;
import use_case.Alex.Event_related.avaliable_events_module.edit_event.EditEventInputData;

public class EditEventController {

    private final EditEventInputBoundary editEventInteractor;

    public EditEventController(EditEventInputBoundary editEventInteractor) {
        this.editEventInteractor = editEventInteractor;
    }

    /**
     * Called by the UI when the user submits an edit request.
     *
     * @param id          ID of the event to be edited
     * @param name        New name for the event
     * @param category    New category for the event
     * @param description New description for the event
     */
    public void execute(String id, String name, String category, String description) {
        EditEventInputData inputData = new EditEventInputData(id, name, category, description);
        editEventInteractor.execute(inputData);
    }
}
