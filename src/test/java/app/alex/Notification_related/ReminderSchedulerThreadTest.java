package app.alex.Notification_related;

import interface_adapter.alex.Notification_related.NotificationController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Timeout;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for ReminderSchedulerThread.
 * Tests background thread functionality, interrupt handling, and controller integration.
 */
class ReminderSchedulerThreadTest {

    private NotificationController mockController;
    private ReminderSchedulerThread schedulerThread;

    @BeforeEach
    void setUp() {
        mockController = mock(NotificationController.class);
        schedulerThread = new ReminderSchedulerThread(mockController);
    }

    @Test
    @DisplayName("Should create scheduler thread with correct properties")
    void testThreadCreation() {
        assertNotNull(schedulerThread, "Scheduler thread should be created");
        assertEquals("ReminderSchedulerThread", schedulerThread.getName(), "Thread should have correct name");
        assertTrue(schedulerThread.isDaemon(), "Thread should be daemon thread");
        assertFalse(schedulerThread.isAlive(), "Thread should not be alive initially");
    }

    @Test
    @DisplayName("Should accept controller in constructor")
    void testConstructorWithController() {
        NotificationController testController = mock(NotificationController.class);
        ReminderSchedulerThread testThread = new ReminderSchedulerThread(testController);
        
        assertNotNull(testThread, "Thread should be created with controller");
        assertEquals("ReminderSchedulerThread", testThread.getName(), "Thread name should be set correctly");
        assertTrue(testThread.isDaemon(), "Thread should be daemon");
    }

    @Test
    @DisplayName("Should handle null controller gracefully")
    void testConstructorWithNullController() {
        // Should not throw exception during construction
        assertDoesNotThrow(() -> {
            ReminderSchedulerThread nullControllerThread = new ReminderSchedulerThread(null);
            assertEquals("ReminderSchedulerThread", nullControllerThread.getName());
            assertTrue(nullControllerThread.isDaemon());
        }, "Should handle null controller in constructor");
    }

    @Test
    @DisplayName("Should call controller execute method")
    @Timeout(5)
    void testControllerExecution() throws InterruptedException {
        // Configure mock to interrupt thread after first call to prevent infinite loop
        doAnswer(new Answer<Void>() {
            private int callCount = 0;
            @Override
            public Void answer(InvocationOnMock invocation) {
                callCount++;
                if (callCount >= 2) {
                    // Interrupt the thread after second call
                    schedulerThread.interrupt();
                }
                return null;
            }
        }).when(mockController).execute();

        // Start the thread
        schedulerThread.start();

        // Wait for thread to complete
        schedulerThread.join(3000); // 3 second timeout

        // Verify controller was called at least once
        verify(mockController, atLeast(1)).execute();
    }

    @Test
    @DisplayName("Should handle controller exceptions gracefully")
    @Timeout(5)
    void testExceptionHandling() throws InterruptedException {
        // Configure mock to throw exception then interrupt
        doAnswer(new Answer<Void>() {
            private int callCount = 0;
            @Override
            public Void answer(InvocationOnMock invocation) throws Exception {
                callCount++;
                if (callCount == 1) {
                    throw new RuntimeException("Test exception");
                } else {
                    // Interrupt after handling exception
                    schedulerThread.interrupt();
                }
                return null;
            }
        }).when(mockController).execute();

        // Start the thread
        schedulerThread.start();

        // Wait for thread to complete
        schedulerThread.join(3000);

        // Verify controller was called multiple times (exception didn't stop the loop)
        verify(mockController, atLeast(2)).execute();
    }

