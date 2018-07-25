package finup.feather.utils.assertion;

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
                beforeValue = null==(beforeMap.get(key)) ? "null" : beforeMap.get(key).toString().trim();
                afterValue = null==(afterMap.get(key)) ? "null" : afterMap.get(key).toString().trim();
            }catch (Exception e){
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

        if ("".equals(output)||output == null) {
            output = "Same map";
        }
//        System.out.println("----"+output);
//        output = output.substring(0, output.length() - 2);
        return output;
    }


    /**
     * 传入jsonObject，获取map并比较
     * @param jsonOne
     * @param jsonTwo
     * @return
     */
    public static String compareResult(JSONObject jsonOne, JSONObject jsonTwo,String resultMap){
        if(jsonOne.isEmpty()||jsonTwo.isEmpty()){
            System.out.println("jsonResut 返回有为空的");
        }else{
            String jsonMapOne = JSONObject.toJSONString(jsonOne.get(resultMap), SerializerFeature.WriteMapNullValue);
            Map<String,Object> mapOne = JSONObject.parseObject(jsonMapOne);
            String jsonMapTwo = JSONObject.toJSONString(jsonTwo.get(resultMap), SerializerFeature.WriteMapNullValue);
            Map<String,Object> mapTwo = JSONObject.parseObject(jsonMapTwo);
            return compareResult(mapOne,mapTwo);
        }
        return "jsonResut 返回有为空的";
    }

    /**
     * 比较连个Set集合
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
                    if(output.equals(",")) output = "";
                    output = output + ","+beforeArray[beforePtr].key + " value changed from '" + beforeArray[beforePtr].value + "' to '" + afterArray[afterPtr].value + "'";
                }
                beforePtr++;
                afterPtr++;
            } else if (difference < 0) {
                System.out.println(beforeArray[beforePtr].key + " is missing");
                if (output.equals(",")){
                    output = beforeArray[beforePtr].key + " is missing";
                }else {
                output = output + "," + beforeArray[beforePtr].key + " is missing";
            }
                beforePtr++;
            } else {
                System.out.println(afterArray[afterPtr].key + " is added");
                if(output.equals(","))
                {
                    output = afterArray[afterPtr].key + " is added";
                }else {
                    output = output + "," + afterArray[afterPtr].key + " is added";
                }
                afterPtr++;
            }
        }
        System.out.println("44444"+output);
        return output;
    }


}