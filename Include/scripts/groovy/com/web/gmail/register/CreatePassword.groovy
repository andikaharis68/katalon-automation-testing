package com.web.gmail.register

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
import com.mandiri.pageobject.WebPageObject
import com.mandiri.supports.WebSupport

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import internal.GlobalVariable

public class CreatePassword extends WebPageObject {
	
	TestObject txfPassword = createObjectByXpath("txfPassword", "//div[text() = 'Password']//preceding-sibling::input[@type = 'password']")
	TestObject txfConfirm = createObjectByXpath("txfConfirm", "//div[text() = 'Confirm']//preceding-sibling::input[@type = 'password']")
	TestObject btnNext = createObjectByXpath("btnNext", "//span[text()='Next']")
	
	public void inputPassword(String password) {
		WebUI.setText(txfPassword, password)
	}
	
	public void inputConfirm(String confirm) {
		WebUI.setText(txfConfirm, confirm)
	}
	
	public void tapButtonNext() {
		WebUI.click(btnNext)
	}
	
	public void takeReport() {
		ArrayList tos = new ArrayList()
		tos.add(txfPassword)
		tos.add(txfConfirm)
		tos.add(btnNext)
		WebSupport.takeScreenshotWithHightlight(tos)
	}
	
}
