package com.ttn.WebAutomation.utillib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.BucketNameUtils;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.ttn.WebAutomation.base.BaseLib;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.WebElement;

import com.ttn.WebAutomation.seleniumUtils.DetailReport;
import com.ttn.WebAutomation.seleniumUtils.FrameworkException;
import com.ttn.WebAutomation.utillib.GetPropertyValues;


/**
 * this class contains various utility methods
 * @author TTN
 *
 */
public class Utility {
    
    /**
     * generate the current system date as 
     * per given date format. 
     * @param format
     * @return
     */
    public static String generateCurrentDate(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
    
    /**
     * generate the current system time as 
     * per given format
     * @param format
     * @return
     */
    public static String generateCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
    
    /**
     * gets the current system date +/- variance (in days)  
     * as per given date format
     * @param format
     * @param variance
     * @return
     */
    public static String generateDateWithVarianceInDays(
            String format, int variance) {
        SimpleDateFormat simpleDateFormat 
                = new SimpleDateFormat(format);
        Date date = new Date();
        date = DateUtils.addDays(date, variance);
        return simpleDateFormat.format(date);
    }
    
    /**
     * generate random number of given digit
     * @param digits
     * @return
     */
    public static String generateRandomNumber(int digits) {
        StringBuffer num = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < digits; i++) {
            num.append(random.nextInt(9));
        }
        return num.toString();
    }
    
    /**
     * generate random number of given digit and prefix
     * e.g. PNR123
     * 
     * @param size
     * @param prefix
     * @return
     */
    public static String generateRandomNumberWithPrefix(
            String prefix, int size) {
        return prefix + generateRandomNumber(size);
    }
    
