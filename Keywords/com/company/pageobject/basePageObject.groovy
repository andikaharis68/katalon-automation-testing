package com.company.pageobject

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.openqa.selenium.Point

import com.kms.katalon.core.testobject.ConditionType

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
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

import internal.GlobalVariable

public class BasePageObject {

	String userLanguage = ""
	private static BasePageObject thisObj

	static void main(String[] args){
		//test code here
	}

	public BasePageObject(String lang) {
		if(lang) {
			userLanguage = lang.toLowerCase()
		}
	}

	public static BasePageObject getObject(String lang = "ID") {
		//static accessor for this Class (to use function inside it)
		return new BasePageObject(lang)
	}

	public String getXpath(TestObject obj) {
		String objId = composeObjectId()
		String objIdValue = obj.findPropertyValue(objId)
		if(objIdValue) {
			// create xpath using resource-id / name
			String xpath = "//*[@${objId} = '${objIdValue}']"

			return xpath
		}else {
			String xpath = obj.getSelectorCollection()[obj.selectorMethod] //get xpath from Katalon's test object
			if(!xpath) {
				xpath = obj.findPropertyValue("xpath")
			} //get xpath from Custom test object

			return xpath
		}
	}

	public TestObject createTestObjectByXpath(String objName, String xpath, String textIdent="") {
		textIdent = composeTextIdentifier(textIdent)

		TestObject testObj = new TestObject(objName)
		testObj.addProperty("xpath", ConditionType.EQUALS, xpath)
		testObj.addProperty("textIdentifier", ConditionType.EQUALS, textIdent)
		return testObj
	}

	public TestObject createTestObjectByPoint(String objName, int x, int y, String textIdent="") {
		textIdent = composeTextIdentifier(textIdent)

		TestObject testObj = new TestObject(objName)
		testObj.addProperty("x", ConditionType.EQUALS, x)
		testObj.addProperty("y", ConditionType.EQUALS, y)
		testObj.addProperty("textIdentifier", ConditionType.EQUALS, textIdent)
		return testObj
	}

	public TestObject createTestObjectById(String objName, String id, String textIdent="") {
		// method to create test object by id (Android: resource-id; iOS: name)
		textIdent = composeTextIdentifier(textIdent)
		String appId
		TestObject testObj = new TestObject(objName)
		if (MobileHelpers.isAndroid()) {
			appId = MobileHelpers.getAppId()
			//			testObj.addProperty(composeObjectId(), ConditionType.EQUALS, appId + ":id/" + id)
			testObj.addProperty(composeObjectId(), ConditionType.EQUALS, "${appId}:id/${id}")
		}else {
			testObj.addProperty(composeObjectId(), ConditionType.EQUALS, id)
		}
		testObj.addProperty("textIdentifier", ConditionType.EQUALS, textIdent)
		return testObj
	}

	public TestObject createTestObject(String objName, String type, String key, String textIdent="", String additionalXpath="", int index=1) {
		//copy of findTestObjectByText
		String xpath = ""
		String text = MobileHelpers.getTextObjectRepositoryValue(userLanguage, key)
		println "$objName from text repo: " + text
		text = text ? (text.contains(key) ? key : text) : ""

		textIdent = composeTextIdentifier(textIdent)

		TestObject testObj = new TestObject(objName)
		if(MobileHelpers.isIos()) {
			xpath = "(*//$type[contains(@$textIdent, '$text')])[$index]" + additionalXpath
		}else {
			xpath = "//*[@class = '$type' and contains(@$textIdent, '$text')]" + additionalXpath
		}
		testObj.addProperty("xpath", ConditionType.EQUALS, xpath)
		testObj.addProperty("textIdentifier", ConditionType.EQUALS, textIdent)

		return testObj
	}

	public void tapAtPosition(TestObject testObj, int additionalX=0, int additionalY=0) {
		Point position = MobileHelpers.getElementPointByXpath(getXpath(testObj))

		// get position by id when id value is filled; default to use xpath
		String objIdValue = testObj.findPropertyValue(composeObjectId())
		if(objIdValue) {
			position = MobileHelpers.getElementPointById(objIdValue)
		}
		int x = position.x + additionalX
		int y = position.y + additionalY

		KeywordUtil.logInfo("Tap at position: x: ${x}, y: ${y}")
		Mobile.tapAtPosition(x, y)
	}

	public void tapAndHoldAtPosition(TestObject testObj, int additionalX=0, int additionalY=0) {
		Point position = MobileHelpers.getElementPointByXpath(getXpath(testObj))

		// get position by id when id value is filled; default to use xpath
		String objIdValue = testObj.findPropertyValue(composeObjectId())
		if(objIdValue) {
			position = MobileHelpers.getElementPointById(objIdValue)
		}
		int x = position.x + additionalX
		int y = position.y + additionalY

		KeywordUtil.logInfo("Tap and hold at position: x: ${x}, y: ${y}")
		Mobile.tapAndHoldAtPosition(x, y, 1)
	}

