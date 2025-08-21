package view.feedback_panel.feedback_entry;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.json.JSONArray;
import org.json.JSONObject;

import constants.Constants;

public class CorrelationPanel extends JPanel {
    public CorrelationPanel(String correlationJson) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(Constants.EIGHT, Constants.EIGHT, Constants.EIGHT, Constants.EIGHT));

        if (correlationJson == null || correlationJson.isBlank()) {
            add(new JLabel("No correlation data available."), BorderLayout.CENTER);
            return;
        }

        try {
            final JSONObject obj = new JSONObject(correlationJson);

            // Parse effect_summary -> table
            final String[] columns = {"Variable", "Direction", "Confidence"};
            final JSONArray effects = obj.getJSONArray("effect_summary");
            final Object[][] data = new Object[effects.length()][Constants.THREE];
            for (int i = 0; i < effects.length(); i++) {
                final JSONObject eff = effects.getJSONObject(i);
                data[i][0] = eff.getString("variable");
                data[i][1] = eff.getString("direction");
                data[i][2] = String.format("%.0f%%", eff.getDouble("confidence") * Constants.HUNDRED);
            }

            final JTable table = new JTable(data, columns);
            table.setEnabled(false);

            // --- Styling: bigger font & centered cells ---
            final Font tableFont = table.getFont().deriveFont(Constants.F14);
            table.setFont(tableFont);
            table.setRowHeight(Constants.TWENTY_FOUR);

            // Center cell renderer
            final DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(SwingConstants.CENTER);
            for (int c = 0; c < table.getColumnCount(); c++) {
                table.getColumnModel().getColumn(c).setCellRenderer(center);
            }

            // Header: centered & bold
            final DefaultTableCellRenderer hdr =
                    (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            hdr.setHorizontalAlignment(SwingConstants.CENTER);
            table.getTableHeader().setFont(tableFont.deriveFont(Font.BOLD));

            // Compact scroll area for the table
            final JScrollPane tableScroll = new JScrollPane(table);
            final int visibleRows = Math.min(table.getRowCount(), 4);
            final int prefHeight = table.getTableHeader().getPreferredSize().height
                    + (visibleRows * table.getRowHeight()) + 8;
            tableScroll.setPreferredSize(new Dimension(Constants.FOUR_HUNDRED, prefHeight));

            // Just table — no notes
            final JPanel centre = new JPanel(new BorderLayout());
            centre.add(tableScroll, BorderLayout.NORTH);

            add(centre, BorderLayout.CENTER);

        }
        catch (Exception exception) {
            add(new JLabel("Invalid correlation data."), BorderLayout.CENTER);
        }
    }
}
