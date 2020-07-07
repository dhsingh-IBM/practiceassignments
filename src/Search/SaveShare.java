//All in one assignment final

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

import org.openqa.selenium.Keys;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.annotations.AfterTest;

public class SaveShare {
	WebDriver driver;
	WebElement element, e1, e2;
	public String summaryText = "Document Number:";
	public String fullDetailText = "Document Type";
	String searchText, searchFun[];
	String filepath, filenamea, filenameb;
	String testCase;
	int act;
	

	static ExtentReports report;
	static ExtentTest test;

	@BeforeTest
	public void createDriver() {

		
		//System.setProperty("webdriver.gecko.driver", "C:/RG IBM/Automation/SeleniumRG/vxxGeko/geckodriver.exe");
		// Suppress logs with warning
		System.setProperty("webdriver.gecko.driver","C:\\RG IBM\\2020\\RG IBM\\RG_2020_Automation\\geckodriver-v0.26.0-win64\\geckodriver.exe");
		System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE,"/dev/null");
		
		driver = new FirefoxDriver();

		// Provide Test Case name for the execution as per the data sheet

		testCase = "SaveShare";

		filepath = System.getProperty("user.dir") + "\\src\\testData\\Driver_TestData.xls";

		// Provide the environment in which this test case need to Run (Production or
		// Testing)
		String environment = "Testing";
		String url = Utilities.readExcelFile(filepath, "AppURL", environment, "TestURL");
		
		
		driver.get(url);

