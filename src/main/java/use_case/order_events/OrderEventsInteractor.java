package use_case.order_events;

import java.util.Comparator;
import java.util.List;

import entity.Alex.Event.Event;
import use_case.repository.EventRepository;

/**
 * The interactor for the order events use case.
 * It handles the business logic of retrieving, sorting, and presenting events based on specified criteria.
 */
public class OrderEventsInteractor implements OrderEventsInputBoundary {
    private final EventRepository eventRepository;
    private final OrderEventsOutputBoundary presenter;

    /**
     * Constructs an {@code OrderEventsInteractor} with the required dependencies.
     *
     * @param eventRepository The repository for accessing event data.
     * @param presenter The output boundary for presenting the sorted data.
     */
    public OrderEventsInteractor(EventRepository eventRepository,
                                 OrderEventsOutputBoundary presenter) {
        this.eventRepository = eventRepository;
        this.presenter = presenter;
    }

    /**
     * Executes the main logic for ordering events.
     * It retrieves a list of events (today's or all).
     *
     * @param inputData The data containing the sorting criteria (field name, direction, and scope).
     * @throws IllegalArgumentException if the provided order criteria is not valid.
     */
    @Override
    public void execute(OrderEventsInputData inputData) {
        final List<Event> events = inputData.isTodayOnly()
                ? eventRepository.getTodayEvents()
                : eventRepository.getAllEvents();

        Comparator<Event> comparator = switch (inputData.getOrderBy().toLowerCase()) {
            case "name" -> Comparator.comparing(exe -> exe.getInfo().getName());
            case "category" -> Comparator.comparing(ex1 -> ex1.getInfo().getCategory());
            case "due" -> Comparator.comparing(ex2 -> ex2.getBeginAndDueDates().getDueDate());
            default -> throw new IllegalArgumentException("Invalid order criteria");
        };

        if (inputData.isReverse()) {
            comparator = comparator.reversed();
        }

        events.sort(comparator);
        presenter.present(new OrderEventsOutputData(events, inputData.getOrderBy()));
    }
}
