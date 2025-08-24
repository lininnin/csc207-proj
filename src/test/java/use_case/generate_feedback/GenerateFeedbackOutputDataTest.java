package use_case.generate_feedback;

import entity.feedback_entry.FeedbackEntryInterf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenerateFeedbackOutputDataTest {

    @Test
    void testConstructorAndGetter() {
        // Arrange
        FeedbackEntryInterf mockEntry = mock(FeedbackEntryInterf.class);

        // Act
        GenerateFeedbackOutputData outputData = new GenerateFeedbackOutputData(mockEntry);

        // Assert
        assertSame(mockEntry, outputData.getFeedbackEntry());
    }

    @Test
    void testConstructorWithNull() {
        // Act
        GenerateFeedbackOutputData outputData = new GenerateFeedbackOutputData(null);

        // Assert
        assertNull(outputData.getFeedbackEntry());
    }

    @Test
    void testMultipleInstances() {
        // Arrange
        FeedbackEntryInterf entry1 = mock(FeedbackEntryInterf.class);
        FeedbackEntryInterf entry2 = mock(FeedbackEntryInterf.class);

        // Act
        GenerateFeedbackOutputData outputData1 = new GenerateFeedbackOutputData(entry1);
        GenerateFeedbackOutputData outputData2 = new GenerateFeedbackOutputData(entry2);

        // Assert
        assertSame(entry1, outputData1.getFeedbackEntry());
        assertSame(entry2, outputData2.getFeedbackEntry());
        assertNotEquals(outputData1.getFeedbackEntry(), outputData2.getFeedbackEntry());
    }

    @Test
    void testImmutability() {
        // Arrange
        FeedbackEntryInterf mockEntry = mock(FeedbackEntryInterf.class);
        GenerateFeedbackOutputData outputData = new GenerateFeedbackOutputData(mockEntry);

        // Act & Assert
        // Calling getter multiple times should return the same reference
        FeedbackEntryInterf retrieved1 = outputData.getFeedbackEntry();
        FeedbackEntryInterf retrieved2 = outputData.getFeedbackEntry();

        assertSame(retrieved1, retrieved2);
        assertSame(mockEntry, retrieved1);
    }
}