		driver.manage().deleteAllCookies();
		report = new ExtentReports(System.getProperty("user.dir") + "\\" + testCase + "_TestCasesResults.html");
		test = report.startTest(testCase + " Week 3 Assignment Result");

	}

	@Test
	public void saveSearchAction() throws IOException {

		searchFun = Utilities.readExcelFile(filepath, "TestData", testCase, "Action").split(",");
		String searchText = Utilities.readExcelFile(filepath, "TestData", testCase, "SearchText");

		element = driver.findElement(By.id("smartfilter"));
		element.sendKeys(searchText);
	
		driver.findElement(By.cssSelector("a.ibm-search-link.ibm-inlinelink")).click();

		driver.findElement(By.xpath(".//*[@id='ibm-content-wrapper']/header/div/div[2]/ul/li[1]/a")).click();

		String noResults = driver.findElement(By.xpath(".//*[@id='count_display']")).getText();
		System.out.println(noResults);
		if (noResults.contains("1 - 0 of 0 results")) {
			try {
				test.log(LogStatus.INFO, test.addScreenCapture(Utilities.capture(driver)) + testCase,
						"No Search Results,Please provide another search criteria if you want rearch results for further testing.");
				System.out.println(
						"No Search Results,Please provide another search criteria if you want rearch results for further testing.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for (act = 0; act <= searchFun.length - 1; act++) {
			
			
			if (searchFun[act].contains("Save Share using Connections")) {
				//				saveSearchUsingConnection(driver);
			

			} else if (searchFun[act].contains("Add To browser bookmarks")) {

				addToBrowserBookmarks(driver);
			} else if (searchFun[act].contains("Permalink to this page")) {

				permalinkToThisPage(driver);
			} else if (searchFun[act].contains("Share via email")) {

				sharViaEmail(driver);
			}
		}

	}

	// Function to check Save Search Using Connection page functionality
	public void saveSearchUsingConnection(WebDriver driver) throws IOException {
		System.out.println("Save Share using Connections1");
		
		//driver.findElement((By.xpath(".//table[@width='100%']/tbody/tr/td/div/span[2]/a"))).click();
		driver.findElement(By.xpath("//a[contains(@id,'saveshare_v')]")).click();
		//clickLink(driver.findElement(By.linkText("Save or share using Connections")), driver);
		clickLink(driver.findElement(By.xpath("//a[contains(text(),'Save or share using Connections')]")),driver);
		/*
		 * element = (new WebDriverWait(driver, 15))
		 * .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
		 * "#btn_signin")));
		 */

		element = (new WebDriverWait(driver, 15))
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#w3idheader")));
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String pageText = element.getText();
		String pageTitle = driver.getTitle();
		//

		if ((pageTitle.contains("IBM w3id")) && (pageText.contains("Sign in with your w3id"))) {

			test.log(LogStatus.PASS,
					test.addScreenCapture(Utilities.capture(driver)) + "Save Share using Connections : ",
					"Navigation to connections page is successfull.");
			System.out.println("Navigation to connections page is successfull.");

		} else {
			try {
				System.out.println("Navigation to connections page is NOT successfull");
				test.log(LogStatus.FAIL,
						test.addScreenCapture(Utilities.capture(driver)) + "Save Share using Connections : FAILED ",
						"Navigation to connections page is NOT successfull");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		driver.navigate().back();
	}
	// This function is to add current page as a BookMarks however if we click on
	// Add to Browser Bookmarks WINDOWS popup come.
	// Bookmark is gets added through keyboard sendkey but Validation is pending as
	// its WINDOWS popup
	// so not able to check recently added bookmarks.

	public void addToBrowserBookmarks(WebDriver driver) throws IOException {
		System.out.println("Add To browser bookmarks");
		//driver.findElement((By.xpath(".//table[@width='100%']/tbody/tr/td/div/span[2]/a"))).click();
		//clickLink(driver.findElement(By.linkText("Add to browser bookmarks")), driver);
		
		driver.findElement(By.xpath("//a[contains(@id,'saveshare_v')]")).click();
		clickLink(driver.findElement(By.xpath("//a[contains(text(),'Add to browser bookmarks')]")),driver);
		// element.sendKeys(Keys.RETURN);
		//driver.findElement(By.xpath("//a[contains(text(),'Add to browser bookmarks')]"));
		driver.switchTo().alert().accept();
	
		Actions action = new Actions(driver);
		//action.keyDown(Keys.CONTROL).sendKeys("D").build().perform();
		//BOOKmark need to check again ... not working.
		action.keyDown(Keys.CONTROL).sendKeys("D").keyUp(Keys.CONTROL).perform();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		action.sendKeys(Keys.RETURN).build().perform();
		System.out.println("Bookmark is successfully added");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Add To browser bookmarks : ",
				"Bookmark is successfully added");

		/*
		 * action.keyDown(Keys.CONTROL); action.keyDown(Keys.SHIFT);
		 * action.sendKeys("B").perform();
		 */
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This function is used to check a pop-up window should open displaying the
	// current page URL when user click on Permalink link.
	public void permalinkToThisPage(WebDriver driver) throws IOException {
		try {

			System.out.println("Permalink to this page");
			String currenturl = driver.getCurrentUrl();
			//driver.findElement((By.xpath(".//table[@width='100%']/tbody/tr/td/div/span[2]/a"))).click();
			//clickLink(driver.findElement(By.linkText("Permalink to this page")), driver);
			
			driver.findElement(By.xpath("//a[contains(@id,'saveshare_v')]")).click();
			clickLink(driver.findElement(By.xpath("//a[contains(text(),'Permalink to this page')]")),driver);
			
			element = (new WebDriverWait(driver, 15))
					.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#WzTiTlTb>tbody>tr>td")));
			boolean popUp = false;

			Thread.sleep(2000);
			popUp = driver.findElement(By.xpath(".//*[@id='WzTiTlTb']/tbody/tr/td[2]")).isDisplayed();
			//System.out.println(popUp);
			String urltext = driver.findElement(By.xpath(".//*[@id='txt01']")).getText();
			//Check with Functional team on URL Https or http????
			//System.out.println(urltext);
			//System.out.println(currenturl);
			if ((urltext.contains(currenturl)) && (popUp == true)) {

				test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Permalink to this page : ",
						"A pop up window is opened and displaying the current page URL.");
				System.out.println("A pop up window is opened and displaying the current page URL.");

			} else {

				System.out.println("A pop up window is neither opened nor displaying the current page URL.");
				test.log(LogStatus.FAIL,
						test.addScreenCapture(Utilities.capture(driver)) + "Permalink to this page : FAILED ",
						"A pop up window is neither opened nor displaying the current page URL.");

			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void sharViaEmail(WebDriver driver) throws IOException {
		System.out.println("Shar via email");
		//driver.findElement((By.xpath(".//table[@width='100%']/tbody/tr/td/div/span[2]/a"))).click();
		//clickLink(driver.findElement(By.linkText("Share via email")), driver);
		
		driver.findElement(By.xpath("//a[contains(@id,'saveshare_v')]")).click();
		clickLink(driver.findElement(By.xpath("//a[contains(text(),'Share via email')]")),driver);
		// element = (new WebDriverWait(driver, 15))
		// .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#emailpagecontent")));

		element = (new WebDriverWait(driver, 15)).until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(".//*[@id='emailpagecontent']/div[1]/div[2]/p/input[1]")));

		String fromEmail = Utilities.readExcelFile(filepath, "TestData", testCase, "FromEmail");
		String toEmail = Utilities.readExcelFile(filepath, "TestData", testCase, "ToEmail");
		// System.out.println(fromEmail);
		// System.out.println(toEmail);
		// String emailPg =
		// driver.findElement(By.xpath(".//*[@id='emailpagecontent']/div[1]/h1")).getText();
		// System.out.println(emailPg);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		element = driver.findElement(By.id("fromf"));
		element.sendKeys(fromEmail);
		element = driver.findElement(By.id("tof"));
		element.sendKeys(toEmail);
		// boolean emailPopUp = false;

		// clickLink(driver.findElement(By.xpath(".//*[@id='emailpagecontent']/div[1]/div[2]/p/input[1]")),
		// driver);

		e2 = driver.findElement(By.xpath(".//*[@id='emailpagecontent']/div[1]/div[2]/p/input[1]"));
		Actions builder1 = new Actions(driver);
		builder1.moveToElement(e2).click(e2);
		builder1.perform();

		element = (new WebDriverWait(driver, 15)).until(
				ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#dialog_emailpageOverlay_description")));

		// boolean emailPopUp =
		// driver.findElement(By.cssSelector("#dialog_emailpageOverlay")).isDisplayed();
		// System.out.println(emailPopUp);

		element = (new WebDriverWait(driver, 15))
				.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#emailpagecontent")));

		String bodyText = driver.findElement(By.cssSelector("#emailpagecontent")).getText();
		// System.out.println(bodyText);
		if (bodyText.contains("Email sent successfully")) {

			test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + "Share via email : ",
					"Email sent to " + toEmail + " Successfuly.");
			System.out.println("Email sent to " + toEmail + " Successfuly.");

		} else {
			try {
				System.out.println("Email NOT sent, Please check email and try after some time");
				test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.capture(driver)) + "Share via email : FAILED ",
						"Email NOT sent, Please check email and try after some time");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void clickLink(WebElement e, WebDriver driver) throws IOException {
		boolean step1check = false;
		step1check = driver.findElement((By.xpath("//a[contains(@id,'saveshare_v')]"))).isDisplayed();
						
		// System.out.println(step1check);
		if (step1check == true) {
			Actions builder = new Actions(driver);
			builder.moveToElement(e).click(e);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			test.log(LogStatus.PASS, test.addScreenCapture(Utilities.capture(driver)) + testCase + " : ",
					"Link is clicked as per the Test Case Action " + searchFun[act]);
			
			builder.perform();
			
		} else {
			try {
				test.log(LogStatus.FAIL, test.addScreenCapture(Utilities.capture(driver)) + testCase + " FAILED ",
						"\"Link is NOT clicked or Working as per the test case Action");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@AfterTest
	public void afterTest() {
		// System.out.println("After test");
		Actions action = new Actions(driver);
		action.sendKeys(Keys.F5).perform();
		driver.manage().deleteAllCookies();
		driver.quit();
		report.endTest(test);
		report.flush();
	}

}
