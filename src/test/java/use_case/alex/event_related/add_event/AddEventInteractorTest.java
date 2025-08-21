package use_case.alex.event_related.add_event;

import data_access.alex.EventAvailableDataAccessObject;
import data_access.alex.TodaysEventDataAccessObject;
import entity.alex.DailyEventLog.DailyEventLogFactory;
import entity.alex.DailyEventLog.DailyEventLogFactoryInterf;
import entity.alex.Event.EventFactory;
import entity.alex.Event.EventFactoryInterf;
import entity.alex.Event.EventInterf;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.BeginAndDueDates.BeginAndDueDatesFactoryInterf;
import entity.info.Info;
import entity.info.InfoInterf;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.add_event.AddEventPresenter;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AddEventInteractorTest {

    private AddEventInputBoundary interactor;
    private TodaysEventDataAccessObject todaysDao;
    private EventAvailableDataAccessObject availableDao;

    @BeforeEach
    public void setup() {
        // 初始化 ViewModel
        AddedEventViewModel addedVM = new AddedEventViewModel();
        TodaysEventsViewModel todaysVM = new TodaysEventsViewModel();

        // 初始化 DAO 和 Factory
        DailyEventLogFactoryInterf logFactory = new DailyEventLogFactory();
        todaysDao = new TodaysEventDataAccessObject(logFactory);
        availableDao = new EventAvailableDataAccessObject(() -> new entity.alex.EventAvailable.EventAvailable());  // Lambda for factory

        // 添加一个事件模板（Info）
        InfoInterf template = new Info.Builder("Test Event")
                .category("School")
                .description("Study for final")
                .build();
        availableDao.save(template);

        // 创建 presenter
        AddEventOutputBoundary presenter = new AddEventPresenter(addedVM, todaysVM, todaysDao);

        // 创建 interactor
        EventFactoryInterf eventFactory = new EventFactory();
        BeginAndDueDatesFactoryInterf dateFactory = new BeginAndDueDatesFactory();

        interactor = new AddEventInteractor(todaysDao, availableDao, presenter, eventFactory, dateFactory);
    }

    @Test
    public void testAddEventSuccess() {
        AddEventInputData input = new AddEventInputData("Test Event", LocalDate.now());

        interactor.execute(input);

        assertEquals(1, todaysDao.getTodaysEvents().size());

        EventInterf event = todaysDao.getTodaysEvents().get(0);
        assertEquals("Test Event", event.getInfo().getName());
        assertEquals("School", event.getInfo().getCategory());
    }

    @Test
    public void testAddEventFailure_EventNotFound() {
        AddEventInputData input = new AddEventInputData("Nonexistent Event", LocalDate.now());

        interactor.execute(input);

        assertEquals(0, todaysDao.getTodaysEvents().size());
    }
}
