package com.mandiri.supports

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
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import internal.GlobalVariable

public class TestDataLoader {

	def static Map loadTestData(String fileName, String sheetName) {
		Map testData = [:]
		boolean isSuccess = false
		String filePath = "./Test Data/${fileName}"
		FileInputStream fileInputStream = new FileInputStream(new File(filePath))
		Workbook workbook = new XSSFWorkbook(fileInputStream)
		Sheet sheet = workbook.getSheet(sheetName) 
		
		int colId
		while(sheet.getRow(1).getCell(colId)) {
			testData[sheet.getRow(1).getCell(colId).toString()] = colId
			colId++
		}
		
		int rowId = 2
		while(sheet.getRow(rowId)) {
			Row rows = sheet.getRow(rowId)
			String scenarioId = rows.getCell(testData["scenario_id"]).toString()
			String status = rows.getCell(testData["status"]).toString()
			if(scenarioId == GlobalVariable.Current_Test_Suites && status == "ready-to-test") {
				isSuccess = true
				testData.each{ key, val ->
					testData[key] = rows.getCell(val).toString()
				}
				break
			}
			rowId++
		}
	
		workbook.close()
		fileInputStream.close()
		if(!isSuccess) {
			KeywordUtil.markFailedAndStop("Test Data Not Found")
		}
		return testData
	}
}
