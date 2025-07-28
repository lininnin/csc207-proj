package use_case.Ina;

public record RegressionResult(
        double intercept,
        String[] featureNames,
        double[] coefficients,
        double rSquared,
        double adjustedRSquared,
        double[] stdErrors
) {}
