package use_case.alex.event_related.add_event;

import entity.Alex.Event.Event;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;

import java.time.LocalDate;


public class AddEventInteractor implements AddEventInputBoundary {

    private final AddEventDataAccessInterf todaysEventDAO;
    private final ReadAvailableEventDataAccessInterf availableEventDAO;
    private final AddEventOutputBoundary presenter;

    public AddEventInteractor(AddEventDataAccessInterf todaysEventDAO,
                              ReadAvailableEventDataAccessInterf availableEventDAO,
                              AddEventOutputBoundary presenter) {
        this.todaysEventDAO = todaysEventDAO;
        this.availableEventDAO = availableEventDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddEventInputData inputData) {
        String name = inputData.getSelectedName();
        LocalDate dueDate = inputData.getDueDate();

        Info baseInfo = availableEventDAO.findInfoByName(name);

        if (baseInfo == null) {
            presenter.prepareFailView("No available event with name: " + name);
            return;
        }

        Event newEvent = new Event.Builder(baseInfo)
                .beginAndDueDates(new BeginAndDueDates(LocalDate.now(), dueDate))
                .build();
        if (todaysEventDAO.contains(newEvent)) {
            presenter.prepareFailView("Event already added today.");
            return;
        }

        todaysEventDAO.save(newEvent);
        presenter.prepareSuccessView(new AddEventOutputData(name, dueDate, true));
    }
}