	//verifying UI copywriting using page source for multiple testObjects
	public boolean verifyCopyWriting(Map copies) {
		Mobile.delay(1)
		if(GlobalVariable.LOCALIZATION_TEST) {
			return verifyCopyWriting(copies, MobilePageSrcHelpers.getPageSource())
		}
	}
	public boolean verifyContainCopyWriting(Map copies) {
		Mobile.delay(1)
		return verifyContainCopyWriting(copies, MobilePageSrcHelpers.getPageSource())
	}

	//verifying UI copywriting using page source for multiple testObjects
	public boolean verifyCopyWriting(Map copies, String pageSrc) {
		boolean verificationResult = true
		boolean verifReport = true
		pageSrc = !pageSrc ? MobilePageSrcHelpers.getPageSource() : pageSrc
		copies.each{ localizationKey, testObj ->
			TestObject obj = ((TestObject) testObj)
			//if(Mobile.waitForElementPresent(obj, 2)) {
			KeywordUtil.logInfo("Test object: '${obj.getObjectId()}', localizations key: '$localizationKey'")

			String xpath = getXpath(obj)

			String textIdentifier = obj.findPropertyValue("textIdentifier") //get where the text should be taken

			//get the text
			String actualText = MobilePageSrcHelpers.getAttributeFromPageSource(pageSrc, xpath, textIdentifier)
			String expectedText = i18n.getMobileLocalizations(localizationKey, userLanguage)
			expectedText = expectedText.replaceAll("\n\n", "")
			if(actualText) {
				if(actualText.trim()) {
					verifReport = i18n.isEqualsText(actualText, expectedText)
					verificationResult = verifReport && verificationResult
				}else {
					// skip validation since actual text is empty string (placeholder)
					verificationResult = true
					verifReport = true
				}
			} else {
				String msg = "i18n FAIL: BasePageObject: verifyCopyWriting: Test object: '${obj.getObjectId()}' not found"
				KeywordUtil.markFailed(msg)
				Mobile.comment(msg)
				verificationResult = false
				verifReport = false
			}
			if(GlobalVariable.RUNNING_TEST_SUITES) {
				String filePath = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/Validation.xlsx"
				String status = ""
				Map value = [:]
				value["Lockey"] = localizationKey
				value["Expected"] = expectedText
				value["Actual"] = actualText
				if(verifReport) {
					status = "PASSED"
				} else {
					status = "FAILED"
					MobileHelpers.takeReportScreenshot(localizationKey)
				}
				value["Status"] = status
				MobileHelpers.writeToExcel(filePath, "ValidateCopy", value)
			}
		}
		return verificationResult
	}

	//verifying UI copywriting using page source for multiple testObjects
	public boolean verifyContainCopyWriting(Map copies, String pageSrc) {
		boolean verificationResult = true
		pageSrc = !pageSrc ? MobilePageSrcHelpers.getPageSource() : pageSrc
		copies.each{ localizationKey, testObj ->
			TestObject obj = ((TestObject) testObj)
			//if(Mobile.waitForElementPresent(obj, 2)) {
			KeywordUtil.logInfo("Test object: '${obj.getObjectId()}', localizations key: '$localizationKey'")

			String xpath = getXpath(obj)

			String textIdentifier = obj.findPropertyValue("textIdentifier") //get where the text should be taken

			//get the text
			String actualText = MobilePageSrcHelpers.getAttributeFromPageSource(pageSrc, xpath, textIdentifier)
			String expectedText = i18n.getMobileLocalizations(localizationKey, userLanguage)
			if(actualText) {
				if(actualText.trim()) {
					verificationResult = i18n.isContainsText(actualText, expectedText) && verificationResult
				}else {
					// skip validation since actual text is empty string (placeholder)
					verificationResult = true
				}
			} else {
				String msg = "i18n FAIL: BasePageObject: verifyCopyWriting: Test object: '${obj.getObjectId()}' not found"
				KeywordUtil.markFailed(msg)
				Mobile.comment(msg)
				verificationResult = false
			}
		}
		return verificationResult
	}

	//provide lists of key copywriting to check in the page source
	public boolean verifyCopyWritingTextsOnly(List textsToVerify, String pageSource = "") {
		boolean verificationResult = true
		String pageSrc = !pageSource ? MobilePageSrcHelpers.getPageSource() : pageSource
		textsToVerify.each{ text ->
			KeywordUtil.logInfo("Localizations key: '$text'")
			String expectedText = i18n.getMobileLocalizations(text, userLanguage)
			if(pageSrc.contains(expectedText)) {
				KeywordUtil.logInfo("PASS: Keyword found: '$expectedText'")
				KeywordUtil.markPassed("BasePageObject: verifyCopyWritingTextsOnly: Text '$expectedText' is found on the page.")
				verificationResult = true
			} else {
				KeywordUtil.logInfo("FAIL: Keyword NOT found: '$expectedText'")
				KeywordUtil.markFailed("BasePageObject: verifyCopyWritingTextsOnly: Text '$expectedText' not found on the page.")
				verificationResult = false
			}
		}
		return verificationResult
	}

