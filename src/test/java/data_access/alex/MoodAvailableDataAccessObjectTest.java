package data_access.alex;

import entity.alex.AvalibleMoodLabel.AvaliableMoodLabelInterf;
import entity.alex.AvalibleMoodLabel.AvaliableMoodlabelFactoryInterf;
import entity.alex.MoodLabel.MoodLabelFactoryInterf;
import entity.alex.MoodLabel.MoodLabelInterf;
import entity.alex.MoodLabel.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MoodAvailableDataAccessObjectTest {

    private MoodAvailableDataAccessObject dao;
    private AvaliableMoodLabelInterf mockLabelStorage;
    private MoodLabelFactoryInterf mockLabelFactory;

    private MoodLabelInterf labelHappy;
    private MoodLabelInterf labelSad;

    @BeforeEach
    public void setup() {
        mockLabelStorage = mock(AvaliableMoodLabelInterf.class);
        mockLabelFactory = mock(MoodLabelFactoryInterf.class);

        AvaliableMoodlabelFactoryInterf mockStorageFactory = mock(AvaliableMoodlabelFactoryInterf.class);
        when(mockStorageFactory.create()).thenReturn(mockLabelStorage);

        // 创建两个默认标签
        labelHappy = mock(MoodLabelInterf.class);
        when(labelHappy.getName()).thenReturn("Happy");

        labelSad = mock(MoodLabelInterf.class);
        when(labelSad.getName()).thenReturn("Sad");

        // 模拟工厂行为
        when(mockLabelFactory.create("Happy", Type.Positive)).thenReturn(labelHappy);
        when(mockLabelFactory.create("Calm", Type.Positive)).thenReturn(mock(MoodLabelInterf.class));
        when(mockLabelFactory.create("Anxious", Type.Negative)).thenReturn(mock(MoodLabelInterf.class));
        when(mockLabelFactory.create("Stressed", Type.Negative)).thenReturn(mock(MoodLabelInterf.class));

        // 默认返回空标签集合以避免空指针
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(new ArrayList<>());
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(new ArrayList<>());

        dao = new MoodAvailableDataAccessObject(mockStorageFactory, mockLabelFactory);
    }

    @Test
    public void testSaveSuccess() {
        MoodLabelInterf newLabel = mock(MoodLabelInterf.class);
        when(newLabel.getName()).thenReturn("Joyful");

        dao.save(newLabel);

        verify(mockLabelStorage).addLabel(newLabel);
    }

    @Test
    public void testSaveThrowsIfDuplicate() {
        MoodLabelInterf duplicate = mock(MoodLabelInterf.class);
        when(duplicate.getName()).thenReturn("Happy");

        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(labelHappy));

        assertThrows(IllegalArgumentException.class, () -> dao.save(duplicate));
    }

    @Test
    public void testGetAllLabels() {
        MoodLabelInterf p1 = mock(MoodLabelInterf.class);
        MoodLabelInterf n1 = mock(MoodLabelInterf.class);

        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(p1));
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(List.of(n1));

        List<MoodLabelInterf> result = dao.getAllLabels();
        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(n1));
    }

    @Test
    public void testRemoveByObjectDelegatesToRemoveByName() {
        MoodLabelInterf label = mock(MoodLabelInterf.class);
        when(label.getName()).thenReturn("Anxious");

        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of());
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(List.of());

        boolean result = dao.remove(label);
        assertFalse(result);

        verify(mockLabelStorage).removeLabelByName("Anxious");
    }

    @Test
    public void testContainsTrueWhenExists() {
        when(labelHappy.getName()).thenReturn("Happy");
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(labelHappy));

        assertTrue(dao.contains("Happy"));
    }

    @Test
    public void testContainsFalseWhenNotExists() {
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of());
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(List.of());

        assertFalse(dao.contains("Unknown"));
    }

    @Test
    public void testContainsByObjectDelegatesToName() {
        MoodLabelInterf label = mock(MoodLabelInterf.class);
        when(label.getName()).thenReturn("Happy");

        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(labelHappy));

        assertTrue(dao.contains(label));
    }

    @Test
    public void testGetByNameFound() {
        when(labelHappy.getName()).thenReturn("Happy");
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(labelHappy));
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(List.of());

        MoodLabelInterf result = dao.getByName("Happy");
        assertEquals(labelHappy, result);
    }

    @Test
    public void testGetByNameNotFound() {
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of());
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(List.of());

        assertNull(dao.getByName("Unknown"));
    }

    @Test
    public void testUpdateSuccess() {
        MoodLabelInterf updated = mock(MoodLabelInterf.class);
        when(updated.getName()).thenReturn("Happy");

        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(labelHappy));

        boolean result = dao.update(updated);
        assertTrue(result);
        verify(mockLabelStorage).removeLabelByName("Happy");
        verify(mockLabelStorage).addLabel(updated);
    }

    @Test
    public void testUpdateFailIfNotExists() {
        MoodLabelInterf updated = mock(MoodLabelInterf.class);
        when(updated.getName()).thenReturn("Unknown");

        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of());
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(List.of());

        assertFalse(dao.update(updated));
    }

    @Test
    public void testExistsByNameDelegatesToContains() {
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(labelHappy));
        assertTrue(dao.existsByName("Happy"));
    }

    @Test
    public void testRemoveByNameReturnsTrueIfExists() {
        when(labelHappy.getName()).thenReturn("Happy");
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of(labelHappy));

        boolean result = dao.removeByName("Happy");
        assertTrue(result);
        verify(mockLabelStorage).removeLabelByName("Happy");
    }

    @Test
    public void testRemoveByNameReturnsFalseIfNotExists() {
        when(mockLabelStorage.getPositiveLabelObjects()).thenReturn(List.of());
        when(mockLabelStorage.getNegativeLabelObjects()).thenReturn(List.of());

        boolean result = dao.removeByName("NotExist");
        assertFalse(result);
        verify(mockLabelStorage).removeLabelByName("NotExist");
    }

    @Test
    public void testClearAll() {
        dao.clearAll();
        verify(mockLabelStorage).clear();
    }

    @Test
    public void testGetCategorizedReturnsStorage() {
        assertEquals(mockLabelStorage, dao.getCategorized());
    }
}

