package com.facebook.register

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.Select

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
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import internal.GlobalVariable

public class RegisterScreen extends PageObject {

	TestObject txfFirstName = createTestObject("txfFirstName", "//input[@name = 'firstname']")
	TestObject txfSurname = createTestObject("txfSurname", "//input[@name = 'lastname']")
	TestObject txfEmail = createTestObject("txfEmail", "//input[@name = 'reg_email__']")
	TestObject txfReEnterEmail = createTestObject("txfReEnterEmail", "//input[@name = 'reg_email_confirmation__']")
	TestObject txfNewPassword = createTestObject("txfNewPassword", "//input[@name = 'reg_passwd__']")
	TestObject drpDate = createTestObject("drpDate", "//select[@name = 'birthday_day']")
	TestObject drpMonth = createTestObject("drpMonth", "//select[@name = 'birthday_month']")
	TestObject drpYear = createTestObject("drpYear", "//select[@name = 'birthday_year']")
	TestObject radFemale = createTestObject("radFemale", "//span//label[text()= 'Female']")
	TestObject radMale = createTestObject("radMale", "//span//label[text()= 'Male']")
	TestObject radCustome = createTestObject("radCustome", "//span//label[text()= 'Custom']")
	TestObject btnSignUp = createTestObject("btnSignUp", "//button[@type = 'submit' and text() = 'Sign Up']")

	public void inputFirstName(String firstName) {
		WebUI.setText(txfFirstName, firstName)
	}

	public void inputSurname(String surname) {
		WebUI.setText(txfSurname, surname)
	}

	public void inputEmail(String email) {
		WebUI.setText(txfEmail, email)
		WebUI.takeFullPageScreenshot()
	}

	public void inputNewPassword(String NewPassword) {
		WebUI.setText(txfNewPassword, NewPassword)
		WebUI.takeScreenshot("Andika")
	}

	public void selectDate(String date) {
		WebElement dropdown = WebUI.findWebElement(drpDate, 0)
		Select select = new Select(dropdown)
		select.selectByValue(date)
	}
	
	public void selectMonth(String month) {
		WebElement dropdown = WebUI.findWebElement(drpMonth, 0)
		Select select = new Select(dropdown)
		select.selectByValue(month)
	}
	
	public void selectYear(String year) {
		WebElement dropdown = WebUI.findWebElement(drpYear, 0)
		Select select = new Select(dropdown)
		select.selectByValue(year)
	}
	
	public void tapGanderMale() {
		WebUI.click(radMale)
	}
	
	public void inputReEnterEmail(String email) {
		WebUI.setText(txfReEnterEmail, email)
	}
	
	public void tapSingUp() {
		WebUI.click(btnSignUp)
	}
	
	public void reportCreateRegister() {
		List<TestObject> to = List.of(txfFirstName, txfSurname, txfEmail, txfReEnterEmail, btnSignUp, radMale, drpYear, drpMonth, drpDate, txfNewPassword);
		WebService.takeReportScreenshot(to, "Create New Register Account")
	}
}
