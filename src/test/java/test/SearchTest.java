package test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.Cons;
import flow.SearchFlow;
import util.TestBase;
import util.TestListener;
import util.TestReport;


@Listeners({ TestListener.class, TestReport.class })
public class SearchTest extends TestBase{
	
	private static Logger log = LoggerFactory.getLogger(SearchFlow.class);

	@Test(timeOut=Cons.TESTCASE_TIMEOUT)
	public void searchLimitNoPerPage()
    {
		log.info("***************[Start] : Search Limit 20 items per page***************");
		int limitNoPerPage = 20;
		String searchKeyword = "沉思录";
		
		log.info("Step 1: Set limit number for every page:" + limitNoPerPage);
		SearchFlow.setLimitNoForEveryPage(driver, limitNoPerPage);
		
		log.info("Step 2: Input search keyword:" + searchKeyword);
		WebElement searchInput = driver.findElement(By.cssSelector("input#kw"));
		searchInput.sendKeys(searchKeyword);
		
		// Submit search criteria
		log.info("Step 3: Submit to search");
		WebElement searchBtn = driver.findElement(By.cssSelector("input[type='submit']"));
		searchBtn.click();
		
		log.info("Validate current page");
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
		
		log.info("Click page 2 to validate");
		WebElement secondPageBtn = driver.findElement(By.xpath("//span[contains(@class,'page-item')][text()=2]"));
		secondPageBtn.click();
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
		
		log.info("Click next page to validate");
		WebElement nextPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][last()]"));
		nextPageBtn.click();
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
		
		log.info("Click previous page to validate");
		WebElement previousPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][1]"));
		previousPageBtn.click();
		SearchFlow.validateSearchResult(driver, limitNoPerPage, searchKeyword);
		SearchFlow.getAdvCount(driver);
		log.info("***************[End] : Search Limit 20 items per page***************");
    }
	
	@Test(timeOut=Cons.TESTCASE_TIMEOUT)
	public void searchLimitLastDay()
    {
		log.info("***************[Start] : Search Limit time in the last day***************");
		String limitTime = "最近一天";
		String searchKeyword = "沉思录";
		
		log.info("Step 1: Search by time limit:" + limitTime + ", search keyword:" + searchKeyword);
		String currentWindowHandle = SearchFlow.searchByLimitTime(driver, limitTime, searchKeyword);
		
		log.info("Validate current page");
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
		
		log.info("Click page 2 to validate");
		WebElement secondPageBtn = driver.findElement(By.xpath("//span[contains(@class,'page-item')][text()=2]"));
		secondPageBtn.click();
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
		
		log.info("Click next page to validate");
		WebElement nextPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][last()]"));
		nextPageBtn.click();
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
		
		log.info("Click previous page to validate");
		WebElement previousPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][1]"));
		previousPageBtn.click();
		SearchFlow.validateLimitLastDay(driver, currentWindowHandle);
		
		log.info("***************[End] : Search Limit time in the last day***************");
    }	
}
