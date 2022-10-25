package com.ttn.WebAutomation.starter;

import java.io.IOException;


public class Launcher {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		TestNgCreator ngCreator = new TestNgCreator();
		try {
			ngCreator.initiateTestProcess();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
