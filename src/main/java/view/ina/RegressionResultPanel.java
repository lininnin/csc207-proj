//package view.ina;
//
//import use_case.Ina.ourAlgorithm.RegressionResult;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//import java.util.Optional;
//
//public class RegressionResultPanel extends JPanel {
//
//    public RegressionResultPanel(Optional<RegressionResult> maybeResult,
//                                 String[] featureNames,
//                                 double[][]xData,
//                                 double[] yData
//                                 ) {
//        setLayout(new BorderLayout(80, 80));
//        setBorder(new EmptyBorder(10, 10, 10, 10));
//
//        if (maybeResult.isEmpty()) {
//            add(noDataLabel(), BorderLayout.CENTER);
//            return;
//        }
//
//        RegressionResult result = maybeResult.get();
//
//        // Header
//    }
//}
