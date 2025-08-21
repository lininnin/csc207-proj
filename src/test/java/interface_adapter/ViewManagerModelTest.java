package interface_adapter;

import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ViewManagerModelTest {

    private static final class CapturingListener implements PropertyChangeListener {
        final List<PropertyChangeEvent> events = new ArrayList<>();
        @Override public void propertyChange(PropertyChangeEvent evt) { events.add(evt); }
    }

    @Test
    void initial_activeView_isNull() {
        ViewManagerModel vm = new ViewManagerModel();
        assertNull(vm.getActiveView());
    }

    @Test
    void setActiveView_firesViewEvent_withOldAndNew() {
        ViewManagerModel vm = new ViewManagerModel();
        CapturingListener l = new CapturingListener();
        vm.addPropertyChangeListener(l);

        vm.setActiveView("Home");

        assertEquals("Home", vm.getActiveView());
        assertEquals(1, l.events.size());

        PropertyChangeEvent e = l.events.get(0);
        assertEquals("view", e.getPropertyName());
        assertNull(e.getOldValue());           // was null initially
        assertEquals("Home", e.getNewValue()); // now "Home"
        assertSame(vm, e.getSource());
    }

    @Test
    void setActiveView_again_updatesOldAndNewCorrectly() {
        ViewManagerModel vm = new ViewManagerModel();
        CapturingListener l = new CapturingListener();
        vm.addPropertyChangeListener(l);

        vm.setActiveView("Home");
        vm.setActiveView("Settings");

        assertEquals("Settings", vm.getActiveView());
        assertEquals(2, l.events.size());

        PropertyChangeEvent e2 = l.events.get(1);
        assertEquals("view", e2.getPropertyName());
        assertEquals("Home", e2.getOldValue());
        assertEquals("Settings", e2.getNewValue());
    }

    @Test
    void firePropertyChanged_emitsStateEvent_withSelfAsNewValue() {
        ViewManagerModel vm = new ViewManagerModel();
        CapturingListener l = new CapturingListener();
        vm.addPropertyChangeListener(l);

        vm.firePropertyChanged();

        assertEquals(1, l.events.size());
        PropertyChangeEvent e = l.events.get(0);
        assertEquals("state", e.getPropertyName());
        assertNull(e.getOldValue());
        assertSame(vm, e.getNewValue());
    }

    @Test
    void removeListener_stopsReceivingEvents() {
        ViewManagerModel vm = new ViewManagerModel();
        CapturingListener l = new CapturingListener();
        vm.addPropertyChangeListener(l);

        vm.setActiveView("A");
        assertEquals(1, l.events.size());

        vm.removePropertyChangeListener(l);
        l.events.clear();

        vm.setActiveView("B");
        vm.firePropertyChanged();

        assertTrue(l.events.isEmpty(), "No events should be captured after removal");
    }
}
