package finup.feather.utils.listenter;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

public class TestngRetry implements IRetryAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(TestngRetry.class);

    int retryCount = 1;
    private static int maxRetryCount;
//    private static ConfigReader config;
    static {
        //外围文件配置最大运行次数
        //config = new ConfigReader(TestngListener.CONFIG);
        //maxRetryCount = config.getMaxRunCount();
        maxRetryCount=2;
        logger.info("maxRunCount=" + (maxRetryCount));
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount <= maxRetryCount) {
            String message = "running retry for '" + result.getName() + "' on class " + this.getClass().getName() + " Retrying "
                    + retryCount + " times";
            logger.info("retire:::"+message);
            Reporter.setCurrentTestResult(result);
            Reporter.log("RunCount=" + (retryCount + 1));
            retryCount++;
            return true;
        }
        return false;
    }
}