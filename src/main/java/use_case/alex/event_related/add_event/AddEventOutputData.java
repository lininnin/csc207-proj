package use_case.alex.event_related.add_event;

import java.time.LocalDate;

/**
 * OutputData for the AddEvent use case.
 * Encapsulates the result of adding a Today's Event.
 */
public class AddEventOutputData {

    /** The name of the event added to today. */
    private final String name;

    /** The due date assigned to the event (can be null). */
    private final LocalDate dueDate;

    /** Whether the event was added successfully. */
    private final boolean success;

    /**
     * Constructs an output data object for the AddEvent use case.
     *
     * @param eventName  The name of the event added.
     * @param eventDue   The due date of the event (can be null).
     * @param isSuccess  Whether the event was added successfully.
     */
    public AddEventOutputData(final String eventName,
                              final LocalDate eventDue,
                              final boolean isSuccess) {
        this.name = eventName;
        this.dueDate = eventDue;
        this.success = isSuccess;
    }

    /**
     * Gets the name of the added event.
     *
     * @return The event name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the due date of the added event.
     *
     * @return The due date, can be null.
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Indicates whether the event was added successfully.
     *
     * @return true if added successfully; false otherwise.
     */
    public boolean isSuccess() {
        return success;
    }
}
