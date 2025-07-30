package entity.Alex.Event;

import entity.Info.Info;
import entity.BeginAndDueDates.BeginAndDueDates;

import java.time.LocalDate;

/**
 * Interface for Event entity to support abstraction and dependency inversion.
 */
public interface EventInterf {

    /**
     * @return the Info of this event.
     */
    Info getInfo();

    /**
     * @return the BeginAndDueDates of this event.
     */
    BeginAndDueDates getBeginAndDueDates();

    /**
     * Edit the Info of this event.
     * @param info the new Info object
     * @throws IllegalArgumentException if info is null
     */
    void editInfo(Info info);

    /**
     * Edit the due date of this event.
     * @param newDueDate the new due date
     * @throws IllegalArgumentException if date is null or before begin date
     * @throws IllegalStateException if beginAndDueDates is not initialized
     */
    void editDueDate(LocalDate newDueDate);
}

