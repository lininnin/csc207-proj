package view.feedback_entry;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CorrelationPanel extends JPanel {
    public CorrelationPanel(String correlationJson) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(8, 8, 8, 8));

        if (correlationJson == null || correlationJson.isBlank()) {
            add(new JLabel("No correlation data available."), BorderLayout.CENTER);
            return;
        }

        try {
            JSONObject obj = new JSONObject(correlationJson);

            // --- Parse effect_summary ---
            String[] columns = {"Variable", "Direction", "Confidence"};
            JSONArray effects = obj.getJSONArray("effect_summary");
            Object[][] data = new Object[effects.length()][3];
            for (int i = 0; i < effects.length(); i++) {
                JSONObject eff = effects.getJSONObject(i);
                data[i][0] = eff.getString("variable");
                data[i][1] = eff.getString("direction");
                data[i][2] = String.format("%.0f%%", eff.getDouble("confidence") * 100);
            }
            JTable table = new JTable(data, columns);
            table.setEnabled(false);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // --- Parse notes (optional) ---
            String notes = obj.optString("notes", "");
            if (!notes.isEmpty()) {
                JLabel notesLabel = new JLabel("Notes: " + notes);
                notesLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
                add(notesLabel, BorderLayout.SOUTH);
            }
        } catch (Exception e) {
            add(new JLabel("Invalid correlation data."), BorderLayout.CENTER);
        }
    }
}
