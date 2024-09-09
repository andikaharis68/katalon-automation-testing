package com.mandiri.services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.company.pageobject.PageObject
import com.company.services.WebService
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import internal.GlobalVariable

public class WebServices {
	def static void getDataBeforeTestCase(TestCaseContext testCaseContext) {
		String pathTC = testCaseContext.getTestCaseId()
		TestCase testCaseName = findTestCase(pathTC)
		GlobalVariable.Current_Test_Case = testCaseName.getName().replace("_", " ")
		GlobalVariable.Current_Test_Case_Desc = testCaseName.getDescription()
		KeywordUtil.logInfo(GlobalVariable.Current_Test_Case_Desc)
	}

	def static void getDataBeforeTestSuites(TestSuiteContext testSuiteContext) {
		KeywordUtil.logInfo(testSuiteContext.getTestSuiteId())
		GlobalVariable.Current_Test_Suites = testSuiteContext.getTestSuiteId().split("/")[1]
		GlobalVariable.Path_Test_Report = RunConfiguration.getReportFolder()
	}	
}
