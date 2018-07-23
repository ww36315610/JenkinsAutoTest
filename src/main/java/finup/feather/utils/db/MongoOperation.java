package finup.feather.utils.db;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import finup.feather.utils.file.ConfigTools;

import java.util.ArrayList;
import java.util.List;

public class MongoOperation extends ConfigTools {
    static DB db = null;

    public static DB getMongoDatabase(String channel) {
        String userName = config.getString(channel + ".server.username");
        String authDB = config.getString(channel + ".server.default.auth.db");
        String passWord = config.getString(channel + ".server.password");
        String serverAddress = config.getString(channel + ".server.address");
        int serverPort = config.getInt(channel + ".server.port");
        String serverDB = config.getString(channel + ".server.db");
        MongoCredential mc = MongoCredential.createCredential(userName, authDB, passWord.toCharArray());
        List<MongoCredential> mongoList = new ArrayList<MongoCredential>();
        mongoList.add(mc);
        List<ServerAddress> hostList = new ArrayList<ServerAddress>();
        hostList.add(new ServerAddress(serverAddress, serverPort));
        MongoClient mongoClient = new MongoClient(hostList, mongoList);
        db = null; // 此处是为了建立不同的db实例，真正的需要去掉这一句[提供连接不同渠道]
        if (db == null) {
            synchronized (MongoDatabase.class) {
                if (db == null) {
                    db = mongoClient.getDB(serverDB);
                }
            }
        }
        return db;
    }

    public static DBCollection mongoDBConn(String tableName) {
        DBCollection dbConn = db.getCollection(tableName);
        return dbConn;
    }

    public static void main(String[] args) {
        getMongoDatabase(
                "mongo_qz");
        System.out.println(mongoDBConn("CreditCardStore"));


//        System.out.println(MongoDao.queryByRegex(mongoDBConn("AlipayStore"), "key", "6214"));
    }
}