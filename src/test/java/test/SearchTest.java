package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.Cons;
import flow.SearchFlow;
import util.TestBase;
import util.TestListener;
import util.TestReport;


@Listeners({ TestListener.class, TestReport.class })
public class SearchTest extends TestBase{

	@Test(timeOut=Cons.TESTCASE_TIMEOUT)
	public void searchLimitNoPerPage()
    {
		int limitNoPerPage = 20;
		String searchKeyword = "沉思录";
		
		SearchFlow.setLimitNoForEveryPage(driver, limitNoPerPage);
		
		// Input search keyword
		WebElement searchInput = driver.findElement(By.cssSelector("input#kw"));
		searchInput.sendKeys(searchKeyword);
		
		// Submit search criteria
		WebElement searchBtn = driver.findElement(By.cssSelector("input[type='submit']"));
		searchBtn.click();
		
		// Validate current page
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
		
		// Click page 2 to validate
		WebElement secondPageBtn = driver.findElement(By.xpath("//span[contains(@class,'page-item')][text()=2]"));
		secondPageBtn.click();
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
		
		// Click next page to validate
		WebElement nextPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][last()]"));
		nextPageBtn.click();
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
		
		// Click previous page to validate
		WebElement previousPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][1]"));
		previousPageBtn.click();
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
    }
	
	@Test(timeOut=Cons.TESTCASE_TIMEOUT)
	public void searchLimitLastDay()
    {
		String limitTime = "最近一天";
		String searchKeyword = "沉思录";
		
		String currentWindowHandle = SearchFlow.searchByLimitTime(driver, limitTime, searchKeyword);
		
		// Validate current page
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
		
		// Click page 2 to validate
		WebElement secondPageBtn = driver.findElement(By.xpath("//span[contains(@class,'page-item')][text()=2]"));
		secondPageBtn.click();
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
		
		// Click next page to validate
		WebElement nextPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][last()]"));
		nextPageBtn.click();
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
		
		// Click previous page to validate
		WebElement previousPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][1]"));
		previousPageBtn.click();
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
    }	
}
