package Search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import org.testng.Assert;

public class Utilities {

	String filepath;

	// Setup Reslults per page value as per the test case data mentioned in the
	// Datasheet

	/*********************************************************************************************
	 * Method Name : readExcelFile Parameter Used : String filePath, int sheetIndex,
	 * int row, int column Author : Rohit Prajapati Creation Date : 05/06/2015
	 * Description : Method to read data from Excel
	 ********************************************************************************************/
	@SuppressWarnings({ "rawtypes", "deprecation" })
	public static String readExcelFile(String filePath, String sheetName, String testName, String columnName) {
		try {
			FileInputStream inFile = new FileInputStream(new File(filePath));
			HSSFWorkbook workbook = new HSSFWorkbook(inFile);
			HSSFSheet worksheet = workbook.getSheet(sheetName);
			short column = 0;

			// Get Column No
			HSSFRow headerRow = worksheet.getRow(0);
			java.util.Iterator headerIterator = headerRow.cellIterator();
			while (headerIterator.hasNext()) {
				HSSFCell headerColumn = (HSSFCell) headerIterator.next();
				if (headerColumn != null && headerColumn.getStringCellValue().equalsIgnoreCase(columnName)) {
					column = headerColumn.getCellNum();
					break;
				}
			}

			// Get Row
			java.util.Iterator rows = worksheet.rowIterator();
			short columnNo = 0;
			String cellValue = "";
			while (rows.hasNext()) {
				HSSFRow row = (HSSFRow) rows.next();
				HSSFCell col = row.getCell(columnNo);

				if (col != null && col.getStringCellValue().equalsIgnoreCase(testName)) {
					inFile = new FileInputStream(new File(filePath));
					workbook = new HSSFWorkbook(inFile);
					HSSFCell cell = null;

					cell = worksheet.getRow(row.getRowNum()).getCell(column);
					// System.out.println("The Value is : " + cell.getStringCellValue());

					inFile.close();
					cellValue = cell.getStringCellValue();
					break;
				}
			}
			return cellValue;

		} catch (FileNotFoundException e) {
			System.out.println("File Does Not Exists At " + filePath);
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (NullPointerException e) {
			System.out.println("Cell Value is Empty in File at " + filePath);
			return "";
		}
	}

	
	// This function is to take screenprints for FAILED Step only
	public static String capture(WebDriver driver) throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String Destfilelocation = (System.getProperty("user.dir") + System.currentTimeMillis() + ".png");
		
		// String Destfilelocation = (System.getProperty("user.dir") +
		// "\\src\\test-output\\" + System.currentTimeMillis() + ".png");
		File Dest = new File(Destfilelocation);
		String errflpath = Dest.getAbsolutePath();
		FileHandler.copy(scrFile, Dest);
		return errflpath;
		// return Destfilelocation;
	}

}
