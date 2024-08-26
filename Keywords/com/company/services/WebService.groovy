package com.company.services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.text.SimpleDateFormat

import javax.imageio.ImageIO

import org.bouncycastle.jcajce.provider.asymmetric.util.KeyUtil
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

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
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class WebService {

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

	@Keyword
	def static void openBrowser(String url) {
		WebUI.openBrowser("")
		WebUI.maximizeWindow()
		WebUI.navigateToUrl(url)
	}

	def static void takeReportScreenshot(TestObject to) {
		String destPath = "${GlobalVariable.Path_Test_Report}/Screenshoots"
		Files.createDirectories(Paths.get(destPath))
		WebDriver driver = DriverFactory.getWebDriver()
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)
		BufferedImage fullImg = ImageIO.read(screenshot)

		//Highlight the element by drawing a rectangle around it
		Graphics2D graphics = fullImg.createGraphics()
		graphics.setColor(Color.RED)

		WebElement element = WebUI.findWebElement(to, 0)
		int elementWidth = element.getSize().getWidth()
		int elementHeight = element.getSize().getHeight()
		int elementX = WebUI.executeJavaScript('return arguments[0].getBoundingClientRect().left;', Arrays.asList(element))
		int elementY = WebUI.executeJavaScript('return arguments[0].getBoundingClientRect().top;', Arrays.asList(element))
		graphics.drawRect(elementX, elementY, elementWidth, elementHeight)
		graphics.dispose()

		// Save the screenshot with the highlighted element
		ImageIO.write(fullImg, "png", new File("${destPath}/${GlobalVariable.Current_Test_Case}.png"))
		GlobalVariable.Evidence_Image[GlobalVariable.Current_Test_Case] = GlobalVariable.Current_Test_Case_Desc
	}

	def static void takeReportScreenshot(List<TestObject> listTo) {
		String destPath = "${GlobalVariable.Path_Test_Report}/Screenshoots"
		Files.createDirectories(Paths.get(destPath))
		WebDriver driver = DriverFactory.getWebDriver()
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)
		BufferedImage fullImg = ImageIO.read(screenshot)
		Graphics2D graphics = fullImg.createGraphics()
		graphics.setColor(Color.RED)

		listTo.each { to ->
			WebElement element = WebUI.findWebElement(to, 1)
			int elementWidth = element.getSize().getWidth()
			int elementHeight = element.getSize().getHeight()
			int elementX = WebUI.executeJavaScript('return arguments[0].getBoundingClientRect().left;', Arrays.asList(element))
			int elementY = WebUI.executeJavaScript('return arguments[0].getBoundingClientRect().top;', Arrays.asList(element))
			graphics.drawRect(elementX, elementY, elementWidth, elementHeight)
		}
		graphics.dispose()

		// Save the screenshot with the highlighted element
		ImageIO.write(fullImg, "png", new File("${destPath}/${GlobalVariable.Current_Test_Case}.png"))
		GlobalVariable.Evidence_Image[GlobalVariable.Current_Test_Case] = GlobalVariable.Current_Test_Case_Desc
	}

	//	GET FILE LIST
	//	Path sourcePath = Paths.get("${GlobalVariable.Path_Test_Report}/Screenshoots")
	//	Files.list(sourcePath).each { path ->
	//		String imageName = path.fileName.toString().split("_")[1].replace(".png", "")
	//		image = new DataReportEvidenceImage(imageName, path.toString())
	//		field.add(image)
	//	}
}
