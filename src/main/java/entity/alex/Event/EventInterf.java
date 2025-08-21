package entity.alex.Event;

import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.InfoInterf;

import java.time.LocalDate;

/**
 * Interface for Event entity to support abstraction and dependency inversion.
 */
public interface EventInterf {

    /**
     * @return the Info of this event.
     */
    InfoInterf getInfo();

    /**
     * @return the BeginAndDueDates of this event.
     */
    BeginAndDueDatesInterf getBeginAndDueDates();

    /**
     * Edit the Info of this event.
     * @param info the new Info object
     * @throws IllegalArgumentException if info is null
     */
    void editInfo(InfoInterf info);

    /**
     * Edit the due date of this event.
     * @param newDueDate the new due date
     * @throws IllegalArgumentException if date is null or before begin date
     * @throws IllegalStateException if beginAndDueDates is not initialized
     */
    void editDueDate(LocalDate newDueDate);
}

