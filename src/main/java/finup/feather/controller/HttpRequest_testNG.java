package finup.feather.controller;

import com.alibaba.fastjson.JSONArray;
import finup.feather.utils.file.ConfigTools;
import finup.feather.utils.http.HttpClientImp;
import finup.feather.utils.http.Oauth;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Map;

public class HttpRequest_testNG extends ConfigTools {
    public static String swithMethod = "test";
    public static final String httpUrl;
    public static final String oauthURL;
    public static final String clientId;
    public static final String cientSecret;
    static HttpClientImp hci = new HttpClientImp();
    static OAuth2AccessToken token;
    static Map<String, Object> header;

    static{
        httpUrl = config.getString("cif_utc_rest."+swithMethod+".url")+"moxie";
        swithMethod = swithMethod.equals("pre") ? "line" : swithMethod.equals("dev") ? "test" : swithMethod.equals("beta") ? "test" : swithMethod.equals("test") ? "test" : "line";
        oauthURL = config.getString("oauth_"+swithMethod+"_rest.oauth.url");
        clientId = config.getString("oauth_"+swithMethod+"_rest.oauth.clientId ");
        cientSecret = config.getString("oauth_"+swithMethod+"_rest.oauth.cientSecret");
        token = Oauth.getToken(oauthURL, clientId, cientSecret);
        header = Oauth.headerPut(token);
    }


    @BeforeTest
    public void befor() {
        System.out.println("-----------------------------beforMethod-----------------------------\n");
    }

    @Test
    public static void runMethod(){
        String json = "{\"mkey\":\"user_id\",\"mvalue\":\"779bd7d4f9fc25d9f3824897d4734dd1\",\"tagName\":\"mo_account_age\",\"channelId\":\"3003\"}";
        HttpClient client = new DefaultHttpClient();
        JSONArray jsonArray = hci.postJsonArray(client,httpUrl, header, json);
        System.out.println(jsonArray);
    }
    public static void main(String[] args) {
      runMethod();
    }

}