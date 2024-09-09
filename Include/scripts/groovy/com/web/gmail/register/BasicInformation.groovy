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

public class BasicInformation extends WebPageObject {

	TestObject drpBirthMount = createTestObject("drpBirthMount", "//select[@class = 'gNnnTd' and @id='month']")
	TestObject txfBirthDate = createTestObject("txfBirthDate", "//input[@id= 'day']")
	TestObject txfBirthYear = createTestObject("txfBirthYear", "//input[@id= 'year']")
	TestObject drpGender = createTestObject("drpGender", "//select[@id= 'gender']")
	TestObject btnNext = createTestObject("btnNext", "//span[text()='Next']")

	public void selectBirthMonth(String birthMonth) {
		WebUI.selectOptionByLabel(drpBirthMount, birthMonth, false)
	}

	public void inputBirthDate(String birthDate) {
		WebUI.setText(txfBirthDate, birthDate)
	}

	public void inputBirthYear(String birthYear) {
		WebUI.setText(txfBirthYear, birthYear)
	}

	public void selectGender(String month) {
		WebUI.selectOptionByLabel(drpGender, month, false)
	}


	public void tapButtonNext() {
		WebUI.click(btnNext)
	}
}
