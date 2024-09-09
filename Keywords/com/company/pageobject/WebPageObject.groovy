package com.company.pageobject

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
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class WebPageObject {

	public createObjectByXpath(String name, String xpath) {
		TestObject testObject = new TestObject(name)
		testObject.addProperty("xpath", ConditionType.EQUALS, xpath)
		return testObject
	}

	public createObjectById(String name, String id) {
		TestObject testObject = new TestObject(name)
		testObject.addProperty("resource_id", ConditionType.EQUALS, id)
		return testObject
	}

	public TestObject createTestObject(String name, String xpath) {
		TestObject testObject = new TestObject()
		testObject.addProperty("xpath", ConditionType.EQUALS, xpath)
		return testObject
	}
}
