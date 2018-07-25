package finup.feather.utils.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.HttpClient;

import java.util.List;
import java.util.Map;

public interface HttpClientDao {

    // Get接口,返回jsonArray
    JSONArray getJsonArray(HttpClient client, String httpUrl, Map<String, Object> header) ;

    // 输入一个参数[对象、json]，返回jsonArray
    JSONArray postJsonArray(HttpClient client, String httpUrl, Map<String, Object> header, String json);

    // 输入多个参数组成list，返回jsonArray
    JSONArray postJsonArray(HttpClient client, String httpUrl, Map<String, Object> header, List<Object> listKey, List<Object> listValue);


    // Get接口,JSONObject
    JSONObject getJsonObject(HttpClient client, String httpUrl, Map<String, Object> header) ;

    // 输入一个参数[对象、json]，返回JSONObject
    JSONObject postJsonObject(HttpClient client, String httpUrl, Map<String, Object> header, String json);

    // 输入多个参数组成list，JSONObject
    JSONObject postJsonObject(HttpClient client, String httpUrl, Map<String, Object> header, List<Object> listKey, List<Object> listValue);

}