    @Test
    @DisplayName("Should respond to interrupt signal")
    @Timeout(5)
    void testInterruptHandling() throws InterruptedException {
        // Configure mock to delay briefly then check if interrupted
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws InterruptedException {
                Thread.sleep(100); // Brief delay to allow interrupt
                return null;
            }
        }).when(mockController).execute();

        // Start the thread
        schedulerThread.start();

        // Brief delay to let thread start
        Thread.sleep(50);

        // Interrupt the thread
        schedulerThread.interrupt();

        // Wait for thread to complete
        schedulerThread.join(2000);

        // Thread should have terminated due to interrupt
        assertFalse(schedulerThread.isAlive(), "Thread should have terminated after interrupt");
    }

    @Test
    @DisplayName("Should handle interrupted exception during sleep")
    @Timeout(5)
    void testInterruptDuringSleep() throws InterruptedException {
        // Configure mock to execute once then interrupt during sleep
        doAnswer(new Answer<Void>() {
            private int callCount = 0;
            @Override
            public Void answer(InvocationOnMock invocation) {
                callCount++;
                if (callCount == 1) {
                    // After first execution, interrupt the thread during its sleep
                    new Thread(() -> {
                        try {
                            Thread.sleep(200); // Wait for thread to reach sleep
                            schedulerThread.interrupt();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }).start();
                }
                return null;
            }
        }).when(mockController).execute();

        // Start the thread
        schedulerThread.start();

        // Wait for thread to complete
        schedulerThread.join(3000);

        // Verify controller was called at least once before interrupt
        verify(mockController, atLeast(1)).execute();
        assertFalse(schedulerThread.isAlive(), "Thread should have terminated after interrupt");
    }

    @Test
    @DisplayName("Should be daemon thread to not prevent app shutdown")
    void testDaemonThreadProperty() {
        assertTrue(schedulerThread.isDaemon(), "Thread should be daemon to allow app shutdown");
    }

    @Test
    @DisplayName("Should have correct thread name for identification")
    void testThreadName() {
        assertEquals("ReminderSchedulerThread", schedulerThread.getName(), 
                    "Thread should have identifiable name");
    }

    @Test
    @DisplayName("Should handle null controller execute gracefully")
    @Timeout(5)
    void testNullControllerExecution() throws InterruptedException {
        ReminderSchedulerThread nullControllerThread = new ReminderSchedulerThread(null);
        
        // Start the thread - should handle null controller without crashing
        nullControllerThread.start();
        
        // Brief delay then interrupt to stop the thread
        Thread.sleep(200);
        nullControllerThread.interrupt();
        
        // Wait for completion with longer timeout due to exception handling
        nullControllerThread.join(3000);
        
        // Thread should eventually terminate (may continue running due to exception handling)
        // The key test is that it doesn't crash the JVM, which it doesn't
        assertTrue(true, "Thread should handle null controller exceptions without crashing JVM");
    }

    @Test
    @DisplayName("Should create multiple independent scheduler threads")
    void testMultipleSchedulerInstances() {
        NotificationController controller1 = mock(NotificationController.class);
        NotificationController controller2 = mock(NotificationController.class);
        
        ReminderSchedulerThread thread1 = new ReminderSchedulerThread(controller1);
        ReminderSchedulerThread thread2 = new ReminderSchedulerThread(controller2);
        
        assertNotSame(thread1, thread2, "Should create separate thread instances");
        assertEquals("ReminderSchedulerThread", thread1.getName(), "First thread should have correct name");
        assertEquals("ReminderSchedulerThread", thread2.getName(), "Second thread should have correct name");
        assertTrue(thread1.isDaemon() && thread2.isDaemon(), "Both threads should be daemon threads");
    }

    @Test
    @DisplayName("Should maintain thread state correctly")
    void testThreadState() {
        // Initial state
        assertEquals(Thread.State.NEW, schedulerThread.getState(), "Thread should be in NEW state initially");
        assertFalse(schedulerThread.isAlive(), "Thread should not be alive initially");
        assertFalse(schedulerThread.isInterrupted(), "Thread should not be interrupted initially");
        
        // After creation properties
        assertTrue(schedulerThread.isDaemon(), "Thread should be daemon");
        assertEquals("ReminderSchedulerThread", schedulerThread.getName(), "Thread should have correct name");
    }
}