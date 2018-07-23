package finup.feather.utils.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

import java.io.*;
import java.util.List;
import java.util.Map;

public class CsvReadTools_restserver {
    static Map<String,String> mapURL = Maps.newHashMap();

    public static void main(String[] args) {
//        List<String> listCSV = getDataFromCSV("/Users/apple/Downloads/graylog-search-result-absolute-2018-04-17T00_00_00.000Z-2018-04-17T03_00_00.000Z.csv");
//        getDataFromCSV("/Users/apple/Downloads/graylog-search-result-relative-300.csv");
//        System.err.println(mapURL.keySet());
////        Map<String,List<String>> mapCase = makeRealCase(listCSV);
////        returnCaseList(mapCase);
//        System.err.println(mapURL.keySet());

        String fileCSV = "/Users/apple/Downloads/graylog-search-result-relative-14400.csv";
        List<String> list = getDataFromCSV(fileCSV);
        list.forEach(s->{
            System.out.println(s);
        });
//        getCaseFromCSV(fileCSV);

    }

    /**
     * 用于组装这些逻辑关系，方便调用
     * @param filePath
     * @return
     */
//    public static List<String> getCaseFromCSV(String filePath) {
//        List<String> listCSV = getDataFromCSV(filePath);
//        Map<String,List<String>> mapCase = makeRealCase(listCSV);
//        System.out.println(mapCase.keySet());
//      return returnCaseList(mapCase);
//    }

    /**
     * 读取CSV获取想要的list
     * 依赖makeCase()处理每一行
     * @param filePath
     * @return
     */
    public static List<String> getDataFromCSV(String filePath) {

//        File csv = new File("/Users/apple/Downloads/graylog-search-result-absolute-2018-04-17T00_00_00.000Z-2018-04-17T03_00_00.000Z.csv");  // CSV文件路径
        File csv = new File(filePath);
        BufferedReader br = null;
        List<String> allString = Lists.newArrayList();
        try {
            br = new BufferedReader(new FileReader(csv));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = br.readLine()) != null)  //读取到的内容给line变量
            {
//                System.out.println(line);
                if (!"".equals(line)&&line.contains("accept query["))
                    allString.add(line.substring(line.indexOf("[")+1,line.length()-3).replace("\"\"","\""));
            }
            System.out.println("csv表格中所有行数：" + allString.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allString;
    }

    /**
     * 过滤每一行，然后返回key是方法名###value是json实体的 String字符串
     * @param line
     * @return
     */
    public static String makeCase(String line) {
        JSONArray makeCase = null;
        String returnCase = "";
        String contains = "requestBody";
        String body = "";
        if (line.contains(contains)) {
            if (line.contains("normal request"))
                body = line.substring(line.indexOf("\"normal request"));
            if ("".equals(body) || StringUtils.isEmpty(body))
                body = line.substring(line.indexOf("\"slow request"));
            String bodySub = body.substring(body.indexOf(":") + 1, body.length() - 1);
            bodySub = bodySub.replace("\"\"", "\"");
            String[] bodySp = bodySub.split("=");

            bodySp[1] = bodySp[1].substring(0, bodySp[1].lastIndexOf(",")).replace(" ","");
            bodySp[3] = bodySp[3].substring(0, bodySp[3].lastIndexOf(",")).replace(" ","");

            mapURL.put(bodySp[1], bodySp[3]);
            try {
                JSONObject jsonContains = JSON.parseObject(bodySp[3]);
                returnCase = bodySp[1] + "###" + bodySp[3];
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
            try {
//                returnCase = URLEncoder.encode(bodySp[1], "UTF-8") + "###" + URLEncoder.encode(bodySp[3], "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return "";
        }
        return returnCase;
    }


    /**
     * 根据list 返回 map
     * key来自mapURL
     * @param list 来自getDataFromCSV()（/api/v1/tagType###{"params":"123123"}）
     * @return
     */
    private static Map<String, List<String>> makeRealCase(List<String> list) {
        Map<String, List<String>> map = Maps.newHashMap();
        for (String mKey : mapURL.keySet()) {
            List<String> listValue = Lists.newArrayList();
        for (String line : list) {
                String key = line.split("###")[0];
                String af = line.split("###")[1];
                if (key.equals(mKey)) {
                    listValue.add(af);
                }
            }
            map.put(mKey, listValue);
        }
        return map;
}

    /**
     * 正式组装list-case
     * 会按照现在的case模版组装case  pre.method_1=json
     * @param mapKV  来自makeRealCase()
     * @return
     */
    private static List<String> returnCaseList(Map<String, List<String>> mapKV) {
        List<String> relistCase = Lists.newArrayList();
        for(String key :mapKV.keySet()){
        List<String>  listCase  = mapKV.get(key);
           for(int i=1;i<listCase.size();i++){
               String caseSplit = "pre."+key.substring(key.lastIndexOf("/")+1)+"_"+i+"="+listCase.get(i).toString();
//               System.out.println(caseSplit);
               relistCase.add(caseSplit);
           }
         }
         return relistCase;
    }
}