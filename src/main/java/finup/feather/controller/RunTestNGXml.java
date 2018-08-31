package finup.feather.controller;

import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class RunTestNGXml {


    public static void main(String[] args) {
        TestNG testNG = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add("./Testng.xml");
//        suites.add("./test-output/testng-failed.xml");
        testNG.setTestSuites(suites);
        testNG.run();


//        TestNG testNG1 = new TestNG();
//        List<String> suites1 = Lists.newArrayList();
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        suites1.add("./test-output/testng-failed.xml");
//        testNG1.setTestSuites(suites1);
//        testNG1.run();

    }


}
