package com.ttn.WebAutomation.utillib;

/**
 * This Java program demonstrates how read data from Google Sheet.

 *
 * @author TTN
 */
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ttn.WebAutomation.base.BaseLib;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.testng.ITestContext;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

public class GoogleSheetReader {

   public int totalRows;
    public HashMap<String, HashMap<String, HashMap<String, String>>> oWorkbookData = new HashMap<>();

   // public static HashMap<String, HashMap<String, HashMap<String, String>>> testData = new HashMap<String, HashMap<String, HashMap<String, String>>>();
    public DataFormatter formatter = new DataFormatter();
 
    
    
	protected static Logger log = Logger.getLogger(BaseLib.class.getName());
    private static final String APPLICATION_NAME = "PRISM";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    public HashMap<String,Integer> rowCount = new HashMap<>();
    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    public static File directory = new File(".");//SheetsScopes.SPREADSHEETS_READONLY
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
    private static String CREDENTIALS_FILE_PATH = System.getProperty("user.dir") + "/credentials.json";
   public static HashMap<String, Hashtable<String, String>> oSheetData = new HashMap<>();
   
   public Object[][] oData = new Object[1][1];

    public  void initData(ITestContext iTestContext,String sheetName,String testCaseName) throws IOException, GeneralSecurityException {
      
    	List<List<Object>> values = connectWithGoogleSheets(iTestContext, sheetName);
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
            log.info("No Data found");
        } else {

       
            oSheetData = readDataSheet(values, totalRows - 1, sheetName);
          //Added
            oData = getData(values,sheetName,testCaseName);
            System.out.println("Sheetname: "+ sheetName);
            System.out.println("totalRows: "+ totalRows);
        

        }
    }


    private List<List<Object>> connectWithGoogleSheets(ITestContext iTestContext,String sheetName) throws IOException, GeneralSecurityException {

            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final String spreadsheetId = "";//Sheet id
      
            
           final String range = sheetName +"!A1:AZ";
          
        
          

            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();


            ValueRange result = service.spreadsheets().values().get(spreadsheetId, range).execute();
        
            
            
            totalRows = result.getValues() != null ? result.getValues().size() : 0;
      
            
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
         
            return response.getValues();
        
        }



    public HashMap<String, Hashtable<String, String>> readDataSheet(List<List<Object>> values, int totalRows, String sheetNam) {
        HashMap<String, Hashtable<String, String>> sheet_data = new HashMap<String, Hashtable<String, String>>();
        Random rand = new Random();
        int numRows=0;
        try {
            for (int i = 1; i <= totalRows; i++) {


              try{
                 String val = (String) values.get(i).get(0);

                  numRows++;
                  sheet_data.put((String) values
                          .get(i).get(0) + "." + rand.nextInt(10000), getRowData(values, i));
                }
              catch (IndexOutOfBoundsException e){

              }

            }

            rowCount.put(sheetNam, numRows);

        } catch (Exception e) {
            // log.error(e);
            e.printStackTrace();
        }

         return sheet_data;
    }

    public Hashtable<String, String> getRowData(List<List<Object>> values, int rowNum) {
    
        List<Object> headerRow = values.get(0);
        Hashtable<String, String> rowData = new Hashtable<String, String>();
      
            for (int columnNumber = 0; columnNumber < values.get(0).size(); columnNumber++) {
               

                try {

                    rowData.put((String) headerRow.get(columnNumber), (String) values
                            .get(rowNum).get(columnNumber));
                } catch (IndexOutOfBoundsException e) {
                    rowData.put((String) headerRow.get(columnNumber), "");
                }

            }
        //}
        return rowData;
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
       // InputStream in = GoogleSheetReader.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);

        System.out.println("***************"+in);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static String[] getSheetList(HashMap<String, HashMap<String, HashMap<String, String>>> oWorkbookData) {
    	int i = 0;
        String[] arrayExcelData = null;
    	try {
            System.out.println("error     " + oWorkbookData.size());
            arrayExcelData = new String[oWorkbookData.size()];

            for (String sheetName : oWorkbookData.keySet()) {


                arrayExcelData[i] = sheetName;
                i++;
            }

        }
    	catch (Exception e){
    	    System.out.println("eeor to mil gye   " + e.getMessage());
        }
    	return arrayExcelData;
    	}
    public Object[][] getData(List<List<Object>> values, String sheetName, String testCaseName)
    {
        int testStartRowNum = 0;
        int colStartRowNum = testStartRowNum + 1;
        int totalCols = 0;
        int dataStartRowNum = 1;
        Hashtable<String, String> table = null;

        int count = 0;
        for(int rNum = dataStartRowNum; rNum < (dataStartRowNum + totalRows -1); rNum++)
        {

            table = getRowData(values,rNum);
            if(table.get("TCID").equals(testCaseName))
            {
                if(table.get("RUNMODE").equals("Y"))
                {
                    count++;
                }

            }}
        Object[][] data = new Object[count][1];

        for (int rNum = dataStartRowNum; rNum < (dataStartRowNum + totalRows-1); rNum++)
        {

            int index = 0;
         
            table = getRowData(values,rNum);
            if(table.get("TCID").equals(testCaseName))
            {
                if(table.get("RUNMODE").equals("Y"))
                {
                    data[index][0] = table;
                    index++;


                }}}




        return data;
    }
    

}






