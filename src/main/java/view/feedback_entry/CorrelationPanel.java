package view.feedback_entry;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class CorrelationPanel extends JPanel {

    public CorrelationPanel(String correlationJson) {
        setLayout(new BorderLayout());

        JSONObject json = new JSONObject(correlationJson);

        // Bayesian
        String[] columnNames = {
                "Wellness Factor",
                "Mean Corr.",
                "95% CI Lower",
                "95% CI Upper",
                "P(Effect>0)"
        };
        JSONArray factors = json.getJSONArray("factors");
        Object[][] data = new Object[factors.length()][5];

        for (int i = 0; i < factors.length(); i++) {
            JSONObject f = factors.getJSONObject(i);
            String name = f.getString("name");
            double corr = f.getDouble("correlation");
            double lower = f.optDouble("lower", Double.NaN);
            double upper = f.optDouble("upper", Double.NaN);
            double probGtZero = f.optDouble("prob_gt_zero", Double.NaN);

            data[i][0] = name;
            data[i][1] = String.format("%.2f", corr);
            data[i][2] = Double.isNaN(lower) ? "—" : String.format("%.2f", lower);
            data[i][3] = Double.isNaN(upper) ? "—" : String.format("%.2f", upper);
            data[i][4] = Double.isNaN(probGtZero) ? "—" : String.format("%.2f", probGtZero);
        }

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);

        add(new JLabel("Bayesian Analysis of Wellness Factors vs. Completion Rate:"), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        if (json.has("note")) {
            JLabel note = new JLabel(json.getString("note"));
            note.setFont(note.getFont().deriveFont(Font.ITALIC, 11f));
            add(note, BorderLayout.SOUTH);
        }
    }
}
