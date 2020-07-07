//ALL in one assignment final

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

import org.apache.tools.ant.util.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;

public class ResultPerPage {
	WebDriver driver;
	WebElement element;
	public String resultPerPage;
	String searchText, nextpagelinks, nextpagelinke;
	String filepath;
	String testCase;
	String resultPerpageDropdown;
	String totalhits;
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
		testCase = "ResultPerPage";
		filepath = System.getProperty("user.dir") + "\\src\\testData\\Driver_TestData.xls";

		// Provide the environment in which this test case need to Run (Production or
		// Testing)
		String environment = "Production";
		String url = Utilities.readExcelFile(filepath, "AppURL", environment, "TestURL");

		driver.get(url);

		driver.manage().deleteAllCookies();

		//report = new ExtentReports(System.getProperty("user.dir") + "\\ExtendReportResults.html");
		report = new ExtentReports(System.getProperty("user.dir") + "\\" + testCase + "_TestCasesResults.html");
		test = report.startTest(testCase + " Assignment Result");

	}

	@Test
	public void SearchandPagination() throws IOException, InterruptedException {

		try {

			resultPerPage = Utilities.readExcelFile(filepath, "TestData", testCase, "ResultperPage");
			//resultPerpageDropdown = resultPerPage + " results/page";
			resultPerpageDropdown = resultPerPage;
			// System.out.println(resultPerpageDropdown);
			searchText = Utilities.readExcelFile(filepath, "TestData", testCase, "SearchText");
			// System.out.println(searchText);
			resultperpagesetup(driver, resultPerpageDropdown);
			seachandcheckresult(driver, searchText);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	// This function set the Result Per Page in Display Preference
	public void resultperpagesetup(WebDriver driver, String valuePerPage) throws InterruptedException {
		driver.findElement(By.xpath("//a[contains(text(),'My SSI')]")).click();
		driver.findElement(By.cssSelector("#select2-noperpage1-container")).click();
		driver.findElement(By.xpath("//ul[@class='select2-results__options']/li[contains(text(),'" + valuePerPage + "')]")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Save & Search')]")).click();

	}

	// This function search the test and do first page validation
	public void seachandcheckresult(WebDriver driver, String searchText) throws IOException {

		try {
			//System.out.println(searchText);
			element = driver.findElement(By.id("smartfilter"));
			element.sendKeys(searchText);
			driver.findElement(By.cssSelector("a.ibm-search-link.ibm-inlinelink")).click();

			String searchResults = driver.findElement(By.xpath(".//*[@id='ibm-content-main']/div[1]/div[1]")).getText();
			// Below statement is to test failed scenario if result message is blank.
			// String searchResults = " ";

			if (searchResults.contains(resultPerPage)) {
				totalhits = driver.findElement(By.xpath("//*[@id='ibm-content-main']/div[1]/div[1]/strong[3]"))
						.getText();
				String result1 = "There are total " + totalhits + " Entries for the Search Text '" + searchText
						+ "' and " + resultPerpageDropdown + " displayed.";
				System.out.println(result1);
				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Results Per Page : PASS",
						result1);

				int totalnextlinks = Integer.parseInt(totalhits) / Integer.parseInt(resultPerPage);
				int s = Integer.parseInt(resultPerPage) + 1;
				int e = Integer.parseInt(resultPerPage) * 2;

				nextpagelinks = Integer.toString(s);
				nextpagelinke = Integer.toString(e);
				// System.out.println(totalnextlinks);
				if (totalnextlinks >= 1) {
					nextpagechecking(driver, totalnextlinks);
				} else {
					System.out.println("Next Link is NOT present");
					test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Pagination",
							"Next Link is NOT present");
				}

			} else if (searchResults.contains("No results found after applying Filters")) {
				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Results Per Page : PASS",
						"NO SEARCH RESULTS: Please change the search criteria.");
				System.out.println("NO SEARCH RESULTS: Please change the search criteria.");
			} else if (searchResults.contains("1")) {

				totalhits = driver.findElement(By.xpath("//*[@id='ibm-content-main']/div[1]/div[1]/strong[3]"))
						.getText();
				String result2 = "There are only  " + totalhits + " Entries for the Search Text '" + searchText
						+ "' for " + resultPerpageDropdown + " so Next Link is NOT Present.";
				System.out.println(result2);
				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Results Per Page : PASS ",
						result2);

			} else {

				System.out.println("Blank message");
				// Separate take screenshot function also added to take screenshot for FAILED
				// test case in Extend Report
				test.log(LogStatus.FAIL,
						test.addScreenCapture(Utilities.capture(driver)) + "Results Per Page : FAILED ",
						"Something went worng as No message is displayed");
				// test.log(LogStatus.FAIL, "Step 1", "Next Link is NOT present");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// This function check pagination if next page is available currently
	// implemented for first next page.
	public void nextpagechecking(WebDriver driver, int totalnextlinks) throws IOException {
		try {

			driver.findElement(By.cssSelector(".ibm-table-navigation-links>a")).click();
			String searchResultsnp = driver.findElement(By.xpath(".//*[@id='ibm-content-main']/div[1]/div[1]"))
					.getText();
			// Below statement is to test failed scenario if result message is blank.
			// String searchResultsnp = " ";
			String nextpagemsg = nextpagelinks + " - " + nextpagelinke;
			// System.out.println(searchResultsnp);
			String nextpgstart = nextpagelinks;
			// System.out.println(nextpgstart);
			int nextpages = totalnextlinks - 1;
			if (searchResultsnp.contains(nextpagemsg)) {
				String result1a = "Next Page Paginaiton is correct and there are more " + nextpages + " pages.";
				System.out.println(result1a);
				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Pagination : PASS",
						result1a);

			} else if (searchResultsnp.contains(nextpgstart)) {

				String result1b = "Next Page paginaiton is correct but this is the last search result page so Next link is NOT available.";
				System.out.println(result1b);
				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Pagination : PASS",
						result1b);

			} else {

				System.out.println("Blank message");

				test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.capture(driver)) + "Pagination : FAILED ",
						"Something went worng as No message is displayed");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterTest
	public void afterTest() {
		driver.quit();
		report.endTest(test);
		report.flush();
	}
}
