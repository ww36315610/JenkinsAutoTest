package finup.feather.utils.assertion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MapCompareTools implements Comparable<MapCompareTools> {

    public String key;
    public String value;

    public MapCompareTools(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public int compareTo(MapCompareTools o) {
        return key.compareTo(o.key);
    }

    public static void main(String[] args) {
        Map<String, Object> beforeMap = new HashMap<String, Object>();
        beforeMap.put("a", "1");
        beforeMap.put("b", "2");
        beforeMap.put("c", "3");
        beforeMap.put("d", "d");
        Map<String, Object> afterMap = new HashMap<String, Object>();
        afterMap.put("a", "1");
        afterMap.put("d", "d");
        afterMap.put("c", "333");
        afterMap.put("e", "55");
        String result = compareResult(beforeMap, afterMap);
        if (result.equals("Same map")) {
            System.out.println(result);
        } else {
            System.out.println(result);
        }

        Map<String, Object> beforeMap2 = new HashMap<String, Object>();
        beforeMap.put("a", "1");
        beforeMap.put("b", "2");
        Map<String, Object> afterMap2 = new HashMap<String, Object>();
        afterMap.put("b", "2");
        afterMap.put("a", "1");
        String result2 = compareResult(beforeMap2, afterMap2);
        if (result.equals("Same map")) {
            System.out.println(result2);
        } else {
            System.out.println(result2);
        }

        Map<String, Object> beforeMap1 = new HashMap<String, Object>();
        beforeMap.put("a", "[{\"aa\":\"aa\"},{\"bb\":\"bb\"}]");

        Map<String, Object> afterMap1 = new HashMap<String, Object>();
        afterMap.put("a", "[{\"aa\":\"aa\"},{\"bb\":\"bb\"}]");

        String result1 = compareResult(beforeMap1, afterMap1);
        if (result.equals("Same map")) {
            System.out.println(result1);
        } else {
            System.out.println(result1);
        }


        Set<MapCompareTools> beforeSet = new TreeSet<>();
        beforeSet.add(new MapCompareTools("a", "1"));
        beforeSet.add(new MapCompareTools("b", "2"));
        beforeSet.add(new MapCompareTools("c", "4"));

        Set<MapCompareTools> afterSet = new TreeSet<>();
        afterSet.add(new MapCompareTools("a", "1"));
        afterSet.add(new MapCompareTools("c", "333"));
        afterSet.add(new MapCompareTools("aa", "4"));

        String bb = "{\"LA_APPLR_id\":[{\"processing_time\":1530573481000,\"questionnaire\":1,\"create_time\":1530068603000,\"audit_state\":7,\"state_type\":\"PROCESSING\",\"end_time\":1530071316000,\"loan_amount\":105000.00,\"reconsideration_nu\":2,\"first_start_time\":1530068598000,\"expand_periods_loan\":\"false\",\"magic_data_center_id\":\"3dbd0d8955fb14d3c684ae776da7a496\",\"start_time\":1530068598000,\"data_center_id\":201201806271581092,\"update_time\":1530573481000,\"cycle_loan\":\"false\",\"accomplish\":0,\"app_customer_id\":1582840,\"customer_service_manager_number\":29020,\"checked\":0,\"id\":1581092,\"state\":3}]}";
        JSONObject jsonB = JSONObject.parseObject(bb).getJSONArray("LA_APPLR_id").getJSONObject(0);
        String aa = "{\"LA_APPLR_id\":[{\"processing_time\":1530573481000,\"questionnaire\":1,\"state_type\":\"PROCESSING\",\"reconsideration_nu\":2,\"magic_data_center_id\":\"3dbd0d8955fb14d3c684ae776da7a496\",\"data_center_id\":201201806271581092,\"update_time\":1530573481000,\"cycle_loan\":\"false\",\"accomplish\":0,\"checked\":0,\"id\":1581092,\"state\":3,\"create_time\":1530068603000,\"audit_state\":7,\"end_time\":1530071316000,\"loan_amount\":105000.00,\"first_start_time\":1530068598000,\"expand_periods_loan\":\"false\",\"start_time\":1530068598000,\"app_customer_id\":1582840,\"customer_service_manager_number\":29020}]}";
        JSONObject jsonA = JSONObject.parseObject(bb).getJSONArray("LA_APPLR_id").getJSONObject(0);

        String resultba = compareResult(jsonB, jsonA);
        System.out.println("aaaabbbb" + resultba);


        String ll = "{\"AF_YLZCREDUCE_batchno\":[{\"mobile\":\"xy8ce0f982be1b3e76f7d6c21f4c2b270f20160926\",\"message\":\"第三方没有相关数据\",\"channel\":\"fpx_gedaishanjie\",\"id\":2479965,\"update_time\":1533894064000,\"third_name\":\"YLZC_YINLIANZHICE\",\"code\":\"12310\",\"business_code\":\"A2018081017410405184451646\",\"id_no\":\"xy68f35c9b1823a7370ccc8939f1f7ce412cef7db84d8a3588a979ba76dd77ea3620160926\",\"create_time\":1533894064000,\"name\":\"聂晓明\",\"request_id\":\"176833\",\"bankcard_no\":\"xyf38b96c66fe1752cc8d39187bf8df1616411b4c890b7de5a21b4478bfc9302fe20160926\",\"batch_no\":\"0d8b316e40a843f9b91f2e800ebea095\"}]}";
        System.err.println("===="+ll.trim().length());
        JSONObject jsonLL = JSONObject.parseObject(ll);

        String pp ="{\"AF_YLZCREDUCE_batchno\":[{\"batch_no\":\"0d8b316e40a843f9b91f2e800ebea095\",\"code\":\"12310\",\"create_time\":1533894064000,\"mobile\":\"xy8ce0f982be1b3e76f7d6c21f4c2b270f20160926\",\"channel\":\"fpx_gedaishanjie\",\"business_code\":\"A2018081017410405184451646\",\"third_name\":\"YLZC_YINLIANZHICE\",\"message\":\"第三方没有相关数据\",\"update_time\":1533894064000,\"id_no\":\"xy68f35c9b1823a7370ccc8939f1f7ce412cef7db84d8a3588a979ba76dd77ea3620160926\",\"name\":\"聂晓明\",\"id\":2479965,\"request_id\":\"176833\",\"bankcard_no\":\"xyf38b96c66fe1752cc8d39187bf8df1616411b4c890b7de5a21b4478bfc9302fe20160926\"}]}";
        System.err.println("===="+pp.trim().length());
        JSONObject jsonPP = JSONObject.parseObject(pp);

        System.out.println("pppppllllll---" + compareResult(jsonLL, jsonPP));
//        System.out.println("-------" + SetCompare(beforeSet, afterSet));
    }


    /**
     * 比较两个map
     *
     * @param beforeMap
     * @param afterMap
     * @return 相同返回Same map  不同返回不同key或者value
     */

    public static String compareResult(Map<String, Object> beforeMap, Map<String, Object> afterMap) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String output = new String();
        for (String key : beforeMap.keySet()) {
            Object beforeValue = beforeMap.get(key);
            Object afterValue = afterMap.get(key);
            try {
                beforeValue = null == beforeValue ? "null" : beforeValue;
                afterValue = null == afterValue ? "null" : afterValue;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (beforeValue.equals(afterValue)) {
            } else if (afterValue == null) {
                output = output + key + " is missing, ";
            } else {
                String beforeString = JSON.toJSONString(beforeValue,SerializerFeature.WriteMapNullValue);
                String afterString = JSON.toJSONString(afterValue,SerializerFeature.WriteMapNullValue);
                if (beforeString.length() == afterString.length()) {
                    if (beforeString.startsWith("[{") && afterString.startsWith("[{")) {
                        //序列化
                        JSONArray jsonBefor = JSONArray.parseArray(beforeString);
                        JSONArray jsonAfter = JSONArray.parseArray(afterString);
                        for (int i = 0; i < jsonBefor.size(); i++) {
                            //对于basicinfo-等接口递归遍历
                            compareResult(jsonBefor.getJSONObject(i), jsonAfter.getJSONObject(i));
                        }
                    }
                } else {
                    output = output + key + " has changed from " + beforeString + " to " + afterString + " , ";
                }
            }
            afterMap.remove(key);
        }
        for (String key : afterMap.keySet()) {
            output = output + key + " was added with value " + afterMap.get(key) + ", ";
        }
        if ("".equals(output) || output == null) {
            output = "Same map";
        }
//        System.out.println("-------"+output);
        return output;
    }

    public static String compareResultBak(Map<String, Object> beforeMap, Map<String, Object> afterMap) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String output = new String();
        for (String key : beforeMap.keySet()) {
            Object beforeValue = beforeMap.get(key);
            Object afterValue = afterMap.get(key);
            try {
                beforeValue = null == (beforeMap.get(key)) ? "null" : beforeMap.get(key).toString().trim();
                afterValue = null == (afterMap.get(key)) ? "null" : afterMap.get(key).toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (beforeValue.equals(afterValue)) {
            } else if (afterValue == null) {
                output = output + key + " is missing, ";
                continue;
            } else {
                output = output + key + " has changed from " + beforeValue + " to " + afterValue + " , ";
            }
            afterMap.remove(key);
        }

        for (String key : afterMap.keySet()) {
            output = output + key + " was added with value " + afterMap.get(key) + ", ";
        }

        if ("".equals(output) || output == null) {
            output = "Same map";
        }
//        System.out.println("----"+output);
//        output = output.substring(0, output.length() - 2);
        return output;
    }




    /**
     * 传入jsonObject，获取map并比较
     *
     * @param jsonOne
     * @param jsonTwo
     * @return
     */
    public static String compareResult(JSONObject jsonOne, JSONObject jsonTwo, String resultMap) {
        if (jsonOne.isEmpty() || jsonTwo.isEmpty()) {
            System.out.println("jsonResut 返回有为空的");
        } else {
            String jsonMapOne = JSONObject.toJSONString(jsonOne.get(resultMap), SerializerFeature.WriteMapNullValue);
            Map<String, Object> mapOne = JSONObject.parseObject(jsonMapOne);
            String jsonMapTwo = JSONObject.toJSONString(jsonTwo.get(resultMap), SerializerFeature.WriteMapNullValue);
            Map<String, Object> mapTwo = JSONObject.parseObject(jsonMapTwo);
            return compareResult(mapOne, mapTwo);
        }
        return "jsonResut 返回有为空的";
    }

    /**
     * 比较连个Set集合
     *
     * @param beforeSet
     * @param afterSet
     * @return 相同返回Same map  不同返回不同key或者value
     */

    public static String SetCompare(Set<MapCompareTools> beforeSet, Set<MapCompareTools> afterSet) {

        MapCompareTools[] beforeArray = beforeSet.toArray(new MapCompareTools[beforeSet.size()]);
        MapCompareTools[] afterArray = afterSet.toArray(new MapCompareTools[afterSet.size()]);
        String output = new String();
        output = ",";
        int beforePtr = 0;
        int afterPtr = 0;
        while (beforePtr < beforeArray.length || afterPtr < afterArray.length) {
            int difference = afterPtr >= afterArray.length ? -1 : beforePtr >= beforeArray.length ? 1 : beforeArray[beforePtr].compareTo(afterArray[afterPtr]);
            if (difference == 0) {
                if (!beforeArray[beforePtr].value.equals(afterArray[afterPtr].value)) {
                    System.out.println(beforeArray[beforePtr].key + " value changed from '" + beforeArray[beforePtr].value + "' to '" + afterArray[afterPtr].value + "'");
                    if (output.equals(",")) output = "";
                    output = output + "," + beforeArray[beforePtr].key + " value changed from '" + beforeArray[beforePtr].value + "' to '" + afterArray[afterPtr].value + "'";
                }
                beforePtr++;
                afterPtr++;
            } else if (difference < 0) {
                System.out.println(beforeArray[beforePtr].key + " is missing");
                if (output.equals(",")) {
                    output = beforeArray[beforePtr].key + " is missing";
                } else {
                    output = output + "," + beforeArray[beforePtr].key + " is missing";
                }
                beforePtr++;
            } else {
                System.out.println(afterArray[afterPtr].key + " is added");
                if (output.equals(",")) {
                    output = afterArray[afterPtr].key + " is added";
                } else {
                    output = output + "," + afterArray[afterPtr].key + " is added";
                }
                afterPtr++;
            }
        }
        System.out.println("44444" + output);
        return output;
    }


}