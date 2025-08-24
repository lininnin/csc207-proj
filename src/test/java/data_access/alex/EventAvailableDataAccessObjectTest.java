package data_access.alex;

import entity.alex.EventAvailable.EventAvailableFactoryInterf;
import entity.alex.EventAvailable.EventAvailableInterf;
import entity.info.Info;
import entity.info.InfoFactoryInterf;
import entity.info.InfoInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventAvailableDataAccessObjectTest {

    private EventAvailableInterf mockEventAvailable;
    private EventAvailableDataAccessObject dao;
    private InfoInterf mockInfo;

    @BeforeEach
    public void setup() {
        mockEventAvailable = mock(EventAvailableInterf.class);
        EventAvailableFactoryInterf factory = mock(EventAvailableFactoryInterf.class);
        when(factory.create()).thenReturn(mockEventAvailable);

        dao = new EventAvailableDataAccessObject(factory);
        mockInfo = mock(InfoInterf.class);
    }

    @Test
    public void testSaveDelegatesToAddEvent() {
        dao.save(mockInfo);
        verify(mockEventAvailable).addEvent(mockInfo);
    }

    @Test
    public void testRemoveDelegatesToRemoveEvent() {
        when(mockEventAvailable.removeEvent(mockInfo)).thenReturn(true);
        assertTrue(dao.remove(mockInfo));
        verify(mockEventAvailable).removeEvent(mockInfo);
    }

    @Test
    public void testGetAllEvents() {
        List<InfoInterf> list = new ArrayList<>();
        list.add(mockInfo);
        when(mockEventAvailable.getEventAvailable()).thenReturn(list);

        List<InfoInterf> result = dao.getAllEvents();
        assertEquals(1, result.size());
        assertSame(mockInfo, result.get(0));
    }

    @Test
    public void testGetEventByIdFound() {
        when(mockInfo.getId()).thenReturn("123");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(mockInfo));

        InfoInterf result = dao.getEventById("123");
        assertEquals(mockInfo, result);
    }

    @Test
    public void testGetEventByIdNotFound() {
        when(mockInfo.getId()).thenReturn("456");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(mockInfo));

        InfoInterf result = dao.getEventById("123");
        assertNull(result);
    }

    @Test
    public void testContainsTrue() {
        when(mockInfo.getId()).thenReturn("abc");
        when(mockEventAvailable.contains(mockInfo)).thenReturn(true);

        assertTrue(dao.contains(mockInfo));
    }

    @Test
    public void testUpdateSuccess() {
        InfoInterf stored = mock(InfoInterf.class);
        when(stored.getId()).thenReturn("id123");

        InfoInterf updated = mock(InfoInterf.class);
        when(updated.getId()).thenReturn("id123");
        when(updated.getName()).thenReturn("new name");
        when(updated.getCategory()).thenReturn("new category");
        when(updated.getDescription()).thenReturn("new description");

        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(stored));

        boolean result = dao.update(updated);
        assertTrue(result);

        verify(stored).setName("new name");
        verify(stored).setCategory("new category");
        verify(stored).setDescription("new description");
    }

    @Test
    public void testUpdateFail() {
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of());

        InfoInterf updated = mock(InfoInterf.class);
        when(updated.getId()).thenReturn("not_found");

        boolean result = dao.update(updated);
        assertFalse(result);
    }

    @Test
    public void testFindInfoByName() {
        InfoInterf stored = mock(InfoInterf.class);
        when(stored.getName()).thenReturn("target");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(stored));

        InfoInterf result = dao.findInfoByName("target");
        assertEquals(stored, result);
    }

    @Test
    public void testFindInfoByNameNotFound() {
        InfoInterf stored = mock(InfoInterf.class);
        when(stored.getName()).thenReturn("other");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(stored));

        InfoInterf result = dao.findInfoByName("target");
        assertNull(result);
    }

    @Test
    public void testClearAvailableEventCategorySuccess() {
        InfoInterf stored = mock(InfoInterf.class);
        when(stored.getId()).thenReturn("e1");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(stored));

        boolean result = dao.clearAvailableEventCategory("e1");
        assertTrue(result);
        verify(stored).setCategory(null);
    }

    @Test
    public void testClearAvailableEventCategoryFail() {
        InfoInterf stored = mock(InfoInterf.class);
        when(stored.getId()).thenReturn("e2");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(stored));

        boolean result = dao.clearAvailableEventCategory("wrongId");
        assertFalse(result);
    }

    @Test
    public void testFindAvailableEventsByCategoryCastsCorrectly() {
        // 使用真正的工厂来创建 Info 对象
        InfoFactoryInterf factory = (name, description, category) ->
                new Info.Builder(name).description(description).category(category).build();

        InfoInterf event = factory.create("event name", "desc", "cat");

        // 将它放入 DAO 的底层 mock 存储中
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(event));

        List<Info> result = dao.findAvailableEventsByCategory("cat");

        assertEquals(1, result.size());
        assertEquals("cat", result.get(0).getCategory());
        assertEquals("event name", result.get(0).getName());
        assertEquals("desc", result.get(0).getDescription());
    }


    @Test
    public void testClearAllCallsUnderlying() {
        dao.clearAll();
        verify(mockEventAvailable).clearAll();
    }
    @Test
    public void testGetEventsByCategory() {
        List<InfoInterf> mockList = List.of(mockInfo);
        when(mockEventAvailable.getEventsByCategory("work")).thenReturn(mockList);

        List<InfoInterf> result = dao.getEventsByCategory("work");
        assertEquals(mockList, result);
    }

    @Test
    public void testGetEventsByName() {
        List<InfoInterf> mockList = List.of(mockInfo);
        when(mockEventAvailable.getEventsByName("event")).thenReturn(mockList);

        List<InfoInterf> result = dao.getEventsByName("event");
        assertEquals(mockList, result);
    }

    @Test
    public void testGetEventCount() {
        when(mockEventAvailable.getEventCount()).thenReturn(5);
        assertEquals(5, dao.getEventCount());
    }

    @Test
    public void testExistsByIdTrue() {
        when(mockInfo.getId()).thenReturn("123");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(mockInfo));

        assertTrue(dao.existsById("123"));
    }

    @Test
    public void testExistsByIdFalse() {
        when(mockInfo.getId()).thenReturn("456");
        when(mockEventAvailable.getEventAvailable()).thenReturn(List.of(mockInfo));

        assertFalse(dao.existsById("123"));
    }

    @Test
    public void testFindTodaysEventsByCategoryReturnsEmptyList() {
        List<Info> result = dao.findTodaysEventsByCategory("any");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testClearTodaysEventCategoryReturnsFalse() {
        boolean result = dao.clearTodaysEventCategory("any");
        assertFalse(result);
    }

}

