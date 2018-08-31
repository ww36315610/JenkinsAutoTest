package finup.feather.utils.listenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class TestngListener extends TestListenerAdapter {
    private static Log logger = LogFactory.getLog(TestngListener.class);

    public static final String CONFIG = "config.properties";

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        logger.info(tr.getName() + " Failure");
//        takeScreenShot(tr);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        logger.info(tr.getName() + " Skipped");
//        takeScreenShot(tr);
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        logger.info(tr.getName() + " Success");
    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        logger.info(tr.getName() + " Start");
    }


    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
//
        ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
        Set<Integer> passedTestIds = new HashSet<Integer>();
        for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
            logger.info("PassedTests = " + passedTest.getName());
            passedTestIds.add(getId(passedTest));
        }

        Set<Integer> failedTestIds = new HashSet<Integer>();
        for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
            logger.info("failedTest = " + failedTest.getName());
            int failedTestId = getId(failedTest);
            if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
                testsToBeRemoved.add(failedTest);
            } else {
                failedTestIds.add(failedTestId);
            }
        }

// finally delete all tests that are marked
        for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext();) {
            ITestResult testResult = iterator.next();
            if (testsToBeRemoved.contains(testResult)) {
                logger.info("Remove repeat Fail Test: " + testResult.getName());
                iterator.remove();
            }
        }

    }



    private int getId(ITestResult result) {
    int id = result.getTestClass().getName().hashCode();
    id = id + result.getMethod().getMethodName().hashCode();
    id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
    return id;
    }

    /**
    @Override
    public void onFinish(ITestContext testContext) {
    super.onFinish(testContext);
        Iterator<ITestResult> listOfFailedTests = testContext.getFailedTests().getAllResults().iterator();
        while (listOfFailedTests.hasNext()) {
            ITestResult failedTest = (ITestResult) listOfFailedTests.next();
            ITestNGMethod method = failedTest.getMethod();
            System.err.println(method.getMethodName());
            if (testContext.getFailedTests().getResults(method).size() > 1) {
                listOfFailedTests.remove();
            } else {
                if (testContext.getPassedTests().getResults(method).size() > 0) {
                    listOfFailedTests.remove();
                }

            }

        }
    }
     */

    /**
     * 自动截图，保存图片到本地以及html结果文件中
     *
     * @param tr
     */
//    private void takeScreenShot(ITestResult tr) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
//        String mDateTime = formatter.format(new Date());
//        String fileName = mDateTime + "_" + tr.getName();
//        String filePath = OrangeiOS.driver.getScreenshotAs(fileName);
//        Reporter.setCurrentTestResult(tr);
//        Reporter.log(filePath);
//
////这里实现把图片链接直接输出到结果文件中，通过邮件发送结果则可以直接显示图片
//        Reporter.log("<img src=\"../" + filePath + "\"/>");
//
//    }
}