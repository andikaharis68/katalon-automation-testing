package com.company.services
//
//import java.nio.file.Files
//import java.nio.file.Path
//import java.nio.file.Paths
//import java.text.DecimalFormat
//import java.text.ParseException
//import java.text.SimpleDateFormat
//
//import java.time.Duration
//import java.time.LocalDate
//import java.util.concurrent.ThreadLocalRandom
//import java.util.concurrent.TimeUnit
//import java.util.function.Function
//import java.util.regex.Matcher
//
//import com.github.kklisura.cdt.protocol.commands.DeviceOrientation
//import com.keyword.custom.CustomAndroidKey
//import com.keyword.custom.Reporting
//
//import org.apache.commons.io.FileUtils
//import org.apache.poi.hssf.usermodel.HSSFWorkbook
//import org.apache.poi.ss.usermodel.Row
//import org.apache.poi.ss.usermodel.Sheet
//import org.apache.poi.ss.usermodel.Workbook
//import org.apache.poi.ss.util.WorkbookUtil
//import org.apache.poi.xssf.usermodel.XSSFCell
//import org.apache.poi.xssf.usermodel.XSSFRow
//import org.apache.poi.xssf.usermodel.XSSFSheet
//import org.apache.poi.xssf.usermodel.XSSFWorkbook
//import org.openqa.selenium.By
//import org.openqa.selenium.InvalidElementStateException
//import org.openqa.selenium.Keys
//import org.openqa.selenium.OutputType
//
//import com.kms.katalon.core.appium.driver.AppiumDriverManager
//import com.kms.katalon.core.configuration.RunConfiguration
//import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
//import com.kms.katalon.core.exception.StepErrorException
//import com.kms.katalon.core.exception.StepFailedException
//import com.kms.katalon.core.logging.KeywordLogger
//import com.kms.katalon.core.mobile.driver.AppiumDriverSession
//import com.kms.katalon.core.mobile.driver.MobileDriverType
//import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
//import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
//import com.kms.katalon.core.model.FailureHandling
//import com.kms.katalon.core.testcase.TestCase
//import com.kms.katalon.core.testdata.TestData
//import com.kms.katalon.core.testobject.ConditionType
//import com.kms.katalon.core.testobject.ResponseObject
//import com.kms.katalon.core.testobject.TestObject
//import com.kms.katalon.core.util.KeywordUtil
//
//import internal.GlobalVariable
//import io.appium.java_client.AppiumDriver
//import io.appium.java_client.MobileElement
//import io.appium.java_client.TouchAction
//import io.appium.java_client.remote.AutomationName
//import io.appium.java_client.remote.MobileCapabilityType
//import io.appium.java_client.screenrecording.BaseStartScreenRecordingOptions
//import io.appium.java_client.screenrecording.CanRecordScreen
//import io.appium.java_client.touch.TapOptions
//import io.appium.java_client.touch.WaitOptions
//import io.appium.java_client.touch.offset.PointOption
//import io.appium.java_client.android.AndroidDriver
//import io.appium.java_client.android.AndroidStartScreenRecordingOptions
//import io.appium.java_client.clipboard.ClipboardContentType
//import io.appium.java_client.clipboard.HasClipboard
//import io.appium.java_client.functions.ExpectedCondition
//import io.appium.java_client.ios.IOSDriver
//import io.appium.java_client.ios.IOSStartScreenRecordingOptions
//import io.appium.java_client.ios.IOSStartScreenRecordingOptions.VideoQuality
//import io.appium.java_client.MobileBy
//import io.appium.java_client.MobileBy.ByCustom
//import org.openqa.selenium.Point
//import org.openqa.selenium.StaleElementReferenceException
//import org.openqa.selenium.remote.DesiredCapabilities
//import org.openqa.selenium.remote.RemoteWebDriver
//
//import atu.testrecorder.ATUTestRecorder
//import atu.testrecorder.exceptions.ATUTestRecorderException
//import groovy.json.JsonSlurper
//
//import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
//
//public class MobileHelpers {
//	static Map textObjectRepository
//
//	static {
//		if(!textObjectRepository) {
//			String dir = System.getProperty("user.dir") + GlobalVariable.EVEREST_ASSETS_LOCATION + "/text_object_repository.json"
//			JsonSlurper jsonSlurper = new JsonSlurper()
//
//			textObjectRepository = jsonSlurper.parse(new File(dir))
//		}
//	}
//
//	def static String getAppFilePath() {
//		if(isIos()) {
//			return "${GlobalVariable.APP_PATH_IOS}/${GlobalVariable.APP_NAME_IOS}"
//		}
//
//		if(isAndroid()) {
//			return "${GlobalVariable.APP_PATH_ANDROID}/${GlobalVariable.APP_NAME_ANDROID}"
//		}
//	}
//
//	def static void restartExistingApplication(Map mergeDataRow = null) {
//		if(GlobalVariable.IS_GRID) {
//			if(!getDriver()) {
//
//				DesiredCapabilities capabilities = new DesiredCapabilities()
//
//				String os = mergeDataRow["OS Name"].toString().toLowerCase()
//
//				capabilities.setCapability(MobileCapabilityType.UDID, mergeDataRow["TargetDeviceId"]);
//				capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, mergeDataRow["Device Model"]);
//				capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, mergeDataRow["OS Name"]);
//				capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, mergeDataRow["OS Version"]);
//				capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 180);
//				capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
//				capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
//				capabilities.setCapability("appium:mjpegServerPort", mergeDataRow["MjpegServerPort"]);
//				//				capabilities.setCapability("appium:mjpegScreenshotUrl", "http://localhost:${mergeDataRow['MjpegServerPort']}");
//
//				if(os == "ios") {
//
//					capabilities.setCapability("wdaLocalPort", mergeDataRow["SystemPort"]);
//					capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
//					capabilities.setCapability(MobileCapabilityType.APP, GlobalVariable.IOS_BUNDLE_ID);
//
//					AppiumDriverManager.createMobileDriver(MobileDriverType.IOS_DRIVER, capabilities, new URL("${GlobalVariable.GRID_URL}/wd/hub"))
//				}else if(os == "android") {
//
//					capabilities.setCapability("systemPort", mergeDataRow["SystemPort"]);
//					capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
//					capabilities.setCapability("appPackage", GlobalVariable.ANDROID_PACKAGE_NAME);
//					capabilities.setCapability("appActivity", GlobalVariable.ANDROID_APP_ACTIVITY);
//					KeywordUtil.logInfo('masuk')
//					AppiumDriverManager.createMobileDriver(MobileDriverType.ANDROID_DRIVER, capabilities, new URL("${GlobalVariable.GRID_URL}/wd/hub"))
//				}
//				KeywordUtil.logInfo(getDriver().getCapabilities().toString())
//
//				MobileHelpers.resetApp() // ensure application is terminated to prevent app is not open if there is Livin session in task manager
//			}else {
//				MobileHelpers.resetApp()
//			}
//		}else {
//			if(!getDriver()) {
//				if(GlobalVariable.IsAppiumOld) {
//					String os = Mobile.getDeviceOS().toLowerCase()
//					String appID
//					if(os == "ios") {
//						appID = GlobalVariable.IOS_BUNDLE_ID
//					}else {
//						appID = GlobalVariable.ANDROID_PACKAGE_NAME
//					}
//					Mobile.startExistingApplication(appID)
//					AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//					Mobile.delay(5)
//					if(os == "android") {
//						//			Mobile.delay(10) // delay to wait the app fully loaded before it can be killed
//						driver.terminateApp(getAppId())  //kill and restart it again
//						//com.kms.katalon.core.appium.driver.AppiumDriverManager.getDriver().quit()
//						Mobile.startExistingApplication(getAppId())
//						Mobile.delay(5) // delay to wait the app fully loaded before continue to next step
//					}
//
//					if(isIos()) {
//						if(RunConfiguration.getRunningMode().getMode() == "console") {
//							driver.terminateApp(getAppId())  //need to kill and restart it again if running mode is console
//							Mobile.delay(1)
//							launchApp(getAppId())
//							Mobile.delay(5)
//						}
//					}
//				}else {
//					DesiredCapabilities capabilities = new DesiredCapabilities()
//					Mobile.delay(5)
//
//					capabilities.setCapability(MobileCapabilityType.UDID, MobileDriverFactory.getDeviceId());
//					capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, MobileDriverFactory.getDeviceName());
//					capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobileDriverFactory.getDevicePlatform());
//					capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, MobileDriverFactory.getDeviceOSVersion());
//					capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
//					capabilities.setCapability(MobileCapabilityType.APP, GlobalVariable.IOS_BUNDLE_ID);
//
//					String os = Mobile.getDeviceOS().toLowerCase()
//					if (os == "android") {
//						AppiumDriverManager.createMobileDriver(MobileDriverType.ANDROID_DRIVER, capabilities, new URL("http://localhost:9000/wd/hub"))
//					}
//					else if (os == "ios") {
//						AppiumDriverManager.createMobileDriver(MobileDriverType.IOS_DRIVER, capabilities, new URL("http://localhost:9000/wd/hub"))
//					}
//				}
//			}else {
//				MobileHelpers.resetApp()
//			}
//		}
//	}
//
//	def static AppiumDriver getDriver() {
//		//		return AppiumDriverManager.getDriver()
//		try {
//			return AppiumDriverManager.getDriver()
//		}catch (StepFailedException e) {
//			KeywordUtil.logInfo(e.getMessage())
//			return null
//		}
//	}
//
//	def static void quitDriver() {
//		if(getDriver()) {
//			getDriver().quit()
//		}
//	}
//
//	def static void resetApp() {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		driver.terminateApp(getAppId())
//		MobileHelpers.launchApp(MobileHelpers.getAppId())
//	}
//
//	def static String getDesiredCapabilities(String key) {
//		/*
//		 * key return value:
//		 * platformName: device os -> iOS / Android
//		 * platformVersion: device os version -> 16.0
//		 * udid: deviceId / udid -> e8f8f9a194ec85c75f7204f8b85c09f2aab4055f
//		 * deviceName: device name -> (IphoneX)
//		 */
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		return driver.getCapabilities().getCapability(key).toString()
//	}
//
//	def static String getAppId() {
//		//		String os = Mobile.getDeviceOS().toLowerCase() tbr
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if (os == "android") {
//			return GlobalVariable.ANDROID_PACKAGE_NAME
//		}
//		else if (os == "ios") {
//			return GlobalVariable.IOS_BUNDLE_ID
//		}
//	}
//
//
//	def static String getSMSAppId() {
//		//		String os = Mobile.getDeviceOS().toLowerCase()
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if(os == "android") {
//			//please install google messaging app and set it as default SMS client
//			//https://play.google.com/store/apps/details?id=com.google.android.apps.messaging&hl=en
//			return "com.google.android.apps.messaging"
//		}
//		else if(os == "ios") {
//			return "com.apple.MobileSMS" // using native iOS message
//		}
//	}
//
//	def static String getMailAppId() {
//
//		//		String os = Mobile.getDeviceOS().toLowerCase()
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if(os == "android") {
//			return "com.google.android.gm" //using gmail to read email
//		}
//		else if(os == "ios") {
//			return "com.google.Gmail" // using gmail to read email
//		}
//	}
//
//	def static TestObject findTestObject(String path, Map variable=[:]) {
//		MobileDriverFactory driver = new MobileDriverFactory();
//		//		String manufacture = Mobile.getDeviceManufacturer().toLowerCase()
//		String deviceModel = driver.getDeviceModel()
//		//		println "DEBUG: device $deviceModel manufacturer is $manufacture"
//
//		String tempPath = ""
//		if(isIos()) {
//			// Modify Path to point to correct Object Repository
//			// e.g From Android/Provisioning/Debit Card Input/btn_debitCardInput_Submit
//			//     into iOS/Provisioning/Debit Card Input/btn_debitCardInput_Submit
//			//     and vice versa
//			if(path.contains("Android")) {
//				path = path.replaceFirst(Constants.ANDROID, Constants.IOS)
//			}
//
//			//			tempPath = path
//			//			if (deviceModel == Constants.IPHONE8_MODEL) {
//			//				path = path.replaceFirst(Constants.IOS, Constants.IPHONE8)
//			//			} else if (deviceModel == Constants.IPHONEXR_MODEL) {
//			//				path = path.replaceFirst(Constants.IOS, Constants.IPHONEXR)
//			//			}
//		}else {
//			// to resolve different bundle id as per environment in resource-id (Android)
//			// XPATH in the object repository must follow; //*[@resource-id = '${appId}:id/button_next']
//			// so the appId can be replaced with GlobalVariable.ANDROID_PACKAGE_NAME or GlobalVariable.IOS_BUNDLE_ID
//
//			Map appId = ['appId': getAppId()]
//			variable = variable + appId
//
//			// Modify Path to point to correct Object Repository
//			// e.g From Android/Provisioning/Debit Card Input/btn_debitCardInput_Submit
//			//     into Object Repository/Samsung/Provisioning/Debit Card Input/btn_debitCardInput_Submit
//			if(path.contains("iOS")) {
//				path = path.replaceFirst(Constants.IOS, Constants.ANDROID)
//			}
//
//			//			tempPath = path
//			//			if (manufacture == Constants.GENPROZ.toLowerCase()) {
//			//				path = path.replaceFirst(Constants.ANDROID, Constants.GENPROZ)
//			//			} else if (manufacture == Constants.HONOR.toLowerCase()) {
//			//				path = path.replaceFirst(Constants.ANDROID, Constants.HONOR)
//			//			} else if (manufacture == Constants.SAMSUNG.toLowerCase()) {
//			//				path = path.replaceFirst(Constants.ANDROID, Constants.SAMSUNG)
//			//			} else if (manufacture == Constants.ASUS.toLowerCase()) {
//			//				path = path.replaceFirst(Constants.ANDROID, Constants.ASUS)
//			//			} else if (manufacture == Constants.XIAOMI.toLowerCase()) {
//			//				path = path.replaceFirst(Constants.ANDROID, Constants.XIAOMI)
//			//			} else if (manufacture == Constants.OPPO.toLowerCase()) {
//			//				path = path.replaceFirst(Constants.ANDROID, Constants.OPPO)
//			//			}
//		}
//
//		println "DEBUG: Test Object Path is $path"
//
//		if (!verifyObjectExistInRepository(path)) {
//			println "DEBUG: Path is not found. Using alternative path."
//			path = tempPath //Use original path because OR in specific device path is not exist.
//		}
//
//		println "DEBUG: Test Object being used is $path"
//
//		// Get the TestObject
//		TestObject obj = com.kms.katalon.core.testobject.ObjectRepository.findTestObject(path, variable)
//
//		return obj
//	}
//
//	//	def static int findObjectIdx(String page, String object) {
//	//		MobileDriverFactory driver = new MobileDriverFactory();
//	//		String manufacture = Mobile.getDeviceManufacturer().toLowerCase()
//	//		String deviceModel = driver.getDeviceModel()
//	//		println "DEBUG: device $deviceModel manufacturer is $manufacture"
//	//
//	//		String device = "default"
//	//		if(isIos()) {
//	//			if (deviceModel == Constants.IPHONE8_MODEL) {
//	//				device = Constants.IPHONE8
//	//			} else if (deviceModel == Constants.IPHONEXR_MODEL) {
//	//				device = Constants.IPHONEXR
//	//			}
//	//		}else {
//	//			if (manufacture == Constants.GENPROZ.toLowerCase()) {
//	//				device = Constants.GENPROZ
//	//			} else if (manufacture == Constants.HONOR.toLowerCase()) {
//	//				device = Constants.HONOR
//	//			} else if (manufacture == Constants.SAMSUNG.toLowerCase()) {
//	//				device = Constants.SAMSUNG
//	//			} else if (manufacture == Constants.ASUS.toLowerCase()) {
//	//				device = Constants.ASUS
//	//			} else if (manufacture == Constants.XIAOMI.toLowerCase()) {
//	//				device = Constants.XIAOMI
//	//			} else if (manufacture == Constants.OPPO.toLowerCase()) {
//	//				device = Constants.OPPO
//	//			}
//	//		}
//	//
//	//		String dir = GlobalVariable.EVEREST_DATA_LOCATION + "/device_page_objects.json"
//	//		JsonSlurper jsonSlurper = new JsonSlurper()
//	//		Map indexes = jsonSlurper.parse(new File(dir))
//	//
//	//		int index = -1
//	//		if(indexes) {
//	//			index = getIndexValue(indexes, device, page, object)
//	//
//	//			if(index == -1) {
//	//				device = "default"
//	//				index = getIndexValue(indexes, device, page, object)
//	//			}
//	//
//	//		}
//	//
//	//		LogHelpers.printLog("Stored index of $device / $page / $object = $index")
//	//		return index
//	//	}
//
//	def static int getIndexValue(Map indexes, String device, String page, String object) {
//		int index = -1
//		try {
//			index = Integer.valueOf(indexes[device][page][object])
//		} catch (Exception ex) {
//		}
//		return index
//	}
//
//	def static boolean verifyObjectExistInRepository(String path) {
//		if(path.startsWith("Object Repository")) {
//			path = "./" + path  + ".rs"
//		} else {
//			path = "./Object Repository/" + path + ".rs"
//		}
//
//		File file = new File(path)
//		return file.exists()
//	}
//
//	def static boolean scrollToObject(TestObject obj, int timeoutCount=1, int offset=0, int x=0, int y=0, String direction = Constants.SCROLL_DOWN, FailureHandling failureHandler = FailureHandling.OPTIONAL) {
//		/**
//		 * obj: object to be searched
//		 * timeoutCount: how many scroll do we need before mark it as failed
//		 * offset: offset to perform Y axis scroll,
//		 * x: to override start 'x' coordinate
//		 * y: to override start 'y' coordinate
//		 * direction: default scroll to 'down', possible value is 'up' to do scroll up
//		 * failureHandler: failure handling if object is not found
//		 */
//
//		// if object is not defined, just scroll as much as timeoutCount value
//
//		int height = Mobile.getDeviceHeight()
//		int width = Mobile.getDeviceWidth()
//
//		// if offset is not set, default is to scroll from almost bottom to top of the screen
//		int startY
//		int endY
//		if(direction == Constants.SCROLL_UP) {
//			startY = !offset ? height * 0.1 : height * 0.5
//			endY = !offset ? height * 0.9 : startY + offset
//		}else {
//			// scroll down
//			startY = !offset? height * 0.9 : height * 0.5
//			endY = !offset ? height * 0.1 : startY - offset
//		}
//
//		int centerX = width / 2
//
//		// override startX and startY if exist on parameter
//		centerX = x ? x : centerX
//		startY = y ? y : startY
//
//		int idx = 1
//		boolean isFound = obj ? Mobile.verifyElementVisible(obj, 1, FailureHandling.OPTIONAL) : false
//		println "idx: " + idx + " found: " + isFound
//
//		takeScreenshot()
//		while((idx<=timeoutCount) && !isFound) {
//			Mobile.swipe(centerX, startY, centerX, endY)
//			keepAlive()
//			Mobile.delay(1)
//			idx++
//			isFound = obj ? Mobile.verifyElementVisible(obj, 1, FailureHandling.OPTIONAL) : false
//			println "idx: " + idx + " found: " + isFound
//		}
//		takeScreenshot()
//
//		if(obj && !isFound) {
//			return Mobile.verifyElementVisible(obj, 1, failureHandler)
//		}
//
//		return isFound
//	}
//
//	def static boolean waitElementIsNotPresent(String className, int index, int interval, int maxInterval) {
//		boolean isPresent = isElementPresentAndVisible(className, index)
//		int start = interval
//		int retry = 1
//		LogHelpers.printLog("Element is present($isPresent). Will wait for $start seconds (max wait $maxInterval seconds) until the element is not present anymore.")
//
//		while((start <= maxInterval) && isPresent) {
//			LogHelpers.printLog("Element is present($isPresent). Wait duration: $start seconds. Remaining retry: $retry times")
//			Mobile.swipe(10, 20, 10, 10)
//			Mobile.delay(interval)
//			start += interval
//
//			isPresent = isElementPresentAndVisible(className, index)
//			if(isPresent) {
//				//element still present
//			} else {
//				//element not present or not detected. Retry for next interval.
//				if(retry > 0) {
//					retry--
//					isPresent = true
//				}
//			}
//
//			takeScreenshot()
//		}
//		LogHelpers.printLog("Element is present($isPresent). Total wait duration: $start seconds.")
//
//		return isPresent
//	}
//
//	def static boolean waitElementIsNotPresent(TestObject obj, int interval, int maxInterval) {
//		boolean isPresent = false
//		int start = interval
//
//		try {
//			isPresent = Mobile.waitForElementPresent(obj, interval, FailureHandling.OPTIONAL)
//			boolean isVisible = Mobile.verifyElementVisible(obj, 1, FailureHandling.OPTIONAL)
//			KeywordUtil.logInfo("Object is present: $isPresent and visible: $isVisible")
//			while(isPresent && (start <= maxInterval)) {
//				Mobile.delay(interval)
//				start+=interval
//				isPresent = Mobile.waitForElementPresent(obj, interval, FailureHandling.OPTIONAL)
//				isVisible = Mobile.verifyElementVisible(obj, 1, FailureHandling.OPTIONAL)
//			}
//
//			KeywordUtil.logInfo("Object is present: $isPresent and visible: $isVisible after $start seconds")
//			KeywordUtil.markPassed("Element no longer present")
//			takeScreenshot()
//		} catch (StaleElementReferenceException ex) {
//			isPresent = false
//		}
//
//		return isPresent
//	}
//
//	def static boolean waitElementText(TestObject obj, int interval, int maxInterval, String expectedText) {
//		boolean isTextCorrect = false
//		int start = interval
//
//		try {
//			while((start <= maxInterval) && !isTextCorrect) {
//				Mobile.delay(interval)
//				start+=interval
//				if(Mobile.waitForElementPresent(obj, interval, FailureHandling.OPTIONAL)) {
//					String text = Mobile.getText(obj, 5, FailureHandling.OPTIONAL)
//					Mobile.comment("Found text: $text")
//					if(text == expectedText) {
//						isTextCorrect = true
//					}
//				}
//			}
//
//			takeScreenshot()
//		} catch (StaleElementReferenceException ex) {
//			isTextCorrect = false
//		}
//		return isTextCorrect
//	}
//
//	def static boolean waitElementIsPresent(TestObject obj, int interval, int maxInterval) {
//		boolean isPresent = false
//		int start = interval
//
//		try {
//			isPresent = Mobile.waitForElementPresent(obj, interval, FailureHandling.OPTIONAL)
//
//			while((start <= maxInterval) && !isPresent) {
//				Mobile.delay(interval)
//				start+=interval
//				isPresent = Mobile.waitForElementPresent(obj, interval, FailureHandling.OPTIONAL)
//				takeScreenshot()
//			}
//		} catch (StaleElementReferenceException ex) {
//			isPresent = false
//		}
//		return isPresent
//	}
//
//	def static void inputEPIN(String epin) {
//		Mobile.delay(2)
//		takeScreenshot()
//		Mobile.comment("Input EPIN: " + epin)
//
//		epin = epin.contains(".")? epin.substring(0, epin.indexOf(".")) : epin.trim()
//		//for(int i=0; i<epin.length(); i++) {
//		int remainingDigit = 6
//		for(int i = 0; i < epin.length() && remainingDigit > 0; i++) {
//			// in case need to handle delete button, parsing string 'x' to 'del' (as per numpad object name to delete)
//			String idx = ""
//			if(epin.charAt(i) == "x") {
//				idx = "del"
//				remainingDigit++
//			} else {
//				idx = epin.charAt(i)
//				remainingDigit--
//			}
//
//			Mobile.comment("Input digit ${i + 1}: $idx")
//			Mobile.tap(findTestObject("iOS/General/Numpad/btn_epinInput_$idx"), 15)
//		}
//	}
//
//	def static boolean verifyElementExist(TestObject obj, int timeOut) {
//		boolean isExist = false
//		try {
//			isExist = Mobile.verifyElementExist(obj, timeOut)
//		}
//		catch (StepFailedException e) {
//			println(e.message)
//		}
//		catch (Exception e) {
//			println(e.message)
//		}
//
//		return isExist
//	}
//
//	def static boolean verifyElementVisible(TestObject obj, int timeOut) {
//		boolean isExist = false
//		try {
//			isExist = Mobile.verifyElementVisible(obj, timeOut)
//		}
//		catch (StepFailedException e) {
//			println(e.message)
//		}
//		catch (Exception e) {
//			println(e.message)
//		}
//
//		return isExist
//	}
//
//	def static String takeScreenshot() {
//		Date date =  new Date()
//		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd_HHmmss")
//		String formattedDate = sdf.format(date)
//		String filename = formattedDate
//
//		File destFile = new File(GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/" + filename + ".png")
//
//		//println "DEBUG: Screenshot location: $destFile"
//
//		AppiumDriver driver = MobileDriverFactory.getDriver()
//		try {
//			File tempFile = driver.getScreenshotAs(OutputType.FILE)
//			FileUtils.copyFile(tempFile, destFile)
//			Mobile.comment("Screenshot location: ${destFile.canonicalPath}")
//			LogHelpers.printLog("Screenshot location: ${destFile.canonicalPath}")
//		} catch (Exception ex) {
//		}
//
//		return destFile.getCanonicalPath()
//	}
//
//
//	def static driverTakeScreenshot() {
//		// to take screenshot with async method
//		Date date =  new Date()
//		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd_HHmmss")
//		String formattedDate = sdf.format(date)
//		String filename = formattedDate
//
//		AppiumDriver driver = MobileDriverFactory.getDriver()
//		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
//
//		File tempFile = driver.getScreenshotAs(OutputType.FILE)
//		File location = new File(GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/" + filename + ".png")
//
//		println "DEBUG: Screenshot location: $location"
//		FileUtils.copyFile(tempFile, location)
//		Mobile.comment("Screenshot saved in: " + location)
//	}
//
//	def static boolean isIos() {
//		//		return Mobile.getDeviceOS().toLowerCase() == Constants.IOS.toLowerCase()
//		return getDesiredCapabilities("platformName").toLowerCase() == Constants.IOS.toLowerCase()
//	}
//
//	def static boolean isAndroid() {
//		//		return Mobile.getDeviceOS().toLowerCase() == Constants.ANDROID.toLowerCase()
//		return getDesiredCapabilities("platformName").toLowerCase() == Constants.ANDROID.toLowerCase()
//	}
//
//	def static boolean isAndroidWithSmallScreen() {
//		boolean isSmallScreen = false
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if(os == Constants.ANDROID.toLowerCase()) {
//			int height = Mobile.getDeviceHeight()
//			Mobile.comment("Device height is $height")
//			if(height <= 1920) {
//				isSmallScreen = true
//			}
//		}
//		return isSmallScreen
//	}
//
//	def static String getTargetDevice() {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		return isIos() ? driver.getCapabilities().getCapability("udid") : driver.getCapabilities().getCapability('deviceName')
//	}
//
//	def static String getTargetOS() {
//		// previously this code is get target device
//		return isIos() ? Constants.IOS : Constants.ANDROID
//	}
//
//	def static void getDeviceInfo() {
//		AndroidDriver<?> driver = MobileDriverFactory.getDriver()
//		String deviceName = driver.getCapabilities().getCapability('deviceName')
//		String deviceModel = driver.getCapabilities().getCapability('deviceModel')
//		String deviceManufacturer = driver.getCapabilities().getCapability('deviceManufacturer')
//		String udid = driver.getCapabilities().getCapability('udid')
//		String deviceOS = Mobile.getDeviceOS()
//		String deviceOSVersion = Mobile.getDeviceOSVersion()
//
//		println "Device Name = " + deviceName
//		println "Device Model = " + deviceModel
//		println "Device Manufacture = " + deviceManufacturer
//		println "Device OS = " + deviceOS
//		println "Device OSVersion = " + deviceOSVersion
//
//		def capabilities = driver.getCapabilities()
//		capabilities.each{key, value ->
//			println "$key : $value"
//		}
//	}
//
//	def static void swipeAndHoldAtPosition(int startX, int startY, int endX, int endY, int duration, AppiumDriver<?> driver=null) throws StepFailedException {
//		/**
//		 * parameter:
//		 * startX: x axis start coordinate
//		 * startY: y axix start coordinate
//		 * endX: x axis end coordinate
//		 * endY: y axix end coordinate
//		 * duration: number of seconds. How long the hold gesture at the endY and endX before release it
//		 * driver: pass driver for async call
//		 */
//
//		// TODO: if there is any better way than this updated it
//		AppiumDriver<?> currentDriver = driver ? driver : MobileDriverFactory.getDriver()
//
//		TouchAction touchAction = new TouchAction(currentDriver)
//				.press(PointOption.point(startX, startY))
//				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
//				.moveTo(PointOption.point(endX, endY))
//				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(duration)))
//				.release()
//
//		if(driver) {
//			new Thread({ touchAction.perform() }).start()
//		}else {
//			touchAction.perform()
//		}
//	}
//
//	def static String maskingCardNo(String cardNo, String type) {
//
//		/**
//		 * type: cc / casa
//		 */
//		String lastFourDigits = ""
//		Integer cardSize = cardNo.size()-5
//		String symbol = ""
//
//
//		for(int i = 0; i<=cardSize; i++) {
//			symbol += "•"
//		}
//
//		if(cardNo.length() > 4) {
//			lastFourDigits = cardNo.substring(cardNo.length() - 4);
//		} else {
//			lastFourDigits = cardNo;
//		}
//
//
//		return type == Constants.CASA ? "$symbol$lastFourDigits" : Constants.CC ?"••••••••••••$lastFourDigits" : "•••••••••••••"
//	}
//
//	def static String maskingCardNoCustom(String cardNo, String type, String symbolMask) {
//
//		/**
//		 * type: cc / casa
//		 * symbolMask: **** / ••••
//		 */
//		String lastFourDigits = ""
//		Integer cardSize = cardNo.size()-5
//		String symbol = ""
//
//
//		for(int i = 0; i<=cardSize; i++) {
//			symbol += symbolMask
//		}
//
//		if(cardNo.length() > 4) {
//			lastFourDigits = cardNo.substring(cardNo.length() - 4);
//		} else {
//			lastFourDigits = cardNo;
//		}
//
//
//		return type == Constants.CASA ? "$symbol$lastFourDigits" : Constants.CC ?"••••••••••••$lastFourDigits" : "•••••••••••••"
//	}
//
//	def static String getClipboardText() {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//
//		if(MobileHelpers.isIos()) {
//			// iOS: WebDriverAgentRunner harus jalan secara foreground agar dapat get text dari clipboard
//			driver.activateApp('com.apple.springboard')
//			MobileElement wdaRunner = driver.findElementByAccessibilityId('WebDriverAgentRunner-Runner')
//			wdaRunner.click()
//
//			((HasClipboard) driver).getClipboard(ClipboardContentType.PLAINTEXT)
//			String clipboardText = ((HasClipboard) driver).getClipboardText()
//			KeywordUtil.logInfo("clipboard text: ${clipboardText}")
//			// Kembali ke Livin' App
//			driver.activateApp(GlobalVariable.IOS_BUNDLE_ID)
//
//			return clipboardText
//		}else {
//			// Android
//			((HasClipboard) driver).getClipboard(ClipboardContentType.PLAINTEXT)
//			String clipboardText = ((HasClipboard) driver).getClipboardText()
//			KeywordUtil.logInfo("clipboard text: ${clipboardText}")
//
//			return clipboardText
//		}
//	}
//
//	def static void startRecording() {
//		if(GlobalVariable.MOBILE_VIDEO_RECORDING) {
//			// initiate start video recording
//			AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//			if (isIos()) {
//				//For IOS, please install ffmpeg (brew install ffmpeg)
//				((CanRecordScreen) driver).startRecordingScreen(new IOSStartScreenRecordingOptions()
//						.withVideoQuality(VideoQuality.MEDIUM)
//						.withVideoScale("1280:720")
//						.withVideoType("h264")
//						.withTimeLimit(Duration.ofSeconds(600)))
//			} else {
//				try {
//					if(isUniqueManufacture()) {
//						startAndroidQRecording()
//					}else {
//						((CanRecordScreen) driver).startRecordingScreen(new AndroidStartScreenRecordingOptions()
//								.withBitRate(1000000)
//								.withVideoSize("1280x720")
//								.withTimeLimit(Duration.ofSeconds(600)))
//					}
//				} catch (Exception ex) {
//					Mobile.comment("This device does not support screen recording.")
//					GlobalVariable.MOBILE_VIDEO_RECORDING = false
//				}
//			}
//		}
//	}
//
//	def static void stopRecording() {
//		if(GlobalVariable.MOBILE_VIDEO_RECORDING) {
//			if(!isIos() && isUniqueManufacture()) {
//				stopAndroidQRecording()
//			}else {
//				// for ios: need to install 'brew install ffmpeg'
//				// stop recording and save video recording file
//				Date date =  new Date()
//				SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd_HHmmss")
//				String formattedDate = sdf.format(date)
//				String filename = formattedDate
//
//				AppiumDriver<?> driver
//				try {
//					driver = MobileDriverFactory.getDriver()
//				} catch (Exception e) {
//				}
//
//				if(driver) {
//					String base64 = ((CanRecordScreen) driver).stopRecordingScreen()
//					String videoLocation = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/" + filename + ".mp4"
//					if(base64) {
//						byte[] decode = Base64.getDecoder().decode(base64);
//						FileUtils.writeByteArrayToFile(new File(videoLocation), decode);
//					}
//					//		byte[] data = Base64.
//					//		String destinationPath = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/" + filename + ".mp4"
//					//		Path path = Paths.get(destinationPath)
//					//		Files.write(path, decode)
//				}
//			}
//		}
//	}
//
//
//	def static void tap(String className, int index){
//		int instanceNum = index - 1
//		MobileElement element = getElement(className, index)
//
//		if(element) {
//			element.click()
//		}
//	}
//
//
//	def static boolean waitForElementPresent(String className, int index, int timeout){
//		boolean isPresent = isElementPresentAndVisible(className, index)
//		for(int i = 0; i < timeout && !isPresent; i++) {
//			Mobile.delay(1) // delay 1 second
//			if(i % 3 == 0 ) {
//				MobileHelpers.keepAlive()
//			}
//			isPresent = isElementPresentAndVisible(className, index) // check again
//		}
//
//		if(isPresent) {
//			LogHelpers.printLog("Element is $isPresent")
//			return true
//		} else {
//			//KeywordUtil.markFailed("Element is not found.")
//		}
//	}
//
//	def static String getText(String className, int index) {
//		String txt = ""
//		MobileElement element = getElement(className, index)
//		if(element) {
//			txt = element.getText()
//			println "DEBUG: text is found: $txt "
//		}
//		return txt
//	}
//
//	def static MobileElement getElement(String className, int index) {
//		MobileElement element
//		List<MobileElement> elements = getElements(className)
//		if(elements) {
//			int size = elements.size()
//			int arrayIndex = index - 1
//
//			if(size > 0 && arrayIndex < size) {
//				LogHelpers.printLog("getElement: Returning $className in index $index of $size")
//				element = elements.get(arrayIndex)
//			}
//		}
//		return element
//	}
//
//	def static List<MobileElement> getElements(String className) {
//		List<MobileElement> elements
//		//keepAlive()
//
//		try {
//			AppiumDriver driver = MobileDriverFactory.getDriver()
//			keepAlive()
//			elements = (List<MobileElement>) driver.findElementsByClassName(className)
//			keepAlive()
//			if(GlobalVariable.DEBUG_MODE) {
//				//in debug mode, print the element atts
//				int size = elements.size()
//				LogHelpers.printLog("Found $size elements of $className...")
//
//				for(int i = 0; i < size; i++) {
//					if(i % 3 == 0) keepAlive()
//					String content = elements.get(i).getText()
//					//supported attribute for Android: checkable, checked, {class,className}, clickable, {content-desc,contentDescription}, enabled, focusable, focused, {long-clickable,longClickable}, package, password, {resource-id,resourceId}, scrollable, selection-start, selection-end, selected, {text,name}, bounds, displayed, contentSize
//
//
//					String name = elements.get(i).getAttribute("name")
//					String label = elements.get(i).getAttribute("label")
//					String visible = "false"
//					if(isIos()) {
//						visible = elements.get(i).getAttribute("visible")
//					} else {
//						visible = elements.get(i).getAttribute("displayed")
//					}
//					String selected = elements.get(i).getAttribute("selected")
//					String enabled = elements.get(i).getAttribute("enabled")
//					int index = i + 1
//					LogHelpers.printLog("  $index: '$content'.")
//					LogHelpers.printLog("        name: $name, label: $label, visible: $visible, selected: $selected, enabled: $enabled")
//				}
//			}
//		} catch (Exception e) {
//			//e.printStackTrace()
//		}
//
//		return elements
//	}
//
//	def static Point getElementLocationByXPath(String xpath) {
//		Point location
//		MobileElement element
//
//		try {
//			AppiumDriver driver = MobileDriverFactory.getDriver()
//			element = (MobileElement) driver.findElementByXPath(xpath)
//			location = element.getCenter()
//		} catch (Exception e) {
//			e.printStackTrace()
//		}
//
//		return location
//	}
//
//	def static HashMap<String, Point> getElementsLocationsByXPath(String xpath) {
//		HashMap<String, Point> objects = new HashMap<String, Point>()
//		List<MobileElement> elements
//
//		try {
//			AppiumDriver driver = MobileDriverFactory.getDriver()
//			elements = (List<MobileElement>) driver.findElementsByXPath(xpath)
//			Mobile.comment(elements.size() + " is found")
//
//			for(int i = 0; i < elements.size(); i++) {
//				Point location = elements.get(i).getCenter()
//				KeywordUtil.logInfo("x: ${location.x}, y: ${location.y}")
//				objects["$i"] = location
//			}
//		} catch (Exception e) {
//			e.printStackTrace()
//		}
//
//		return objects
//	}
//
//	def static void sortElementsByLocations(HashMap<String, Point> objects) {
//		Point tempLoc
//
//		//Mobile.comment("Before sorting: $objects")
//		try {
//			//2, 1, 3,
//			for(int i = 0; i < objects.size(); i++) {
//				Point loc1 = objects["$i"]
//
//				for(int j = objects.size() - 1; j > i; j--) {
//					Point loc2 = objects["$j"]
//					//Mobile.comment("first obj: $i ${loc1.x}")
//					//Mobile.comment("second obj: $j ${loc2.x}")
//
//					if(loc1.x > loc2.x) {
//						//switch
//						tempLoc = objects["$i"]
//						objects["$i"] = objects["$j"]
//						objects["$j"] = tempLoc
//						//Mobile.comment("switch: $objects")
//						loc1 = objects["$i"]
//					}
//				}
//				//Mobile.comment("------------")
//			}
//			//Mobile.comment("After sorting: $objects")
//		} catch (Exception e) {
//			e.printStackTrace()
//		}
//	}
//
//	def static boolean isElementPresentAndVisible(String className, int index) {
//		MobileElement element
//		boolean elementPresentandVisible = false
//		try {
//			element = getElement(className, index)
//		} catch (Exception ex) {
//			element = getElement(className, index)
//		}
//		if(element) {
//			if(isIos()) {
//				elementPresentandVisible = element.getAttribute("visible")
//			} else {
//				elementPresentandVisible = element.getAttribute("displayed")
//			}
//			LogHelpers.printLog("Element $className is present and visible($elementPresentandVisible)")
//		}
//
//		return elementPresentandVisible
//	}
//
//	def static int getNumberOfElement(String className) {
//		List<MobileElement> elements = getElements(className)
//		int numOfElement = 0
//		if(elements) {
//			numOfElement = elements.size()
//		}
//		KeywordUtil.logInfo("summary of element : $numOfElement")
//		return numOfElement
//	}
//
//	def static void sendKeys(String className, int index, String input) {
//		MobileElement element = getElement(className, index)
//		if(element) {
//			element.sendKeys(input)
//		} else {
//			println "Element not found!"
//		}
//	}
//
//	def static void sendKeys(String input) {
//		//read this: http://appium.io/docs/en/commands/mobile-command/
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		driver.executeScript("mobile: type", ["text": input])
//	}
//
//	public static String getXpath(TestObject obj) {
//		String xpath = obj.getSelectorCollection()[obj.selectorMethod] //get xpath from Katalon's test object
//		if(!xpath) {
//			xpath = obj.findPropertyValue("xpath")
//		} //get xpath from Custom test object
//
//		return xpath
//	}
//
//	def static void tapAtPosition(TestObject obj, int offsetX = 0, int offsetY = 0) {
//		//getting coordinate from page source
//		String xpath = getXpath(obj)
//		def (x,y,width,height) = MobilePageSrcHelpers.getObjectPosition(xpath)
//		Mobile.comment("Obj location: $x, $y. Tap at ${x + offsetX}, ${y + offsetY}.")
//		Mobile.tapAtPosition(x + offsetX, y + offsetY)
//	}
//
//	def static void tapAtCoord(Point location) {
//		tapAtCoord(location.x, location.y)
//	}
//
//	def static void tapAtCoord(int x, int y) {
//		//read this: http://appium.io/docs/en/commands/mobile-command/
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		//driver.executeScript("mobile: tap", ["x": x, "y": y])  //this is deprecated
//
//		Mobile.comment("Tap at $x, $y")
//		TouchAction touchAction = new TouchAction(driver)
//		touchAction.tap(PointOption.point(x, y))
//				.perform()
//	}
//
//	def static void getActiveElementAndSendKeys(String text, boolean isClear = true) {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		MobileElement element = driver.switchTo().activeElement()
//		if(isClear) {
//			element.clear()
//		}
//		element.sendKeys(text)
//	}
//
//	def static void delays(int duration) {
//		for(int i = 0; i < duration; i++) {
//			Mobile.delay(1) //delay 1 seconds
//
//			if(i % 5 == 0) {
//				//every 5 seconds
//				LogHelpers.printLog("Delay duration: $i second(s).")
//				MobilePageSrcHelpers.getPageSource()
//			}
//		}
//	}
//
//	def static void keepAlive() {
//		//to avoid Appium getting killed by Android (Error: socket hang up)
//		//Mobile.swipe(10, 20, 10, 10)
//		//		if(!isIos()) {
//		//			Mobile.tapAtPosition(1, 1)
//		//		}
//	}
//
//	def static String getAttributeUsingClassName(String className, int index, String attName) {
//		MobileElement el = getElement(className, index)
//		String attValue = ""
//		if(el) {
//			attValue = el.getAttribute(attName)
//			LogHelpers.printLog("Getting attribute $attName of $className{$index}: $attValue")
//		}
//
//		return attValue
//	}
//
//	def static void setTextUsingClassName(String className, int index, String textInput) {
//		LogHelpers.printLog("Input '$textInput' into $className{$index}")
//		MobileElement el = getElement(className, index)
//		if(el && textInput) {
//			el.setValue(textInput)
//		}
//	}
//
//	def static void clearTextUsingClassName(String className, int index) {
//		MobileElement el = getElement(className, index)
//		if(el) {
//			el.clear()
//		}
//	}
//
//	def static void launchApp(String appId) {
//
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		((AppiumDriver)driver).activateApp(appId)
//	}
//
//	def static void terminateApp(String appId) {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		driver.terminateApp(appId)
//	}
//
//	def static List<MobileElement> findElementsByXpath(String xpath) {
//		AppiumDriver driver = MobileDriverFactory.getDriver()
//		List<MobileElement> elements = (List<MobileElement>) driver.findElementsByXPath(xpath)
//		return elements
//	}
//
//	def static TestObject findTestObjectByXpath(String xpath, String objectName) {
//		TestObject testObject = new TestObject(objectName)
//		testObject.addProperty("xpath", ConditionType.EQUALS, xpath)
//		return testObject
//	}
//
//	def static TestObject findTestObjectByText(String type, String key, String language, String attribute="", String additionalXpath="", int index=1) {
//		/**
//		 * dynamically create object by 'type' (iOS) or 'class' (Android) that contains specific 'text' from text_object_repository 'key'
//		 * type: to define base xpath. eg: XCUIElementTypeStaticText or XCUIElementTypeOther/XCUIElementTypeStaticText
//		 * key: to define object name and reference to text_object_repository.json. eg: transferResult.btnShare
//		 * language: to define language (en / id) (not case sensitive)
//		 * attribute: to define object attribute search reference. Default iOS: 'label' and Android: 'text'
//		 * additionalXpath to define additional xpath (if needed). eg: '/following-sibling::', '/parent::', etc
//		 * index: to define xpath index. Default to 1
//		 */
//
//		// if language is empty search object by 'key' as search reference
//		String text = language ? getTextObjectRepositoryValue("${language.toLowerCase()}.$key") : getTextObjectRepositoryValue("$key")
//		println "from text repo: " + text
//
//		TestObject testObject = new TestObject(key + additionalXpath)
//		if(isIos()) {
//			attribute = attribute ? attribute : "label"
//			String xpath = "(*//$type[contains(@$attribute, '$text')])[$index]" + additionalXpath
//			testObject.addProperty("xpath", ConditionType.EQUALS, xpath)
//		}else {
//			attribute = attribute ? attribute : "text"
//			String xpath = "//*[@class = '$type' and contains(@$attribute, '$text')]" + additionalXpath
//			testObject.addProperty("xpath", ConditionType.EQUALS, xpath)
//		}
//
//		return testObject
//	}
//
//	// def static void androidElementTap(String type, String key, String language) {
//	// 	// if language is empty search object by 'key' as search reference
//	// 	String text = language ? getTextObjectRepositoryValue("${language.toLowerCase()}.$key") : getTextObjectRepositoryValue("$key")
//	// 	println "from text repo: " + text
//
//	// 	AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//	// 	MobileElement testObject = driver.findElement(By.xpath("//*[@class = '$type' and  (contains(text() = '$text') or contains(., '$text'))]"))
//	// 	testObject.click()
//	// }
//
//	// def static String androidElementVerify(String type, String key, String language) {
//	// 	// if language is empty search object by 'key' as search reference
//	// 	String text = language ? getTextObjectRepositoryValue("${language.toLowerCase()}.$key") : getTextObjectRepositoryValue("$key")
//	// 	println "from text repo: " + text
//
//	// 	AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//	// 	MobileElement testObject = driver.findElement(By.xpath("//*[@class = '$type' and (text() = '$text' or . = '$text')]"))
//
//	// 	String testObjectValue = testObject.getText()
//	// 	return testObjectValue
//	// }
//
//
//	def static String getTextObjectRepositoryValue(String lang, String key) {
//		// if language is empty search object by 'key' as search reference
//		String text = lang ? getTextObjectRepositoryValue("${lang.toLowerCase()}.$key") : getTextObjectRepositoryValue("$key")
//		println text
//		return text
//	}
//
//	def static String getTextObjectRepositoryValue(String key) {
//		/**
//		 * get map value, key separated by '.'
//		 * return 'key' if not found in text object repository
//		 */
//		String[] properties = key.split("\\.")
//
//		def value = textObjectRepository[properties[0]] ? textObjectRepository[properties[0]] : ""
//		for(int i=1; value && i<properties.length; i++) {
//			value = value[properties[i]] ? value[properties[i]] : ""
//		}
//
//		return (String) (value ? value : key)
//	}
//
//	/* remove currency and decimal*/
//	def static String parsedSubscriptAmount(String amount) {
//		if(!amount) amount = "" //removing null
//		String parsedAmount = amount.replaceAll("\\D+","")
//		KeywordUtil.logInfo("amount : $amount")
//		KeywordUtil.logInfo("parsed amount : $parsedAmount")
//		// if (amount != '0') {
//		// 	substrAmount = parsedAmount.substring(0, (parsedAmount.length() - 2))
//		// }
//		if(parsedAmount && parsedAmount.length() > 2 ) {
//			parsedAmount = parsedAmount.substring(0, (parsedAmount.length() - 2))
//		}else {
//			// return original value if parsedAmount is empty / length < 2
//			parsedAmount = amount
//		}
//		KeywordUtil.logInfo("substr amount : $parsedAmount")
//		return parsedAmount
//	}
//	/*add point decimal*/
//	def static String parsedDecimal(String amount) {
//
//		String parsedAmount = amount.substring(0, amount.length() - 2) + "." + amount.substring(amount.length() - 2)
//		KeywordUtil.logInfo("parsed Amount: $parsedAmount")
//		return parsedAmount
//	}
//
//	/* remove decimal*/
//	def static String parsedDecimalAmount(String amount) {
//		if(amount && amount.charAt(amount.length() - 2) == "." ) {
//			KeywordUtil.logInfo('masuk')
//			amount = amount.substring(0, (amount.length() - 2))
//		}else {
//			// return original value if parsedAmount is empty / length < 2
//			amount = amount
//		}
//	}
//
//
//	/* Remove currency*/
//	def static String parsedAmount(String amount) {
//		String parsedAmount = amount.replaceAll("\\D+","")
//		KeywordUtil.logInfo(parsedAmount)
//		return parsedAmount
//	}
//
//	def static String parsedRoundUnit(String amount) {
//		String parsedAmount = amount.replace(".", "")
//		parsedAmount = amount.replace("Unit", "")
//		Double minUnit = Double.parseDouble(parsedAmount.replace(",", "."))
//		minUnit = minUnit * 10000
//		minUnit = Math.round(minUnit)
//		minUnit = minUnit / 10000
//		String unit = String.valueOf(minUnit)
//		unit = unit.replace(".", ",")
//		return unit
//	}
//
//	/*get currency*/
//	def static String parseCurrency(String amount) {
//		Matcher regexMatcher = amount =~ /^(\D+?)\s*\d/
//		String currency = ""
//		if(regexMatcher.count > 0) {
//			currency = regexMatcher[0][1] //first match
//		}
//		return currency
//	}
//
//	def static String formatCurrency(String amount) {
//		if(amount != "") {
//			Double dobAmount = Double.parseDouble(amount) / 100
//			String newAmount = String.format("%,.2f", dobAmount)
//			KeywordUtil.logInfo(newAmount)
//			return newAmount
//		}
//	}
//
//	def static String parseClearSentence(String amount) {
//		//		Matcher regexMatcher = amount =~ /^([0-9,.])/
//		//		String currency = ""
//		//		if(regexMatcher.count > 0) {
//		//			currency = regexMatcher[0][1] //first match
//		//		}
//		String parsedAmount = amount.replaceAll("[a-zA-Z-#<>:]","")
//		KeywordUtil.logInfo("amount : $parsedAmount")
//		return parsedAmount
//	}
//
//	def static String getSentenceOnly(String text) {
//		//		Matcher regexMatcher = amount =~ /^([0-9,.])/
//		//		String currency = ""
//		//		if(regexMatcher.count > 0) {
//		//			currency = regexMatcher[0][1] //first match
//		//		}
//		String parsedAmount = text.replaceAll("[0-9]","")
//		KeywordUtil.logInfo("amount : $parsedAmount")
//		return parsedAmount
//	}
//
//	def static boolean parseUnit(String amount) {
//		String ActualAmount = amount.replace(" Unit", "")
//		//		ActualAmount = ActualAmount.replaceAll("[\\\\d.]", "")
//		ActualAmount = ActualAmount.replaceAll(",", "")
//		String regexMatcher = /^\$?\d+(\.\d{2,4})?$/
//		if(ActualAmount.matches(regexMatcher)) {
//			KeywordUtil.markPassed("Valid : '$ActualAmount' ")
//			return true
//		}else {
//			KeywordUtil.markFailed("Fail : '$ActualAmount' ")
//			return false
//		}
//	}
//
//
//	def static String addDecimal(String amount) {
//		DecimalFormat decfor = new DecimalFormat("0.00");
//		String parsedAmount = amount.replace(".","")
//		parsedAmount = amount.replace(".", "")
//		parsedAmount = parsedAmount.replace(",", ".")
//		Double dobAmount = Double.parseDouble(parsedAmount)
//		String newAmount = String.format("%,.2f", dobAmount)
//		return newAmount
//	}
//	def static Boolean isComa(String unit) {
//		if(unit.contains(",")) {
//			return true
//		}else {
//			return false
//		}
//	}
//	def static Boolean isValidUnit(String unit) {
//		Boolean Coma = isComa(unit)
//		if (Coma) {
//			//KeywordUtil.markPassed("Format Unit Valid: " + unit) di take out karena diambil dr API
//			return true
//		}else {
//			//KeywordUtil.markFailed("Format Unit Not Valid : " + unit + " Requied five digits after comma")
//			return false
//		}
//	}
//	/* add decimal*/
//	def static String addSubscriptAmount(String amount) {
//		return amount + "00"
//	}
//
//	def static boolean validateValue(String testObject, String actualText, String expectedText) {
//		boolean verificationResult = true
//		if(!actualText) {
//			actualText = ""
//		}
//		KeywordUtil.logInfo("Comparing actual: '$actualText' and expected: '$expectedText'")
//		if(actualText.trim().equals(expectedText.trim())) {
//			KeywordUtil.markPassed("MobileHelpers: isEquals: actual text: '$actualText' IS match with expected text: '$expectedText'")
//		} else {
//			KeywordUtil.markFailed("MobileHelpers: isEquals: actual text: '$actualText' is NOT match with expected text: '$expectedText'")
//			verificationResult = false
//		}
//
//		if(GlobalVariable.RUNNING_TEST_SUITES) {
//			KeywordUtil.logInfo('Masuk Test')
//			String filePath = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/Validation.xlsx"
//			String status = ""
//			Map value = [:]
//			value["Lockey"] = testObject
//			value["Expected"] = expectedText
//			value["Actual"] = actualText
//			if(verificationResult) {
//				status = "PASSED"
//			} else {
//				status = "FAILED"
//				MobileHelpers.takeReportScreenshot(testObject)
//			}
//			value["Status"] = status
//			MobileHelpers.writeToExcel(filePath, "ValidateValue", value)
//		}
//		return verificationResult
//	}
//
//	def static boolean validateValueContains(String testObject, String actualText, String expectedText) {
//		boolean verificationResult = true
//		if(!actualText) {
//			actualText = ""
//		}
//		KeywordUtil.logInfo("Comparing actual: '$actualText' and expected: '$expectedText'")
//		if(actualText.trim().contains(expectedText.trim())) {
//			KeywordUtil.markPassed("MobileHelpers: sContains: actual text: '$actualText' IS match with expected text: '$expectedText'")
//		} else {
//			KeywordUtil.markFailed("MobileHelpers: sContains: actual text: '$actualText' is NOT match with expected text: '$expectedText'")
//			verificationResult = false
//		}
//
//		if(GlobalVariable.RUNNING_TEST_SUITES) {
//			String filePath = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/Validation.xlsx"
//			String status = ""
//			Map value = [:]
//			value["Lockey"] = testObject
//			value["Expected"] = expectedText
//			value["Actual"] = actualText
//			if(verificationResult) {
//				status = "PASSED"
//			} else {
//				status = "FAILED"
//				MobileHelpers.takeReportScreenshot(testObject)
//			}
//			value["Status"] = status
//			MobileHelpers.writeToExcel(filePath, "ValidateValue", value)
//		}
//		return verificationResult
//	}
//
//	def static boolean isEquals(String actualText, String expectedText) {
//		if(!actualText) {
//			actualText = ""
//		}
//		KeywordUtil.logInfo("Comparing actual: '$actualText' and expected: '$expectedText'")
//		takeScreenshot()
//		if(actualText.trim().equals(expectedText.trim())) {
//			KeywordUtil.markPassed("MobileHelpers: isEquals: actual text: '$actualText' IS match with expected text: '$expectedText'")
//			return true
//		}else {
//			KeywordUtil.markFailed("MobileHelpers: isEquals: actual text: '$actualText' is NOT match with expected text: '$expectedText'")
//			return false
//		}
//	}
//
//	def static boolean isNotEquals(String actualText, String expectedText) {
//		if(!actualText) {
//			actualText = ""
//		}
//		KeywordUtil.logInfo("Comparing actual: '$actualText' and expected: '$expectedText'")
//		takeScreenshot()
//		if(!actualText.trim().equals(expectedText.trim())) {
//			KeywordUtil.markPassed("MobileHelpers: isNotEquals: actual text: '$actualText' is NOT match with expected text: '$expectedText'")
//			return true
//		}else {
//			KeywordUtil.markFailed("MobileHelpers: isEquals: actual text: '$actualText' is match with expected text: '$expectedText'")
//			return false
//		}
//	}
//
//	def static boolean isContains(String actualText, String expectedText) {
//		if(!actualText) actualText = ""
//		if(actualText.trim().contains(expectedText.trim())) {
//			KeywordUtil.markPassed("MobileHelpers: isContains: expected text: '$expectedText' IS included in actual text: '$actualText'")
//			return true
//		}else {
//			KeywordUtil.markFailed("MobileHelpers: isContains: expected text: '$expectedText' is NOT included in actual text: '$actualText'")
//			return false
//		}
//	}
//
//	def static boolean isEqualsIgnoreCase(String actualText, String expectedText) {
//		if(!actualText) actualText = ""
//		if(actualText.trim().equalsIgnoreCase(expectedText.trim())) {
//			KeywordUtil.markPassed("MobileHelpers: isContains: expected text: '$expectedText' IS included in actual text: '$actualText'")
//			return true
//		}else {
//			KeywordUtil.markFailed("MobileHelpers: isContains: expected text: '$expectedText' is NOT included in actual text: '$actualText'")
//			return false
//		}
//	}
//
//	def static boolean isValidDateFormat(String date, String format, Locale locale = Locale.ENGLISH) {
//		takeScreenshot()
//		try {
//			SimpleDateFormat sdf = new SimpleDateFormat(format, locale)
//			Date parsedDate = sdf.parse(date)
//			String formattedDate = sdf.format(parsedDate)
//			println "parsedDate: " + parsedDate
//			println "formattedDate: " + formattedDate
//
//			if(date.equals(formattedDate)) {
//				KeywordUtil.markPassed("Actual date format: '$date' ARE match with expected date format: '$format'")
//				return true
//			}else {
//				KeywordUtil.markFailed("MobileHelpers: isValidDateFormat: actual date format '$date' are NOT match with expected date format: '$format'")
//				return false
//			}
//		}catch(ParseException pex) {
//			KeywordUtil.markFailed("MobileHelpers: isValidDateFormat: ParseException: actual date format '$date' expected date format: '$format'")
//			return false
//		}catch(Exception e) {
//			KeywordUtil.markFailed("MobileHelpers: isValidDateFormat: Exception: actual date format '$date' expected date format: '$format'")
//			return false
//		}
//	}
//
//	def static String parsedDate(String date, String sourceFormat, String expectedFormat, Locale locale = Locale.ENGLISH) {
//		SimpleDateFormat sourceSdf = new SimpleDateFormat(sourceFormat, locale)
//		Date parsedSourceDate = sourceSdf.parse(date)
//
//		// convert date to actual format
//		SimpleDateFormat expectedSdf = new SimpleDateFormat(expectedFormat, locale)
//		String convertedDate = expectedSdf.format(parsedSourceDate)
//
//		return convertedDate
//	}
//
//	def static void hideKeyboard() {
//		if(isIos()) {
//			TestObject returnKeyboard = findTestObjectByXpath("//XCUIElementTypeButton[contains(@name,'Return')]", "returnKeyboard")
//
//			if(Mobile.waitForElementPresent(findTestObject('iOS/General/Native/btn_keyboard_done'), 2, FailureHandling.OPTIONAL)) {
//				Mobile.tap(findTestObject('iOS/General/Native/btn_keyboard_done'), 5)
//			}else {
//				if(Mobile.waitForElementPresent(returnKeyboard, 5, FailureHandling.OPTIONAL)) {
//					MobileHelpers.tapAtPosition(returnKeyboard, 50, 20)
//				}
//				KeywordUtil.markPassed("MobileHelpers: hideKeyboard: Done button is not appear. no need to tap it.")
//			}
//		}else {
//			TestObject btnDone = findTestObjectByXpath("//android.widget.TextView[contains(@resource-id, 'btn_done')]", "btnDone")
//			Mobile.delay(2)
//			if (Mobile.waitForElementPresent(btnDone, 5)) {
//				Mobile.tap(btnDone, 0)
//			} else {
//				Mobile.hideKeyboard()
//			}
//
//			//Mobile.pressBack()
//		}
//	}
//
//	// cancel native share
//	def static void tapCancelNativeShare(String lang) {
//		// TODO: check on android
//		if(isIos()) {
//			TestObject btnClose = findTestObjectByXpath("//${Constants.IOS_TYPE_BUTTON}[contains(@name, 'Close') or contains(@name, 'Cancel') or contains(@name, 'Tutup')]", "btn_nativeShare_close") //Tutup
//			if(Mobile.waitForElementPresent(btnClose, 4)) {
//				Mobile.tap(btnClose, 5)
//			}
//		} else {
//			TestObject btnCancel = findTestObjectByXpath("//android.widget.Button[@text = 'CANCEL' or @text = 'Close' or @text = 'Cancel']", "btnCancel")
//			if (Mobile.waitForElementPresent(btnCancel, 15)) {
//				Mobile.tap(btnCancel, 5)
//			} else {
//				//use alternative method to cancel
//				int height = Mobile.getDeviceHeight() * 0.1
//				int width = Mobile.getDeviceWidth() * 0.5
//				try {
//					Mobile.tapAtPosition(width, height)
//				} catch(InvalidElementStateException ex) {
//					//since this is tapping on empty area of the screen, appium will always throw this error
//					//no need to do anything
//				}
//			}
//		}
//	}
//
//	// photo permission
//	def static void tapAllowPhotoPermission(String lang) {
//		if(MobileHelpers.isIos()) {
//			// TestObject btnAllow = findTestObjectByText(Constants.IOS_TYPE_BUTTON, "common.permissionPhoto.allow", lang)
//			TestObject btnAllow = findTestObjectByXpath("//${Constants.IOS_TYPE_BUTTON}[contains(@name, 'OK')]", "btnAllow")
//			if(Mobile.waitForElementPresent(btnAllow, 3)) {
//				Mobile.tap(btnAllow, 5)
//			}else {
//				MobilePageSrcHelpers.getPageSource()
//				KeywordUtil.markPassed("tapAllowPhotoPermission: Allow permission is not appear. No need to tap Allow button")
//			}
//		} else {
//			TestObject btnAllow = findTestObjectByXpath("//android.widget.Button[contains(@resource-id, 'allow')]", "btnAllow")
//			if(Mobile.waitForElementPresent(btnAllow, 1)) {
//				Mobile.tap(btnAllow, 5)
//			}else {
//				KeywordUtil.markPassed("tapAllowPhotoPermission: Allow permission is not appear. No need to tap Allow button")
//			}
//		}
//	}
//
//	def static Point getElementPointByXpath(String xpath) {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		MobileElement element = driver.findElementByXPath(xpath)
//		if(element) {
//			Point location = element.getLocation()
//			KeywordUtil.logInfo("x: ${location.x}, y: ${location.y}")
//			return location
//		} else {
//			println "Element not found!"
//			return null
//		}
//	}
//
//	def static Point getElementPointById(String id) {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		MobileElement element = driver.findElementById(id)
//		if(element) {
//			Point location = element.getLocation()
//			KeywordUtil.logInfo("x: ${location.x}, y: ${location.y}")
//			return location
//		} else {
//			println "Element not found!"
//			return null
//		}
//	}
//
//	def static String getRangeDate(String startDate, String endDate, String range, String lang) {
//		println "start: " + startDate
//		println "end: " + endDate
//		println "range: " + range
//		println "constant: " + Constants.SCH_TRF_FREQ_WEEKLY
//		println "constant: " + Constants.SCH_TRF_FREQ_WEEKLY
//		Locale locale = lang.toUpperCase() == Constants.LANG_ID ? new Locale("in", "ID") : Locale.ENGLISH
//		if (range == Constants.SCH_TRF_FREQ_WEEKLY) {
//			SimpleDateFormat actualSdf = new SimpleDateFormat(Constants.dd_MMMM_yyyy, locale)
//			Date actualDate1 = actualSdf.parse(startDate)
//			long epoch1 = actualDate1.getTime()
//			KeywordUtil.logInfo("weekly epoch1: $epoch1")
//
//			Date actualDate2 = actualSdf.parse(endDate)
//			long epoch2 = actualDate2.getTime()
//			KeywordUtil.logInfo("epoch2: $epoch2")
//
//			int calculation = Math.floor((epoch2 - epoch1) / 1000 / 3600 / 24 / 7) + 1
//			String numberTransaction = String.valueOf(calculation)
//			KeywordUtil.logInfo("no of trx: $numberTransaction")
//			return numberTransaction
//		} else if (range == Constants.SCH_TRF_FREQ_MONTHLY) {
//			SimpleDateFormat actualSdf = new SimpleDateFormat(Constants.MMMM_yyyy, locale)
//			Date actualDate1 = actualSdf.parse(startDate)
//			long epoch1 = actualDate1.getTime()
//			KeywordUtil.logInfo("monthly epoch1: $epoch1")
//
//			Date actualDate2 = actualSdf.parse(endDate)
//			long epoch2 = actualDate2.getTime()
//			KeywordUtil.logInfo("epoch2: $epoch2")
//
//			//			int calculation = Math.floor((epoch2 - epoch1) / 1000 / 3600 / 24 / 30) + 1
//
//			//per tgl 20 Okt 2022 menggunakan Math.round untuk membulatkan ke nilai yang terdekat
//			int calculation = Math.round((epoch2 - epoch1) / 1000 / 3600 / 24 / 30) + 1
//			String numberTransaction = String.valueOf(calculation)
//			KeywordUtil.logInfo("no of trx: $numberTransaction")
//			return numberTransaction
//		}
//	}
//
//	def static String composeInitial(String name) {
//		String[] names = name.split(" ")
//		String initial = ""
//		if (MobileHelpers.isIos()) {
//			initial = names.length > 1 ? String.valueOf(names[0].charAt(0)) + String.valueOf(names[names.length-1].charAt(0)) : String.valueOf(names[0].charAt(0))
//		} else {
//			//			if (names.length > 1) {
//			//				for (int i = 1; i<=names.length; i++) {
//			//					initial = initial + String.valueOf(names[i-1].charAt(0))
//			//				}
//			//			} else {
//			//				initial =  String.valueOf(names[0].charAt(0))
//			//			}
//
//			initial = names.length > 1 ? String.valueOf(names[0].charAt(0)) + String.valueOf(names[names.length-1].charAt(0)) : String.valueOf(names[0].charAt(0))
//		}
//		return initial
//	}
//
//	def static void scrollDown(int times, int x=0, int y=0, boolean showComment = false) {
//		int height = Mobile.getDeviceHeight()
//		int width = Mobile.getDeviceWidth()
//
//		int startX = x ? x : width * 0.5
//		int startY = y ? y : height * 0.8
//		int endY = height * 0.2
//
//		for (int i=1; i<=times; i++) {
//			if(showComment) {
//				Mobile.comment("Swiping from $startY to $endY")
//			}
//			Mobile.swipe(startX, startY, startX, endY)
//		}
//	}
//
//	def static void scrollDownWithOffset(int timeoutCount, int offset=0, int x=0, int y=0, boolean showComment = false) {
//
//		int startY, endY, centerX
//		int height = Mobile.getDeviceHeight()
//		int width = Mobile.getDeviceWidth()
//
//		startY = !offset? height * 0.9 : height * 0.5
//		endY = !offset ? height * 0.1 : startY - offset
//
//		centerX = x ? x : centerX
//		startY = y ? y : startY
//
//		for (int i=1; i<=timeoutCount; i++) {
//			if(showComment) {
//				Mobile.comment("Swiping from $startY to $endY")
//			}
//			Mobile.swipe(centerX, startY, centerX, endY)
//		}
//	}
//
//	def static void scrollUpWithOffset(int timeoutCount, int offset=0, int x=0, int y=0, boolean showComment = false) {
//
//		int startY, endY, centerX
//		int height = Mobile.getDeviceHeight()
//		int width = Mobile.getDeviceWidth()
//
//		//startY = !offset? height * 0.9 : height * 0.5
//		//endY = !offset ? height * 0.1 : startY - offset
//
//		startY = !offset? height * 0.1 : height * 0.5
//		endY = !offset ? height * 0.9 : startY + offset
//
//		centerX = x ? x : centerX
//		startY = y ? y : startY
//
//		for (int i=1; i<=timeoutCount; i++) {
//			if(showComment) {
//				Mobile.comment("Swiping from $startY to $endY")
//			}
//			Mobile.swipe(centerX, startY, centerX, endY)
//		}
//	}
//
//	def static void scrollUp(int times, int x=0, int y=0) {
//		int height = Mobile.getDeviceHeight()
//		int width = Mobile.getDeviceWidth()
//
//		int startX = x ? x : width * 0.5
//		int startY = y ? y : height * 0.2
//		int endY = height * 0.8
//
//		for (int i=1; i<=times; i++) {
//			Mobile.swipe(startX, startY, startX, endY)
//		}
//	}
//
//	def static void debugWait(String os) {
//		if(os == getTargetOS()) {
//			//wait indefinitely, until 'q' is entered in console
//			Scanner scanner = new Scanner(System.in);
//
//			println "Quit (q)?"
//			String isNext = scanner.nextLine()
//
//			while(!isNext.equals("q")) {
//				// Input 'q' to end the loop
//				println "Quit (q)?"
//				isNext = scanner.nextLine()
//			}
//		}
//	}
//
//	def static String parsedDenominationAmount(String unparsedDenomAmount, String lang) {
//		String thousand = getTextObjectRepositoryValue(lang, "common.denomination.thousand")
//		String million = getTextObjectRepositoryValue(lang, "common.denomination.million")
//
//		if(unparsedDenomAmount.toLowerCase().contains(thousand)) {
//			return "000"
//		}else if(unparsedDenomAmount.toLowerCase().contains(million)) {
//			return "000000"
//		}else {
//			return ""
//		}
//	}
//
//	def static boolean isValidFormatCurrencyDisplay(String amount, boolean isDecimal=true, String currency="idr") {
//		// reference: https://confluence.supporting.devmandiri.co.id:8443/pages/viewpage.action?pageId=6360742
//
//		String parsedCurrency = "Rp"
//		if(currency.equalsIgnoreCase("usd")) {
//			parsedCurrency = "USD"
//		}
//
//		boolean isValid = true
//		if(isDecimal) {
//			isValid = amount ==~ /^${parsedCurrency} \d{1,3}([ .]?\d{3})*(\d{2})$/
//		}else {
//			isValid = amount ==~ /^${parsedCurrency} \d{1,3}([ .]?\d{3})*$/
//		}
//
//		if(isValid) {
//			KeywordUtil.markPassed("isValidFormatCurrencyDisplay: VALID format currency display: '$amount'")
//		}else {
//			KeywordUtil.markFailed("isValidFormatCurrencyDisplay: INVALID format currency display: '$amount', need decimal: '$isDecimal'")
//		}
//		return isValid
//	}
//
//	def static void pressEnter() {
//		if(isIos()) {
//			AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//			driver.getKeyboard().pressKey(Keys.ENTER);
//		}else {
//			CustomAndroidKey.pressEnter()
//		}
//	}
//
//	def static void dragAndDrop(TestObject firstObject, TestObject secondObject) {
//		// move first object to second object position
//
//		AppiumDriver<?> currentDriver = MobileDriverFactory.getDriver()
//		Point firstLocation = getElementPointByXpath(firstObject.findPropertyValue("xpath"))
//		Point secondLocation = getElementPointByXpath(secondObject.findPropertyValue("xpath"))
//
//		KeywordUtil.logInfo(currentDriver.toString())
//
//		TouchAction touchAction = new TouchAction(currentDriver)
//		touchAction.press(PointOption.point((int) firstLocation.x+10, (int) firstLocation.y+10))
//				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(10)))
//				.moveTo(PointOption.point((int) secondLocation.x + 30, (int) secondLocation.y+30))
//				.release()
//
//		touchAction.perform()
//	}
//
//	def static void dragAndDrop(int x1, int y1, int x2, int y2) {
//		// move first object to second object position
//
//		AppiumDriver<?> currentDriver = MobileDriverFactory.getDriver()
//		if(isIos()) {
//			KeywordUtil.logInfo(currentDriver.toString())
//
//			TouchAction touchAction = new TouchAction(currentDriver)
//			touchAction.press(PointOption.point(x1, y1))
//					.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(10)))
//					.moveTo(PointOption.point(x2, y2))
//					.release()
//
//			touchAction.perform()
//
//			//Mobile.dragAndDrop(firstObject, secondObject, 10)
//		}else {
//			TouchAction touchAction = new TouchAction(currentDriver)
//					.longPress(PointOption.point(x1, y1))
//					.moveTo(PointOption.point(x2, y2))
//					.release()
//
//			touchAction.perform()
//		}
//	}
//
//	def static String parsedNpwp(String npwp) {
//		// length should be 15 digits. parsed result: 01.312.325.2-024.123
//		return npwp.replaceAll(/(\d{2})(\d{3})(\d{3})(\d{1})(\d{3})(\d{3})/, '$1.$2.$3.$4-$5.$6')
//	}
//
//	def static void selectSlider(TestObject sliderObject, int moveToX) {
//		AppiumDriver<?> currentDriver = MobileDriverFactory.getDriver()
//		Point sliderLocation = getElementPointByXpath(sliderObject.findPropertyValue("xpath"))
//		KeywordUtil.logInfo("moveToX: $moveToX")
//		int startX = sliderLocation.x + 10
//		int startY = sliderLocation.y + 10
//
//		TouchAction touchAction = new TouchAction(currentDriver)
//				.press(PointOption.point(startX, startY))
//				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
//				.moveTo(PointOption.point(startX + moveToX, startY))
//				.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
//				.release()
//
//		touchAction.perform()
//	}
//
//	def static String parsedDateToEng(String date, String sourceFormat, String expectedFormat) {
//		// parse from indonesia to english
//		SimpleDateFormat sourceSdf = new SimpleDateFormat(sourceFormat, new Locale("in", "ID"))
//		Date parsedSourceDate = sourceSdf.parse(date)
//
//		// convert date to actual format
//		SimpleDateFormat expectedSdf = new SimpleDateFormat(expectedFormat, Locale.ENGLISH)
//		String convertedDate = expectedSdf.format(parsedSourceDate)
//
//		return convertedDate
//	}
//
//	def static String createDifferentPassword(String curPass) {
//		Random rand = new Random()
//		String intRand1 = rand.nextInt(9).toString()
//		String intRand2 = rand.nextInt(9).toString()
//
//		String newPass = curPass.substring(0, curPass.length()-4) + intRand1 + intRand2
//		KeywordUtil.logInfo("New Password created: $newPass")
//		return newPass
//	}
//
//	def static String savePassword(String newPass, Map rowFilter) {
//		String xlsLoc = './Data Files/Device_Master_Test_Data_v0.2.xlsx'
//		String sheetName = 'Provisioning'
//		String columnName = 'Password'
//		int columnIndex = ExcelHelper.getColumnIndex(xlsLoc, sheetName, "Password")
//		int rowIndex = ExcelHelper.getRowNumber(xlsLoc, sheetName, rowFilter)
//		ExcelHelper.updateCellValue(xlsLoc, sheetName, columnIndex, rowIndex, newPass)
//	}
//
//	def static String saveEmail(String newEmail, Map rowFilter) {
//		String xlsLoc = './Data Files/Device_Master_Test_Data_v0.2.xlsx'
//		String sheetName = 'Devices'
//		String columnName = 'EmailAccount'
//		int columnIndex = ExcelHelper.getColumnIndex(xlsLoc, sheetName, "EmailAccount")
//		int rowIndex = ExcelHelper.getRowNumber(xlsLoc, sheetName, rowFilter)
//		ExcelHelper.updateCellValue(xlsLoc, sheetName, columnIndex, rowIndex, newEmail)
//	}
//
//	def static boolean waitForLoadingCompleted(TestObject obj) {
//		boolean loadingPresent = false
//
//		try {
//			loadingPresent =  Mobile.waitForElementPresent(obj, 1, FailureHandling.OPTIONAL)
//
//			while (loadingPresent) {
//				//if loading is detected, then wait until it is gone
//				Mobile.delay(2)
//				loadingPresent = Mobile.waitForElementPresent(obj, 1, FailureHandling.OPTIONAL)
//			}
//		} catch (StaleElementReferenceException ex) {
//			loadingPresent = false
//		}
//
//		return loadingPresent
//	}
//
//	def static boolean scrollUpDown(TestObject obj) {
//		//scroll down, then scroll up
//		boolean found = scrollToObject(obj, 10, 300, 0, 0, Constants.SCROLL_DOWN)
//		if(!found) {
//			found = scrollToObject(obj, 20, 300, 0, 0, Constants.SCROLL_UP)
//		}
//		return found
//	}
//
//	/**
//	 * Create new file excel, if exist = skip
//	 * @param filePathName : GlobalVariable.EVEREST_DATA_LOCATION + '/FileName.xlsx'
//	 * @param sheetName : 'Sheet Name'
//	 */
//	static void createExcelFile(String filePathName, String sheetName) {
//		String ext = com.google.common.io.Files.getFileExtension(filePathName)
//		File file = new File(filePathName)
//		if (file.exists()) {
//			KeywordUtil.logInfo('File name already exist')
//		} else {
//			FileOutputStream fileOutputStream = new FileOutputStream(filePathName)
//			Workbook workbook = ext.equalsIgnoreCase("xlsx") ? new XSSFWorkbook() : new HSSFWorkbook()
//			workbook.createSheet(WorkbookUtil.createSafeSheetName(sheetName))
//			workbook.write(fileOutputStream)
//			fileOutputStream.flush()
//			fileOutputStream.close()
//		}
//	}
//
//	static void createExcelValidation() {
//		String filePathName = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/Validation.xlsx"
//		String ext = com.google.common.io.Files.getFileExtension(filePathName)
//		File file = new File(filePathName)
//		if (file.exists()) {
//			KeywordUtil.logInfo('File name already exist')
//		} else {
//			FileOutputStream fileOutputStream = new FileOutputStream(filePathName)
//			Workbook workbook = ext.equalsIgnoreCase("xlsx") ? new XSSFWorkbook() : new HSSFWorkbook()
//			workbook.createSheet(WorkbookUtil.createSafeSheetName("ValidateCopy"))
//			workbook.createSheet(WorkbookUtil.createSafeSheetName("ValidateValue"))
//			workbook.createSheet(WorkbookUtil.createSafeSheetName("ValidateScreen"))
//			workbook.write(fileOutputStream)
//			fileOutputStream.flush()
//			fileOutputStream.close()
//		}
//		String[] columnValidate = [
//			"Lockey",
//			"Expected",
//			"Actual",
//			"Status"
//		]
//		String[] columnSS = ["Name", "Path"]
//		createDefaultFormatExcel(filePathName, "ValidateCopy", columnValidate)
//		createDefaultFormatExcel(filePathName, "ValidateValue", columnValidate)
//		createDefaultFormatExcel(filePathName, "ValidateScreen", columnSS)
//	}
//
//	/**
//	 * Create default format excel, only generate column title
//	 * @param filePathName : GlobalVariable.EVEREST_DATA_LOCATION + '/FileName.xlsx'
//	 * @param selectedSheet : 'SheetName'
//	 * @param columnName : List['Column1', 'Column2']
//	 */
//	static void createDefaultFormatExcel(String filePathName, String selectedSheet, String[] columnName) {
//
//		FileInputStream file = new FileInputStream (new File(filePathName))
//		XSSFWorkbook workbook = new XSSFWorkbook(file)
//		XSSFSheet sheet = workbook.getSheet(selectedSheet)
//		sheet.createRow(1)
//		int cell = 0
//		columnName.each{ val ->
//			sheet.getRow(1).createCell(cell).setCellValue(val)
//			cell++
//		}
//
//		file.close();
//		FileOutputStream outFile = new FileOutputStream(new File(filePathName));
//		workbook.write(outFile);
//		outFile.close();
//	}
//
//	/**
//	 * Write to existing file excel, matching with column name
//	 * @param filePathName : GlobalVariable.EVEREST_DATA_LOCATION + '/FileName.xlsx'
//	 * @param selectedSheet : 'SheetName'
//	 * @param value : Map value = ['ColumnName' : 'Value']
//	 */
//	def static void writeToExcel(String filePath, String selectedSheet, Map value){
//		//		GlobalVariable.EVEREST_DATA_LOCATION + '/' + 'Mobile_Investment_Temporary_Variable.xlsx'
//		FileInputStream file = new FileInputStream (new File(filePath))
//		XSSFWorkbook workbook = new XSSFWorkbook(file)
//		XSSFSheet sheet = workbook.getSheet(selectedSheet)
//		XSSFRow dataRow = sheet.getRow(1)
//
//		int indexRow = 2
//		while(true){
//			if(!sheet.getRow(indexRow)){
//				break
//			} else {
//				indexRow += 1
//			}
//		}
//		sheet.createRow(indexRow)
//		sheet.getRow(indexRow).createCell(1).setCellValue(indexRow)
//
//		value.any{ key, val ->
//			for(int i = 0; i < 50; i++) {
//				XSSFCell dataCell = dataRow.getCell(i)
//				String cellValue = dataCell.toString()
//				if(key == cellValue) {
//					sheet.getRow(indexRow).createCell(i).setCellValue(val)
//				}
//			}
//		}
//
//		file.close();
//		FileOutputStream outFile = new FileOutputStream(new File(filePath));
//		workbook.write(outFile);
//		outFile.close();
//	}
//
//	/**
//	 * Read existing file excel, return value of field
//	 * @param selectedSheet
//	 * @param field
//	 */
//	def static String readExcel(String selectedSheet, String field){
//		FileInputStream file = new FileInputStream (new File(GlobalVariable.EVEREST_DATA_LOCATION + '/' + 'Mobile_Investment_Temporary_Variable.xlsx'))
//		XSSFWorkbook workbook = new XSSFWorkbook(file)
//		XSSFSheet sheet = workbook.getSheet(selectedSheet)
//		XSSFRow dataRow = sheet.getRow(1)
//		for(int i = 0; i < 10; i++) {
//			XSSFCell dataCell = dataRow.getCell(i)
//			String cellValue = dataCell.toString()
//			if(field == cellValue) {
//				dataRow = sheet.getRow(2)
//				dataCell = dataRow.getCell(i)
//				String result = dataCell.toString()
//				return result
//			}
//		}
//
//		file.close();
//		FileOutputStream outFile =new FileOutputStream(new File(GlobalVariable.EVEREST_DATA_LOCATION + '/' + 'Mobile_Investment_Temporary_Variable.xlsx'));
//		workbook.write(outFile);
//		outFile.close();
//	}
//
//	/**
//	 * Read existing file excel black list product, per field
//	 * @param selectedSheet
//	 * @param field
//	 */
//	def static ArrayList<String> readExcelBlacklistProduct(String selectedSheet, String field){
//		FileInputStream file = new FileInputStream (new File(System.getProperty("user.dir") + GlobalVariable.EVEREST_ASSETS_LOCATION + '/' + 'Investment_Product_Blacklist.xlsx'))
//		XSSFWorkbook workbook = new XSSFWorkbook(file)
//		XSSFSheet sheet = workbook.getSheet(selectedSheet)
//		XSSFRow dataRow = sheet.getRow(1)
//		int index = 2
//		ArrayList<String> productBlacklist = new ArrayList<>()
//		for(int i = 0; i < 11; i++) {
//			XSSFCell dataCell = dataRow.getCell(i)
//			String cellValue = dataCell.toString()
//			if(field == cellValue) {
//				while(true){
//					dataRow = sheet.getRow(index)
//					dataCell = dataRow.getCell(i) //if error try to add empty table in last row
//					if(dataCell.toString() != ""){
//						KeywordUtil.logInfo("value = "+ dataCell.toString())
//						productBlacklist.add(dataCell.toString())
//						index++
//					} else {
//						break
//					}
//				}
//				return productBlacklist
//			} else {
//				KeywordUtil.logInfo("not match with any field")
//			}
//		}
//
//		file.close();
//		FileOutputStream outFile =new FileOutputStream(new File(GlobalVariable.EVEREST_DATA_LOCATION + '/' + 'Mobile_Investment_Temporary_Variable.xlsx'));
//		workbook.write(outFile);
//		outFile.close();
//	}
//
//	def static String parsedDatePicker(String lblDate, String actualMonthYear,  String sourceFormat, String expectedFormat, Locale locale = Locale.ENGLISH) {
//		String txtDate = ''
//		if(MobileHelpers.isIos()) {
//			String[] splitDate = lblDate.split(' ')
//			txtDate = splitDate[splitDate.size()-1]
//		} else {
//			txtDate = lblDate
//		}
//		String[] splitMonthYear = actualMonthYear.split(" ")
//		int lengthArr = splitMonthYear.size()
//		actualMonthYear = splitMonthYear[lengthArr-2] + " " + splitMonthYear[lengthArr-1]
//		KeywordUtil.logInfo("actual month is : $actualMonthYear" )
//
//		String actualDate = txtDate + ' ' + actualMonthYear
//		KeywordUtil.logInfo("actual format date is : $actualDate")
//
//		SimpleDateFormat sourceSdf = new SimpleDateFormat(sourceFormat, locale)
//		Date parsedSourceDate = sourceSdf.parse(actualDate)
//
//		// convert date to actual format
//		SimpleDateFormat expectedSdf = new SimpleDateFormat(expectedFormat, locale)
//		String convertedDate = expectedSdf.format(parsedSourceDate)
//
//		KeywordUtil.logInfo("expected format date is : $convertedDate")
//		return convertedDate
//	}
//
//	def static String CreateStepReport(String Step, String Description) {
//		Mobile.comment("$Step")
//		MobileHelpers.takeScreenshot()
//		Mobile.comment("<h3> $Description </h3>")
//	}
//
//	def static String takeReportScreenshot(String lockey) {
//		File destFile = new File(GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/" + lockey + ".png")
//		AppiumDriver driver = MobileDriverFactory.getDriver()
//		try {
//			File tempFile = driver.getScreenshotAs(OutputType.FILE)
//			FileUtils.copyFile(tempFile, destFile)
//		} catch (Exception ex) {
//		}
//		Map value = [:]
//		value["Name"] = "Issue " + lockey
//		value["Path"] = destFile.getCanonicalPath()
//		String filePath = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/Validation.xlsx"
//		MobileHelpers.writeToExcel(filePath, "ValidateScreen", value)
//	}
//
//	def static String PushFIle(String Namefile) {
//		AppiumDriver driver = MobileDriverFactory.getDriver()
//
//		File img = new File(System.getProperty("user.dir") + GlobalVariable.EVEREST_ASSETS_LOCATION + '/' + 'cc.png')
//		KeywordUtil.logInfo(img.toString())
//
//		if(img.exists()) {
//			KeywordUtil.logInfo(img.toString())
//		} else {
//			KeywordUtil.logInfo("not found " + img.toString())
//			throw new IOException("File not found " + img.getAbsolutePath())
//		}
//		driver .manage().timeouts().implicitlyWait(3, TimeUnit. SECONDS )
//		if(isIos()) {
//			//((IOSDriver) driver ).pushFile("@com.apple.DocumentsApp/"+img.getName(), img)
//			//((IOSDriver) driver).pushFile("@com.apple.DocumentsApp/" + img.getName(), new File(img))
//			((IOSDriver) driver ).pushFile("@com.apple.mobileslideshow/"+img.getName(), img)
//		}else {
//			((AndroidDriver) driver ).pushFile("/"+img.getName(), img) //need fix get iD Apps id Android
//		}
//	}
//
//	def static String getDropboxId() {
//
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if(os == "android") {
//			return "com.dropbox.android"
//		}
//		else if(os == "ios") {
//			return "com.getdropbox.Dropbox"
//		}
//	}
//
//	def static void openDropBoxApp() {
//		Mobile.delay(5)
//
//		launchApp(getDropboxId())
//	}
//
//	def static void killDropbox() {
//		AppiumDriver<?> driver = MobileDriverFactory.getDriver()
//		Mobile.delay(10)
//		driver.terminateApp(getDropboxId())
//	}
//
//	def static String getWhatsAppId() {
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if(os == "android") {
//			return "com.whatsapp" //using gmail to read email
//		}
//		else if(os == "ios") {
//			return "net.whatsapp.WhatsApp" // using gmail to read email
//		}
//	}
//
//	def static String getAlbumAppId() {
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if(os == "android") {
//			return "com.google.android.apps.photos" //using Google Photos
//		}
//		else if(os == "ios") {
//			return "com.apple.mobileslideshow" // using Photos
//		}
//	}
//
//	def static void openChromeApp() {
//		Mobile.delay(5)
//		launchApp(getChromeMobileId())
//	}
//
//	def static String getChromeMobileId() {
//		String os = getDesiredCapabilities("platformName").toLowerCase()
//		if(os == "android") {
//			return "com.android.chrome" //using chrome
//		}
//		else if(os == "ios") {
//			return "com.google.chrome.ios" // using chrome
//		}
//	}
//
//	def static selectDropdown(String item) {
//		TestObject itemDropdown
//		Boolean isFound = false
//		if(MobileHelpers.isIos()) {
//			itemDropdown = findTestObjectByXpath("//${Constants.IOS_TYPE_BUTTON}[contains(@label, '$item')]", "btn_contains_$item")
//		} else {
//			itemDropdown = findTestObjectByXpath("//android.widget.TextView[contains(@resource-id, 'tv_title') and contains(@text, '$item')]", "btn_$item")
//		}
//		for(int i = 0; !isFound; i ++) {
//			if(Mobile.verifyElementVisible(itemDropdown, 1, FailureHandling.OPTIONAL)) {
//				Mobile.tap(itemDropdown, 5)
//				isFound = true
//				break
//			} else {
//				if(MobileHelpers.isIos()) {
//					int height = Mobile.getDeviceHeight()
//					int width = Mobile.getDeviceWidth()
//					int startY = height * 0.7
//					int endY = startY - 120
//					int centerX = width / 2
//					Mobile.swipe(centerX, startY, centerX, endY)
//				} else {
//					MobileHelpers.scrollDownWithOffset(1, 500)
//				}
//				KeywordUtil.logInfo("Trying to find dropdown option $itemDropdown, scroll count: $i")
//			}
//		}
//	}
//
//	def static selectDropdownAddress(String province, String city, String district, String village) {
//		TestObject lblSearch, lblProvince, lblCity, lblDistrict, lblVillage
//		String txtSearch = 'Cari provinsi...'
//		if(MobileHelpers.isIos()) {
//			lblSearch = findTestObjectByXpath("//${Constants.IOS_TYPE_TEXTFIELD}[contains(@value, '$txtSearch')]", "lblSearch")
//			lblProvince = findTestObjectByXpath("//${Constants.IOS_TYPE_STATIC_TEXT}[contains(@label, '$province')]", "lbl_$province")
//			lblCity = findTestObjectByXpath("//${Constants.IOS_TYPE_STATIC_TEXT}[contains(@label, '$city')]", "lbl_$city")
//			lblDistrict = findTestObjectByXpath("//${Constants.IOS_TYPE_STATIC_TEXT}[contains(@label, '$district')]", "lbl_$district")
//			lblVillage = findTestObjectByXpath("//${Constants.IOS_TYPE_STATIC_TEXT}[contains(@label, '$village')]", "lbl_$village")
//		} else {
//			lblSearch = findTestObjectByXpath("//android.widget.EditText[contains(@resource-id, 'et_search')]", "btn_search")
//			lblProvince = findTestObjectByXpath("//android.widget.TextView[contains(@resource-id, 'tvPlaceTitle') and contains(@text, '$province')]", "lbl_$province")
//			lblCity = findTestObjectByXpath("//android.widget.TextView[contains(@resource-id, 'tvPlaceTitle') and contains(@text, '$city')]", "lbl_$city")
//			lblDistrict = findTestObjectByXpath("//android.widget.TextView[contains(@resource-id, 'tvPlaceTitle') and contains(@text, '$district')]", "lbl_$district")
//			lblVillage = findTestObjectByXpath("//android.widget.TextView[contains(@resource-id, 'tvPlaceTitle') and contains(@text, '$village')]", "lbl_$village")
//		}
//		Mobile.setText(lblSearch, province, 0)
//		Mobile.waitForElementPresent(lblProvince, 5)
//		Mobile.tap(lblProvince, 0)
//		Mobile.waitForElementPresent(lblCity, 5)
//		Mobile.tap(lblCity, 0)
//		Mobile.waitForElementPresent(lblDistrict, 5)
//		Mobile.tap(lblDistrict, 0)
//		Mobile.waitForElementPresent(lblVillage, 5)
//		Mobile.tap(lblVillage, 0)
//	}
//
//	def static ArrayList<String> readExcelReportValidation(String filePathName, String sheetName) {
//		MobileHelpers.writeToExcel(filePathName, sheetName, ["Lockey" : "-"])
//		FileInputStream file = new FileInputStream (new File(filePathName))
//		XSSFWorkbook workbook = new XSSFWorkbook(file)
//		List<ArrayList> copyWriting = new ArrayList<>()
//		ArrayList<String> value = new ArrayList<>()
//
//		int index = 2
//		while(true) {
//			XSSFSheet sheet = workbook.getSheet(sheetName)
//			XSSFRow dataRow = sheet.getRow(index)
//			XSSFCell dataCell = dataRow.getCell(0)
//			if(dataCell.toString() == "-") {
//				break
//			}
//			for(int i = 0; i < 4; i++) {
//				dataCell = dataRow.getCell(i)
//				String cellValue = dataCell.toString()
//				value.add(cellValue)
//			}
//			if(value[3] == "FAILED") {
//				copyWriting.add(value)
//			}
//			value = []
//			index++
//		}
//		KeywordUtil.logInfo(copyWriting.toString())	//array list
//		return copyWriting
//
//		file.close();
//		FileOutputStream outFile =new FileOutputStream(new File(filePathName));
//		workbook.write(outFile);
//		outFile.close();
//	}
//
//	def static ArrayList<String> readExcelReportFailed(String filePathName, String sheetName) {
//
//		MobileHelpers.writeToExcel(filePathName, sheetName, ["Lockey" : "-"])
//		FileInputStream file = new FileInputStream (new File(filePathName))
//		XSSFWorkbook workbook = new XSSFWorkbook(file)
//		ArrayList<String> value = new ArrayList<>()
//		ArrayList<String> valueFail = new ArrayList<>()
//
//		int index = 2
//		boolean isFail = false
//		while(true) {
//			XSSFSheet sheet = workbook.getSheet(sheetName)
//			XSSFRow dataRow = sheet.getRow(index)
//			XSSFCell dataCell = dataRow.getCell(0)
//			if(dataCell.toString() == "-") {
//				break
//			}
//			for(int i = 0; i < 4; i++) {
//				dataCell = dataRow.getCell(i)
//				String cellValue = dataCell.toString()
//				value.add(cellValue)
//			}
//			KeywordUtil.logInfo(value.toString()) //map value
//			if(value[3] == "FAILED") {
//				KeywordUtil.logInfo(value[3])
//				valueFail.add(value[0])
//			}
//			value = []
//			index++
//		}
//		KeywordUtil.logInfo(valueFail.toString())	//array list
//		return valueFail
//
//		file.close();
//		FileOutputStream outFile =new FileOutputStream(new File(filePathName));
//		workbook.write(outFile);
//		outFile.close();
//	}
//
//	def static ArrayList<String> readExcelReportScreenshot(String filePathName) {
//		MobileHelpers.writeToExcel(filePathName, "ValidateScreen", ["Name" : "-"])
//		FileInputStream file = new FileInputStream (new File(filePathName))
//		XSSFWorkbook workbook = new XSSFWorkbook(file)
//		List<ArrayList> copyWriting = new ArrayList<>()
//		ArrayList<String> value = new ArrayList<>()
//
//		int index = 2
//		while(true) {
//			XSSFSheet sheet = workbook.getSheet("ValidateScreen")
//			XSSFRow dataRow = sheet.getRow(index)
//			XSSFCell dataCell = dataRow.getCell(0)
//			if(dataCell.toString() == "-") {
//				break
//			}
//			for(int i = 0; i < 2; i++) {
//				dataCell = dataRow.getCell(i)
//				String cellValue = dataCell.toString()
//				value.add(cellValue)
//			}
//			KeywordUtil.logInfo(value.toString()) //map value
//			copyWriting.add(value)
//			value = []
//			index++
//		}
//		KeywordUtil.logInfo(copyWriting.toString())	//array list
//		return copyWriting
//
//		file.close();
//		FileOutputStream outFile =new FileOutputStream(new File(filePathName));
//		workbook.write(outFile);
//		outFile.close();
//	}
//
//	def static String splitToComponentTimes(long biggy) {
//		long longVal = biggy.longValue();
//		int hours = (int) longVal / 3600;
//		int remainder = (int) longVal - hours * 3600;
//		int mins = remainder / 60;
//		remainder = remainder - mins * 60;
//		int secs = remainder;
//		String hour, min, sec
//
//		if(hours < 9) {
//			hour = '0' + hours.toString()
//		} else {
//			hour = hours.toString()
//		}
//
//		if(mins < 9) {
//			min = '0' + mins.toString()
//		} else {
//			min = mins.toString()
//		}
//
//		if (secs < 9) {
//			sec = '0' + secs.toString()
//		} else {
//			sec = secs.toString()
//		}
//
//		String time = hour + ':' + min + ':' + sec
//		return time
//	}
//
//	def static apiSendRequest(Map value) {
//		KeywordUtil.logInfo(value.toString())
//		ResponseObject response = WS.sendRequest(findTestObject('Object Repository/API/createNewExecutionLog', value))
//		KeywordUtil.logInfo(response.getStatusCode().toString())
//	}
//
//	def static startAndroidQRecording() {
//		// Recording only available for Android Version >= 10 (Q)
//		AppiumDriver driver = MobileDriverFactory.getDriver()
//		driver.executeScript('mobile: shell', [
//			'command': 'pm',
//			'args': [
//				'grant',
//				'io.appium.settings',
//				'android.permission.RECORD_AUDIO'
//			]])
//		KeywordUtil.logInfo("command pm allow record audio") // to be tested di Huawei / hp lain (kalo tidak bisa jalankan adb shell secara manual)
//
//		driver.executeScript('mobile: shell', [
//			'command': 'appops',
//			'args': [
//				'set',
//				'io.appium.settings',
//				'PROJECT_MEDIA',
//				'allow'
//			]])
//		KeywordUtil.logInfo("command allow media projection") // to be tested di Huawei / hp lain (kalo tidak bisa jalankan adb shell secara manual)
//
//		driver.executeScript('mobile: shell', [
//			'command': 'am',
//			'args': [
//				'start',
//				'-n',
//				'io.appium.settings/io.appium.settings.Settings',
//				'-a',
//				'io.appium.settings.recording.ACTION_START',
//				'--es',
//				'filename',
//				'temp.mp4',
//				'--es',
//				'priority',
//				'low',
//				'--es',
//				'max_duraion_sec',
//				'900',
//				'--es',
//				'resolution',
//				'1280x720'
//			],
//			'includeStderr': true,
//			'timeout': 5000
//		])
//	}
//
//	def static stopAndroidQRecording() {
//		// Recording only available for Android Version >= 10 (Q)
//		AppiumDriver driver = MobileDriverFactory.getDriver()
//		driver.executeScript('mobile: shell', [
//			'command': 'am',
//			'args': [
//				'start',
//				'-n',
//				'io.appium.settings/io.appium.settings.Settings',
//				'-a',
//				'io.appium.settings.recording.ACTION_STOP'
//			],
//			'includeStderr': true,
//			'timeout': 5000
//		])
//		Mobile.delay(1) // sebelum ditambah delay video hampir selalu tidak bisa di play di mesin Mac / Windows
//
//		Date date =  new Date()
//		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd_HHmmss")
//		String formattedDate = sdf.format(date)
//		String filename = formattedDate
//
//		String videoLocation = GlobalVariable.CURRENT_TEST_REPORT + "/" + GlobalVariable.CURRENT_TEST_CASE + "/" + filename + ".mp4"
//
//		byte[] fileBase64 = driver.pullFile("/storage/emulated/0/Android/data/io.appium.settings/files/temp.mp4")
//		FileOutputStream fos = new FileOutputStream(videoLocation)
//		fos.write(fileBase64)
//		fos.flush()
//		fos.close()
//	}
//
//	def static isUniqueManufacture() {
//		// deviceApiLevel: 30 is Android 10 (Android Q)
//		String deviceManufacturer = getDesiredCapabilities("deviceManufacturer")
//		int deviceApiLevel = Integer.parseInt(getDesiredCapabilities("deviceApiLevel"))
//
//		if(deviceApiLevel >= 30 && (deviceManufacturer.contains("OPPO") || deviceManufacturer.contains("HUAWEI") || deviceManufacturer.contains("XIAOMI"))) {
//			return true
//		}else {
//			KeywordUtil.markWarning("deviceManufacturer: '${deviceManufacturer}' deviceApiLevel: '${deviceApiLevel}' video recording only available for api level >= '30'")
//			return false
//		}
//	}
//
//	/**
//	 * Change phone number prefix using parameter prefix with parsePrefix. Ex: 628123456789 to 08123456789.
//	 * Return unchanged phone number when start with prefix condition is not met.
//	 * @param phoneNumber related information of the device msisdn test data.
//	 * @param prefix related information of the phoneNumber prefix
//	 * @param parsePrefix related information of the to be replaced phoneNumber prefix
//	 */
//	def static String parsePhoneNumberPrefix(String phoneNumber, String prefix, String parsePrefix) {
//		String parsedPhoneNumber = phoneNumber
//		if(phoneNumber.startsWith(prefix)) {
//			parsedPhoneNumber = phoneNumber.replace(prefix, parsePrefix)
//		}
//		return parsedPhoneNumber
//	}
//}