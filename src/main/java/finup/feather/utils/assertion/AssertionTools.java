package finup.feather.utils.assertion;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import finup.feather.bean.FilterEnum;
import finup.feather.utils.file.FileOperation;
import org.apache.commons.lang3.tuple.Pair;

public class AssertionTools {
    private String key;
    private String json;
    private String filePath;
    private String resultMap;
    private JSONObject jsonResultPre;
    private JSONObject jsonResultLine;

    private AssertionTools() {
    }

    public AssertionTools(String key, String json, JSONObject jsonResultPre, JSONObject jsonResultLine, String filePath, String resultMap) {
        this.key = key;
        this.json = json;
        this.jsonResultPre = jsonResultPre;
        this.jsonResultLine = jsonResultLine;
        this.filePath = filePath;
        this.resultMap = resultMap;
    }

    //单例模式因为全局的遍历必须是static，所以多线程的时候会导致串
    //如：参数json两个线程同时运行，第一个线程没有比较完，第二个开始比较，就需要❤新getInstace，因为json是静态的，所以，第一个json就变成第二个的了
//    public static synchronized AssertionTools getInstace() {
//        return new AssertionTools(key, json, jsonResultPre, jsonResultLine, filePath);
//    }

    public Pair<Integer, Integer> assertJSON() {
        int castTimePre = 0;
        int castTimeLine = 0;
        if (jsonResultPre.size() > 0 && jsonResultLine.size() > 0) {
            if(jsonResultPre.size() !=jsonResultLine.size()){
                System.err.println(jsonResultPre);
                System.err.println(jsonResultLine);
                System.out.println(jsonResultPre.size()+"--"+jsonResultLine.size()+"--"+json);
                FileOperation.writeFileTrue(filePath + "length.txt", json);
            }else if (jsonResultLine.getString("resultMap").contains("code#####")) {
                FileOperation.writeFileTrue(filePath + "precode.txt", json);
            } else if (jsonResultPre.getString("resultMap").contains("code#####")) {
                FileOperation.writeFileTrue(filePath + "linecode.txt", json);
            } else {
                castTimePre = Integer.parseInt(jsonResultPre.getString("castTime"));
                castTimeLine = Integer.parseInt(jsonResultLine.getString("castTime"));
//                for (FilterEnum ef : FilterEnum.values()) {
//                    if (key.contains(ef.toString())) {
//                        return Pair.of(castTimePre, castTimeLine);
//                    }
//                }
                if (jsonResultPre.getInteger("failCount") > 0 || jsonResultLine.getInteger("failCount") > 0) {
                    FileOperation.writeFileTrue(filePath + "failCount.txt", json);
                } else {
                    String resultAssert = MapCompareTools.compareResult(jsonResultPre, jsonResultLine, resultMap) == "Same map" ? "True【" + json + "】" : "False【" + json + "】";
                    if (resultAssert.contains("False")) {
                        //Error for bug
                        String preJson = JSONObject.toJSONString(jsonResultPre.getJSONObject("resultMap"), SerializerFeature.WriteMapNullValue);
                        String lineJson = JSONObject.toJSONString(jsonResultLine.getJSONObject("resultMap"), SerializerFeature.WriteMapNullValue);
                        System.out.print(resultAssert);
                        System.out.print("---pree:::" + preJson);
                        System.out.println("---line:::" + lineJson);
                        FileOperation.writeFileTrue(filePath + "compare.txt", json);
                    } else {
                        if (castTimeLine > 10000 || castTimePre > 10000) {
                            FileOperation.writeFileTrue(filePath + "bigTime.txt", json);
                        }
                    }
                }
            }
        }else{
            FileOperation.writeFileTrue(filePath + "size.txt", json);
        }
        return Pair.of(castTimePre, castTimeLine);
    }
}
