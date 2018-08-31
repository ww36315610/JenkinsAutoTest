package finup.feather.controller;


import finup.feather.utils.file.ExcellOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.collections.Maps;


import java.util.List;
import java.util.Map;

public class AsyncTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTest.class);
    private static final String ExcellPath = "/Users/apple/Documents/case/Async_case.xlsx";
    private static final String ExcellSheet = "case";
    private static final String fileOut = "/Users/apple/Documents/linlin/log/Async/";
    private Map<String, Object> map = Maps.newHashMap();

    public static void main(String[] args) {
        AsyncTest as = new AsyncTest();
        as.runHttpPost();
    }

    public void runHttpPost() {
        List<Map<String, Object>> listMap = ExcellOperation.getExcellData(ExcellPath, ExcellSheet);

        listMap.forEach(m -> {
            m.forEach((k, v) -> {

            logger.info("k::{}###v:{}",k,v);
//            System.out.println(k);
            });
        });
    }
}
