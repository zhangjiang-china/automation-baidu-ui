package flow;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import util.TimeUtil;

public class SearchFlow {
	private static Logger log = LoggerFactory.getLogger(SearchFlow.class);
	public static void clickSettingIcon(WebDriver driver) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//For different pages, the DOM structure is a little different for setting icon in top user setting
		try {
			WebElement settingIcon = driver.findElement(By.xpath("//*[@name='tj_settingicon']"));
			settingIcon.click();
		}
		catch(ElementNotInteractableException e) {
			WebElement settingIcon = driver.findElement(By.id("s-usersetting-top"));
			settingIcon.click();
		}
	}
	
	// Get advertisement count in current page
	public static void getAdvCount(WebDriver driver) {
		List<WebElement> advList = driver.findElements(By.xpath("//span[text() = '广告']"));
		int advNum = advList.size();
		log.info("There are " + advNum + " advertisement.");
		
	}
	
	public static void setLimitNoForEveryPage(WebDriver driver, int limitNoPerPage) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		
		clickSettingIcon(driver);
		
		WebElement searchSetMenu = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.setpref span.set")));
		js.executeScript("arguments[0].click();", searchSetMenu);
		
		String everyPageLimitElXpath = "//input[@name='NR'][@value=" + limitNoPerPage + "]";
		WebElement everyPayLimitEl = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(everyPageLimitElXpath)));
		everyPayLimitEl.click();
		
		WebElement saveSettingBtn = driver.findElement(By.cssSelector("a.prefpanelgo"));
		saveSettingBtn.click();
		
		driver.switchTo().alert().accept();
	}
	
	public static String searchByLimitTime(WebDriver driver, String limitTime, String searchKeyword) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		
		SearchFlow.clickSettingIcon(driver);
		
		WebElement searchSetMenu = driver.findElement(By.cssSelector("a.setpref span.set"));
		js.executeScript("arguments[0].click();", searchSetMenu);
		
		WebElement advancedSearchTab = driver.findElement(By.xpath("//li[@data-tabid='advanced']"));
		advancedSearchTab.click();
		
		WebElement timeSelectBox = driver.findElement(By.xpath("//span[@id='adv-setting-gpc']//div[contains(@class,'adv-gpc-select')]"));
		timeSelectBox.click();
		
		WebElement lastDaySelectItem = driver.findElement(By.xpath("//div[@class='c-select-dropdown-list']//p[2]"));
		lastDaySelectItem.click();
		
		// Get selected time
		WebElement selectedValueSpan = driver.findElement(By.xpath("//span[@id='adv-setting-gpc']//span[@class='c-select-selected-value']"));
		String actualSelectedTime = selectedValueSpan.getText();
		
		Assert.assertEquals(actualSelectedTime, limitTime, "The expected selected value is " + limitTime + ", but got " + actualSelectedTime);
		
		WebElement containAllKeywordsEl = driver.findElement(By.xpath("//li[@class='result-setting']/div[1]//input"));
		containAllKeywordsEl.sendKeys(searchKeyword);
		
		// Store the current window handle
		String winHandle1 = driver.getWindowHandle();
		String winHandle2 = "";
		WebElement advancedSearchSubmitBtn = driver.findElement(By.xpath("//input[@type='submit'][contains(@class,'advanced-search-btn')]"));
		js.executeScript("arguments[0].click();", advancedSearchSubmitBtn);
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
	
	public static void validateSearchResult(WebDriver driver, int expectedSearhOutNo, String expectedKeyword) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Exclude advertisement and recommend list
		List<WebElement> searchedOutList = driver.findElements(By.xpath("//div[@id='content_left']/div[contains(@class,'result')][@tpl!='recommend_list'][not(descendant::span[text()='广告'])]"));
		int actualSearchOutNo = searchedOutList.size();
		log.info("There are " + actualSearchOutNo + " result totally, exclude advertisement and recommend list.");
		
		// Validate searched out number
		Assert.assertEquals(actualSearchOutNo, expectedSearhOutNo, "The expected searched out count is " + expectedSearhOutNo + ", but got " + actualSearchOutNo);
		
		// Validate searched out content's keyword
		for(WebElement el : searchedOutList) {
//			js.executeScript("arguments[0].scrollIntoView(true);", el);
			String title = el.getText();
			Assert.assertTrue(title.contains(expectedKeyword), "The searched out result should contain " + expectedKeyword + ", but not found, the title is :" + title);
		}
	}
	
	public static void validateLimitLastDay(WebDriver driver, String oldWinHandle) {
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		
		List<WebElement> searchedOutList = driver.findElements(By.xpath("//div[@id='content_left']/div[contains(@class,'result')][@tpl!='recommend_list'][not(descendant::span[text()='广告'])]"));
		int actualSearchOutNum2 = searchedOutList.size();
		log.info("Search by last day, There are " + actualSearchOutNum2 + " item totally.");
		
		SearchFlow.getAdvCount(driver);
		
		for(WebElement el:searchedOutList) {
			wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("h3.c-title>a")));
			WebElement titleLinkEl = el.findElement(By.cssSelector("h3.c-title>a"));
			String title = titleLinkEl.getText();
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
				js.executeScript("arguments[0].click();", titleLinkEl);
				
				// Switch to new window opened
				for(String winHandle : driver.getWindowHandles()){
				    if(!winHandlesBefore.contains(winHandle)) {
				    	driver.switchTo().window(winHandle);
				    }
				}
				
				// Get page's update date from meta attribute
				updateDateRaw = driver.findElement(By.xpath("//meta[@itemprop='dateUpdate']")).getAttribute("content");
				String updateDate = updateDateRaw.substring(0, 10);
				
				String today = TimeUtil.getToday("yyyy-MM-dd");
				String yesterday = TimeUtil.getYesterday("yyyy-MM-dd");
				
				Assert.assertTrue(updateDate.equals(today) || updateDate.equals(yesterday),"Expected date is:" + today + " or " + yesterday + ", but actual is :" + updateDate);
				
				// Close current opened window and return to old one
				driver.close();
				driver.switchTo().window(oldWinHandle);
			}
			log.info("Title: " + title + ", update date : " + updateDateRaw);
		}
	}
	
}
