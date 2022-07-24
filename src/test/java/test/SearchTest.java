package test;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import base.Cons;
import flow.SearchFlow;
import page.SearchPage;
import util.TestBase;
import util.TestListener;
import util.TestReport;

@Listeners({ TestListener.class, TestReport.class })
public class SearchTest extends TestBase{
	
	private static Logger log = LoggerFactory.getLogger(SearchFlow.class);

	@Test(enabled=true, timeOut=Cons.TESTCASE_TIMEOUT)
	public void searchLimitNoPerPage()
    {
		WebDriver driver = getDriver("searchLimitNoPerPage");
		SearchPage searchPage = new SearchPage(driver);
		SearchFlow searchFlow = new SearchFlow(driver, searchPage);
		
		log.info("***************[Start] : Search Limit 20 items per page***************");
		int limitNoPerPage = 20;
		String searchKeyword = "沉思录";
		
		log.info("Step 1: Set limit number for every page:" + limitNoPerPage);
		searchFlow.setLimitNoForEveryPage(limitNoPerPage);
		
		log.info("Step 2: Input search keyword:" + searchKeyword);
		searchPage.clearSearchInput();
		searchPage.sendKeysToSearchInput(searchKeyword);
		
		// Submit search criteria
		log.info("Step 3: Submit to search");
		searchPage.clickSearchBtn();
		
		log.info("Validate current page");
		searchFlow.validateSearchResult(limitNoPerPage, searchKeyword);
		searchFlow.getAdvCount();
		
		log.info("Click page 2 to validate");
		searchPage.clickSecondPageBtn();
		searchFlow.validateSearchResult(limitNoPerPage, searchKeyword);
		searchFlow.getAdvCount();
		
		log.info("Click next page to validate");
		searchPage.clickNextPageBtn();
		searchFlow.validateSearchResult(limitNoPerPage, searchKeyword);
		searchFlow.getAdvCount();
		
		log.info("Click previous page to validate");
		searchPage.clickPreviousPageBtn();
		searchFlow.validateSearchResult(limitNoPerPage, searchKeyword);
		searchFlow.getAdvCount();
		searchPage.clearSearchInput();
		log.info("***************[End] : Search Limit 20 items per page***************");
    }
	
	@Test(enabled=true, timeOut=Cons.TESTCASE_TIMEOUT)
	public void searchLimitLastDay()
    {
		WebDriver driver = getDriver("searchLimitLastDay");
		SearchPage searchPage = new SearchPage(driver);
		SearchFlow searchFlow = new SearchFlow(driver, searchPage);
		
		log.info("***************[Start] : Search Limit time in the last day***************");
		String limitTime = "最近一天";
		String searchKeyword = "沉思录";
		
		log.info("Step 1: Search by time limit:" + limitTime + ", search keyword:" + searchKeyword);
		String currentWindowHandle = searchFlow.searchByLimitTime(limitTime, searchKeyword);
		
		log.info("Validate current page");
		searchFlow.validateLimitLastDay(currentWindowHandle);
		
		log.info("Click page 2 to validate");
		searchPage.clickSecondPageBtn();
		searchFlow.validateLimitLastDay(currentWindowHandle);
		
		log.info("Click next page to validate");
		searchPage.clickNextPageBtn();
		searchFlow.validateLimitLastDay(currentWindowHandle);
		
		log.info("Click previous page to validate");
		searchPage.clickPreviousPageBtn();
		searchFlow.validateLimitLastDay(currentWindowHandle);
		searchPage.clearSearchInput();
		log.info("***************[End] : Search Limit time in the last day***************");
    }	
}
