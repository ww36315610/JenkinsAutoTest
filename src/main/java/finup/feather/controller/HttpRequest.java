package finup.feather.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finup.feather.utils.assertion.AssertionTools;
import finup.feather.utils.file.ConfigTools;
import finup.feather.utils.file.CsvReadTools;
import finup.feather.utils.http.HttpClientImp;
import finup.feather.utils.http.Oauth;

import finup.feather.utils.thread.ThreadPoolUtils;
import jdk.management.resource.internal.inst.InitInstrumentation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class HttpRequest extends ConfigTools {
    private static String swithMethod = "pre";
    private static final String fileCSV;
    private static final String httpUrl;
    private static final String oauthURL;
    private static final String clientId;
    private static final String cientSecret;
    private static final String fileOut = "/Users/apple/Documents/linlin/";
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
        fileCSV = "/Users/apple/Downloads/graylog-search-result-absolute-2018-04-17T00_00_00.000Z-2018-04-17T03_00_00.000Z.csv";
    }


    public static List<String> getCase(String fileName) {
        return CsvReadTools.getDataFromCSV(fileName);
    }

    int timePre = 0;
    int timeLine = 0;

    public void httpRunnerAssert() {
        ThreadPoolExecutor pool = ThreadPoolUtils.getThreadPoolExecutor();
        List<String> listCase = getCase(fileCSV);
        for (String jsonList : listCase) {
            pool.execute(() -> {
                HttpClient client = new DefaultHttpClient();
                String key = jsonList.split("###")[0];
                String vaule = jsonList.split("###")[1];
                String uurrll = httpUrl + key.substring(key.lastIndexOf("/") + 1);
                JSONObject jsonPre = hci.postJsonArray(client, uurrll, header, vaule).getJSONObject(0);
                uurrll = uurrll.replace(before, after);
                JSONObject jsonLine = hci.postJsonArray(client, uurrll, header, vaule).getJSONObject(0);
                AssertionTools assertionTools = new AssertionTools(key, jsonList, jsonPre, jsonLine, fileOut, resultMap);
                Pair<Integer, Integer> pair = assertionTools.assertJSON();
                timePre = timePre + pair.getLeft();
                timeLine = timeLine + pair.getRight();
                if (Math.random() * 100 > 99) {
                    System.out.println("PPPP::[" + timePre + "]--LLLL::[" + timeLine + "]");
                }
            });

        }
    }

    public static void main(String[] args) {
        HttpRequest hr = new HttpRequest();

        for (int i = 0; i < 1; i++) {
        new Thread(new Runnable() {
            public void run() {
                hr.httpRunnerAssert();
            }
        }).start();
        }

//        hr.httpRunner();
//        hr.outCaseKeys();

    }







    public void httpRunner() {
        ThreadPoolExecutor pool = ThreadPoolUtils.getThreadPoolExecutor();
        List<String> listCase = getCase(fileCSV);
        for (String jsonList : listCase) {
            pool.execute(() -> {
                long before = System.currentTimeMillis();
                HttpClient client = new DefaultHttpClient();
                String key = jsonList.split("###")[0];
                String vaule = jsonList.split("###")[1];
                System.out.println("11111:::" + String.valueOf(System.currentTimeMillis() - before));
                String uurrll = httpUrl + key.substring(key.lastIndexOf("/") + 1);
                JSONArray jsonArray = hci.postJsonArray(client, uurrll, header, vaule);
                System.out.println("22222:::" + String.valueOf(System.currentTimeMillis() - before));
                System.out.println(jsonArray);
            });
        }
    }

    public void outCaseKeys() {
        Map<String, Object> keyMaps = Maps.newHashMap();
        Map<String, Object> valueMaps = Maps.newHashMap();
        List<String> listCase = getCase(fileCSV);
        listCase.forEach(l -> {
            String key = l.split("###")[0];
            String vaule = l.split("###")[1];
            keyMaps.put(key, "kkk");
            valueMaps.put(vaule, "vvv");
        });
        keyMaps.forEach((k, v) -> {
            System.out.println(k);
        });
//        valueMaps.forEach((k, v) -> {
//            System.out.println(k);
//        });
        System.out.println(valueMaps.size());
    }

}