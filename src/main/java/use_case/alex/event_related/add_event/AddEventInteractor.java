package use_case.alex.event_related.add_event;

import entity.alex.Event.EventFactoryInterf;
import entity.alex.Event.EventInterf;
import entity.BeginAndDueDates.BeginAndDueDatesFactoryInterf;
import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.InfoInterf;

import java.time.LocalDate;

/**
 * Interactor for the AddEvent use case.
 * Handles the logic of adding a new event based on the selected available event
 * and the user-provided due date.
 */
public class AddEventInteractor implements AddEventInputBoundary {

    /** DAO for today's event pool. */
    private final AddEventDataAccessInterf todaysEventDAO;

    /** DAO for retrieving available event templates. */
    private final ReadAvailableEventDataAccessInterf availableEventDAO;

    /** Presenter for preparing output view models. */
    private final AddEventOutputBoundary presenter;

    /** Factory for creating events. */
    private final EventFactoryInterf eventFactory;

    /** Factory for creating begin and due dates. */
    private final BeginAndDueDatesFactoryInterf beginAndDueDatesFactory;

    /**
     * Constructs an AddEventInteractor.
     *
     * @param todaysDAO       The DAO for saving today's events.
     * @param availableDAO    The DAO for reading available event definitions.
     * @param outputPresenter The presenter that handles output.
     * @param eventFactory    Factory for creating Event objects.
     * @param datesFactory    Factory for creating BeginAndDueDates objects.
     */
    public AddEventInteractor(final AddEventDataAccessInterf todaysDAO,
                              final ReadAvailableEventDataAccessInterf availableDAO,
                              final AddEventOutputBoundary outputPresenter,
                              final EventFactoryInterf eventFactory,
                              final BeginAndDueDatesFactoryInterf datesFactory) {
        this.todaysEventDAO = todaysDAO;
        this.availableEventDAO = availableDAO;
        this.presenter = outputPresenter;
        this.eventFactory = eventFactory;
        this.beginAndDueDatesFactory = datesFactory;
    }

    /**
     * Executes the AddEvent use case.
     * Attempts to construct and save a new event based on user selection.
     * If an event with the same info already exists, shows a failure view.
     *
     * @param inputData The input data containing selected event name and due date.
     */
    @Override
    public void execute(final AddEventInputData inputData) {
        String name = inputData.getSelectedName();
        LocalDate dueDate = inputData.getDueDate();

        InfoInterf baseInfo = availableEventDAO.findInfoByName(name);

        if (baseInfo == null) {
            presenter.prepareFailView(
                    "No available event with name: " + name);
            return;
        }

        BeginAndDueDatesInterf beginAndDueDates =
                beginAndDueDatesFactory.create(LocalDate.now(), dueDate);

        EventInterf newEvent = eventFactory.createEvent(baseInfo, beginAndDueDates);

        if (todaysEventDAO.contains(newEvent)) {
            presenter.prepareFailView(
                    "Event already added today.");
            return;
        }

        todaysEventDAO.save(newEvent);
        presenter.prepareSuccessView(
                new AddEventOutputData(name, dueDate, true));
    }
}

