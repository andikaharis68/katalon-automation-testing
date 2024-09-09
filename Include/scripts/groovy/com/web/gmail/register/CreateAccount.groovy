package com.web.gmail.register

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

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
import com.mandiri.pageobject.WebPageObject
import com.mandiri.services.WebServices
import com.mandiri.supports.WebSupport

import static org.assertj.core.api.InstanceOfAssertFactories.LIST

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import internal.GlobalVariable

public class CreateAccount extends WebPageObject {

	TestObject txfFirstName = createTestObject("txfFirstName", "//input[@name = 'firstName']")
	TestObject txfLastName = createTestObject("txfLastName", "//input[@name = 'lastName']")
	TestObject btnNext = createTestObject("btnNext", "//span[text() = 'Next']")

	public void inputFisrtName(String firstName) {
		WebUI.setText(txfFirstName, firstName)
	}

	public void inputLastName(String lastName) {
		WebUI.setText(txfLastName, lastName)
	}

	public void tapButtonNext() {
		WebUI.click(btnNext)
	}
	
	public void takeReportImage() {
		ArrayList tos = new ArrayList()
		tos.add(txfFirstName)
		tos.add(txfLastName)
		tos.add(btnNext)
		WebSupport.takeScreenshotWithHightlight(tos)
	}
}