    /**
     * generate random string of given size
     * @param size
     * @return
     */
    public static String generateRandomString(int size) {
        StringBuffer num = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            num.append((char)(65 + (random.nextInt(100) %26)));
        }
        return num.toString();
    }
    
    /**
     * generate random string of given size and prefix
     * @param size
     * @param prefix
     * @return
     */
    public static String generateRandomStringWithPrefix(
            int size, String prefix) {
        return prefix + generateRandomString(size);
    }
    
    /**
     * generate the time with given variance in hours
     * @param format
     * @param variance
     * @return
     */
    public static String generateTimeWithVarianceInHours(
            String format, int variance) {
        SimpleDateFormat simpleDateFormat 
                = new SimpleDateFormat(format);
        Date date = new Date();
        date = DateUtils.addHours(date, variance);
        return simpleDateFormat.format(date);
    }
    
    /**
     * generate the time with given variance in minutes
     * @param format
     * @param variance
     * @return
     */
    public static String generateTimeWithVarianceInMinutes(
            String format, int variance) {
        SimpleDateFormat simpleDateFormat 
                = new SimpleDateFormat(format);
        Date date = new Date();
        date = DateUtils.addMinutes(date, variance);
        return simpleDateFormat.format(date);
    }
    
    /**
     * generates unique string which is based 
     * of current date/time format "ddMMyyyhhmmssSS"
     * e.g. 22022017052848433
     * 
     * @return
     */
    public static String generateUniqueString() {
        SimpleDateFormat simpleDateFormat 
                = new SimpleDateFormat("ddMMyyyhhmmssSS");
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            logger.info("Error occured during wait"
                    + ", while getting unique string", e);
        }
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
    
    /**
     * generates unique string with prefix which is based 
     * of current date/time format "<PNR>ddMMyyyhhmmssSS"
     * e.g. PNR22022017052848433
     * @param prefix
     * @return
     */
    public static String generateUniqueStringWithPrefix(String prefix) {
        return prefix + generateUniqueString();
    }
    
    /**
     * get the test run duration
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static String getDuration(Date startDateTime, Date endDateTime) {
        if (endDateTime != null && startDateTime != null) {
            return getDuration(startDateTime.getTime(),
                    endDateTime.getTime());
        } else {
            return "N/A";
        }
    }
    
    /**
     * get the test run duration
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public static String getDuration(long startDateTime, long endDateTime) {
        long timeDifference = endDateTime - startDateTime;
        long diffSeconds = timeDifference /1000 % 60;
        long diffMinutes = (timeDifference / (60 * 1000)) % 60;
        long diffHours = timeDifference / (60 * 60 * 1000);
        String diffSecondsString = (diffSeconds < 10) 
                ? "0" + diffSeconds : diffSeconds + "";
        String diffMinutesString = (diffMinutes < 10) 
                ? "0" + diffMinutes : diffMinutes + "";
        String diffHoursString = (diffHours < 10) 
                ? "0" + diffHours : diffHours + "";
        return diffHoursString + ":" + diffMinutesString 
                + ":" + diffSecondsString;
    }
    
    /**
     * get formated date as given output format and
     * default input data format as "dd/MM/yyyy"
     * with default locale
     * @param inputDate
     * @param outputFormat
     * @return
     */
    public static String getFormatedDate(String inputDate, String outputFormat) {
    	return getFormatedDate(inputDate, outputFormat, "dd/MM/yyyy");
    }
    
    /**
     * get formated date as given output format and
     * input data format with default locale
     * @param inputDate
     * @param outputFormat
     * @param inputDateFormat
     * @return
     */
    public static String getFormatedDate(String inputDate, String outputFormat, String inputDateFormat) {
		Locale locale = Locale.getDefault();
		return getFormatedDate(inputDate, outputFormat, inputDateFormat, locale);
	}
    
    /**
     * get formated date as given output format, input
     * data format and locale
     * @param inputDate
     * @param outputFormat
     * @param inputDateFormat
     * @param locale
     * @return
     */
    public static String getFormatedDate(String inputDate, String outputFormat, String inputDateFormat,
    		Locale locale) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inputDateFormat, locale);
		Date dateObj = null;
		try {
			dateObj = simpleDateFormat.parse(inputDate);
		} catch (ParseException e) {
			logger.info("Error while parsing input date: " + inputDate);
		}
		simpleDateFormat = new SimpleDateFormat(outputFormat);
		return simpleDateFormat.format(dateObj);
	}
    
    
    /**
     * match the value with given regEx pattern
     * @param value
     * @param patternString
     * @return
     */
    public static boolean regExMatcher(String value,
            String patternString) {
        Pattern pattern = null;
        try {
            pattern = Pattern.compile(patternString);
        } catch (Exception e) {
            new Exception(e.getClass().getName() + " -> Invalid patteren \"" 
                    + patternString + "\"", e);
        }
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    
    /**
     * get formated object repository for given property name and 
     * parameters
     * @param params
     * @return
     */
	public static String[] getFormatedOR(WebElement element, Object... params) {
		
		String locatorTypeAndlocatorValue = element.toString();
    	String locatorType = "";
    	String locatorValue = "";
    	int stringLength = 0;
    	
    	   	
    	if(locatorTypeAndlocatorValue.contains("By.")) {
    		locatorType = locatorTypeAndlocatorValue.split("By.")[1].split(":")[0].trim();
        	stringLength = locatorTypeAndlocatorValue.split("By.")[1].split(":")[1].trim().length();
        	locatorValue = locatorTypeAndlocatorValue.split("By.")[1].split(":")[1].trim().substring(0, stringLength-1);
        	
    	} else if(locatorTypeAndlocatorValue.contains("->")) {
    		locatorType = locatorTypeAndlocatorValue.split("->")[1].split(":")[0].trim();
        	stringLength = locatorTypeAndlocatorValue.split("->")[1].split(":")[1].trim().length();
        	locatorValue = locatorTypeAndlocatorValue.split("->")[1].split(":")[1].trim().substring(0, stringLength-1);
    	}
		
		String formatedORPropertyValue = MessageFormat.format(locatorType+"~"+locatorValue, params);
		
		return formatedORPropertyValue.split("~");
	}


		public static void dumpReportInS3(String ReportLocation) {
		logger.info("Initial Writing "+ReportLocation+ " to S3");

	String accessKey = GetPropertyValues.getGenericProperty("s3.access.key");
	String secretAccessKey = GetPropertyValues.getGenericProperty("s3.secret.access.key");
	String bucketName = GetPropertyValues.getGenericProperty("s3.bucket.name");

	BasicAWSCredentials credentials = new BasicAWSCredentials(
			accessKey,
			secretAccessKey);

	// create a client connection based on credentials
	AmazonS3 s3client = new AmazonS3Client(credentials);

	/*AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();*/
		String ReportName = GlobalVariables.REPORT_NAME.split("-")[0];
		String dateIntermediate = GlobalVariables.REPORT_NAME.split(",")[0];
		String day = dateIntermediate.split("-")[1];
		String month = dateIntermediate.split("-")[2];
		String year = dateIntermediate.split("-")[3];
		String ReportDate = day + "-" + month + "-" + year ;
		String S3ReportPath = ReportName + "/" + ReportDate + "/" + ReportName + "-" + ReportDate + ".html";
		

		//Below code to upload single file
		logger.info("Writing Report "+GlobalVariables.REPORT_NAME+ " to S3");
		logger.info("Report Path : " + GlobalVariables.REPORT_PATH);
    try {
		s3client.putObject(new PutObjectRequest(bucketName, S3ReportPath,
				new File(GlobalVariables.REPORT_PATH)));

		logger.info("Writing Screenshots "+ReportLocation+ " to S3");
		S3ReportPath = ReportName + "/" + ReportDate + "/";
		logger.info(">>>>>>>>>> S3 screenshot location" + S3ReportPath + " <<<<<<<<<<");
		logger.info(">>>>>>>>>> Bucket Name" + bucketName + " <<<<<<<<<<");



		TransferManager tm = new TransferManager(s3client);
		MultipleFileUpload upload = tm.uploadDirectory(bucketName,
				S3ReportPath, new File(ReportLocation), true);

		upload.waitForCompletion();

	} catch (Exception e) {
		e.printStackTrace();
	}
}

    /**
	 * 
	 * @throws InterruptedException
	 */
	public static void SendMailWithAttachment() {
		final String emailIdFrom = GetPropertyValues.getGenericProperty("emailIdFrom");
		final String passwordFrom = GetPropertyValues.getGenericProperty("passwordFrom");
		String emailIdSentTo = GetPropertyValues.getGenericProperty("emailIdTo");

		String fileZipPath = DetailReport.getReportLocation();
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session =null;
		try {
			session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailIdFrom, passwordFrom);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			String ScreenAttached = "";
		String url = GetPropertyValues.getEnvironmentProperty("testURL");;

			System.out.println("== Email Sent Successfully to");
			ScreenAttached = "[With ScreenShot of " + GetPropertyValues.getGenericProperty("snapshotLevel").toUpperCase()
					+ "Steps].";
			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress(emailIdFrom));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailIdSentTo));
			message.setSubject(GetPropertyValues.getGenericProperty("emailSubject"));
			
			BodyPart messageBodyPart1 = new MimeBodyPart();
			// Set the body of email
			int pass = 0, warning = 0, skip = 0, fail = 0, error = 0;

			for (String value : GlobalVariables.TEST_RESULT_COUNT) {
				if (value.equalsIgnoreCase("pass"))
					pass++;
				if (value.equalsIgnoreCase("warning"))
					warning++;
				if (value.equalsIgnoreCase("skip"))
					skip++;
				if (value.equalsIgnoreCase("fail"))
					fail++;
				if (value.equalsIgnoreCase("error"))
					error++;
			}
			messageBodyPart1.setContent("" + "<h3>Hi,</h3>\n\n" + "\n\n"
							+ "<h3>This is an Auto-generated Email by Test Automation containing Report"+ ScreenAttached+"</h3> \r\n" + "\r\n"+ "\r\n"
							+ "URL - "+url+ "<BR><BR>"
							+"<!DOCTYPE html>\n"
							+"<html>\n"
							+"<head>\n" +
							"<style>\n" +
							"table { width:30%;	}\n" +
							"table, th, td { border: 1px solid black;  border-collapse: collapse;}\n" +
							"td { padding: 2px;  text-align: left;}" +
							"th {padding: 2px;  text-align: center;}\n" +
							"table#t01 tr:nth-child(even) {background-color: #eee;}\n" +
							"table#t01 tr:nth-child(odd) {background-color: #fff;}\n" +
							"table#t01 th {background-color: lightblue; color: black;}\n" +
							"</style>\n" +
							"</head>\n" +
							"<body>\n" +
							"<h3>AUTOMATION EXECUTION</h3>\n" +
							"<table id=\"t01\">\n" +
							"  <tr>\n" +
							"    <th colspan=\"2\">TEST EXECUTION SUMMARY</th>\n" +
							"   </tr>\n" +
							"  <tr>\n" +
							"    <td><style=\"color:black;\">TOTAL TEST-CASES : </td>\n" +
							"    <td>"+ GlobalVariables.TEST_RESULT_COUNT.size() +"</td>\n" +
							"   </tr>\n" +
							"  <tr>\n" +
							"    <td style=\"color:green;\">PASS :</td>\n" +
							"    <td style=\"color:green;\">"+pass+"</td>\n" +
							"  </tr>\n" +
							"  <tr>\n" +
							"    <td style=\"color:red;\">FAIL :</td>\n" +
							"    <td style=\"color:red;\">"+fail+"</td>\n" +
							"  </tr>\n" +
							"  \n" +
							"  <tr>\n" +
							"    <td style=\"color:orange;\">WARNING : </td>\n" +
							"    <td style=\"color:orange;\">"+warning+"</td>\n" +
							"  </tr>\n" +
							"   <tr>\n" +
							"    <td style=\"color:gray;\">SKIP : </td>\n" +
							"    <td style=\"color:gray;\">"+skip+"</td>\n" +
							"  </tr>\n" +
							"   <tr>\n" +
							"    <td style=\"color:Tomato;\">ERROR : </td>\n" +
							"    <td style=\"color:Tomato;\">"+error+"</td>\n" +
							"  </tr>\n" +
							"</table>\n" +
							"</body>\n" +
							"</html>\n"+"<BR>"
							+ "<h3>Please refer to the attached report for execution details and Kindly read the instructions given below to Analyze Report.</h3>"
							+ " 1) Do Unzip the attached file"+ "<BR>"
							+ " 2) Here you will find the 'ExtentReport_###-##-####,##_##_##_browserName.html' file."+ "<BR><BR>"+ "<BR>"
							+ "<BR><BR>" + "<BR><BR>" + "Thanks!!",
					"text/html");
			
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();
			ZipDir.zip(fileZipPath);
			logger.info("== Successfully Zip == " + fileZipPath);
			String filename = fileZipPath + ".zip";

			DataSource source = new FileDataSource(filename);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName(filename);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart2);
			multipart.addBodyPart(messageBodyPart1);
			message.setContent(multipart);
			Transport.send(message);
			
			logger.info("== Email Sent Successfully to ==" + emailIdSentTo);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	

    private static Logger logger 
            = Logger.getLogger(Utility.class.getName());

}