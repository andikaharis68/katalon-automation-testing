package com.company.services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.util.ArrayList
import java.util.concurrent.atomic.AtomicBoolean
import java.util.regex.Matcher

import javax.swing.JOptionPane

import com.kms.katalon.core.testobject.ConditionType
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.By.ByXPath
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.firefox.ProfilesIni
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.junit.Test
import org.openqa.selenium.interactions.Action
import org.openqa.selenium.interactions.Actions

import com.googlecode.javacv.cpp.swscale
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
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.driver.WebUIDriverType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.sun.management.VMOption.Origin

import groovy.time.Duration

import java.awt.Dimension
import org.openqa.selenium.Point
import java.awt.Robot
import java.awt.Toolkit
import java.awt.Window
import java.awt.PageAttributes.OriginType
import java.awt.datatransfer.StringSelection
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.nio.file.Path
import java.nio.file.Paths
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter

import static org.openqa.selenium.interactions.PointerInput.Kind.MOUSE;
import static org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT;
import static org.openqa.selenium.interactions.PointerInput.MouseButton.RIGHT;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.logging.Logger;
import internal.GlobalVariable

public class WebHelpers {

	private static AtomicBoolean certificateShown = new AtomicBoolean()

	def static void openBrowser(String url) {
		if(isWebdriverLoaded()) {
			//			WebCommonUIHelpers.LogOut()
		} else {
			WebUI.openBrowser("")

			certificateShown.set(true)
			Thread runner = new Thread({ this.escapeCertificateSelection() })
			runner.start()
			WebUI.navigateToUrl(url)
			certificateShown.set(false)

			WebUI.delay(1)

			WebUI.navigateToUrl(url)
			WebUI.setViewPortSize(1400, 800, FailureHandling.OPTIONAL)
		}
	}

	def static boolean isWebdriverLoaded() {
		boolean isloaded = false
		WebDriver driver = null

		try {
			driver = DriverFactory.getWebDriver()
		} catch(Exception ex) {
		}

		if(driver) {
			return true
		} else {
			return false
		}
	}


	def static void escapeCertificateSelection() {
		Robot robot = new Robot()
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
		double width = screenSize.getWidth() / 2
		double height = screenSize.getHeight() * 0.3

		robot.delay(3000)
		println "Browser not navigated to target URL after 3 seconds"
		while(certificateShown.get()) {
			//for(int i = 0; i < 10; i++) {
			if(certificateShown.get()) {
				//certificateShown flag is not false after 2 second
				//tap then press escape in approx. region of the dialog

				robot.delay(500)
				robot.mouseMove(width.intValue(), height.intValue())
				robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
				robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
				robot.delay(500)
				robot.keyPress(KeyEvent.VK_ESCAPE);
				robot.keyRelease(KeyEvent.VK_ESCAPE);

				println "robot click!"
			}
		}
	}

	def static Map getPagination() {
		Map pagination = [:]
		String paginationText = WebUI.getText(findTestObject('Web/Common/lbl_pagination'))
		println "Pagination text: $paginationText"
		Matcher mapMatcher = paginationText =~ /^Showing (\d+) - (\d+) of (\d+) entries/
		pagination["FirstItemOnCurrentPage"] = mapMatcher[0][1]
		pagination["LastItemOnCurrentPage"] = mapMatcher[0][2]
		pagination["TotalRowOnCurrentPage"] = mapMatcher[0][2] - mapMatcher[0][1]
		pagination["TotalItem"] = mapMatcher[0][3]

		return pagination
	}

	def static String getDate() {
		return getDate("dd-MM-yyyy")
	}

	def static String getDateTime() {
		return getDate("dd-MM-yyyy / hh:mm:ss")
	}

	def static String getTime() {
		return getDate("HH:mm")
	}

	//	def static void main (String[] args) {
	//		println getTime()
	//		println addMinutes(getTime(), 15)
	//	}


	def static String addMinutes(String startTime, int min) {
		//Simple method to add minutes
		String hour = startTime.substring(0, 3)
		int newMin = Integer.parseInt(startTime.substring(3, 5))
		//println hour + " - " + newMin
		newMin = newMin + min
		if(newMin < 0) {
			while (newMin < 0) {
				newMin = newMin + 60
				startTime = addHours(startTime, -1)
				hour = startTime.substring(0, 3)
			}
		} else if(newMin > 59) {
			while (newMin > 59) {
				newMin = newMin - 60
				startTime = addHours(startTime, 1)
				hour = startTime.substring(0, 3)
			}
		}
		String minResult = newMin < 10? "0" + newMin : newMin.toString()

		String result = hour + minResult
	}

	def static String addHours(String startTime, int hour) {
		int newHour = Integer.parseInt(startTime.substring(0, 2))
		String min = startTime.substring(2, 5)
		newHour = newHour + hour
		newHour = Math.abs(newHour)

		if(newHour > 24) {
			while (newHour > 24) {
				newHour = newHour - 24
			}
		}
		String hourResult = newHour < 10? "0" + newHour : newHour.toString()

		String result = hourResult + min
	}

	def static String getDate(String format) {
		def date = new Date()
		def sdf = new SimpleDateFormat(format)
		return sdf.format(date)
	}

	def static String getDifferentDate(int numOfDays) {
		return getDifferentDate(numOfDays, "dd-MM-yyyy")
	}

