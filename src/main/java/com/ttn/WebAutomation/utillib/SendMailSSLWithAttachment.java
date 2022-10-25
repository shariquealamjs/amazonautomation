package com.ttn.WebAutomation.utillib;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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

import com.ttn.WebAutomation.base.BaseLib;
//import com.ttn.WebAutomation.base.BaseTest;

/**
 * This Java program demonstrates how to Send Extent Report in Mail
 * @author TTN
 *
 */

public class SendMailSSLWithAttachment extends BaseLib {

	public static void sendEmail(String email_from, String email_password, String to) {

		 final String from = email_from;
		 final String password = email_password;
		// Create object of Property file
		Properties props = new Properties();

		// this will set host of server- you can change based on your requirement
		props.put("mail.smtp.host", "smtp.gmail.com");

		// set the port of socket factory
		props.put("mail.smtp.socketFactory.port", "465");

		// set socket factory
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// set the authentication to true
		props.put("mail.smtp.auth", "true");

		// set the port of SMTP server
		props.put("mail.smtp.port", "465");

		// This will handle the complete authentication
		Session session = Session.getDefaultInstance(props,

				new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, password);

					}

				});

		try {

			// Create object of MimeMessage class
			Message message = new MimeMessage(session);

			// Set the from address
			message.setFrom(new InternetAddress(email_from));

			// Set the recipient address
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Add the subject link
			//message.setSubject(GlobalVariables.REPORT_NAME);
			DateFormat dateFormat =new SimpleDateFormat("dd MMM YYYY_hh:mm:ss");
			Date date = new Date();
			String reportName = dateFormat.format(date);

			// Create object to add multimedia type content
			BodyPart messageBodyPart1 = new MimeBodyPart();

			int pass = 0;
			int warning = 0;
			int skip = 0;
			int fail = 0;
			int error = 0;

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


			if(pass !=0)
			{
				message.setSubject("Automation Test Report "+reportName+"   Status : "+"Passed ***");

				messageBodyPart1.setContent(
						"<h3  style=\"color:green;\">Test Completed Successfully</h3>\n"
								+ "<p style=\"color:black;\"><b><u>Job Overall Status : </u></b></p>\n\n"
								+ "<p style=\"color:black;\">=>>  Please refer attached report for execution details</p>",

						"text/html");
			}
			else
			{
				message.setSubject("Automation Test Report "+reportName+"   Status : "+"Failed ***");
				//add time as well
				messageBodyPart1.setContent(
						"<h3 style=\"color:red;\">TEST Failed </h3>\n"
								+ "<p style=\"color:black;\"><b><u>Job Overall Status : </u></b></p>\n\n"
								+ "<p style=\"color:black;\">=>>  Please refer attached report for execution details</p>",

						"text/html");
			}

			// Create another object to add another content
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			File file = new File(GlobalVariables.REPORT_PATH);

			// Mention the file which you want to send
			String filename = file.getPath();

			// Create data source and pass the filename
			DataSource source = new FileDataSource(filename);

			// set the handler
			messageBodyPart2.setDataHandler(new DataHandler(source));

			// set the file
			messageBodyPart2.setFileName(GlobalVariables.REPORT_NAME);

			// Create object of MimeMultipart class
			Multipart multipart = new MimeMultipart();

			// add body part 1
			multipart.addBodyPart(messageBodyPart2);

			// add body part 2
			multipart.addBodyPart(messageBodyPart1);

			// set the content
			message.setContent(multipart);

			// finally send the email
			Transport.send(message);

			System.out.println("=====Email Sent=====");
		} catch (MessagingException e) {

			throw new RuntimeException(e);

		}

	}

}