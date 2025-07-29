package view.ina;

import entity.Ina.FeedbackEntry;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles the “View” button in FeedbackHistoryPanel.
 * Adds a FeedbackEntryPanel if not present and updates ViewManagerModel to switch.
 */
public class FeedbackHistoryController implements FeedbackHistoryPanel.Viewer {

    private final JPanel views;
    private final ViewManagerModel viewManagerModel;
    private final Set<String> registeredCards = new HashSet<>();

    public FeedbackHistoryController(
            JPanel views,
            ViewManagerModel viewManagerModel
    ) {
        this.views = views;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void show(FeedbackEntry entry) {
        String cardName = "entry_" + entry.getDate();

        if (registeredCards.add(cardName)) {
            views.add(new FeedbackEntryPanel(entry), cardName);
        }
        viewManagerModel.setState(cardName);
    }
}
