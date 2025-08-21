package data_access.alex;

import entity.alex.DailyWellnessLog.DailyWellnessLogFactoryInterf;
import entity.alex.DailyWellnessLog.DailyWellnessLogInterf;
import entity.alex.WellnessLogEntry.WellnessLogEntryInterf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TodaysWellnessLogDataAccessObjectTest {

    private TodaysWellnessLogDataAccessObject dao;
    private DailyWellnessLogInterf mockLog;
    private WellnessLogEntryInterf entry;

    @BeforeEach
    public void setup() {
        mockLog = mock(DailyWellnessLogInterf.class);
        DailyWellnessLogFactoryInterf factory = mock(DailyWellnessLogFactoryInterf.class);

        when(factory.create(any(LocalDate.class))).thenReturn(mockLog);
        when(mockLog.getDate()).thenReturn(LocalDate.now());

        dao = new TodaysWellnessLogDataAccessObject(factory);

        entry = mock(WellnessLogEntryInterf.class);
        when(entry.getId()).thenReturn("123");
        when(entry.getTime()).thenReturn(LocalDateTime.now());
    }

    @Test
    public void testSaveSuccess() {
        dao.save(entry);
        verify(mockLog).addEntry(entry);
    }

    @Test
    public void testSaveThrowsIfWrongDate() {
        when(entry.getTime()).thenReturn(LocalDateTime.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> dao.save(entry));
    }

    @Test
    public void testRemoveSuccess() {
        List<WellnessLogEntryInterf> before = List.of(entry);
        List<WellnessLogEntryInterf> after = List.of();

        when(mockLog.getEntries()).thenReturn(before).thenReturn(after);
        boolean result = dao.remove(entry);

        assertTrue(result);
        verify(mockLog).removeEntry("123");
    }

    @Test
    public void testRemoveFailure() {
        List<WellnessLogEntryInterf> unchanged = List.of(entry);
        when(mockLog.getEntries()).thenReturn(unchanged).thenReturn(unchanged);
        boolean result = dao.remove(entry);

        assertFalse(result);
    }

    @Test
    public void testDeleteByIdSuccess() {
        when(mockLog.getEntries()).thenReturn(List.of(entry)).thenReturn(List.of());
        boolean result = dao.deleteById("123");

        assertTrue(result);
        verify(mockLog).removeEntry("123");
    }

    @Test
    public void testDeleteByIdFail() {
        when(mockLog.getEntries()).thenReturn(List.of(entry));
        boolean result = dao.deleteById("unknown");

        assertFalse(result);
    }

    @Test
    public void testGetTodaysWellnessLogEntriesReturnsCopy() {
        List<WellnessLogEntryInterf> list = new ArrayList<>();
        list.add(entry);
        when(mockLog.getEntries()).thenReturn(list);

        List<WellnessLogEntryInterf> result = dao.getTodaysWellnessLogEntries();
        assertEquals(1, result.size());
        assertNotSame(list, result); // ensure it's a copy
    }

    @Test
    public void testContainsTrue() {
        when(mockLog.getEntries()).thenReturn(List.of(entry));
        WellnessLogEntryInterf e2 = mock(WellnessLogEntryInterf.class);
        when(e2.getId()).thenReturn("123");

        assertTrue(dao.contains(e2));
    }

    @Test
    public void testContainsFalse() {
        when(mockLog.getEntries()).thenReturn(List.of(entry));
        WellnessLogEntryInterf e2 = mock(WellnessLogEntryInterf.class);
        when(e2.getId()).thenReturn("999");

        assertFalse(dao.contains(e2));
    }

    @Test
    public void testClearAllCreatesNewLog() {
        DailyWellnessLogInterf newLog = mock(DailyWellnessLogInterf.class);
        DailyWellnessLogFactoryInterf factory = mock(DailyWellnessLogFactoryInterf.class);
        when(factory.create(any(LocalDate.class))).thenReturn(newLog);

        TodaysWellnessLogDataAccessObject newDao = new TodaysWellnessLogDataAccessObject(factory);
        newDao.clearAll();

        assertNotNull(newDao.getDailyLog());
    }

    @Test
    public void testGetByIdFound() {
        when(mockLog.getEntries()).thenReturn(List.of(entry));
        assertEquals(entry, dao.getById("123"));
    }

    @Test
    public void testGetByIdNotFound() {
        when(mockLog.getEntries()).thenReturn(List.of(entry));
        assertNull(dao.getById("xyz"));
    }

    @Test
    public void testUpdateSuccess() {
        when(entry.getTime()).thenReturn(LocalDateTime.now());

        when(mockLog.getEntries()).thenReturn(List.of(entry)).thenReturn(List.of());
        boolean result = dao.update(entry);

        assertTrue(result);
        verify(mockLog).removeEntry("123");
        verify(mockLog).addEntry(entry);
    }

    @Test
    public void testUpdateFailsIfNotFound() {
        when(mockLog.getEntries()).thenReturn(List.of(entry));
        WellnessLogEntryInterf other = mock(WellnessLogEntryInterf.class);
        when(other.getId()).thenReturn("404");
        when(other.getTime()).thenReturn(LocalDateTime.now());

        boolean result = dao.update(other);
        assertFalse(result);
    }

    @Test
    public void testGetDailyLogReturnsInternalLog() {
        assertEquals(mockLog, dao.getDailyLog());
    }
}

