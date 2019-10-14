package finup.feather.utils.listenter;

import com.relevantcodes.extentreports.*;
import org.testng.*;
import org.testng.IReporter;
import org.testng.xml.XmlSuite;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExtentTestNGIReporterListener implements IReporter {
    private ExtentReports extent;
//    private static final String OUTPUT_FOLDER = "test-output/report/new/";
//    private static final String OUTPUT_FOLDER = "test-output/report/";
//    private static final String OUTPUT_File = "test-output/report/index.html";
    private static final String OUTPUT_FOLDER = "/Users/apple/Desktop/tb/";
private static final String OUTPUT_File = "/Users/apple/Desktop/tb/index.html";
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory){
        /**
         * 生成报告文件名
         * true为覆盖已经生成的报告，false 在已有的报告上面生成，不会覆盖旧的结果
         * 最新运行的用例结果在第一个
         * online  报告的一些样式文件需从网络下载，生成的报告文件小   offline  样式及js文件不会从网络下载，生成在文件中
         */


        extent = new ExtentReports(OUTPUT_File,
                true
                , DisplayOrder.NEWEST_FIRST
                , NetworkMode.OFFLINE
        );
//        extent.x("10.202.2.1",27017);
//        extent.addSystemInfo("Host Name", "Anshoo");
//        extent.addSystemInfo("Environment", "QA");
        extent.startReporter(ReporterType.DB, OUTPUT_FOLDER); //生成本地的DB数据文件,保存路径
        for (ISuite suite : suites) {
            Map<String, ISuiteResult> result = suite.getResults();
            for (ISuiteResult r : result.values()) {
                ITestContext context = r.getTestContext();
                buildTestNodes(context.getPassedTests(), LogStatus.PASS);
                buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
                buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
            }
        }
        extent.flush();
        extent.close();
    }

    private void buildTestNodes(IResultMap tests, LogStatus status) {
        ExtentTest test;
        if (tests.size() > 0) {
            for (ITestResult result : tests.getAllResults()) {
                test = extent.startTest(result.getMethod().getMethodName());
                test.setStartedTime(getTime(result.getStartMillis()));
                test.setEndedTime(getTime(result.getEndMillis()));
                for (String group : result.getMethod().getGroups())
                    test.assignCategory(group);
                if (result.getThrowable() != null) {
//                    test.log(status, test.addScreenCapture("../img/"+result.getMethod().getMethodName()+".png"));
                    test.log(status, result.getThrowable());
                }
                else {
                    test.log(status, "Test " + status.toString().toLowerCase() + "ed");
                }

                extent.endTest(test);
            }
        }
    }

    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
}
