package finup.feather.controller;

import com.alibaba.fastjson.JSONObject;
import finup.feather.utils.assertion.AssertionTools;
import finup.feather.utils.file.ConfigTools;
import finup.feather.utils.file.CsvReadTools;
import finup.feather.utils.file.FileOperation;
import finup.feather.utils.http.HttpClientImp;
import finup.feather.utils.http.Oauth;
import finup.feather.utils.listenter.TestngRetry;
import finup.feather.utils.thread.ThreadPoolUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpRequest_TestNG_New extends ConfigTools {
    private static String swithMethod = "pre";
    private static final String fileCSV;
    private static final String httpUrl;
    private static final String oauthURL;
    private static final String clientId;
    private static final String cientSecret;
    private static final String fileOut = "/Users/apple/Documents/linlin/log/";
    private static final String resultMap = "resultMap";
    private static final String before = "api.finupgroup.com/cif-utc-rest-pre";
    private static final String after = "api.finupgroup.com/cif-utc-rest";
    HttpClientImp hci = new HttpClientImp();
    static OAuth2AccessToken token;
    static Map<String, Object> header;


    static {
        httpUrl = config.getString("cif_utc_rest." + swithMethod + ".url");
        swithMethod = swithMethod.equals("pre") ? "line" : swithMethod.equals("dev") ? "test" : swithMethod.equals("beta") ? "test" : swithMethod.equals("test") ? "test" : "line";
        System.out.println(swithMethod);
        oauthURL = config.getString("oauth_" + swithMethod + "_rest.oauth.url");
        clientId = config.getString("oauth_" + swithMethod + "_rest.oauth.clientId ");
        cientSecret = config.getString("oauth_" + swithMethod + "_rest.oauth.cientSecret");
        token = Oauth.getToken(oauthURL, clientId, cientSecret);
        header = Oauth.headerPut(token);
//        fileCSV = "/Users/apple/Downloads/graylog-search-result-absolute-2018-04-17T00_00_00.000Z-2018-04-17T03_00_00.000Z.csv";
        //生产最新的log日志，量大  -测试所有接口
//        fileCSV = "/Users/apple/Downloads/graylog-all_0628.csv";

        // 测试moxie一个小时量
//        fileCSV = "/Users/apple/Downloads/graylog-search-result-relative-3600.csv";
        //testNG-report
//        fileCSV = "/Users/apple/Downloads/ss.csv";

//        fileCSV = "/Users/apple/Documents/case/all_method.txt";
        fileCSV = "/Users/apple/Documents/case/repy.txt";
    }


    public static void main(String[] args) {
        HttpRequest_TestNG_New ht = new HttpRequest_TestNG_New();
        ht.testMethod(args[0]);
    }

    public static List<String> getCase(String fileName) {
//        return CsvReadTools.getDataFromCSV(fileName);
        return FileOperation.readFileByLineString(fileName);
    }

    int timePre = 0;
    int timeLine = 0;


    //    @Test(dataProvider = "file_case_data", dataProviderClass = HttpRequest_TestNG_New.class,timeOut = 35000,threadPoolSize = 2)
//    @Test(dataProvider = "file_case_data", dataProviderClass = HttpRequest_TestNG_New.class,retryAnalyzer = TestngRetry.class)
//    @Test(dataProvider = "file_case_data", dataProviderClass = HttpRequest_TestNG_New.class,threadPoolSize = 2, invocationCount = 3,timeOut = 35000,retryAnalyzer = TestngRetry.class)
    @Test(dataProvider = "file_case_data", dataProviderClass = HttpRequest_TestNG_New.class,threadPoolSize = 10, invocationCount = 1)
    public void testMethod(String requestBody) {
        ThreadPoolExecutor pool = ThreadPoolUtils.getThreadPoolExecutor();
//            pool.execute(() -> {
        HttpClient client = new DefaultHttpClient();
        String key = requestBody.split("#####")[0];
        String vaule = requestBody.split("#####")[1];
        String uurrll = httpUrl + key.substring(key.lastIndexOf("/") + 1);
        JSONObject jsonPre = hci.postJsonArray(client, uurrll, header, vaule).getJSONObject(0);
        System.out.println(jsonPre);
        if (jsonPre.containsKey("failCount")) {
            Assert.assertTrue(0 == jsonPre.getInteger("failCount"));
        } else {
            Assert.assertTrue(false, requestBody);
        }
        uurrll = uurrll.replace(before, after);
        JSONObject jsonLine = hci.postJsonArray(client, uurrll, header, vaule).getJSONObject(0);
        System.out.println("11111" + jsonLine);
        AssertionTools assertionTools = new AssertionTools(key, vaule, jsonPre, jsonLine, fileOut, resultMap);
        Pair<Integer, Integer> pair = assertionTools.assertJSON();
        timePre = timePre + pair.getLeft();
        timeLine = timeLine + pair.getRight();
        if (Math.random() * 100 > 99) {
            System.out.println("【" + "--" + "】==PPPP::[" + timePre + "]--LLLL::[" + timeLine + "]");
        }
//            });
    }


    @DataProvider(name = "file_case_data", parallel = true)
    public static Object[][] getData() {
//        List<String> listFile = FileOperation.readFileByLineList(fileName);
        List<String> listFile = getCase(fileCSV);
        Object[][] obj = new Object[listFile.size()][1];
        for (int i = 0; i < listFile.size(); i++) {
            obj[i][0] = listFile.get(i);
        }
        return obj;
    }
}