	//provide lists of key copywriting to check text object is exist or not
	public boolean verifyCopyWritingTextsOnlyWithObject(List textsToVerify, String pageSource = "") {
		boolean verificationResult = true
		String pageSrc = !pageSource ? MobilePageSrcHelpers.getPageSource() : pageSource
		textsToVerify.each{ text ->
			KeywordUtil.logInfo("Localizations key: '$text'")
			String expectedText = i18n.getMobileLocalizations(text, userLanguage)
			TestObject obj = createTestObjectByXpath(text, "//*[contains(@label, '$expectedText')]")

			if(Mobile.waitForElementPresent(obj, 1)) {
				KeywordUtil.markPassed("BasePageObject: verifyCopyWritingTextsOnly: Text '$expectedText' is found on the page.")
				verificationResult = true
			}else {
				KeywordUtil.markFailed("BasePageObject: verifyCopyWritingTextsOnly: Text '$expectedText' not found on the page.")
				verificationResult = false
			}
		}
		return verificationResult
	}

	//verifying object text value using page source for multiple testObjects
	public boolean verifyValue(Map values, String pageSource = "") {
		boolean verificationResult = true
		String pageSrc = !pageSource ? MobilePageSrcHelpers.getPageSource() : pageSource
		values.each{ value, testObj ->
			TestObject obj = ((TestObject) testObj)
			if(Mobile.waitForElementPresent(obj, 2)) {
				KeywordUtil.logInfo("Test object: '${obj.getObjectId()}', value: '$value'")

				String xpath = getXpath(obj) //get the xpath from TestObject
				String textIdentifier = obj.findPropertyValue("textIdentifier") //get where the text should be taken

				//get the text
				String actualText = MobilePageSrcHelpers.getAttributeFromPageSource(pageSrc, xpath, textIdentifier)
				boolean isEquals = i18n.isEqualsText(actualText, value)
				verificationResult = verificationResult && isEquals
			} else {
				String msg = "BasePageObject: verifyValue: Test object: '${obj.getObjectId()}' not found"
				KeywordUtil.markFailed(msg)
				Mobile.comment(msg)
				verificationResult = false
			}
		}
		return verificationResult
	}

	//provide lists of value to be checked in the page source
	public void verifyValueTextsOnly(List textsToVerify, String pageSource = "") {
		String pageSrc = !pageSource ? MobilePageSrcHelpers.getPageSource() : pageSource
		textsToVerify.each{ text ->
			KeywordUtil.logInfo("Value: '$text'")
			if(pageSrc.contains(text)) {
				KeywordUtil.markPassed("BasePageObject: verifyValueTextsOnly: Text '$text' is found on the page.")
			} else {
				KeywordUtil.markFailed("BasePageObject: verifyValueTextsOnly: Text '$text' not found on the page.")
			}
		}
	}

	//default option to get text from an Element
	private String composeTextIdentifier(String textIdent) {
		if(MobileHelpers.isIos()) {
			return textIdent ? textIdent : "label"
		}else {
			return textIdent ? textIdent : "text"
		}
	}

	// method to get object id of iOS / Android
	private String composeObjectId() {
		if(MobileHelpers.isIos()) {
			return "name"
		}else {
			return "resource-id"
		}
	}

	//method to verify navigation screen
	public void verifyLanding(TestObject to, String screen) {
		MobilePageSrcHelpers.getPageSource()
		if(Mobile.waitForElementPresent(to, 20, FailureHandling.STOP_ON_FAILURE)) {
			MobileHelpers.takeScreenshot()
			KeywordUtil.markPassed("Success navigate to screen " + screen)
		} else {
			MobileHelpers.takeScreenshot()
			KeywordUtil.markFailed("Failed navigate to screen " + screen)
		}
	}

	public FailureHandling verifyLandingWithHandling(TestObject to, String screen) {
		MobilePageSrcHelpers.getPageSource()
		FailureHandling ValueHandling = FailureHandling.CONTINUE_ON_FAILURE
		if(Mobile.waitForElementPresent(to, 20, FailureHandling.STOP_ON_FAILURE)) {
			MobileHelpers.takeScreenshot()
			KeywordUtil.markPassed("Success navigate to screen " + screen)
		} else {
			MobileHelpers.takeScreenshot()
			KeywordUtil.markFailed("Failed navigate to screen " + screen)
			ValueHandling = FailureHandling.STOP_ON_FAILURE
		}

		return ValueHandling
	}
}
