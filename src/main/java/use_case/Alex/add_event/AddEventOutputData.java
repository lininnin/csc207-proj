package use_case.Alex.add_event;

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

    public AddEventOutputData(String name, LocalDate dueDate, boolean success) {
        this.name = name;
        this.dueDate = dueDate;
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isSuccess() {
        return success;
    }
}

