//package use_case.Ina.ourAlgorithm;
//
//import entity.Alex.WellnessLogEntry.WellnessLogEntry;
//import entity.Angela.DailyLog;
//import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
//
//import java.util.*;
//
//public class CorrelationAnalyzerImpl implements CorrelationAnalyzer {
//    @Override
//    public RegressionResult analyze(List<DailyLog> logs) {
//
//        // Dataset
//        List<double[]> x = new ArrayList<>();
//        List<Double> y = new ArrayList<>();
//
//        for (DailyLog log: logs) {
//            double completion = log.getDailyTaskSummary().getCompletionRate();
//            if (Double.isNaN(completion)) continue;
//
//            List<WellnessLogEntry> wellnessLogEntries = log.getDailyWellnessLog().getEntries();
//            if (wellnessLogEntries == null || wellnessLogEntries.isEmpty()) continue;
//
//            OptionalDouble stress = wellnessLogEntries.stream().mapToInt(WellnessLogEntry::getStressLevel).average(); // how stressful the user is feeling on average this day
//            OptionalDouble energy = wellnessLogEntries.stream().mapToInt(WellnessLogEntry::getEnergyLevel).average(); // how energetic the user is feeling on average on this day
//            OptionalDouble fatigue = wellnessLogEntries.stream().mapToInt(WellnessLogEntry::getFatigueLevel).average();
//            if(stress.isEmpty() || energy.isEmpty() || fatigue.isEmpty()) continue;
//
//            x.add(new double[]{stress.getAsDouble(), energy.getAsDouble(), fatigue.getAsDouble()});
//            y.add(completion);
//        }
//
//        if (x.size() < 3) return null;
//
//        double[][] X = x.toArray(new double[0][]);
//        double [] Y = y.stream().mapToDouble(Double::doubleValue).toArray();
//
//        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
//
//        ols.setNoIntercept(false);
//        ols.newSampleData(Y, X);
//
//        double[] params = ols.estimateRegressionParameters();
//        double[] stdErrs = ols.estimateRegressionParametersStandardErrors();
//        double r2 = ols.calculateRSquared();
//        double adjR2 = ols.calculateAdjustedRSquared();
//
//        return new RegressionResult(
//                params[0],
//                new String[]{"AvgStress", "AvgEnergy", "AvgFatigue"},
//                Arrays.copyOfRange(params, 1, params.length),
//                r2, adjR2, stdErrs);
//    }
//
////    public String toString(RegressionResult regressionResult) {
////        return
////    }
//}
