package finup.feather.controller;


import com.alibaba.fastjson.JSONObject;
import finup.feather.utils.listenter.TestngRetry;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestReport {

    @BeforeMethod
    public void before() {

    }

    @Test//(retryAnalyzer = TestngRetry.class)
    public void runnerF1() {
        Assert.assertEquals("aa", "bb", "FFFF1111message");
    }

    @Test//(retryAnalyzer = TestngRetry.class)
    public void runnerF2() {
        Assert.assertTrue(false);
    }

    @Test//(retryAnalyzer = TestngRetry.class)
    public void runnerT1() {
        Assert.assertEquals("aa", "aa", "TTTT1111message");
    }

    @Test//(retryAnalyzer = TestngRetry.class)
    public void runnerT2() {
        Assert.assertTrue(true);
    }


    public static void main(String[] args) {
        String param = "{\"aa\":\"AA\",\"bb\":{\"cc\":\"CC\"}}";
        JSONObject jsonObject = JSONObject.parseObject(param);
        System.out.println("111"+jsonObject);
        jsonObject.getJSONObject("bb").put("cc","PPPPPP");
        System.out.println("222"+jsonObject);
        JSONObject rep = jsonObject.getJSONObject("bb");
        System.out.println("bbb"+rep);
        jsonObject.put("rr",rep.toJSONString());
        System.out.println("333"+jsonObject);
        jsonObject.remove("bb");
        System.out.println("444"+jsonObject);
    }
}