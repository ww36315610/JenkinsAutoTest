package finup.feather.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.config.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

public class HttpClientImp implements HttpClientDao {
    static HttpClient client = new DefaultHttpClient();

    @Override
    public JSONArray getJsonArray(String httpUrl, Map<String, Object> header) {
        JSONArray jsonArry = null;
        HttpGet get = new HttpGet(httpUrl);
        for (Map.Entry<String, Object> entry : header.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            get.setHeader(key, value);
        }
        HttpResponse response;
        try {
            response = client.execute(get);
            jsonArry = getResponse(response);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArry;
    }

    @Override
    public JSONArray postJsonArray(String httpUrl, Map<String, Object> header, String json) {
        JSONArray jsonArry = null;
        HttpPost post = new HttpPost(httpUrl);
        header.forEach((k, v) -> {
            post.setHeader(k, v.toString());
        });
        StringEntity entiry = new StringEntity(json, "utf-8");
        post.setEntity(entiry);
        try {
            CloseableHttpResponse response = (CloseableHttpResponse) client.execute(post);
            //强制关闭
//			response.close();

            jsonArry = getResponse(response);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return jsonArry;
    }

    @Override
    public JSONArray postJsonArray(String httpUrl, Map<String, Object> header, List<Object> listKey, List<Object> listValue) {
        JSONArray jsonArry = null;
        HttpPost post = new HttpPost(httpUrl);
        //设置超时时间 20s--不起作用
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(20 * 1).setConnectionRequestTimeout(20 * 1)
                .setSocketTimeout(20 * 1).build();
        post.setConfig(requestConfig);
        for (Map.Entry<String, Object> entry : header.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            post.setHeader(key, value);
        }
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (int i = 0; i < listKey.size(); i++) {
            list.add(new BasicNameValuePair(listKey.get(i).toString(), listValue.get(i).toString()));
        }
        UrlEncodedFormEntity entity;
        try {
            entity = new UrlEncodedFormEntity(list, "utf-8");
            post.setEntity(entity);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        try {
            HttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entityA = response.getEntity();
                jsonArry = getResponse(response);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return jsonArry;
    }

    @Override
    public JSONObject getJsonObject(String httpUrl, Map<String, Object> header) {
        return null;
    }

    @Override
    public JSONObject postJsonObject(String httpUrl, Map<String, Object> header, String json) {
        return null;
    }

    @Override
    public JSONObject postJsonObject(String httpUrl, Map<String, Object> header, List<Object> listKey, List<Object> listValue) {
        return null;
    }


    // 根据resonse的返回结果，生成JSONArray返回【公用代码抽象出来的】
    public static JSONArray getResponse(HttpResponse response) {
        List<String> list = null;
        JSONArray jsonArry = new JSONArray();
        JSONObject object = new JSONObject();
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity hEntity = response.getEntity();
            try {
                if (hEntity != null) {
                    String respons = EntityUtils.toString(hEntity, EntityUtils.getContentCharSet(hEntity));
                    try {
                        if (respons.startsWith("[")) {
                            jsonArry = JSONArray.parseArray(respons);
                        } else if (StringUtils.isEmpty(respons)) {
                            String responsNew = "[{" + respons + "}]";
                            jsonArry = JSONArray.parseArray(responsNew);
                        } else {
                            String responsNew = "[" + respons + "]";
                            jsonArry = JSONArray.parseArray(responsNew);
                        }
                    } catch (Exception e) {
                        System.out.println("格式转换错误，返回json格式不正确！！！");
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonArry;
        } else {
            return JSONArray.parseArray("[{\"resultMap\":{\"返回code#####\":" + response.getStatusLine().getStatusCode() + "}}]");
        }
    }
}
