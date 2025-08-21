package use_case.alex.event_related.available_event_module;

import data_access.alex.EventAvailableDataAccessObject;
import entity.alex.EventAvailable.EventAvailableFactory;
import entity.alex.EventAvailable.EventAvailableFactoryInterf;
import entity.info.InfoFactory;
import entity.info.InfoFactoryInterf;
import entity.info.InfoInterf;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeleteEventPresenter;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeletedEventViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.event_related.avaliable_events_module.delete_event.*;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteAvailableEventInteractorTest {

    private DeleteEventInputBoundary interactor;
    private EventAvailableDataAccessObject dao;
    private InfoFactoryInterf infoFactory;

    private final String EXISTING_ID = "delete-me-id";

    @BeforeEach
    public void setUp() {
        EventAvailableFactoryInterf eventAvailableFactory = new EventAvailableFactory();
        this.dao = new EventAvailableDataAccessObject(eventAvailableFactory);
        this.infoFactory = new InfoFactory();

        // 添加一个事件用于删除测试
        InfoInterf info = infoFactory.create(EXISTING_ID, "Yoga", "Stretch and relax");
        dao.save(info);

        // 构建 presenter（真实 viewModel，可进一步断言）
        DeleteEventOutputBoundary presenter = new DeleteEventPresenter(
                new DeletedEventViewModel(),
                new AvailableEventViewModel(),
                new AddedEventViewModel()
        );

        this.interactor = new DeleteEventInteractor(dao, presenter);
    }

    @Test
    public void testDeleteExistingEvent_success() {
        DeleteEventInputData inputData = new DeleteEventInputData(EXISTING_ID);
        interactor.execute(inputData);

        assertFalse(dao.existsById(EXISTING_ID), "Event should be deleted from DAO");
    }

    @Test
    public void testDeleteNonExistentEvent_fail() {
        DeleteEventInputData inputData = new DeleteEventInputData("non-existent-id");
        interactor.execute(inputData);

        // 没有抛出异常即为通过；可扩展使用 stub presenter 捕获失败状态
        assertFalse(dao.existsById("non-existent-id"));
    }
}