	def static String getDifferentDate(String startDate, int numOfDays) {
		def sdf = new SimpleDateFormat("dd-MM-yyyy")
		Date currentdate = Date.parse("dd-MM-yyyy", startDate)
		return getDifferentDate(currentdate, numOfDays, "dd-MM-yyyy")
	}
	//	public static void main(String[] args) {
	//		println getDifferentDate(-15, "dd-MM-yyyy")
	//		println getDifferentDate(+20, "dd-MM-yyyy")
	//	}

	def static String getDifferentDate(int numOfDays, String format) {
		Date date = new Date()
		return getDifferentDate(date, numOfDays, format)
	}

	def static String getDifferentDate(Date date, int numOfDays, String format) {
		def sdf = new SimpleDateFormat(format)
		def currentdate = sdf.format(date)
		def targetDate = sdf.format(date.plus(numOfDays))
		WebUI.comment("Current Date: '$currentdate', Date Changed to: '$targetDate'")
		return targetDate
	}

	def static List getTableHeader(String tableXPathPrefix) {
		def columns = []
		WebUI.waitForAngularLoad(5)
		WebDriver driver = new DriverFactory().getWebDriver()

		//if(verifyElementPresent(tableXPathPrefix + '//thead//tr[1]')) {
		TestObject rowHeader = customTestObject("RowHeader", tableXPathPrefix + "//thead//tr[1]//th[1]")
		if(WebUI.waitForElementVisible(rowHeader, 5, FailureHandling.OPTIONAL) ) {
			println "Column is visible"
			//Get Columns
			WebElement colHeader = driver.findElement(By.xpath(tableXPathPrefix + '//thead//tr[1]'))
			List<WebElement> tblColumns = colHeader.findElements(By.tagName('th'))

			for (int i = 0; i < tblColumns.size(); i++) {
				//String colName = tblColumns[i].getText()
				WebElement colElement = driver.findElement(By.xpath(tableXPathPrefix + "//thead//tr[1]//th[${ i + 1 }]"))  //   //thead//tr[1]//th[${ i + 1 }]/span
				String colName = colElement.getText()
				if(!colName) {
					colName = "Col " + i
				}
				println "ColumnName: ${ colName }"

				columns[i] = colName
			}
		}
		return columns;
	}

	def static boolean verifyElementChecked(TestObject to) {
		String xpath = to.getSelectorCollection()[to.selectorMethod] //get xpath from test object
		if(!xpath) {
			xpath = to.findPropertyValue("xpath")
		}

		WebDriver driver = new DriverFactory().getWebDriver()
		WebElement chkbox = driver.findElement(By.xpath(xpath))
		if(chkbox) {
			return chkbox.isSelected()
		} else {
			return false
		}
	}

	def static Map getTableContent(String tableXPathPrefix = "", int numOfRowToGet = -1) {
		def table = [:]
		def row = [:]

		TestObject dt = WebHelpers.customTestObject("dt", tableXPathPrefix + "//tbody//tr[1]/td[1]") //  //tbody//tr[1]/td[1]//span
		WebUI.waitForElementPresent(dt, 10, FailureHandling.OPTIONAL)
		WebUI.scrollToPosition(0, 250)
		takeScreenshot()
		WebDriver driver = new DriverFactory().getWebDriver()

		def columns = getTableHeader(tableXPathPrefix)
		int rowNum = 0
		List<WebElement> tblRows = driver.findElements(By.xpath(tableXPathPrefix + '//tbody//tr'))
		if(tblRows) {
			rowNum = tblRows.size()
			println "DEBUG: columns -- " + columns.size() + " rows -- $rowNum"
		} else {
			//return null. Decision for Fail or Pass is in test case
			//KeywordUtil.markFailed("No data found in the table")
		}
		WebUI.comment("Extracting table content...")
		if(numOfRowToGet == -1) {
			numOfRowToGet = rowNum
		}
		for(int i = 0; i < rowNum && i < numOfRowToGet; i++) {
			row = [:]
			for (int j = 0; j < columns.size(); j++) {
				int indexRow = i + 1
				int indexCol = j + 1
				WebElement item
				try {
					item = driver.findElement(By.xpath(tableXPathPrefix + "//tbody//tr[$indexRow]//td[$indexCol]"))
				} catch (Exception e) {
					item = driver.findElement(By.xpath(tableXPathPrefix + "//tbody//tr[$indexRow]//td[$indexCol]"))
				}

				String txt = item.getText()
				//println "Text: $txt"
				if(txt) {
					row[columns[j]] = txt
				} else {
					//get first input checkbox inside this row and get the status
					//List<WebElement> chkbox = ((WebElement)tblRow[j]).findElements(By.xpath(".//input[@type='checkbox']"))
					List<WebElement> chkbox = driver.findElements(By.xpath(tableXPathPrefix + "//tbody//tr[$indexRow]//td[$indexCol]//input[@type='checkbox']"))
					if(chkbox) {
						boolean isSelected = ((WebElement)chkbox[0]).isSelected()
						//println " Checkbox found: $isSelected"
						row[columns[j]] = isSelected
					} else {
						row[columns[j]] = ""
					}
				}
			}
			println "row $i : $row"
			table["row $i"] = row
		}

		takeScreenshot()
		WebUI.scrollToPosition(0, 0)
		return table

		'''Note: Table will contains map of ( String rowId | Map columnName:rowValue ). Example:
			row 1 | { colName:rowValue | colName:rowValue }
			row 2 | { colName:rowValue | colName:rowValue }

			Sample usage: 
			tblWorkflow.each{ rowId, content ->
				println "$rowId " + content["Role Name"] + " Level1: " + content["Level 1 Approver"] + " Level2: " + content["Level 2 Approver"]
			}
		'''
	}

