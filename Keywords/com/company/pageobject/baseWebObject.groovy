package com.company.pageobject

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.openqa.selenium.Point

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.SelectorMethod
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class BaseWebObject {

	public BaseWebObject() {
	}

	public String getXpath(TestObject obj) {
		return obj.findPropertyValue("xpath")
	}

	public TestObject createTestObjectByXpath(String objName, String xpath) {
		println "Creating object with xpath: $xpath"
		TestObject testObj = new TestObject(objName)
		testObj.addProperty("xpath", ConditionType.EQUALS, xpath)
		testObj.selectorMethod = SelectorMethod.BASIC
		return testObj
	}

	public void updateXpath(TestObject obj, String var, String content) {
		TestObjectProperty prop = obj.findProperty("xpath")
		String xpath = prop.getValue()
		String xpathUpdate = xpath.replaceAll(var, content)
		prop.setValue(xpathUpdate)
	}

	public void verifyBOCopywriting(TestObject obj, String boLocalization) {
		WebHelpers.takeScreenshot()
		String actualText = WebHelpers.getText(obj)
		String expectedText = i18n.getBoLocalizations(boLocalization)
		i18n.isEqualsText(actualText, expectedText)
	}

	public void verifyBOCopywritings(Map objectsToVerify) {
		WebHelpers.takeScreenshot()
		objectsToVerify.each{ localizationKey, testObj ->
			if(testObj) {
				String actualText = WebHelpers.getText(testObj).trim()
				String expectedText = i18n.getBoLocalizations(localizationKey).trim()
				i18n.isEqualsText(actualText, expectedText)
			} else {
				KeywordUtil.markFailed("java.lang.NullPointerException on Object ${testObj.toString()}. Unable to validate copy with locKey: $localizationKey")
			}
		}
	}
}
