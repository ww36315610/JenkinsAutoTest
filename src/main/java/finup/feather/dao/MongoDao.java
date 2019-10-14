package finup.feather.dao;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;

public class MongoDao {


    //转bason直接插入
    public static void saveMongo(DBCollection dbConn,String collection, JSONObject jsonRead,String requestId){
        DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(jsonRead, SerializerFeature.WriteMapNullValue));
        dbo.put("_id", requestId);
        dbConn.getCollection(collection).save(dbo);
    }


    // 从文件中读取bason然后插入mongo、、并返回进件号[String]
    public static String insertMongo(DBCollection dbConn, JSONArray jsonArray, long time) {
        // String className = FileOperation.getStore(jsonArray, "className");
        // String[] classNameSplit = className.split("\\.");
        JSONObject jsonRead = (JSONObject) jsonArray.get(0);
        DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(jsonRead, SerializerFeature.WriteMapNullValue));
        JSONObject json = (JSONObject) JSONObject.toJSON(dbo);
        json.replace("applyNo", time + "");
        json.replace("requestId", time + "");
        json.replace("_id", time + 1 + "");
        json.replace("idCardNum", time + 2 + "");
        json.replace("idNo", time + 2 + "");
        json.replace("idNnum", time + 2 + "");
        dbConn.insert(new BasicDBObject(json));
        return time + "";
    }

    // 从文件中读取bason然后插入mongo、、并返回进件号[long]
    public static long insertMongoLong(DBCollection dbConn, JSONArray jsonArray, long time) {
        // String className = FileOperation.getStore(jsonArray, "className");
        // String[] classNameSplit = className.split("\\.");
        JSONObject jsonRead = (JSONObject) jsonArray.get(0);
        DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(jsonRead, SerializerFeature.WriteMapNullValue));
        JSONObject json = (JSONObject) JSONObject.toJSON(dbo);
        json.replace("applyNo", time + "");
        json.replace("requestId", time + "");
        json.replace("_id", time + 1 + "");
        json.replace("idCardNum", time + 2 + "");
        json.replace("idNo", time + 2 + "");
        json.replace("idNnum", time + 2 + "");
        dbConn.insert(new BasicDBObject(json));
        return time;
    }


    // 从文件中读取bason然后插入mongo、、并返回进件号[long]
    public static long insertMongoLong(DBCollection dbConn, JSONObject jsonObject, long time) {
        // String className = FileOperation.getStore(jsonArray, "className");
        // String[] classNameSplit = className.split("\\.");
        DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue));
        JSONObject json = (JSONObject) JSONObject.toJSON(dbo);
        json.replace("applyNo", time + "");
        json.replace("requestId", time + "");
        json.replace("_id", time + 1 + "");
        json.replace("idCardNum", time + 2 + "");
        json.replace("idNo", time + 2 + "");
        json.replace("idNnum", time + 2 + "");
        try {
            dbConn.insert(new BasicDBObject(json));
        }catch (Exception e){
            json.replace("_id", time + 2+time + "");
            dbConn.insert(new BasicDBObject(json));
            System.out.println("-----------"+json.getString("_id"));
            e.printStackTrace();
        }
        return time;
    }


    // 插入mongo
    public static void insertMongo(DBCollection dbConn, JSONArray jsonArray) {
        int num = 0;
        if (jsonArray.size() > 0) {
            for (Object object : jsonArray) {
                String _id = Math.random() * 1000000000 + num + "";
                JSONObject jsonObject = JSONObject.parseObject(object.toString());
                DBObject dbo = (DBObject) com.mongodb.util.JSON.parse(JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue));
                JSONObject json = (JSONObject) JSONObject.toJSON(dbo);
                // json.replace("_id", JiaMi.getAes(_id));
                dbConn.insert(new BasicDBObject(json));
                num++;
            }
        }
    }

    // 根据固定Key查询mongo对象，返回值类型为：JSONArray
    public static JSONArray findMongoByInsert(DBCollection dbConn, String paramKey, String paramValue) {
        List<DBObject> aList = new ArrayList<DBObject>();
        BasicDBObject condition = new BasicDBObject();
        condition.put(paramKey, new BasicDBObject(QueryOperators.EXISTS, true));
        aList = dbConn.find(new BasicDBObject(paramKey, paramValue)).toArray();
        JSONArray jsona = JSONArray.parseArray(aList.toString());
        return jsona;
    }


    // 存在查询\前几行limit，返回值类型为：JSONArray 某个tablename存在的前几行 [例：param ="applyNo"]
    public static JSONArray queryByExists(DBCollection dbConn, String param, int limit) {
        JSONObject jsonObject = new JSONObject();
        List<DBObject> list = new ArrayList<DBObject>();
        JSONArray jsonArray = new JSONArray();
        BasicDBObject condition = new BasicDBObject();
        condition.put(param, new BasicDBObject(QueryOperators.EXISTS, true));
        // condition.put("idCardNum", "430502198109092516");
        try {
            System.out.println("开始查找休眠30毫秒：" + param);
            // FindIterable responseData = (FindIterable)
            // dbConn.find(condition).limit(limit);
            // MongoCursor<Document> iterator = responseData.iterator();
            // System.out.println("结束查找：" + param);
            // while (iterator.hasNext()) {
            // Document doc = iterator.next();
            // String jsonStr = doc.toJson();
            // jsonObject = JSONObject.parseObject(jsonStr);
            // list.add(jsonObject);
            // }
            // List<DBObject> aList = (List<DBObject>)
            list = dbConn.find(condition).limit(limit).toArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.parseArray(list.toString());
    }

    // 模糊查询 paramLey 包含 paramValue的数据 [例：paramKey ="applyNo" paramValue="102"]
    public static JSONArray queryByRegex(DBCollection dbConn, String paramKey, String paramValue) {
        JSONObject jsonObject = new JSONObject();
        List<DBObject> list = new ArrayList<DBObject>();
        JSONArray jsonArray = new JSONArray();
        try {
            // 正则匹配含有paramValue参数的value
            Pattern pattern = Pattern.compile("^.*" + paramValue + ".*$");
            BasicDBObject queryObject = new BasicDBObject(paramKey, pattern);
//			queryObject.put(paramKey, new BasicDBObject("$regex", pattern));
            //以前是这样写
//			 list = dbConn.find(queryObject).toArray();
            // 这样写的目的【节省资源】，游标只是把数据找出来，不会放入内存，只有next的时候才会调用真的数据
            DBObject dbObject = null;
            DBCursor cursor = dbConn.find(new BasicDBObject(queryObject));
            while (cursor.hasNext()) {
                dbObject = cursor.next();
                list.add(dbObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray.parseArray(list.toString());
    }

}
