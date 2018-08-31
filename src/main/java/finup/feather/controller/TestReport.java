package finup.feather.controller;


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

}