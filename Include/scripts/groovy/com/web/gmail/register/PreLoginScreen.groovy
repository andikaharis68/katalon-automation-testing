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
import com.mandiri.supports.WebSupport

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import internal.GlobalVariable

public class PreLoginScreen extends WebPageObject {

	TestObject btnCreateAccount = createTestObject("btnCreateAccount", "(//div[@class = 'dropdown__label'])[1]")
	TestObject btnCreateForPersonal = createTestObject("btnCreateForPersonal", "(//div[@class = 'dropdown__options']/a[text()= 'For my personal use'])[1]")

	public void tapButtonCreateAccount() {
		WebUI.click(btnCreateAccount)
	}

	public void tapButtonCreateForPersonal() {
		WebUI.click(btnCreateForPersonal)
	}

	public void takeReportScreenShoot() {
		WebSupport.takeScreenshot()
	}
}
