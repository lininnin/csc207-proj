package use_case.Alex.create_event;

import entity.Info.Info;
import entity.Info.InfoFactory;

public class CreateEventInteractor implements CreateEventInputBoundary {
    private final CreateEventDataAccessInterface eventInfoDataAccessObject;
    private final CreateEventOutputBoundary eventInfoPresenter;
    private final InfoFactory eventInfoFactory;

    public CreateEventInteractor(CreateEventDataAccessInterface createEventDataAccessInterface,
                                 CreateEventOutputBoundary createEventOutputBoundary,
                                 InfoFactory eventInfoFactory) {
        this.eventInfoDataAccessObject = createEventDataAccessInterface;
        this.eventInfoPresenter = createEventOutputBoundary;
        this.eventInfoFactory = eventInfoFactory;
    }

    @Override
    public void execute(CreateEventInputData createEventInputData) {
        final Info eventInfo = (Info) eventInfoFactory.create(
                createEventInputData.getName(),
                createEventInputData.getDescription(),
                createEventInputData.getCategory()
        );

        eventInfoDataAccessObject.save(eventInfo);

        final CreateEventOutputData createEventOutputData = new CreateEventOutputData(
                eventInfo.getName(),
                false
        );
        eventInfoPresenter.prepareSuccessView(createEventOutputData);
    }

}



