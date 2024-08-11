package com.company.services

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.company.reports.DataReportEvidenceImage
import com.company.reports.DataReportTestCase
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

import internal.GlobalVariable
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource

public class CustomReport {
	
	public void createReportTestCase() {
		Map<String, Object> parameters = new HashMap<>()
		parameters.put("scenarioName", "Scenario Pembuatan Custom Report")
		parameters.put("detailPlatformName", "Chrome")
		parameters.put("detailPlatformVersion", "v21.10.02")
		parameters.put("detailExecutionStart", "21-10-24:10:10:24")
		parameters.put("detailExecutionEnd", "21-10-24:10:10:24")
		parameters.put("detailExecutionTime", "10:10:24")
		parameters.put("detailApplicationId", "Kopra Mandiri")
		parameters.put("detailApplicationVersion", "SIT 2.1.0")
		parameters.put("detailHostName", "Andika Haris")
		parameters.put("barcodeValue", "www.google.com")
		
		DataReportTestCase testCase = new DataReportTestCase("1", "Test Case Percobaan 1 Custom Report", "Passed")
//		DataReportTestCase testCase = new DataReportTestCase("1", "Test Case Percobaan 1 Custom Report", "Passed")
		List<DataReportTestCase> field = new ArrayList<DataReportTestCase>()
		field.add(testCase)
		field.add(testCase)
		
		String fileOriginPath = "./Data/ReportTestCase.jrxml"
		String fileDestinationPath = "./Data/ReportTestCase.pdf"
		JasperReport report = JasperCompileManager.compileReport(fileOriginPath)
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(field)
		parameters.put("dataSources", dataSource)
		JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource)
		JasperExportManager.exportReportToPdfFile(print, fileDestinationPath)	
	}
	
	public void createReportEvidenceImage() {
		Map<String, Object> parameters = new HashMap<>()
		
		DataReportEvidenceImage testCase = new DataReportEvidenceImage("Test Case Percobaan 1 Custom Report", "./Data/web_image.png")
		DataReportEvidenceImage testCase2 = new DataReportEvidenceImage("Test Case Percobaan 1 Custom Report", "./Data/mobile_image.jpg")
		List<DataReportTestCase> field = new ArrayList<DataReportTestCase>()
		field.add(testCase)
		field.add(testCase)
		field.add(testCase)
		field.add(testCase2)
		field.add(testCase2)
		
		String fileOriginPath = "./Data/ReportEvidenceImage.jrxml"
		String fileDestinationPath = "./Data/ReportEvidenceImage.pdf"
		JasperReport report = JasperCompileManager.compileReport(fileOriginPath)
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(field)
		parameters.put("dataSources", dataSource)
		JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource)
		JasperExportManager.exportReportToPdfFile(print, fileDestinationPath)	
	}
}
