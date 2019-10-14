package finup.feather.controller;

import com.alibaba.fastjson.JSONArray;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mysql.jdbc.Connection;
import finup.feather.dao.MongoDao;
import finup.feather.dao.MysqlDao;
import finup.feather.dao.MysqlDaoImp;
import finup.feather.utils.db.MongoOperation;
import finup.feather.utils.db.MysqlOperation;
import finup.feather.utils.file.ConfigTools;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;

public class KVGet {

    private static List<Map<String, Object>> list;
    private static MysqlOperation mcbp;
    private static Connection conn;
    private static MysqlDao md;
    static String driver;
    static String url;
    static String user;
    static String pass;


    static {
        driver = ConfigTools.config.getString("mysql_wj_test_resfull.jdbc.dbDriver");
        url = ConfigTools.config.getString("mysql_wj_test_resfull.jdbc.dbUrl");
        user = ConfigTools.config.getString("mysql_wj_test_resfull.jdbc.username");
        pass = ConfigTools.config.getString("mysql_wj_test_resfull.jdbc.password");
        mcbp = MysqlOperation.getInstance(driver, url, user, pass);
        conn = (Connection) mcbp.getConnection();
        md = new MysqlDaoImp();
    }

    public static void main(String[] args) {
        //创建mongo链接
        String mongDBT = "mongo_nirvana";
        List collectLists = Lists.newArrayList();
        Map<String, String> mapKey = Maps.newHashMap();
        mapKey.put("tb_basic_info", "tb_basic_info");
        mapKey.put("tb_collection_shop", "tb_collection_shop");
        mapKey.put("tb_received_address", "tb_received_address");
        mapKey.put("tb_shopping_cart", "tb_shopping_cart");
        mapKey.put("tb_trans_detail_info", "tb_trans_detail_info");
        mapKey.put("tb_trans_list", "tb_trans_list");

        DB db = MongoOperation.getMongoDatabase("mongo_nirvana");
        DBCollection dbConn = MongoOperation.mongoDBConn(mapKey.get("tb_basic_info"));

        // 读取数据
        String key = "id_num_biz";
        String value = "xyb8fe01fc222917cec41f8cf346300a30547bc4f3d5c09244a09ef2d4640cc10b20160926";
        JSONArray jsonFroMongo = MongoDao.findMongoByInsert(dbConn, "id_num_biz", "xyb8fe01fc222917cec41f8cf346300a30547bc4f3d5c09244a09ef2d4640cc10b20160926");
        System.out.println(jsonFroMongo);


    }


}