	def static Map getTableRowContent(String tableXPathPrefix = "", int rowIdx) {
		def row = [:]

		TestObject dt = WebHelpers.customTestObject("dt", tableXPathPrefix + "//tbody//tr[1]/td[1]") //  //tbody//tr[1]/td[1]//span
		WebUI.waitForElementPresent(dt, 10, FailureHandling.OPTIONAL)

		WebDriver driver = new DriverFactory().getWebDriver()

		def columns = getTableHeader(tableXPathPrefix)
		int rowNum = 0
		List<WebElement> tblRows = driver.findElements(By.xpath(tableXPathPrefix + '//tbody//tr'))
		if(tblRows) {
			rowNum = tblRows.size()
			println "DEBUG: columns -- " + columns.size() + " rows -- $rowNum"
		} else {
			//return null. Decision for Fail or Pass is in test case
			//KeywordUtil.markFailed("No data found in the table")
		}
		WebUI.comment("Extracting table content...")

		row = [:]
		for (int j = 0; j < columns.size(); j++) {
			int indexRow = rowIdx + 1
			int indexCol = j + 1
			WebElement item
			try {
				item = driver.findElement(By.xpath(tableXPathPrefix + "//tbody//tr[$indexRow]//td[$indexCol]"))
			} catch (Exception e) {
				item = driver.findElement(By.xpath(tableXPathPrefix + "//tbody//tr[$indexRow]//td[$indexCol]"))
			}

			String txt = item.getText()
			//println "Text: $txt"
			if(txt) {
				row[columns[j]] = txt
			} else {
				//get first input checkbox inside this row and get the status
				//List<WebElement> chkbox = ((WebElement)tblRow[j]).findElements(By.xpath(".//input[@type='checkbox']"))
				List<WebElement> chkbox = driver.findElements(By.xpath(tableXPathPrefix + "//tbody//tr[$indexRow]//td[$indexCol]//input[@type='checkbox']"))
				if(chkbox) {
					boolean isSelected = ((WebElement)chkbox[0]).isSelected()
					//println " Checkbox found: $isSelected"
					row[columns[j]] = isSelected
				} else {
					row[columns[j]] = ""
				}
			}
		}

		return row
	}

	def static int getTableRowSize(String tableXPath) {
		WebDriver driver = new DriverFactory().getWebDriver()
		def rowSize = 0

		try {
			//Get Rows
			WebElement tblBody = driver.findElement(By.xpath(tableXPath + '//tbody'))
			List<WebElement> tblRows = tblBody.findElements(By.tagName('tr'))
			println "rows num: " + tblRows.size()
			rowSize = tblRows.size()
		} catch(Exception e) {
			rowSize = 0 //return 0 if the table not exist or no row found
		}
		return rowSize
	}

	def static void uploadFile(TestObject uploadButton, String filePath, TestObject uploadedPath) {
		//uploadFile(uploadButton, filePath, uploadedPath, true)
		//uploadFile2(uploadButton, filePath, uploadedPath)
		uploadFile2(filePath)
	}

	def static void uploadFile(TestObject uploadButton, String filePath, TestObject uploadedPath, boolean requireTab) {
		WebUI.verifyElementPresent(uploadButton, 10)
		WebUI.click(uploadButton)

		File file = new File(filePath)

		String path = System.getProperty("user.dir") + file
		System.out.println("Image source path: $path")
		StringSelection ss = new StringSelection(path)
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null)
		Robot robot = new Robot()

