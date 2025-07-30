package use_case.Alex.Event_related.add_event;

import java.time.LocalDate;

/**
 * InputData for the AddEvent use case.
 * Holds the user's selected name and optional due date.
 */
public class AddEventInputData {

    private final String selectedName;
    private final LocalDate dueDate;

    public AddEventInputData(String selectedName, LocalDate dueDate) {
        this.selectedName = selectedName;
        this.dueDate = dueDate;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}

