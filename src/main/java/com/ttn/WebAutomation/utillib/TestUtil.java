
package com.ttn.WebAutomation.utillib;
/**
 
 * @author TTN
 */

import java.util.Hashtable;

import com.ttn.WebAutomation.utillib.PublicVariables;

public class TestUtil {
	public static boolean isExecutable(String testName, XLReader xls) {

		for (int rowNum = 2; rowNum <= xls.getRowCount(PublicVariables.TEST_CASE_SHEET_NAME); rowNum++) {
			// System.out.println("+++++Check if test case Runmode is
			// Enabled+++++");
			try {
				if (xls.getCellData(PublicVariables.TEST_CASE_SHEET_NAME, PublicVariables.TEST_CASE_SHEET_1ST_COLUMN,
						rowNum).equals(testName)) {
					if (xls.getCellData(PublicVariables.TEST_CASE_SHEET_NAME, PublicVariables.TEST_CASES_RUNMODE_COLUMN,
							rowNum).equals(PublicVariables.RUNMODE_YESVALUE))
						return true;
					else
						return false;
				}
			} catch (Exception ex) {
				System.out.println("------Could not find if the test case is executable-------" + ex.getMessage());
				//
			}
		}
		return false;
	}

	public static Object[][] getData(String testName, XLReader xls) {
		int testStartRowNum = 0;
		
		try {
			for (int rNum = 1; rNum <= xls.getRowCount(PublicVariables.TEST_CASES_DATA_SHEET_NAME); rNum++) {
				try {
					

					if (xls.getCellData(PublicVariables.TEST_CASES_DATA_SHEET_NAME, 0, rNum).equals(testName)) {
						testStartRowNum = rNum;
						break;
					}
				} catch (Exception ex) {
					System.out.println("------++++Test Case Skipped+++++------" + testName+","+PublicVariables.TEST_CASES_DATA_SHEET_NAME+","+rNum);
					System.out.println("------Print Exception Message------" + ex);

				}
			}
		} catch (Exception e1) {
			System.out.println("=========Check for Test case Name Or Runmode==========" + e1.getMessage());
		}
		int colStartRowNum = testStartRowNum + 1;
		int totalCols = 0;
		while (!xls.getCellData(PublicVariables.TEST_CASES_DATA_SHEET_NAME, totalCols, colStartRowNum).equals("")) {
			totalCols++;
		}
		int dataStartRowNum = testStartRowNum + 2;
		int totalRows = 0;
		while (!xls.getCellData(PublicVariables.TEST_CASES_DATA_SHEET_NAME, 0, dataStartRowNum + totalRows)
				.equals("")) {
			totalRows++;
		}

		Object[][] data = new Object[totalRows][1];
		int index = 0;
		Hashtable<String, String> table = null;
		try {
			System.out.println(">>>>>>>>>>>>DataStartRowNum: " + dataStartRowNum);
			System.out.println(">>>>>>>>>>>>(dataStartRowNum + totalRows): " + (dataStartRowNum + totalRows));
			System.out.println(">>>>>>>>>>>>totalCols: " + totalCols);
			System.out.println("++--Test Case:--++" + testName);
			for (int rNum = dataStartRowNum; rNum < (dataStartRowNum + totalRows); rNum++) {
				table = new Hashtable<String, String>();
				Thread.sleep(3000);

				for (int cNum = 0; cNum < totalCols; cNum++) {
				
					
					table.put(xls.getCellData(PublicVariables.TEST_CASES_DATA_SHEET_NAME, cNum, colStartRowNum),
							xls.getCellData(PublicVariables.TEST_CASES_DATA_SHEET_NAME, cNum, rNum));
					Thread.sleep(3000);
				
				}
				data[index][0] = table;
				index++;
			}
		} catch (Exception ex) {
			System.out.println("Exception caused by : " + ex.getMessage());
			return null;
		}

		return data;
	}
}
