package use_case.alex.event_related.add_event;

import entity.Alex.Event.EventInterf;
import entity.Alex.Event.EventFactoryInterf;
import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.BeginAndDueDates.BeginAndDueDatesFactoryInterf;
import entity.info.InfoInterf;

import java.time.LocalDate;

/**
 * Interactor for adding an event to today's event list.
 * Now fully decoupled from the concrete Event class, using EventInterf and BeginAndDueDatesInterf.
 */
public class AddEventInteractor implements AddEventInputBoundary {

    private final AddEventDataAccessInterf todaysEventDAO;
    private final ReadAvailableEventDataAccessInterf availableEventDAO;
    private final AddEventOutputBoundary presenter;
    private final EventFactoryInterf eventFactory;
    private final BeginAndDueDatesFactoryInterf beginAndDueDatesFactory;

    public AddEventInteractor(AddEventDataAccessInterf todaysEventDAO,
                              ReadAvailableEventDataAccessInterf availableEventDAO,
                              AddEventOutputBoundary presenter,
                              EventFactoryInterf eventFactory,
                              BeginAndDueDatesFactoryInterf beginAndDueDatesFactory) {
        this.todaysEventDAO = todaysEventDAO;
        this.availableEventDAO = availableEventDAO;
        this.presenter = presenter;
        this.eventFactory = eventFactory;
        this.beginAndDueDatesFactory = beginAndDueDatesFactory;
    }

    @Override
    public void execute(AddEventInputData inputData) {
        String name = inputData.getSelectedName();
        LocalDate dueDate = inputData.getDueDate();

        InfoInterf baseInfo = availableEventDAO.findInfoByName(name);

        if (baseInfo == null) {
            presenter.prepareFailView("No available event with name: " + name);
            return;
        }

        // 创建 BeginAndDueDatesInterf
        BeginAndDueDatesInterf dates = beginAndDueDatesFactory.create(LocalDate.now(), dueDate);

        // 创建 EventInterf（通过工厂）
        EventInterf newEvent = eventFactory.createEvent(baseInfo, dates);

        if (todaysEventDAO.contains(newEvent)) {
            presenter.prepareFailView("Event already added today.");
            return;
        }

        todaysEventDAO.save(newEvent);
        presenter.prepareSuccessView(new AddEventOutputData(name, dueDate, true));
    }
}
