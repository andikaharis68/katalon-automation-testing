package com.facebook.login

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.company.pageobject.PageObject
import com.company.services.WebService
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import internal.GlobalVariable

public class LoginScreen extends PageObject {

	TestObject btnRegis = createTestObject("btnRegis", "//*[text() = 'Create new account']")
	TestObject btnSidebar = createTestObject("btnSidebar", "//*[text() = 'Open Menu']")
	TestObject btnAbout = createTestObject("btnAbout", "//*[text() = 'About']")

	public void tapButtonRegister() {
		WebUI.click(btnRegis)
	}
	
	public void tapBar() {
		WebUI.click(btnSidebar)
	}
	
	public void tapAbout() {
		WebUI.click(btnAbout)
	}
	
	public void takeReportClickButtonLogin() {
		WebService.takeReportScreenshot(btnRegis)
	}
}
