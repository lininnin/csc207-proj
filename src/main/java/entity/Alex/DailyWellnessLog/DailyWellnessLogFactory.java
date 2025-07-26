package entity.Alex.DailyWellnessLog;

import java.time.LocalDate;

public class DailyWellnessLogFactory implements DailyWellnessLogFactoryInterf {

    @Override
    public DailyWellnessLogInterf create(LocalDate date) {
        return new DailyWellnessLog(date);
    }
}


