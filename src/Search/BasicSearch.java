
//All in One assignment final 
package Search;

import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.AfterTest;

public class BasicSearch {

	WebDriver driver;
	WebElement element;
	public String summaryText = "Document Number:";
	public String fullDetailText = "Document Type";
	String searchText;
	String filepath, filename;
	String testCase;

	static ExtentReports report;
	static ExtentTest test;

	@BeforeTest
	public void createDriver() {

		//System.setProperty("webdriver.gecko.driver", "C:/RG IBM/Automation/SeleniumRG/vxxGeko/geckodriver.exe");
		System.setProperty("webdriver.gecko.driver","C:\\RG IBM\\2020\\RG IBM\\RG_2020_Automation\\geckodriver-v0.26.0-win64\\geckodriver.exe");
		// Suppress logs with warning
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
		
		driver = new FirefoxDriver();
		// Provide Test Case name for the execution as per the data sheet
		testCase = "BasicSearch";

		filepath = System.getProperty("user.dir") + "\\src\\testData\\Driver_TestData.xls";

		// Provide the environment in which this test case need to Run (Production or
		// Testing)
		String environment = "Testing";
		String url = Utilities.readExcelFile(filepath, "AppURL", environment, "TestURL");

		
		driver.get(url);

		
		driver.manage().deleteAllCookies();
	
		//report = new ExtentReports(System.getProperty("user.dir") + "\\ExtendReportResults.html");
		report = new ExtentReports(System.getProperty("user.dir") + "\\" + testCase + "_TestCasesResults.html");

		test = report.startTest(testCase + " Assignment Result");

	}

	@Test
	public void basicSearchAction() throws IOException {

		String[] searchFun = Utilities.readExcelFile(filepath, "TestData", testCase, "Action").split(",");
		String searchText = Utilities.readExcelFile(filepath, "TestData", testCase, "SearchText");

		element = driver.findElement(By.id("smartfilter"));
		element.sendKeys(searchText);
		driver.findElement(By.cssSelector("a.ibm-search-link.ibm-inlinelink")).click();

		// driver.findElement(By.linkText("Back to Classic SSI Search")).click();
		driver.findElement(By.xpath(".//*[@id='ibm-content-wrapper']/header/div/div[2]/ul/li[1]/a")).click();
		// This is to take proper screenshot.

		String noResults = driver.findElement(By.xpath(".//*[@id='count_display']")).getText();

		boolean sresult = true;

		for (int act = 0; act <= searchFun.length - 1; act++) {

			if (noResults.contains("1 - 0 of 0 results") && (sresult == true)) {
				try {
					sresult = false;
					test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.capture(driver)) + testCase + " FAILED ",
							"No Search Results,Please provide another search criteria for further testing.");
					System.out.println("No Search Results,Please provide another search criteria for further testing.");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (searchFun[act].contains("HideSummaries") && (sresult == true)) {
				hideSummaries(driver);
			} else if (searchFun[act].contains("ShowSummaries") && (sresult == true)) {

				showSummaries(driver);
			} else if (searchFun[act].contains("FullDetails") && (sresult == true)) {

				fullDetail(driver);
			}
		}
	}

	// Function to validate Hide summaries functionality
	public void hideSummaries(WebDriver driver) throws IOException {

		driver.findElement(By.xpath(".//*[@id='show']")).click();
		element = driver.findElement(By.xpath(".//*[@id='hide']"));
		element.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,200)");
		String noDocId = (driver.findElement(By.cssSelector(".ibm-data-table.ibm-alternating>tbody>tr>td"))).getText();

		if ((noDocId.contains(summaryText)) || (noDocId.contains(fullDetailText))) {

			try {
				test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.capture(driver)) + "Hide Summaries : FAILED ",
						"Document Type and ID is displayed");
				System.out.println("Document Type and ID is displayed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Hide Summaries : ",
					"Document Type and ID is NOT displayed which is as expected for Hide Summaries link.");
			System.out.println("Document Type and ID is NOT displayed which is as expected for Hide Summaries link.");
		}

	}

	// Function to validate Show summaries functionality
	public void showSummaries(WebDriver driver) {

		element = driver.findElement(By.xpath(".//*[@id='show']"));
		element.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,200)");
		String sumText = (driver.findElement(By.cssSelector(".ibm-data-table.ibm-alternating>tbody>tr>td>b")))
				.getText();
		// System.out.println(sumText);

		if (sumText.contains(summaryText)) {
			try {
				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Show Summaries : ",
						"Show summaries info. like Document Number is displayed");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Show summaries info. like Document Number is displayed");
		} else {

			try {
				test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.capture(driver)) + "Show Summaries : FAILED ",
						"Show summaries info. like Document Number is NOT displayed.");
				System.out.println("Show summaries info. like Document Number is NOT displayed.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// Function to validate Full details functionality
	public void fullDetail(WebDriver driver) throws IOException {

		element = driver.findElement(By.xpath(".//*[@id='full']"));
		element.click();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,200)");
		String deText = (driver.findElement(By.cssSelector(".ssi-gray>b"))).getText();

		if ((deText.contains(fullDetailText))) {
			try {
				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Full details : ",
						"Full details info. like Document ID is displayed.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Full details info. like Document ID is displayed.");
		} else {
			System.out.println("Full details info. like Document ID is NOT displayed.");
			test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.capture(driver)) + "Full details : ",
					"Full details info. like Document ID is NOT displayed.");
		}

	}

	@AfterTest
	public void afterTest() {
		// System.out.println("After test");
		driver.quit();
		report.endTest(test);
		report.flush();
	}
}
