package com.ttn.WebAutomation.starter;

public class BrowserWrapper {
	String browser_version, os_version;
	String browser, os, environment, execution_status;

	public String getExecution_status() {
		return execution_status;
	}

	public void setExecution_status(String execution_status) {
		this.execution_status = execution_status;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getBrowser_version() {
		return browser_version;
	}

	public void setBrowser_version(String browser_version) {
		this.browser_version = browser_version;
	}

	public String getOs_version() {
		return os_version;
	}

	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

}
