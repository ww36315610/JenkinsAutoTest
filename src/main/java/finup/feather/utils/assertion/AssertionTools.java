package finup.feather.utils.assertion;

import com.alibaba.fastjson.JSONObject;
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
            if (jsonResultLine.getString("resultMap").contains("code#####")) {
                FileOperation.writeFileTrue(filePath + "precode.txt", key + "::::" + json);
            } else if (jsonResultPre.getString("resultMap").contains("code#####")) {
                FileOperation.writeFileTrue(filePath + "linecode.txt", key + "::::" + json);
            } else {
                castTimePre = Integer.parseInt(jsonResultPre.getString("castTime"));
                castTimeLine = Integer.parseInt(jsonResultLine.getString("castTime"));
                for (FilterEnum ef : FilterEnum.values()) {
                    if (key.contains(ef.toString())) {
                        System.out.println(json);
                        return Pair.of(castTimePre, castTimeLine);
                    }
                }
                if (jsonResultPre.getInteger("failCount") > 0 || jsonResultLine.getInteger("failCount") > 0) {
                    FileOperation.writeFileTrue(filePath + "failCount.txt", key + "::::" + json);
                } else {
                    String resultAssert = MapCompareTools.compareResult(jsonResultPre, jsonResultLine, resultMap) == "Same map" ? "True【" + json + "】" : "False【" + json + "】";
                    if (resultAssert.contains("False")) {
                        System.out.print(resultAssert);
                        System.out.print("---pree:::" + jsonResultPre.getString("resultMap"));
                        System.out.println("--line:::" + jsonResultLine.getString("resultMap"));
                        FileOperation.writeFileTrue(filePath + "compare.txt", key + "::::" + json);
                    } else {
                        if (castTimeLine > 5000 || castTimePre > 5000) {
                            FileOperation.writeFileTrue(filePath + "bigTime.txt", key + "::::" + json);
                        }
                    }
                }
//                System.out.println(castTimePre + "--" + castTimeLine + "[" + jsonResultPre + "]---[" + jsonResultLine + "]");
            }
        }
        return Pair.of(castTimePre, castTimeLine);
    }
}
