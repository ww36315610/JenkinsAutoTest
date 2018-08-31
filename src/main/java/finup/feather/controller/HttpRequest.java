package finup.feather.controller;

import com.alibaba.fastjson.JSONObject;
import finup.feather.utils.assertion.AssertionTools;
import finup.feather.utils.file.ConfigTools;
import finup.feather.utils.file.CsvReadTools;
import finup.feather.utils.http.HttpClientImp;
import finup.feather.utils.http.Oauth;
import finup.feather.utils.thread.ThreadPoolUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
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
    private static final String fileOut;
    private static final String resultMap = "resultMap";
    private static final String before = "api.finupgroup.com/cif-utc-rest-pre";
    private static final String after = "api.finupgroup.com/cif-utc-rest";

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

        //生产最新的log日志，量大  -测试所有接口
        fileCSV = "/Users/apple/Downloads/graylog-all_0628.csv";
        //输入错误日志
        fileOut = "/Users/apple/Documents/linlin/log/";
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
    }

    int timePre = 0;
    int timeLine = 0;
    HttpClientImp hci = new HttpClientImp();

    public void httpRunnerAssert() {
        ThreadPoolExecutor pool = ThreadPoolUtils.getThreadPoolExecutor();
        List<String> listCase = getCase(fileCSV);
        for (int i=0;i<listCase.size();i++) {
            final int a = i;
            pool.execute(() -> {
                HttpClient client = new DefaultHttpClient();
                String key = listCase.get(a).split("#####")[0];
                String vaule = listCase.get(a).split("#####")[1];
                String uurrll = httpUrl + key.substring(key.lastIndexOf("/") + 1);
                JSONObject jsonPre = hci.postJsonArray(client, uurrll, header, vaule).getJSONObject(0);
                uurrll = uurrll.replace(before, after);
                JSONObject jsonLine = hci.postJsonArray(client, uurrll, header, vaule).getJSONObject(0);
                AssertionTools assertionTools = new AssertionTools(key, listCase.get(a), jsonPre, jsonLine, fileOut, resultMap);
                Pair<Integer, Integer> pair = assertionTools.assertJSON();
                timePre = timePre + pair.getLeft();
                timeLine = timeLine + pair.getRight();
                if (Math.random() * 100 > 99) {
                    System.out.println("【"+a+"】==PPPP::[" + timePre + "]--LLLL::[" + timeLine + "]");
                }
            });
        }
    }

    public static List<String> getCase(String fileName) {
        return CsvReadTools.getDataFromCSV(fileName);
//        return FileOperation.readFileByLineString(fileName);
    }
}