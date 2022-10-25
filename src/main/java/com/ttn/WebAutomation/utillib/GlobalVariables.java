package com.ttn.WebAutomation.utillib;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GlobalVariables {

	public static final String TEST_CASE_WORKBOOK_NAME = "TestCaseList.xlsx";
	public static final String TEST_CASE_1st_SHEET_NAME = "TestCases";
	public static final String TEST_CASE_1st_SHEET_1st_COLUMN = "ClassName";
	public static final String TEST_CASE_1st_SHEET_2nd_COLUMN = "TestPackage";
	public static final String TEST_CASE_1st_SHEET_3rd_COLUMN = "ExecutionStatus";
	public static final String TEST_CASE_1st_SHEET_4th_COLUMN = "Groups";
	public static final String TEST_CASE_2nd_SHEET_NAME = "Browsers";
	public static final String TEST_CASE_2nd_SHEET_Browser_COLUMN = "Browsers";
	public static final String TEST_CASE_2nd_SHEET_ExeStatus_COLUMN = "ExecutionStatus";
	public static final String TEST_CASE_2nd_SHEET_BrowserVersion_COLUMN = "Browser_Version";
	public static final String TEST_CASE_2nd_SHEET_OS_COLUMN = "OS";
	public static final String TEST_CASE_2nd_SHEET_OSVersion_COLUMN = "OS_Version";
	public static final String TEST_CASE_2nd_SHEET_Env_COLUMN = "Environment";
	private static File directory = new File(".");
	public static String PROPERTY_FILE_NAME = "generic.properties";
	public static String LOG4J_FILE_NAME = "log4j.properties";
	public static String EXTENT_CONFIG_NAME = "extent-config.xml";
	public static String REPORT_NAME = "";
	public static String REPORT_PATH = "";
	public static String RESULT_BASE_LOCATION ="";
	public static String MYSQL_URL = "mysql.url";
	public static String MYSQL_USER = "mysql.user";
	public static String MYSQL_PASSWORD = "mysql.password";
	public static String BROWSER = "browser";
	public static String OS = "os";
	public static String TESTLINK_URL = "url";
	public static String TESTLINK_DEVKEY = "devKey";
	public static String TESTLINK_BUILDNAME = "buildName";
	public static String TESTLINK_TESTPLANID = "testPlanID";
	public static String JIRA_URL = "jiraURL";
	public static String JIRA_USERNAME = "username";
	public static String JIRA_PASSWORD = "password";
	public static String JIRA_PROJECTNAME = "projectName";
	public static String JIRA_DEFECTTYPE = "defectType";
	public static String SEP = System.getProperty("file.separator");
	public static String CURRENTDir = System.getProperty("user.dir");

	public static String DOWNLOAFFILEPATH = (System.getProperty("user.dir") + GlobalVariables.SEP + "temp/downloads");
	public static String SCREENSHOT = CURRENTDir + GlobalVariables.SEP
			+ "temp/Screen";
	public static final String TEST_CASE_SHEET_PATH = CURRENTDir + SEP + "resources/xlfiles/"
			+ TEST_CASE_WORKBOOK_NAME;

	public static ArrayList<String> TEST_RESULT_COUNT = new ArrayList<>();

	public static String getPropertiesPath() throws IOException {
		return directory.getCanonicalPath() + File.separator + "src/main/java/com/ttn/WebAutomation/properties"
				+ File.separator;
	}

	public static String getExtentCongigPath() throws IOException {
		return directory.getCanonicalPath() + GlobalVariables.SEP + System.getProperty("user.dir")+GlobalVariables.SEP +"target/"+GlobalVariables.SEP+"screenshot/" + GlobalVariables.SEP;
	}
	public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyy hh:mm:ss");
}
