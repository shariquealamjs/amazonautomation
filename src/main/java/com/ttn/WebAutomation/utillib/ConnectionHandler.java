package com.ttn.WebAutomation.utillib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.ttn.WebAutomation.base.BaseLib;
//import com.ttn.WebAutomation.base.BaseTest;

public class ConnectionHandler extends BaseLib {

	private static Connection rdsConn;
	private static Statement rdsStat;

	public static Statement connectToRds() {

		try {

			log.info("Making connection with RDS");
			rdsConn = DriverManager.getConnection(properties.getProperty(GlobalVariables.MYSQL_URL),
					properties.getProperty(GlobalVariables.MYSQL_URL),
					properties.getProperty(GlobalVariables.MYSQL_URL));

			rdsStat = rdsConn.createStatement();

		} catch (SQLException se) {
			se.printStackTrace();
//			log.error("Error connecting to MySql  :\n" + se);
			log.info("Error connecting to MySql  :\n" + se);

		}

		return rdsStat;

	}

	public static void closeConnection() {
		log.info("Closing Db connections");

		if (null != rdsStat) {
			try {
				rdsStat.close();
				log.info("My SQL connection closed");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
