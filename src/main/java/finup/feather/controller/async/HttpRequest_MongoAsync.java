package finup.feather.controller.async;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.FileCodec;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import finup.feather.utils.db.MongoOperation;
import finup.feather.utils.file.CaseForAaync;
import finup.feather.utils.file.ConfigTools;
import finup.feather.utils.file.CsvReadTools;
import finup.feather.utils.file.FileOperation;
import finup.feather.utils.http.HttpClientImp;
import finup.feather.utils.http.Oauth;
import finup.feather.utils.thread.ThreadPoolUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 此类主要是利用case把pre环境同步程序结果，插入mongo
 */
public class HttpRequest_MongoAsync extends ConfigTools {
    private static String swithMethod = "pre";
    private static final String fileCSV;
    private static final String httpUrl;
    private static final String oauthURL;
    private static final String clientId;
    private static final String cientSecret;
    private static final String fileOut;
    private static final String resultMap = "resultMap";
    private static final String urlAsync = "http://api.finupgroup.com/cif-utc-rest-pre/api/v2/asyncQuery";
    static OAuth2AccessToken token;
    static Map<String, Object> header;
    static DB db;
    static DBCollection dbConn;

    static {
        httpUrl = config.getString("cif_utc_rest." + swithMethod + ".url");
        swithMethod = swithMethod.equals("pre") ? "line" : swithMethod.equals("dev") ? "test" : swithMethod.equals("beta") ? "test" : swithMethod.equals("test") ? "test" : "line";
        oauthURL = config.getString("oauth_" + swithMethod + "_rest.oauth.url");
        clientId = config.getString("oauth_" + swithMethod + "_rest.oauth.clientId ");
        cientSecret = config.getString("oauth_" + swithMethod + "_rest.oauth.cientSecret");
        token = Oauth.getToken(oauthURL, clientId, cientSecret);
        header = Oauth.headerPut(token);

        //最新的case
        fileCSV = "/Users/apple/Downloads/graylog-search-result-relative-7200.csv";
//        输入错误日志
        fileOut = "/Users/apple/Documents/linlin/log/";
    }

    public HttpRequest_MongoAsync() {
        db = MongoOperation.getMongoDatabase("mongo_nirvana");
        dbConn = MongoOperation.mongoDBConn("AsyncLabelResult_0921");
    }

    public static void main(String[] args) {
        HttpRequest_MongoAsync hr = new HttpRequest_MongoAsync();
        //多线程启动执行类
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                public void run() {
                    for (int a = 0; a < 1; a++) {
                        hr.httpRunnerAssert();
                    }
                }
            }).start();
        }
    }

    HttpClientImp hci = new HttpClientImp();

    public void httpRunnerAssert() {
        ThreadPoolExecutor pool = ThreadPoolUtils.getThreadPoolExecutor();
        List<String> listCase = getCase(fileCSV);
        for (int i = 0; i < listCase.size(); i++) {
            final int a = i;
            pool.execute(() -> {
                HttpClient client = new DefaultHttpClient();
                String key = listCase.get(a).split("#####")[0];
                String vaule = listCase.get(a).split("#####")[1];
                //替换掉case中的resultId跟requestId
                if (vaule.contains("requestId") || vaule.contains("resultId")) {
                    JSONObject jsonRe = JSONObject.parseObject(vaule);
                    jsonRe.remove("requestId");
                    jsonRe.remove("resultId");
                    vaule = JSONObject.toJSONString(jsonRe, SerializerFeature.WriteMapNullValue);
                }
                //case组装成async接口可以使用的格式
                String makeCase = JSONObject.toJSONString(CaseForAaync.doAsyncCaseByBean(listCase.get(a)), SerializerFeature.WriteMapNullValue);
                if (null == makeCase) {
                    return;
                }
                JSONObject jsonAsync = hci.postJsonArray(client, urlAsync, header, makeCase).getJSONObject(0);
                //获取requestId
                String requestId = (jsonAsync.containsKey("requestId")) ? jsonAsync.getString("requestId") : null;
                if (null == requestId) {
                    System.err.println(vaule);
                    System.err.println(makeCase);
                    System.err.println(jsonAsync);
                    return;
                }
                System.out.println(requestId);
                //把requestId写出到文件
                FileOperation.writeFileTrue(fileOut + "requestId.txt", requestId);
                String uurrll = httpUrl + key.substring(key.lastIndexOf("/") + 1);
                JSONObject jsonPre = hci.postJsonArray(client, uurrll, header, vaule).getJSONObject(0);
                //insert Mongo【pre环境】
                insertMongo(dbConn, jsonPre, requestId);
                //TODO:取出双边mongo数据compare
            });
        }
    }


    //插入Mongon
    public void insertMongo(DBCollection dbConn, JSONObject jsonRead, String requestId) {

        DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(jsonRead, SerializerFeature.WriteMapNullValue));
        dbo.put("_id", requestId);
        try {
            db.getCollection("AsyncLabelResult_0921").save(dbo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getCase(String fileName) {
        return CsvReadTools.getDataFromCSV(fileName);
//        return FileOperation.readFileByLineString(fileName);
    }
}