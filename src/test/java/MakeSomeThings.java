import com.alibaba.fastjson.JSONObject;
import finup.feather.utils.file.CsvReadTools;
import finup.feather.utils.file.FileOperation;

import java.util.List;

public class MakeSomeThings {


    public static void main(String[] args) {

//        sqlMake();
        jsonMake();
    }


    public static void jsonMake() {
        String fileName = "/Users/apple/Desktop/json_jin.txt";
        List<String> list = getCase(fileName);
        list.forEach(l -> {
            JSONObject jsonObject = JSONObject.parseObject(l);
            System.out.println(jsonObject.getString("resultMap"));
        });
    }
    public static void sqlMake() {

        String fileName = "/Users/apple/Desktop/log_id_jin.txt";
        String sqlBefore = "SELECT * from log_label_resp where log_id in (";
        List<String> list = getCase(fileName);
        StringBuffer inWhere = new StringBuffer("'");
        list.forEach(l -> {
            inWhere.append(l + "','");
        });
        System.out.println(sqlBefore + inWhere.substring(0, inWhere.length() - 2) + ")");
    }

    public static List<String> getCase(String fileName) {
        if (fileName.endsWith(".txt"))
            return FileOperation.readFileByLineString(fileName);
        else
            return CsvReadTools.getDataFromCSV(fileName);
    }
}
