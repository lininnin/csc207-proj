package use_case.alex.event_related.add_event;

import java.time.LocalDate;

/**
 * InputData for the AddEvent use case.
 * Holds the user's selected name and optional due date.
 */
public class AddEventInputData {

    /** The name of the selected event. */
    private final String selectedName;

    /** The due date of the event, can be null. */
    private final LocalDate dueDate;

    /**
     * Constructs input data for adding an event.
     *
     * @param name The name of the selected event.
     * @param date The optional due date of the event.
     */
    public AddEventInputData(final String name,
                             final LocalDate date) {
        this.selectedName = name;
        this.dueDate = date;
    }

    /**
     * Returns the name of the selected event.
     *
     * @return The selected event name.
     */
    public String getSelectedName() {
        return selectedName;
    }

    /**
     * Returns the due date of the event.
     *
     * @return The due date, can be null.
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
}


