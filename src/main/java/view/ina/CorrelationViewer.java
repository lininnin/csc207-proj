//package view.ina;
//
//import use_case.Ina.ourAlgorithm.RegressionResult;
//
//import javax.swing.*;
//import javax.swing.table.AbstractTableModel;
//import java.util.Arrays;
//
//public class CorrelationViewer { // for regression analysis,
//
//    private CorrelationViewer() {}
//
//    // return a scrollable table with features, std, intercept, error
//    public static JComponent corrTable(RegressionResult result) {
//        JTable table = new JTable(new ParamModel(result));
//        table.setFillsViewportHeight(true);
//        return new JScrollPane(table);
//    }
//
//    public static JComponent scatterPlot(double[][]xCols, double[] y, String[] vars) {
//        return new ScatterChooserPanel(xCols, y, vars);
//    }
//
//    // Table model
//
//    private static class ParamModel extends AbstractTableModel {
//        private final String[] headers = {"Term", "Coefficient", "StdErr"};
//        private final String[] terms;
//        private final double[] coefs;
//        private final double[] stderr;
//
//        ParamModel(RegressionResult result) {
//            terms = new String[result.featureNames().length + 1];
//            terms[0] = "Intercept";
//            System.arraycopy(result.featureNames(), 0, terms, 1, result.featureNames().length);
//
//            coefs = new double[terms.length];
//            coefs[0] = result.intercept();
//            System.arraycopy(result.coefficients(), 0, coefs, 1, result.coefficients().length);
//
//            // standardError may included intercept at index 0
//            if (result.stdErrors() != null && result.stdErrors().length == coefs.length) {
//                stderr = Arrays.copyOf(result.stdErrors(), result.stdErrors().length);
//            } else {
//                stderr = new double[coefs.length];
//                Arrays.fill(stderr, Double.NaN);
//            }
//        }
//        @Override public int getRowCount() {return terms.length;}
//        @Override public int getColumnCount() {return headers.length;}
//        @Override public String getColumnName(int n) {return headers[n];}
//
//
//        @Override public Object getValueAt(int rowIdx, int colIdx) {
//            return switch (colIdx) {
//                case 0 -> terms[rowIdx];
//                case 1 -> coefs[rowIdx];
//                case 2 -> stderr[rowIdx];
//                default -> null;
//            };
//        }
//    }
//}
