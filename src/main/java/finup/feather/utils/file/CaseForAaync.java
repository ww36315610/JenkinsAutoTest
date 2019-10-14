package finup.feather.utils.file;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;

public class CaseForAaync {
    //    private final static String fileCSV = "/Users/apple/Documents/case/all_method.txt";
//    private final static String fileCSV = "/Users/apple/Documents/case/asyncsome.txt";
    //最新的case
    private final static String fileCSV = "/Users/apple/Downloads/graylog-search-result-relative-7200.csv";

    public static void main(String[] args) {
        getCase(fileCSV).stream().forEach(c -> {
//            System.out.println(c);
//            doAsyncCase(c);
            doAsyncCaseByBean(c);
        });
    }

    //操作excell-case 处理成async可以处理的参数
    public static List<String> doAsyncCase(String excellCase) {
        excellCase = excellCase.replace("params", "sqlParam");
        excellCase = excellCase.replace("tagType", "tagTypes");
        excellCase = excellCase.replace("tagName", "tagNames");
        String caseF = excellCase.split("#####")[1];
//        System.out.println(caseF);
        JSONObject jsonRequest = JSONObject.parseObject(caseF);
        String channel = jsonRequest.getString("channelId");
        String sqlParam = jsonRequest.getString("sqlParam");

        JSONObject sqlParamJSON = jsonRequest.getJSONObject("sqlParam");
        if (excellCase.contains("/api/v1/moxie")) {

            String userId = jsonRequest.getString("mvalue");
            Map<String, String> map = Maps.newHashMap();
            map.put("user_id", userId);
            jsonRequest.put("filterParam", map);
            System.out.println(jsonRequest);
        } else if (excellCase.contains("/api/v1/oneTagName") || excellCase.contains("/api/v1/tagType") || excellCase.contains("/api/v1/multiTagName")) {
            String tagNames = null;
            String tagTypes = null;
            try {
                if (sqlParamJSON.containsKey("tagNames"))
                    tagNames = sqlParamJSON.getString("tagNames");
                if (sqlParamJSON.containsKey("tagNames"))
                    tagTypes = sqlParamJSON.getString("tagTypes");
                if (tagNames != null) {
                    jsonRequest.put("tagNames", tagNames);
                }
                if (tagTypes != null) {
                    jsonRequest.put("tagTypes", tagTypes);
                }
                System.out.println(jsonRequest);
            } catch (Exception e) {
                System.err.println(excellCase);
            }
        }
        return null;
    }


    //操作excell-case 处理成async可以处理的参数
    public static JSONObject doAsyncCaseByBean(String excellCase) {
        excellCase = excellCase.replace("params", "sqlParam");
        excellCase = excellCase.replace("tagType", "tagTypes");
        excellCase = excellCase.replace("tagName", "tagNames");
        excellCase = excellCase.replace("tagNamess", "tagNames");
        excellCase = excellCase.replace("tagTypess", "tagTypes");
        String caseF = excellCase.split("#####")[1];
        JSONObject jsonResult = new JSONObject();

        JSONObject jsonRequest = JSONObject.parseObject(caseF);
        String channel = jsonRequest.getString("channelId");
        String tagNames = null;
        String tagTypes = null;
        JSONObject sqlParamJSON = jsonRequest.getJSONObject("sqlParam");
        if (excellCase.contains("/api/v1/moxie")) {
            String userId = jsonRequest.getString("mvalue");
            Map<String, String> map = Maps.newHashMap();
            map.put("user_id", userId);
            jsonResult.put("filterParam", map);
            tagNames = jsonRequest.getString("tagNames");
            tagTypes = jsonRequest.getString("tagTypes");

        } else if (excellCase.contains("/api/v1/oneTagName") || excellCase.contains("/api/v1/tagType") || excellCase.contains("/api/v1/multiTagName") || excellCase.contains("/api/v1/oneIdCardNum")) {
            try {
                if (sqlParamJSON.containsKey("tagNames")) {
                    tagNames = sqlParamJSON.getString("tagNames");
                    sqlParamJSON.remove("tagNames");
                }
                if (sqlParamJSON.containsKey("tagNames")) {
                    tagTypes = sqlParamJSON.getString("tagTypes");
                    sqlParamJSON.remove("tagTypes");
                }
            } catch (Exception e) {
                System.err.println(excellCase);
            }
        } else if (excellCase.contains("/api/v1/basicInfo")) {
            if (sqlParamJSON.containsKey("channel")) {
                tagTypes = sqlParamJSON.getString("channel");
                sqlParamJSON.remove("channel");
            }
        } else return null;
        if (tagNames != null) {
            jsonResult.put("tagNames", tagNames);
        }
        if (tagTypes != null) {
            jsonResult.put("tagTypes", tagTypes);
        }
        jsonResult.put("channelId", channel);
        jsonResult.put("sqlParam", sqlParamJSON);

//        System.err.println(jsonResult);
        return jsonResult;
    }

    //获取所有的excell-case
    public static List<String> getCase(String fileName) {
        return CsvReadTools.getDataFromCSV(fileName);
//        return FileOperation.readFileByLineString(fileName);
    }
}