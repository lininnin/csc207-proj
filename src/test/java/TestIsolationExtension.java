import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import test_utils.TestDataResetUtil;

/**
 * JUnit 5 extension that automatically resets all shared singleton data
 * before each test to ensure test isolation.
 */
public class TestIsolationExtension implements BeforeEachCallback {
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        // Reset all shared data before each test
        TestDataResetUtil.resetAllSharedData();
    }
}