		if(System.getProperty('os.name').toString().contains("Mac")) {
			System.out.println(System.getProperty('os.name').toString())
			// For Mac
			// Cmd + Tab is needed since it launches a Java app and the browser looses focus

			robot.delay(2000)

			if(requireTab) {
				//tab is not required for sequential call to upload file (e.g. in one test case, upload multiple times)
				robot.keyPress(KeyEvent.VK_META)
				robot.keyPress(KeyEvent.VK_TAB)
				robot.keyRelease(KeyEvent.VK_META)
				robot.keyRelease(KeyEvent.VK_TAB)
				robot.delay(500)
			}

			// Open Goto window
			robot.keyPress(KeyEvent.VK_META)
			robot.keyPress(KeyEvent.VK_SHIFT)
			robot.keyPress(KeyEvent.VK_G)
			robot.keyRelease(KeyEvent.VK_META)
			robot.keyRelease(KeyEvent.VK_SHIFT)
			robot.keyRelease(KeyEvent.VK_G)
			robot.delay(500)

			// Paste the clipboard value
			robot.keyPress(KeyEvent.VK_META)
			robot.keyPress(KeyEvent.VK_V)
			robot.keyRelease(KeyEvent.VK_META)
			robot.keyRelease(KeyEvent.VK_V)
			robot.delay(1500)

			System.out.println("Debug: Image Location Pasted")

			// Press Enter key to close the Goto window and Upload window
			robot.keyPress(KeyEvent.VK_ENTER)
			robot.keyRelease(KeyEvent.VK_ENTER)
			robot.delay(1000)
			robot.keyPress(KeyEvent.VK_ENTER)
			robot.keyRelease(KeyEvent.VK_ENTER)
			robot.delay(500)
		} else {
			robot.delay(1000)
			System.out.println(System.getProperty('os.name').toString())
			// for Windows, need to validate whether this command is working or not
			// Press Delete Existing path value
			robot.keyPress(KeyEvent.VK_DELETE);
			robot.keyRelease(KeyEvent.VK_DELETE);
			// Press Enter key
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.delay(500)

			// Paste the clipboard value
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(500)

			// Press Enter key
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			robot.delay(500)
		}
		System.out.println("Debug: Image Selected")
		WebUI.verifyElementPresent(uploadedPath, 10)
	}

	def static void blur() {
		//get out of focus from Active Element
		WebUI.executeJavaScript("document.activeElement.blur()", null)
	}

	def static void uploadFile2(String filePath) {
		uploadFile2(filePath, "//input[@id='mandImage' and @name='mandFile']")
	}

	def static void uploadFile2(String filePath, TestObject obj) {
		String xpath = obj.getSelectorCollection()[obj.selectorMethod]
		if(!xpath) {
			xpath = obj.findPropertyValue("xpath")
		}
		uploadFile2(filePath, xpath)
	}

	def static void uploadFile2(String filePath, String xpath) {
		File file = new File(filePath)
		String path = System.getProperty("user.dir") + file

		By locator = By.xpath(xpath)
		WebDriver driver = new DriverFactory().getWebDriver()
		WebElement element = driver.findElement(locator);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		// Setting value for "style" attribute to make textbox visible
		js.executeScript("arguments[0].style.display='block';", element);
		driver.findElement(locator).sendKeys(path);

		System.out.println("Debug: Image Selected")
	}

	def static void setText(TestObject to, String text) {
		WebUI.verifyElementPresent(to, 10)

		String xpath = to.getSelectorCollection()[to.selectorMethod]
		if(!xpath) {
			xpath = to.findPropertyValue("xpath")
		}
		WebDriver driver = new DriverFactory().getWebDriver()
		WebElement element = driver.findElement(By.xpath(xpath))

		element.sendKeys(text);
	}

	def static String reformatDate(String date, String srcPattern, String tgtPattern) {
		DateTimeFormatter srcFormatter = DateTimeFormatter.ofPattern(srcPattern)
		DateTimeFormatter tgtFormatter = DateTimeFormatter.ofPattern(tgtPattern)

		String reformattedDate = ""
		if(date.matches(/.*\d\d:\d\d:\d\d/)) {
			//if contain time -- using Date Time
			LocalDateTime parsedDateTime = LocalDateTime.parse(date, srcFormatter)
			reformattedDate = parsedDateTime.format(tgtFormatter).toString()
		} else {
			LocalDate parsedDate = LocalDate.parse(date, srcFormatter)
			reformattedDate = parsedDate.format(tgtFormatter).toString()
		}

		return reformattedDate
	}

	def static String reformatDate(String date, String srcPattern, Locale srcLocale, String tgtPattern, Locale tgtLocale) {
		if(!srcLocale) {
			srcLocale = Locale.ENGLISH //new Locale("in", "ID")
		}
		if(!tgtLocale) {
			tgtLocale = Locale.ENGLISH //new Locale("in", "ID")
		}
		DateTimeFormatter srcFormatter = DateTimeFormatter.ofPattern(srcPattern, srcLocale)
		DateTimeFormatter tgtFormatter = DateTimeFormatter.ofPattern(tgtPattern, tgtLocale)

		String reformattedDate = ""
		if(date.matches(/.*\d\d:\d\d:\d\d/)) {
			//if contain time -- using Date Time
			LocalDateTime parsedDateTime = LocalDateTime.parse(date, srcFormatter)
			reformattedDate = parsedDateTime.format(tgtFormatter).toString()
		} else {
			LocalDate parsedDate = LocalDate.parse(date, srcFormatter)
			reformattedDate = parsedDate.format(tgtFormatter).toString()
		}

		return reformattedDate
	}

	def static boolean verifyDateInBetween(String date, String startDate, String endDate) {
		boolean dateIsInBetween = true
		if(startDate && endDate) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

			LocalDate testDate = LocalDate.parse(date.replaceAll("-", "/"), formatter)
			LocalDate localStartDate = LocalDate.parse(startDate.replaceAll("-", "/"), formatter)
			LocalDate localEndDate = LocalDate.parse(endDate.replaceAll("-", "/"), formatter)


			if((testDate.isBefore(localStartDate) || testDate.isAfter(localEndDate))) {

				KeywordUtil.markFailed("Date is not between search date range!")
				dateIsInBetween = false
			}
		}
		return dateIsInBetween
	}

	def static TestObject customTestObject(String objName = "", String xpath) {
		if(!objName) objName = "customTestObject"
		TestObject to = new TestObject(objName)
		//		LogHelpers.printLog("Test Object created with xpath: " + xpath)
		to.addProperty("xpath", ConditionType.EQUALS, xpath)
		return to
	}

	def static void takeScreenshot() {
		Date date =  new Date()
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd_HHmmss")
		String formattedDate = sdf.format(date)
		def filename = formattedDate

		File location = new File(GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/" + filename + ".png")

		println "DEBUG: Screenshot location: $location"
		WebUI.takeScreenshot(location.toString())
		WebUI.comment("Screenshot saved in: " + location.canonicalPath)
	}

	def static void verifyElementNotPresent(TestObject to) {
		if(verifyElementPresent(to)) {
			KeywordUtil.markFailed("Element is still present.")
		} else {
			KeywordUtil.markPassed("Element is not present as expected.")
		}
	}

	def static boolean verifyElementPresent(TestObject to) {
		String xpath = to.getSelectorCollection()[to.selectorMethod] //get xpath from test object
		if(!xpath) {
			xpath = to.findPropertyValue("xpath")
		}

		println "Verifying element with xpath: $xpath"
		boolean found = false
		try {
			//try-catch here because Katalon throw exception when the object does not exist
			//instead of false value like the document said
			if(xpath) {
				found = verifyElementPresent(xpath)
				println "DEBUG: verify element present at $xpath is " + found
			} else {
				found = WebUI.verifyElementVisible(to, FailureHandling.OPTIONAL)
			}
			if(found) {
				scrollUntilObjectShowsInScreen(to)
			}
		} catch (Exception ex) {
			found = false
		}
		//WebUI.comment("Object Visible: $found")
		println("DEBUG: VerifyElementPresent : Visible: $found")
		return found
	}

	def static boolean verifyElementPresent(String xpath) {
		boolean displayed = false
		try {
			WebDriver driver = new DriverFactory().getWebDriver()
			WebDriverWait wait = new WebDriverWait(driver, 2)
			//println driver.getPageSource()
			WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
			displayed = el.isDisplayed()
		} catch (Exception ex) {
			displayed = false
		}

		return displayed
	}

	/*def static void clickUsingJavaScript(TestObject to) {
	 // Turn your TestObject into a WebElement...
	 WebElement element = WebUiCommonHelper.findWebElement(to, 30);
	 // Get the current driver from Katalon and convert to a JavascriptExecutor...
	 WebDriver driver = DriverFactory.getWebDriver();
	 JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
	 // Click your WebElement using JavaScript...
	 jsExecutor.executeScript("arguments[0].click();", element);
	 }*/


	def static void clickUsingJavaScript(TestObject to) {
		// Turn your TestObject into a WebElement...
		WebElement element = WebUiCommonHelper.findWebElement(to, 30);
		clickUsingJavaScript(element)
	}

	def static void clickUsingJavaScript(WebElement element) {

		// Get the current driver from Katalon and convert to a JavascriptExecutor...
		WebDriver driver = DriverFactory.getWebDriver();
		JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;

		// Click your WebElement using JavaScript...

		// Setting value for "style" attribute to make the element visible
		jsExecutor.executeScript("arguments[0].style.display='block';", element);

		jsExecutor.executeScript("arguments[0].click();", element);
	}


	def static void setValueUsingJavaScript(TestObject to, String text) {
		// Turn your TestObject into a WebElement...
		WebElement element = WebUiCommonHelper.findWebElement(to, 30)

		// Get the current driver from Katalon and convert to a JavascriptExecutor...
		WebDriver driver = DriverFactory.getWebDriver()
		JavascriptExecutor jsExecutor = (JavascriptExecutor)driver

		// Setting value for "style" attribute to make the element visible
		jsExecutor.executeScript("arguments[0].style.display='block';", element)

		println "Set Text to: '$text'"
		jsExecutor.executeScript("arguments[0].setAttribute('value', '$text');", element)
		//WebUI.executeJavaScript("document.activeElement.blur()", null) //lose focus on the element
	}

	def static void scrollUntilObjectShowsInScreen(TestObject to){
		if(WebUI.waitForElementVisible(to, 2, FailureHandling.OPTIONAL)) {
			int midPointY = WebUI.getViewportHeight(FailureHandling.OPTIONAL) * 0.2
			int objectY = getPositionInScreen(to).y

			int delta = (objectY - midPointY) > 0? (objectY - midPointY)  : 0
			println("DEBUG: scroll to 0, $delta")
			WebUI.scrollToPosition(0, delta)
		}
		WebHelpers.takeScreenshot()
	}

	def static String getText(TestObject to, String xpathPrefix = "", String xpathSuffix = "") {
		String xpath = to.getSelectorCollection()[to.selectorMethod] //extracting xpath from test object
		if(!xpath) {
			xpath = to.findPropertyValue("xpath")
		}

		String fullxpath = xpathPrefix + xpath + xpathSuffix
		String txt = ""

		try {
			txt = getTextUsingXPath(fullxpath)
		} catch(Exception ex) {
			txt = getTextUsingXPath(fullxpath)
		}
		return txt
	}

	def static String getTextUsingXPath(String xpath) {
		WebDriver driver = new DriverFactory().getWebDriver()
		List<WebElement> el = driver.findElements(By.xpath(xpath))

		println "DEBUG: Get text of element in $xpath"
		if(el.size() > 0) {
			return getTextFromElement((WebElement)el[0])
		} else {
			println "DEBUG: Object not found."
			return ""
		}
	}

	def static String[] getTextsUsingXPath(String xpath) {
		WebDriver driver = new DriverFactory().getWebDriver()
		List<WebElement> els = driver.findElements(By.xpath(xpath))
		String[] items

		println "DEBUG: Get texts of elements in $xpath"
		if(els.size() > 0) {
			items = new String[els.size()]

			for(int i = 0; i < els.size(); i++) {
				items[i] = getTextFromElement((WebElement) els[i])
			}
		} else {
			println "DEBUG: Object not found."
		}

		return items
	}

	def static String getTextFromElement(WebElement el) {
		String theText = ""

		String type = el.getTagName()
		println "DEBUG: element $type is found."

		if(type == "input" || type == "textarea") {
			theText = el.getAttribute("value").toString()
		} else {
			theText = el.getText()
		}

		if(theText != "") {
			println "DEBUG: Text is $theText"
		} else {
			println "DEBUG: Text not found."
		}

		return theText
	}

	def static String getTextUsingJavascript(WebElement el) {
		// Get the current driver from Katalon and convert to a JavascriptExecutor...
		WebDriver driver = DriverFactory.getWebDriver()
		JavascriptExecutor jsExecutor = (JavascriptExecutor)driver

		return jsExecutor.executeScript("return arguments[0].value;", el)
	}

	def static String waitAndGetText(TestObject to, int duration) {
		String theText = WebHelpers.getText(to)

		for(int i = 0; i < duration && theText == ""; i++) {
			WebUI.delay(1)
			try {
				theText = WebHelpers.getText(to)
			} catch (Exception ex) {
				theText = WebHelpers.getText(to)
			}
		}

		return theText
	}

	def static String verifyElementTextMatches(TestObject to, String value, int duration) {
		boolean textMatch = waitUntilElementTextMatches(to, value, duration)
		String theText = WebHelpers.getText(to)

		println "Comparing actual $theText -- expected $value - result: $textMatch"
		if(!textMatch) {
			//WebUI.comment("Text not matches after $duration second(s). Found: '$theText', expected: '$value'")
			KeywordUtil.markFailed("Text not matches after $duration second(s). Found: '$theText', expected: '$value'")
		}
		return theText
	}

	def static boolean waitUntilElementTextMatches(TestObject to, String value, int duration) {
		if(duration < 3) {
			duration = 3
		} //Wait at least 3 second
		String theText = WebHelpers.getText(to)
		boolean textFound = false

		for(int i = 0; i < duration; i++) {
			WebUI.delay(1)
			try {
				theText = WebHelpers.getText(to)
			} catch (Exception ex) {
				theText = WebHelpers.getText(to)
			}
			println "Comparing actual $theText -- expected $value"
			if(theText == value) {
				textFound = true
				break
			}
		}

		takeScreenshot()
		return textFound
	}

	def static ArrayList convertInputToList(String input){
		def result = []
		println "DEBUG: input is $input"
		def temp = input.split(";")
		for(int i = 0; i < temp.size(); i++) {
			String content = temp[i]
			if(content.trim() != "") {
				result.add(content.toString().trim())
			}
		}
		return result
	}

	def static boolean isEnabled(TestObject to) {
		String xpath = to.getSelectorCollection()[to.selectorMethod]
		if(!xpath) {
			xpath = to.findPropertyValue("xpath")
		}
		WebDriver driver = DriverFactory.getWebDriver()
		WebElement el = driver.findElement(By.xpath(xpath))
		return el.enabled
	}

	def static boolean checkAndWaitUntil(String endTime) {
		boolean wait = true
		int waitHour = Integer.parseInt(endTime.substring(0, 2))
		int waitMin = Integer.parseInt(endTime.substring(3, 5))

		String currentTime = getTime()
		int currentHour = Integer.parseInt(currentTime.substring(0, 2))
		int currentMin = Integer.parseInt(currentTime.substring(3, 5))
		if(currentHour >= waitHour && currentMin > waitMin) {
			wait = false
			println "Reached target time..."
		} else {
			return true  //still waiting
		}
	}

	def static void waitUntil(String endTime) {
		boolean wait = true
		int waitHour = Integer.parseInt(endTime.substring(0, 2))
		int waitMin = Integer.parseInt(endTime.substring(3, 5))
		println "Wait until " + waitHour + " - " + waitMin
		while(wait) {
			String currentTime = getTime()
			int currentHour = Integer.parseInt(currentTime.substring(0, 2))
			int currentMin = Integer.parseInt(currentTime.substring(3, 5))
			println "Current time " + currentHour + " - " + currentMin
			if(currentHour >= waitHour && currentMin > waitMin) {
				wait = false
				println "Reached target time..."
			} else {
				println "Waiting..."
				WebUI.delay(5)
			}
		}
	}

	def static void debugWait(String message = "") {
		JOptionPane.showMessageDialog(null,
				"$message\n\n> Continue execution?",
				"Pause Execution > Workaround",
				JOptionPane.WARNING_MESSAGE)
	}

	def static void sendKeys(TestObject obj, Keys key) {
		WebDriver driver = new DriverFactory().getWebDriver()
		String xpath = obj.getSelectorCollection()[obj.selectorMethod] //get xpath from test object in Object Repository
		if(!xpath) {
			xpath = obj.findPropertyValue("xpath") //get xpath from test object created from custom code
		}

		WebElement el = driver.findElement(By.xpath(xpath))
		el.sendKeys(key)
	}

	def static void clearText(TestObject obj) {
		if(WebUI.waitForElementVisible(obj, 2, FailureHandling.OPTIONAL)) {
			WebDriver driver = new DriverFactory().getWebDriver()
			String xpath = obj.getSelectorCollection()[obj.selectorMethod] //get xpath from test object in Object Repository
			if(!xpath) {
				xpath = obj.findPropertyValue("xpath") //get xpath from test object created from custom code
			}

			WebElement el = driver.findElement(By.xpath(xpath))

			el.sendKeys(Keys.chord(Keys.CONTROL, "a"))
			el.sendKeys(Keys.BACK_SPACE)

			String textToDelete = getTextFromElement(el)
			int numOfChar = textToDelete.length()
			println "DEBUG: Text to delete: $textToDelete ($numOfChar char)"
			for(int i = 0; i <= numOfChar; i++) {
				el.sendKeys(Keys.ARROW_RIGHT)
			}
			for(int i = 0; i <= numOfChar; i++) {
				el.sendKeys(Keys.BACK_SPACE)
			}
			el.sendKeys(Keys.TAB)
			takeScreenshot()
		}
	}

	def static String getPageSource() {
		/**
		 * debugger to get current page source
		 */
		WebDriver driver = new DriverFactory().getWebDriver()
		String pgSrc = ""
		try{
			JavascriptExecutor js = (JavascriptExecutor) driver;
			String javascript = "return arguments[0].innerHTML";

			pgSrc =(String)js.executeScript(javascript, driver.findElement(By.tagName("html")));
			pgSrc = "<html>"+ pgSrc +"</html>";
		} catch(Exception ex) {
		}

		return pgSrc
	}

	def static String normalizeCurrency(String amount) {
		//Rp 5.000,00  --> 5000
		println "Processing $amount"
		if(amount && amount != "–" ) {
			amount = amount.replaceAll("Rp", "").trim()
			amount = amount.replaceAll("\\.", "")
			amount = amount.replaceAll(",00", "")
		}
		println "Result $amount"

		return amount
	}

	public static void compareDropDownValues(String optionName, String[] expectedType, String[] actualType) {
		List actuals = actualType.flatten().findAll{ it != null }
		List expected = expectedType.flatten().findAll{ it != null }
		compareDropDownValues(optionName, expected, actuals)
	}

	public static void compareDropDownValues(String optionName, List expectedType, String[] actualType) {
		List actuals = actualType.flatten().findAll{ it != null }
		compareDropDownValues(optionName, expectedType, actuals)
	}
	public static void compareDropDownValues(String optionName, List expectedType, List actualType) {
		String missingItem = ""
		boolean allExpectedValueFound = true

		if(optionName == "Branch") {
			expectedType.each{ expectedItem ->
				boolean foundInActual = false
				actualType.each{ actualItem ->
					if(actualItem.toString().contains(expectedItem)) {
						foundInActual = true
					}
				}
				if(!foundInActual) {
					missingItem += "Item $expectedItem not found in actual list: '$actualType' \n"
				}
				allExpectedValueFound = allExpectedValueFound && foundInActual
			}
		} else {
			//exact match
			expectedType.each{ item ->
				if(!actualType.contains(item)) {
					allExpectedValueFound = false
					missingItem += "Item $item not found in actual list: '$actualType' \n"
				}
			}
		}
		if(!allExpectedValueFound) {
			KeywordUtil.markFailed("Some expected item in $optionName is not found. $missingItem")
		}
	}

	public static String splitAndCapitalize(String text, String separator) {
		println "DEBUG: split $text"
		String result = ""
		if(text.contains("-")) {
			def splitted = text.split(separator).collect { it.toString().toLowerCase().capitalize() }
			result = splitted.inject('') { accumulator, current -> accumulator + current + ' ' }.trim()
			println "DEBUG: combine into $result"
		} else {
			result = text
		}
		return result
	}

	public static int countElements(String xpath) {
		WebDriver driver = new DriverFactory().getWebDriver()
		return driver.findElements(By.xpath(xpath)).size()
	}

	public static Point getPositionInScreen(TestObject obj) {
		WebDriver driver = new DriverFactory().getWebDriver()
		String xpath = obj.getSelectorCollection()[obj.selectorMethod] //get xpath from test object in Object Repository
		if(!xpath) {
			xpath = obj.findPropertyValue("xpath") //get xpath from test object created from custom code
		}

		WebElement el = driver.findElement(By.xpath(xpath))
		println("DEBUG: Element location: ${el.getLocation()}")
		return el.getLocation()
	}

	public static String formatCurrency(String amount) {
		if(amount == "–") {
			return "–"
		} else
			if(amount == "0"){
				return "Rp 0"
			} else {
				//note: accepting only number (e.g. 10000, 20000000)
				amount = amount.replaceAll(/(\d)(?=(\d{3})+(?!\d))/, /$1\./)
				return "Rp $amount,00"
				//return "Rp $amount,00
			}
	}

	public static void toPointer(TestObject endPoint) {
		WebDriver driver = new DriverFactory().getWebDriver()
		Actions builder = new Actions(driver)
		Robot botMouse = new Robot()
		String destenationPoint = endPoint.findPropertyValue("xpath")
		Point destenationCordinate = driver.findElement(By.xpath(destenationPoint)).getLocation()
		//botMouse.mousePress(InputEvent.BUTTON1_DOWN_MASK)
		botMouse.mousePress(InputEvent.BUTTON1_DOWN_MASK)
		WebUI.delay(3)
		botMouse.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
		botMouse.mouseMove((int) destenationCordinate.x, (int) destenationCordinate.y)
		//botMouse.mouseMove((int) destenationCordinate.x +154, (int) destenationCordinate.y +203)

		botMouse.delay(150)
	}

	public static void dragNdrop(TestObject startPoint, TestObject endPoint) {
		WebDriver driver = new DriverFactory().getWebDriver()
		Actions builder = new Actions(driver)

		WebElement sourcePoint = WebUiCommonHelper.findWebElement(startPoint, 10)
		WebElement destenationPoint = WebUiCommonHelper.findWebElement(endPoint, 10)

		int startX = getPositionInScreen(startPoint).x
		int startY = getPositionInScreen(startPoint).y
		int endX = getPositionInScreen(endPoint).x
		int endY = getPositionInScreen(endPoint).y

		//		KeywordUtil.logInfo("Start Location Y =" + startY)
		//		KeywordUtil.logInfo("Start Location X =" + startX)
		//		KeywordUtil.logInfo("End Location Y =" + endY)
		//		KeywordUtil.logInfo("End Location X =" + endX)

		builder.clickAndHold(sourcePoint)
		WebUI.delay(4)
		builder.moveToElement(destenationPoint).pause(5).release().build().perform()
		builder.release().build().perform()
		Mobile.getDeviceHeight()
		WebUI.dragAndDropByOffset(null, 0, 0)
	}
	/*
	 public static void dragNdrop1(TestObject startPoint, TestObject endPoint) {
	 // Configure the action
	 WebDriver driver = new DriverFactory().getWebDriver()
	 Actions builder = new Actions(driver)
	 builder.keyDown(Keys.CONTROL)
	 WebUI.click(startPoint)
	 WebUI.click(endPoint)
	 builder.keyUp(Keys.CONTROL)
	 // Then get the action:
	 Action selectMultiple = builder.build()
	 // And execute it:
	 selectMultiple.perform()
	 }
	 public static void dragNdrop2(TestObject startPoint, TestObject endPoint) {
	 WebDriver driver = new DriverFactory().getWebDriver()
	 Actions builder = new Actions(driver)
	 builder.keyDown(Keys.CONTROL)
	 WebUI.click(startPoint)
	 WebUI.dragAndDropToObject(startPoint, endPoint)
	 builder.keyUp(Keys.CONTROL)
	 Action selected = builder.build()
	 selected.perform()
	 }*/
	public static toPointerx(TestObject loc) {
		WebDriver driver = new DriverFactory().getWebDriver()
		Actions builder = new Actions(driver)

		WebUI.waitForElementPresent(loc, 5)
		if (loc) {
			KeywordUtil.logInfo("Object Found")
			WebElement location = WebUiCommonHelper.findWebElement(loc, 10)
			int locX = getPositionInScreen(loc).x
			int locY = getPositionInScreen(loc).y
			//builder.moveToElement(location).perform()
			//builder.defaultMouse.createPointerMove(5, , locX, locY)
			//builder.defaultMouse.createPointerMove(100, loc, locX, locY)
			builder.defaultMouse.createPointerMove(1, toPointer(loc), locX, locY)
			builder.moveToElement(loc).perform()
			//defaultMouse.createPointerMove
		} else {
			//PointerInput.Origin.pointer(),
			KeywordUtil.logInfo("Object Not Found")
		}

		//WebElement ele = driver.findElement(By.xpath("<xpath>")); //Creating object of an Actions class Actions
		//action = new Actions(driver) //Performing the mouse hover action on the target element.
		//action.moveToElement(ele).perform()
	}
	public static void mouse(TestObject startPoint, TestObject endPoint) {
		WebDriver driver = DriverFactory.getWebDriver();
		WebElement LocatorFrom = WebUiCommonHelper.findWebElement(startPoint, 10) // draggable
		WebElement LocatorTo   = WebUiCommonHelper.findWebElement(endPoint, 10) // droppable

		String xto=Integer.toString(LocatorTo.getLocation().x)
		String yto=Integer.toString(LocatorTo.getLocation().y)

		((JavascriptExecutor)driver).executeScript('function simulate(f,c,d,e){var b,a=null;for(b in eventMatchers)if(eventMatchers[b].test(c)){a=b;break}if(!a)return!1;document.createEvent?(b=document.createEvent(a),a==\'HTMLEvents\'?b.initEvent(c,!0,!0):b.initMouseEvent(c,!0,!0,document.defaultView,0,d,e,d,e,!1,!1,!1,!1,0,null),f.dispatchEvent(b)):(a=document.createEventObject(),a.detail=0,a.screenX=d,a.screenY=e,a.clientX=d,a.clientY=e,a.ctrlKey=!1,a.altKey=!1,a.shiftKey=!1,a.metaKey=!1,a.button=1,f.fireEvent(\'on\'+c,a));return!0} var eventMatchers={HTMLEvents:/^(?:load|unload|abort|error|select|change|submit|reset|focus|blur|resize|scroll)$/,MouseEvents:/^(?:click|dblclick|mouse(?:down|up|over|move|out))$/}; ' +
				'simulate(arguments[0],\'mousedown\',0,0); simulate(arguments[0],\'mousemove\',arguments[1],arguments[2]); simulate(arguments[0],\'mouseup\',arguments[1],arguments[2]); ', LocatorFrom,xto,yto);
	}

	def static String parsedDate(String date, String sourceFormat, String expectedFormat, Locale locale = Locale.ENGLISH) {
		SimpleDateFormat sourceSdf = new SimpleDateFormat(sourceFormat, locale)
		Date parsedSourceDate = sourceSdf.parse(date)

		// convert date to actual format
		SimpleDateFormat expectedSdf = new SimpleDateFormat(expectedFormat, locale)
		String convertedDate = expectedSdf.format(parsedSourceDate)

		return convertedDate
	}
}
