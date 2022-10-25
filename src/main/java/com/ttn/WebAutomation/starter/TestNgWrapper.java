package com.ttn.WebAutomation.starter;

public class TestNgWrapper {
	String TestPackage, ExecutionStatus, Groups, ClassName;

	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public String getTestPackage() {
		return TestPackage;
	}

	public void setTestPackage(String testPackage) {
		TestPackage = testPackage;
	}

	public String getExecutionStatus() {
		return ExecutionStatus;
	}

	public void setExecutionStatus(String executionStatus) {
		ExecutionStatus = executionStatus;
	}

	public String getGroups() {
		return Groups;
	}

	public void setGroups(String groups) {
		Groups = groups;
	}

}
