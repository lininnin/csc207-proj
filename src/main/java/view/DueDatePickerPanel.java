package view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DueDatePickerPanel extends JPanel {

    private final DatePicker datePicker;

    public DueDatePickerPanel() {
        DatePickerSettings settings = new DatePickerSettings();
        settings.setAllowEmptyDates(true);
        settings.setAllowKeyboardEditing(false);
        settings.setFormatForDatesCommonEra("yyyy-MM-dd");

        // ✅ 必须先构造 datePicker 再调用 setDateRangeLimits
        this.datePicker = new DatePicker(settings);
        
        // Only allow today and future dates for regular date selection
        // Use the "Test: Add with Yesterday" button for testing overdue functionality
        settings.setDateRangeLimits(LocalDate.now(), null);

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.add(new JLabel("Due Date:"));
        this.add(datePicker);
    }

    public LocalDate getSelectedDate() {
        return datePicker.getDate();
    }

    public void clear() {
        datePicker.clear();
    }
    
    public void setSelectedDate(LocalDate date) {
        datePicker.setDate(date);
    }
}




