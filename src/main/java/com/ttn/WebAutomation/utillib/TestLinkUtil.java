package com.ttn.WebAutomation.utillib;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

import com.ttn.WebAutomation.base.BaseLib;
//import com.ttn.WebAutomation.base.BaseTest;

import br.eti.kinoshita.testlinkjavaapi.TestLinkAPI;
import br.eti.kinoshita.testlinkjavaapi.constants.ExecutionStatus;

public class TestLinkUtil extends BaseLib {

    private static String buildName;
    private static int testplan_id;
    private static TestLinkAPI testlinkAPIClient;


    //setResult method called by TestMethodListener class for success,failure and skipped test case
    synchronized public static void setResult(int testcaseId, ExecutionStatus status){

        testlinkAPIClient.setTestCaseExecutionResult(testcaseId, null, testplan_id, status, null, null, buildName, null, false, null, null, null, null,false);
    }


    public static void setTestPlan() throws MalformedURLException{


           testplan_id = Integer.parseInt(properties.get(GlobalVariables.TESTLINK_TESTPLANID).toString());

           buildName = properties.getProperty(GlobalVariables.TESTLINK_BUILDNAME);

           LocalDateTime localDate = LocalDateTime.now();

           testlinkAPIClient = new TestLinkAPI(new URL(properties.getProperty(GlobalVariables.TESTLINK_URL)),
                   properties.getProperty(GlobalVariables.TESTLINK_DEVKEY));

           buildName = buildName + "_" + localDate;

           testlinkAPIClient.createBuild(testplan_id, buildName, "new build according to the date");

    }

}
