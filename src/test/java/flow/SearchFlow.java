package flow;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import page.SearchPage;
import util.SeleniumUtil;
import util.TimeUtil;

public class SearchFlow {
	WebDriver driver;
	
	private static Logger log = LoggerFactory.getLogger(SearchFlow.class);
	private SearchPage searchPage;
	
	public SearchFlow(WebDriver driver, SearchPage searchPage) {
		this.searchPage = searchPage;
		this.driver = driver;
	}

	public void clickSettingIcon() {
		SeleniumUtil.waitUntilAllAjaxRequestCompletes(driver, 10);
		
		//For different pages, the DOM structure is a little different for setting icon in top user setting
		try {
			searchPage.clickSettingIcon1();
		}
		catch(ElementNotInteractableException | NoSuchElementException e) {
			searchPage.clickSettingIcon2();
		}
	}
	
	// Get advertisement count in current page
	public void getAdvCount() {
		int advNum = searchPage.getAdvListSize();
		log.info("There are " + advNum + " advertisement.");
		
	}
	
	public void setLimitNoForEveryPage(int limitNoPerPage) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		
		clickSettingIcon();
		searchPage.clickSearchSetMenu();
		
		String everyPageLimitElXpath = "//input[@name='NR'][@value=" + limitNoPerPage + "]";
		WebElement everyPayLimitEl = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(everyPageLimitElXpath)));
		everyPayLimitEl.click();
		
		searchPage.clickSaveSettingBtn();
		
		driver.switchTo().alert().accept();
	}
	
	public String searchByLimitTime(String limitTime, String searchKeyword) {
		clickSettingIcon();
		
		searchPage.clickSearchSetMenu();
		
		searchPage.clickAdvancedSearchTab();
		
		searchPage.clickTimeSelectBox();
		
		searchPage.clickLastDaySelectItem();
		
		String actualSelectedTime = searchPage.getSelectedTimeSpan();
		
		Assert.assertEquals(actualSelectedTime, limitTime, "The expected selected value is " + limitTime + ", but got " + actualSelectedTime);
		
		searchPage.sendKeysToContainAllKeywordsEl(searchKeyword);
		
		// Store the current window handle
		String winHandle1 = driver.getWindowHandle();
		String winHandle2 = "";
		searchPage.clickAdvancedSearchSubmitBtn();
		// Switch to new window opened
		for(String winHandle : driver.getWindowHandles()){
		    if(!winHandle.equals(winHandle1)) {
		    	winHandle2 = winHandle;
		    	driver.switchTo().window(winHandle2);
		    	break;
		    }
		}
		return winHandle2;
	}
	
	public void validateSearchResult(int expectedSearhOutNo, String expectedKeyword) {
		SeleniumUtil.waitUntilAllAjaxRequestCompletes(driver, 10);
		
		// Exclude advertisement and recommend list
		List<WebElement> searchedOutList = searchPage.getSearchedOutList();
		int actualSearchOutNo = searchPage.getSearchedOutListSize();
		log.info("There are " + actualSearchOutNo + " results totally, exclude advertisement and recommend list.");
		
		// Validate searched out number
		Assert.assertEquals(actualSearchOutNo, expectedSearhOutNo, "The expected searched out count is " + expectedSearhOutNo + ", but got " + actualSearchOutNo);
		
		// Validate searched out content's keyword
		for(WebElement el : searchedOutList) {
			// js.executeScript("arguments[0].scrollIntoView(true);", el);
			String title = el.getText();
			Assert.assertTrue(title.contains(expectedKeyword), "The searched out result should contain " + expectedKeyword + ", but not found, the title is :" + title);
		}
	}
	
	public void validateLimitLastDay(String oldWinHandle) {

		SeleniumUtil.waitUntilAllAjaxRequestCompletes(driver, 10);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
		// Exclude advertisement and recommend list
		List<WebElement> searchedOutList = searchPage.getSearchedOutList();
		int actualSearchOutNo = searchPage.getSearchedOutListSize();
		log.info("Search by last day, There are " + actualSearchOutNo + " item totally.");
		
		getAdvCount();
		
		for(WebElement el:searchedOutList) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h3.c-title>a")));
			String title = searchPage.getTitleText();
			String updateDateRaw = "";
			
			try {
				// Time display in current page
				wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".c-color-gray2")));
				WebElement dateUpdateEl = el.findElement(By.cssSelector(".c-color-gray2"));
				updateDateRaw = dateUpdateEl.getText();
				

				String today = TimeUtil.getToday("yyyy年MM月dd日");
				String yesterday = TimeUtil.getToday("yyyy年MM月dd日");
				if(!updateDateRaw.contains("前") && !updateDateRaw.contains(today) && !updateDateRaw.contains(yesterday)) {
					Assert.assertTrue(false, "Search ERROR, this item is not in the last day, the update date is :" + updateDateRaw + ", the title is :" + title);
				}
			}
			catch(NoSuchElementException e) {
				Set<String> winHandlesBefore = driver.getWindowHandles();
				
				// Time display NOT in current page, need to open it in a new page.
				searchPage.clickTitleLink();
				
				// Switch to new window opened
				for(String winHandle : driver.getWindowHandles()){
				    if(!winHandlesBefore.contains(winHandle)) {
				    	driver.switchTo().window(winHandle);
				    }
				}
				
				// Get page's update date from meta attribute
				updateDateRaw = searchPage.getDateUpdateInNewOpenPage();
				String updateDate = updateDateRaw.substring(0, 10);
				
				String today = TimeUtil.getToday("yyyy-MM-dd");
				String yesterday = TimeUtil.getYesterday("yyyy-MM-dd");
				
				Assert.assertTrue(updateDate.equals(today) || updateDate.equals(yesterday),"Expected date is:" + today + " or " + yesterday + ", but actual is :" + updateDate);
				
				// Close current opened window and return to old one
				driver.close();
				driver.switchTo().window(oldWinHandle);
			}
			log.debug("Title: " + title + ", update date : " + updateDateRaw);
		}
	}
	
}
