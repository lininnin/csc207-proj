package use_case.Alex.Event_related.create_event;

import entity.Info.Info;
import entity.Info.InfoFactory;

import java.util.List;

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
    public void execute(CreateEventInputData inputData) {
        String name = inputData.getName();
        String category = inputData.getCategory();
        String description = inputData.getDescription();

        // --- 校验 name ---
        if (name == null || name.trim().isEmpty()) {
            eventInfoPresenter.prepareFailView("Event name cannot be empty.");
            return;
        }

        if (name.length() > 20) {
            eventInfoPresenter.prepareFailView("Event name cannot exceed 20 characters.");
            return;
        }

        List<Info> existingEvents = eventInfoDataAccessObject.getAllEvents();
        for (Info event : existingEvents) {
            if (event.getName().equals(name)) { // ✅ case-sensitive comparison
                eventInfoPresenter.prepareFailView("No duplicate allowed for event name.");
                return;
            }
        }

        // --- 校验 category ---
        if (category != null && category.length() > 20) {
            eventInfoPresenter.prepareFailView("Category cannot exceed 20 characters.");
            return;
        }

        // --- 校验 description ---
        if (description != null && description.length() > 50) {
            eventInfoPresenter.prepareFailView("Description cannot exceed 50 characters.");
            return;
        }

        // --- 创建并保存 ---
        Info newEvent = (Info) eventInfoFactory.create(name.trim(), description, category);

        eventInfoDataAccessObject.save(newEvent);

        CreateEventOutputData outputData = new CreateEventOutputData(
                newEvent.getName(),
                newEvent.getDescription(),
                newEvent.getCategory(),
                false
        );

        eventInfoPresenter.prepareSuccessView(outputData);
    }
}




