package finup.feather.controller;


import com.alibaba.fastjson.JSONObject;
import finup.feather.utils.file.ExcellOperation;
import finup.feather.utils.http.HttpClientImp;
import finup.feather.utils.listenter.TestngRetry;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.collections.Maps;


import java.util.List;
import java.util.Map;

public class AsyncTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTest.class);
    private static final String ExcellPath = "/Users/apple/Documents/case/Async_case.xlsx";
    private static final String ExcellSheet = "case";
    private static final String fileOut = "/Users/apple/Documents/linlin/log/Async/";
    private static final String httpUrl = "http://localhost:30333/aapi/v2/asyncQuery";
    private HttpClientImp hci = new HttpClientImp();
    private Map<String, Object> header = Maps.newHashMap();

    public static void main(String[] args) {
        AsyncTest as = new AsyncTest();
        as.runHttpPost();
    }

    //    @Test
    public void runHttpPost() {
        List<Map<String, Object>> listMap = ExcellOperation.getExcellData(ExcellPath, ExcellSheet);
        HttpClient client = new DefaultHttpClient();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json;charset=UTF-8");
        listMap.forEach(m -> {
            String json = m.get("case_param").toString();
            String expect = m.get("expect").toString().toLowerCase();
            JSONObject jsonResult = hci.postJsonArray(client, httpUrl, header, json).getJSONObject(0);
            if (!jsonResult.isEmpty() && jsonResult.containsKey("success")) {
                if (jsonResult.getBoolean("success")!=Boolean.parseBoolean(expect)) {
                    logger.info("result=======>{}", jsonResult);
//                    Assert.assertEquals(1, 2, "");

                }
            } else {
//                Assert.assertEquals(1, 2, "返回值有误");
                logger.info("expect=======>{}", expect);
            }
        });
    }

    @Test(dataProvider = "file_case_data", dataProviderClass = AsyncTest.class, threadPoolSize = 3, invocationCount = 1)
    public void runHttpPostData(Map<String, Object> m) {
        HttpClient client = new DefaultHttpClient();
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/json;charset=UTF-8");
        String json = m.get("case_param").toString();
        String expect = m.get("expect").toString().toLowerCase();

        JSONObject jsonResult = hci.postJsonArray(client, httpUrl, header, json).getJSONObject(0);
        if (!jsonResult.isEmpty() && jsonResult.containsKey("success")) {
            if (jsonResult.getBoolean("success")!=Boolean.parseBoolean(expect)) {
                logger.info("result=======>{}", jsonResult);
                Assert.assertEquals(1, 2, "返回结果与预期值不一致");
            }else{
                Assert.assertEquals(1, 1, "Success erevry");
            }
        } else {
            logger.info("expect=======>{}", expect);
            Assert.assertEquals(1, 2, "返回值有误");
        }
    }


    @DataProvider(name = "file_case_data", parallel = true)
    public static Object[][] getData() {
        List<Map<String, Object>> listMap = ExcellOperation.getExcellData(ExcellPath, ExcellSheet);
        Object[][] obj = new Object[listMap.size()][1];
        for (int i = 0; i < listMap.size(); i++) {
            obj[i][0] = listMap.get(i);
        }
        return obj;
    }
}
