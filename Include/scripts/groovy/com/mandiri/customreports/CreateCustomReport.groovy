package com.mandiri.customreports

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.logging.TestSuiteXMLLogParser
import com.kms.katalon.core.logging.model.ILogRecord
import com.kms.katalon.core.logging.model.TestCaseLogRecord
import com.kms.katalon.core.logging.model.TestSuiteLogRecord
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
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import net.sf.jasperreports.engine.JasperReport
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.apache.pdfbox.multipdf.PDFMergerUtility
import org.eclipse.core.runtime.NullProgressMonitor


public class CreateCustomReport {

	def static createCustomReport() {
		coverReport()
		tableOfContent()
		createReportTestCase()
		createReportEvidenceImage()
		mergePDF()
	}

	def static void coverReport() {
		String fileOriginPath = "./Data/ReportCover.jrxml"
		String fileDestinationPath = "./Data/ReportCover.pdf"
		JasperReport report = JasperCompileManager.compileReport(fileOriginPath)
		String reportFolder = RunConfiguration.getReportFolder()
		TestSuiteLogRecord testSuiteLogRecord = new TestSuiteXMLLogParser().readTestSuiteLogFromXMLFiles(reportFolder, new NullProgressMonitor())
		Map<String, Object> parameters = new HashMap<>()
		String scenarioName = GlobalVariable.Current_Test_Suites
		parameters.put("coverProjectName", "Facebook")
		parameters.put("coverTesterName", GlobalVariable.Tester_Name)
		JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource())
		JasperExportManager.exportReportToPdfFile(print, fileDestinationPath)
	}

	def static void tableOfContent() {
		String fileOriginPath = "./Data/ReportToc.jrxml"
		String fileDestinationPath = "./Data/ReportToc.pdf"
		JasperReport report = JasperCompileManager.compileReport(fileOriginPath)
		String reportFolder = RunConfiguration.getReportFolder()
		TestSuiteLogRecord testSuiteLogRecord = new TestSuiteXMLLogParser().readTestSuiteLogFromXMLFiles(reportFolder, new NullProgressMonitor())

		List<DataReportToc> field = new ArrayList<DataReportToc>()
		DataReportToc toc0 = new DataReportToc("", "", "")
		DataReportToc toc1 = new DataReportToc("1", "Cover Report", "1")
		DataReportToc toc2 = new DataReportToc("2", "Table of Content", "2")
		DataReportToc toc3 = new DataReportToc("3", "Test Case Summary Result", "3")
		field.add(toc0)
		field.add(toc1)
		field.add(toc2)
		field.add(toc3)

		int idx = 4
		int page = 4
		GlobalVariable.Evidence_Image.each { key, val ->
			DataReportToc toc = new DataReportToc(idx.toString(), key, page.toString())
			field.add(toc)
			if(idx%2!=0) {
				page++
			}
			idx++
		}

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(field)
		Map<String, Object> parameters = new HashMap<>()
		parameters.put("dataSource", dataSource)
		JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource)
		JasperExportManager.exportReportToPdfFile(print, fileDestinationPath)
	}

	def static void createReportTestCase() {
		String fileOriginPath = "./Data/ReportTestCase.jrxml"
		String fileDestinationPath = "./Data/ReportTestCase.pdf"
		JasperReport report = JasperCompileManager.compileReport(fileOriginPath)

		String reportFolder = RunConfiguration.getReportFolder()
		TestSuiteLogRecord testSuiteLogRecord = new TestSuiteXMLLogParser().readTestSuiteLogFromXMLFiles(reportFolder, new NullProgressMonitor())
		Map<String, Object> parameters = new HashMap<>()
		String scenarioName = GlobalVariable.Current_Test_Suites
		parameters.put("scenarioName", scenarioName.replace("_", " "))
		parameters.put("detailPlatformName", testSuiteLogRecord.getBrowser())
		parameters.put("detailTotalTC", "${testSuiteLogRecord.getTotalTestCases()}")
		parameters.put("detailTcPassed", "${testSuiteLogRecord.getTotalPassedTestCases()}")
		parameters.put("detailScenarioStatus", "${testSuiteLogRecord.getSummaryStatus()}")
		parameters.put("detailExecutionStart", epochDateFormat(testSuiteLogRecord.getStartTime()))
		parameters.put("detailExecutionEnd", epochDateFormat(testSuiteLogRecord.getEndTime()))
		parameters.put("detailExecutionTime", epochDuration(testSuiteLogRecord.getStartTime(), testSuiteLogRecord.getEndTime()))
		parameters.put("detailApplicationId", GlobalVariable.Web_Url)
		parameters.put("detailHostName", testSuiteLogRecord.getHostName().split("-")[0])

		List<DataReportTestCase> field = new ArrayList<DataReportTestCase>()
		DataReportTestCase testCase = new DataReportTestCase("", "", "")
		field.add(testCase)
		int idx = 1
		for(ILogRecord childRecord : testSuiteLogRecord.getChildRecords()) {
			String testCaseName = findTestCase(childRecord.getName()).getName()
			String testCaseStatus = childRecord.getStatus().getStatusValue().toString()
			testCase = new DataReportTestCase(idx.toString(), testCaseName, testCaseStatus)
			field.add(testCase)
			idx++
		}
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(field)
		parameters.put("dataSources", dataSource)
		JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource)
		JasperExportManager.exportReportToPdfFile(print, fileDestinationPath)
	}

	def static String epochDateFormat(long epoch) {
		Instant instant = Instant.ofEpochMilli(epoch);
		ZonedDateTime dateTimeGMT7 = instant.atZone(ZoneId.of("Asia/Bangkok"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDate = dateTimeGMT7.format(formatter);
		return formattedDate
	}

	def static String epochDuration(long startEpoch, long endEpoch) {
		long durationEpoch = endEpoch - startEpoch
		String hours = ((int) new BigDecimal(durationEpoch / (1000 * 60 * 60)).remainder(24.0)).toString()
		String minutes = ((int) new BigDecimal(durationEpoch / (1000 * 60)).remainder(60.0)).toString()
		String seconds = ((int) new BigDecimal(durationEpoch / 1000).remainder(60.0)).toString()
		hours = hours.length() <= 1 ? "0" + hours : hours
		minutes = minutes.length() <= 1 ? "0" + minutes : minutes
		seconds = seconds.length() <= 1 ? "0" + seconds : seconds
		return "${hours}:${minutes}:${seconds}"
	}

	def static void createReportEvidenceImage() {
		String fileOriginPath = "./Data/ReportEvidenceImage.jrxml"
		String fileDestinationPath = "./Data/ReportEvidenceImage.pdf"
		JasperReport report = JasperCompileManager.compileReport(fileOriginPath)
		List<DataReportTestCase> field = new ArrayList<DataReportTestCase>()
		DataReportEvidenceImage image = new DataReportEvidenceImage("", "")
		field.add(image)

		int idx = 1
		GlobalVariable.Evidence_Image.each { key, val ->
			KeywordUtil.logInfo(key + " - " + val)
			String imageName = "${idx}. ${key}"
			String imagePath = "${GlobalVariable.Path_Test_Report}/Screenshoots/${key}.png"
			String imageDesc = val
			image = new DataReportEvidenceImage(imageName, imagePath, imageDesc)
			field.add(image)
			idx++
		}

		Map<String, Object> parameters = new HashMap<>()
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(field)
		parameters.put("dataSources", dataSource)
		JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource)
		JasperExportManager.exportReportToPdfFile(print, fileDestinationPath)
	}

	def static void mergePDF() {
		PDFMergerUtility pdfMerger = new PDFMergerUtility()
		pdfMerger.addSource("./Data/ReportCover.pdf")
		pdfMerger.addSource("./Data/ReportToc.pdf")
		pdfMerger.addSource("./Data/ReportTestCase.pdf")
		pdfMerger.addSource("./Data/ReportEvidenceImage.pdf")
		pdfMerger.setDestinationFileName("${GlobalVariable.Path_Test_Report}/Summary_Report.pdf")
		pdfMerger.mergeDocuments(null)
	}
}
