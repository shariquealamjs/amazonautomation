package com.ttn.WebAutomation.starter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.ttn.WebAutomation.utillib.GlobalVariables;
import com.ttn.WebAutomation.utillib.XLReader;

public class TestNgCreator {

	Map<String, String> params = new HashMap<>();
	XLReader reader = new XLReader(GlobalVariables.TEST_CASE_SHEET_PATH);
	File file = null;
	FileWriter writer = null;
	TestNG testng = null;
	XmlSuite suite = new XmlSuite();
	XmlClass testClass = null;
	int threadCount=1;

	ArrayList<TestNgWrapper> testCaseList = new ArrayList<TestNgWrapper>();
	ArrayList<BrowserWrapper> browserList = new ArrayList<BrowserWrapper>();

	ArrayList<XmlSuite> SuiteList = new ArrayList<XmlSuite>();
	ArrayList<XmlTest> TestList = new ArrayList<XmlTest>();

	List<Class<? extends ITestNGListener>> listenerClass = new ArrayList<Class<? extends ITestNGListener>>();

	// This method is reading test case data from excel and adding it to list.
	public void getTestCaseListFromExcel() {
		int rowCount;

		rowCount = reader.getRowCount(GlobalVariables.TEST_CASE_1st_SHEET_NAME);
		for (int i = 2; i <= rowCount; i++) {
			TestNgWrapper testCaseObj = new TestNgWrapper();
			testCaseObj.setClassName(reader.getCellData(GlobalVariables.TEST_CASE_1st_SHEET_NAME,
					GlobalVariables.TEST_CASE_1st_SHEET_1st_COLUMN, i));
			testCaseObj.setTestPackage(reader.getCellData(GlobalVariables.TEST_CASE_1st_SHEET_NAME,
					GlobalVariables.TEST_CASE_1st_SHEET_2nd_COLUMN, i));
			testCaseObj.setExecutionStatus(reader.getCellData(GlobalVariables.TEST_CASE_1st_SHEET_NAME,
					GlobalVariables.TEST_CASE_1st_SHEET_3rd_COLUMN, i));
			testCaseObj.setGroups(reader.getCellData(GlobalVariables.TEST_CASE_1st_SHEET_NAME,
					GlobalVariables.TEST_CASE_1st_SHEET_4th_COLUMN, i));
			testCaseList.add(testCaseObj);
		}
	}

	// This method is getting browser data from excel and adding it to list.
	public void getBrowserListFromExcel() {
		int rowCount;

		rowCount = reader.getRowCount(GlobalVariables.TEST_CASE_2nd_SHEET_NAME);
		for (int i = 2; i <= rowCount; i++) {
			BrowserWrapper browserObj = new BrowserWrapper();
			browserObj.setBrowser(reader.getCellData(GlobalVariables.TEST_CASE_2nd_SHEET_NAME,
					GlobalVariables.TEST_CASE_2nd_SHEET_Browser_COLUMN, i));
			browserObj.setExecution_status(reader.getCellData(GlobalVariables.TEST_CASE_2nd_SHEET_NAME,
					GlobalVariables.TEST_CASE_2nd_SHEET_ExeStatus_COLUMN, i));
			browserList.add(browserObj);
		}
	}

	public void getEnvFromExcel() {
		String environment;

		environment = reader.getCellData(GlobalVariables.TEST_CASE_2nd_SHEET_NAME, GlobalVariables.TEST_CASE_2nd_SHEET_Env_COLUMN, 2);
		params.put("Environment", environment);
	}

	public void initiateTestProcess() throws IOException {

		testng = new TestNG();

		getTestCaseListFromExcel();
		getEnvFromExcel();
		getBrowserListFromExcel();
		
		setUpTestNgSuite();
		SuiteList.add(suite);
		testng.setXmlSuites(SuiteList);
		testng.run();
	}

	public void setUpTestNgSuite() throws IOException {
		SuiteList = new ArrayList<XmlSuite>();

		createXmlSuite("Test_Suite_1");
		createXmlTest();
		setListeners();
		createTestNgFile(suite, "DynamicTestNG");
	}

	public void setListeners() {
		ArrayList<String> listeners = new ArrayList<>();
		suite.setListeners(listeners);
	}

	public void createXmlSuite(String suiteName) {
		suite.setThreadCount(browserList.size());
		suite.setParallel(XmlSuite.ParallelMode.TESTS);
		suite.setName(suiteName);
		suite.setParameters(params);
	}

	public void createXmlTest() throws IOException {
		for (int testNumber = 0; testNumber < browserList.size(); testNumber++) {
			ArrayList<XmlClass> classesList = new ArrayList<XmlClass>();
			if (browserList.get(testNumber).getExecution_status().equalsIgnoreCase("yes")) {
				XmlTest test = new XmlTest(suite);
				test.setName("Test_" + testNumber);
				test.setPreserveOrder(true);
				test.addParameter("Browser", browserList.get(testNumber).getBrowser());
				addXmlClasses(classesList);
				test.setXmlClasses(classesList);
				TestList.add(test);
			}
		}
		suite.setTests(TestList);
	}

	public ArrayList<XmlClass> addXmlClasses(ArrayList<XmlClass> classesList) throws IOException {
		for (int classNumber = 0; classNumber < testCaseList.size(); classNumber++) {
			if (testCaseList.get(classNumber).getTestPackage().equalsIgnoreCase("tests")) {
				if (testCaseList.get(classNumber).getExecutionStatus().equalsIgnoreCase("yes")) {
					testClass = new XmlClass();

					testClass.setName("com.ttn.WebAutomation.tests." + testCaseList.get(classNumber).getClassName());

					classesList.add(testClass);
				}
			} else {
				if (testCaseList.get(classNumber).getExecutionStatus().equalsIgnoreCase("yes")) {
					testClass = new XmlClass();

					testClass.setName("com.ttn.WebAutomation.tests." + testCaseList.get(classNumber).getTestPackage()
							+ "." + testCaseList.get(classNumber).getClassName());

					classesList.add(testClass);
				}
			}
		}
		return classesList;
	}

	public void createTestNgFile(XmlSuite suite, String fileName) throws IOException {
		file = new File(fileName + ".xml");
		FileWriter writer = new FileWriter(file);
		writer.write(suite.toXml());
		writer.close();
	}
	public int calculateThreadCount() {
		int threadCount=1;
		for(int i=0; i<browserList.size();i++) {
			if(browserList.get(i).getExecution_status().equalsIgnoreCase("yes"))
				threadCount++;
		}
		
		return threadCount;
		
	}

}
