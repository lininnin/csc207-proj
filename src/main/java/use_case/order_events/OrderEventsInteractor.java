package use_case.order_events;

import entity.Alex.Event.Event;
import use_case.repository.EventRepository;
import java.util.Comparator;
import java.util.List;

public class OrderEventsInteractor implements OrderEventsInputBoundary {
    private final EventRepository eventRepository;
    private final OrderEventsOutputBoundary presenter;

    public OrderEventsInteractor(EventRepository eventRepository,
                                 OrderEventsOutputBoundary presenter) {
        this.eventRepository = eventRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(OrderEventsInputData inputData) {
        List<Event> events = inputData.isTodayOnly() ?
                eventRepository.getTodayEvents() :
                eventRepository.getAllEvents();

        Comparator<Event> comparator = switch (inputData.getOrderBy().toLowerCase()) {
            case "name" -> Comparator.comparing(e -> e.getInfo().getName());
            case "category" -> Comparator.comparing(e -> e.getInfo().getCategory());
            case "due" -> Comparator.comparing(e -> e.getBeginAndDueDates().getDueDate());
            default -> throw new IllegalArgumentException("Invalid order criteria");
        };

        if (inputData.isReverse()) {
            comparator = comparator.reversed();
        }

        events.sort(comparator);
        presenter.present(new OrderEventsOutputData(events, inputData.getOrderBy()));
    }
}