package finup.feather.controller.async;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mongodb.*;
import finup.feather.utils.assertion.MapCompareTools;
import finup.feather.utils.db.MongoOperation;
import finup.feather.utils.file.FileOperation;
import org.testng.collections.Lists;

import java.util.ArrayList;
import java.util.List;

public class MongoCompare {
    static DB db;
    static DBCollection dbConn;

    static {
        db = MongoOperation.getMongoDatabase("mongo_nirvana");
        dbConn = MongoOperation.mongoDBConn("AsyncLabelResult_0921");
    }

    public static void main(String[] args) {
//        List<String> list = FileOperation.readFileByLineString(args[0]);
//        list.forEach(l -> {
//            compareFromMongo();
//        });
        JSONObject jj = findMongo(dbConn, "_id", "9d0b57cc1e5343f2915250fae49bf066_0921");
        System.out.println("222222"+jj);
    }

    public static void compareFromMongo(String filePath, String requestId, JSONObject jsonPre, JSONObject jsonAsync) {
        if (jsonAsync.getString("resultMap").contains("code#####") || jsonPre.getString("resultMap").contains("code#####")) {
            FileOperation.writeFileTrue(filePath + "code.txt", requestId);
        } else {
            if (jsonPre.getInteger("failCount") > 0) {
                FileOperation.writeFileTrue(filePath + "failCount.txt", requestId);
            } else {
                String resultAssert = MapCompareTools.compareResult(jsonPre, jsonAsync, "resultMap") == "Same map" ? "True【" + requestId + "】" : "False【" + requestId + "】";
                if (resultAssert.contains("False")) {
                    FileOperation.writeFileTrue(filePath + "compare.txt", requestId);
                }
            }
        }
    }
//        public void getM

    // 根据某Key为true查询mongo对象，返回值类型为：JSONArray
    public static JSONObject findMongo(DBCollection dbConn, String paramKey, String paramValue) {
        List<DBObject> aList = new ArrayList<DBObject>();
        BasicDBObject condition = new BasicDBObject();
        condition.put(paramKey, new BasicDBObject(QueryOperators.EXISTS, true));
        aList = dbConn.find(new BasicDBObject(paramKey, paramValue)).toArray();
        System.out.println("111111"+aList);
        JSONArray jsona = JSONArray.parseArray(aList.toString());
        System.out.println("222222"+jsona);
        JSONObject jj = new JSONObject();
        jj.put("resultMap", "code#####");

        return jsona.getJSONObject(0).containsKey("resultMap") ? jsona.getJSONObject(0) : jj;
    }
}
