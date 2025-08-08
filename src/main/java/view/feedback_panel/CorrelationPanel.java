package view.feedback_panel;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
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

            // Parse effect_summary -> table
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

            // --- Styling: bigger font & centered cells ---
            Font tableFont = table.getFont().deriveFont(14f);
            table.setFont(tableFont);
            table.setRowHeight(24);

            // Center cell renderer
            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(SwingConstants.CENTER);
            for (int c = 0; c < table.getColumnCount(); c++) {
                table.getColumnModel().getColumn(c).setCellRenderer(center);
            }

            // Header: centered & bold
            DefaultTableCellRenderer hdr =
                    (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
            hdr.setHorizontalAlignment(SwingConstants.CENTER);
            table.getTableHeader().setFont(tableFont.deriveFont(Font.BOLD));

            // Compact scroll area for the table
            JScrollPane tableScroll = new JScrollPane(table);
            int visibleRows = Math.min(table.getRowCount(), 4); // show up to 4 rows without growing huge
            int prefHeight = table.getTableHeader().getPreferredSize().height
                    + (visibleRows * table.getRowHeight()) + 8;
            tableScroll.setPreferredSize(new Dimension(400, prefHeight));

            // Center container: table on top, notes right below it
            JPanel centre = new JPanel(new BorderLayout());
            centre.add(tableScroll, BorderLayout.NORTH); // keep it compact; doesn't stretch
            // (use NORTH so it keeps its preferred height instead of expanding)

            // Notes directly below the table
            String notes = obj.optString("notes", "");
            if (!notes.isEmpty()) {
                JTextArea notesArea = new JTextArea("Notes: " + notes);
                notesArea.setEditable(false);
                notesArea.setLineWrap(true);
                notesArea.setWrapStyleWord(true);
                notesArea.setBorder(new EmptyBorder(8, 0, 0, 0));
                notesArea.setBackground(getBackground());
                notesArea.setFont(tableFont); // match sizes
                centre.add(notesArea, BorderLayout.CENTER);
            }

            add(centre, BorderLayout.CENTER);

        } catch (Exception e) {
            add(new JLabel("Invalid correlation data."), BorderLayout.CENTER);
        }
    }
}
