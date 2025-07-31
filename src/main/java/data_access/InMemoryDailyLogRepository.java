//package data_access;
//
//import entity.Angela.DailyLog;
//import use_case.repository.DailyLogRepository;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * In-memory implementation of DailyLogRepository for demo purposes.
// */
//public class InMemoryDailyLogRepository implements DailyLogRepository {
//    private final Map<LocalDate, DailyLog> dailyLogs = new HashMap<>();
//
//    @Override
//    public void save(DailyLog dailyLog) {
//        dailyLogs.put(dailyLog.getDate(), dailyLog);
//    }
//
//    @Override
//    public DailyLog findByDate(LocalDate date) {
//        return dailyLogs.get(date);
//    }
//
//    @Override
//    public List<DailyLog> loadBetween(LocalDate from, LocalDate to) {
//        List<DailyLog> result = new ArrayList<>();
//        for (Map.Entry<LocalDate, DailyLog> entry : logMap.entrySet()) {
//            LocalDate date = entry.getKey();
//            if ((date.isEqual(from) || date.isAfter(from)) &&
//                (date.isEqual(to) || date.isBefore(to))) {
//                result.add(entry.getValue());
//            }
//        }
//        result.sort(Comparator.comparing(DailyLog::getDate));
//        return result;
//    }
//}
