package demo;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import util.TestBase;
import util.TestListener;
import util.TestReport;


@Listeners({ TestListener.class, TestReport.class })
public class SearchTest extends TestBase{

	@Test(timeOut=60000)
	public void searchLimit20PerPage()
    {
		JavascriptExecutor js = (JavascriptExecutor) driver;
	
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); 
		WebElement settingIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("s-usersetting-top")));
		settingIcon.click();
		
		WebElement searchSetMenu = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.setpref span.set")));
		js.executeScript("arguments[0].click();", searchSetMenu);
		
		WebElement everyPay20Items = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("nr_2")));
		everyPay20Items.click();
		
		WebElement saveSettingBtn = driver.findElement(By.cssSelector("a.prefpanelgo"));
		saveSettingBtn.click();
		
		driver.switchTo().alert().accept();
		
		WebElement searchInput = driver.findElement(By.cssSelector("input#kw"));
		searchInput.sendKeys("沉思录");
		
		WebElement searchBtn = driver.findElement(By.cssSelector("input[type='submit']"));
		searchBtn.click();
		validateSearchResult();
		
		// Click page 2
		WebElement secondPageBtn = driver.findElement(By.xpath("//span[contains(@class,'page-item')][text()=2]"));
		secondPageBtn.click();
		validateSearchResult();
		
		// Click next page
		WebElement nextPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][last()]"));
		nextPageBtn.click();
		validateSearchResult();
		
		// Click previous page
		WebElement previousPageBtn = driver.findElement(By.xpath("//div[@id='page']//a[@class='n'][1]"));
		previousPageBtn.click();
		validateSearchResult();
    }
	
	@Test(timeOut=60000)
	public void searchLimitLastDay()
    {
		JavascriptExecutor js = (JavascriptExecutor) driver;
	
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5)); 
		WebElement settingIcon = wait.until(ExpectedConditions.elementToBeClickable(By.id("s-usersetting-top")));
//		WebElement settingIcon = driver.findElement(By.xpath("//a[@name='tj_settingicon']"));
		settingIcon.click();
		
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
		String actualSelectedValue = selectedValueSpan.getText();
		String expectedSelectedValue = "最近一天";
		Assert.assertEquals(actualSelectedValue, expectedSelectedValue, "The expected selected value is " + expectedSelectedValue + ", but got " + actualSelectedValue);
		
		WebElement containAllKeywordsEl = driver.findElement(By.xpath("//li[@class='result-setting']/div[1]//input"));
		containAllKeywordsEl.sendKeys("沉思录");
		
		// Store the current window handle
		String winHandleBefore = driver.getWindowHandle();
		WebElement advancedSearchSubmitBtn = driver.findElement(By.xpath("//input[@type='submit'][contains(@class,'advanced-search-btn')]"));
		js.executeScript("arguments[0].click();", advancedSearchSubmitBtn);
		// Switch to new window opened
		for(String winHandle : driver.getWindowHandles()){
		    if(!winHandle.equals(winHandleBefore)) {
		    	driver.switchTo().window(winHandle);
		    }
		}
		
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<WebElement> searchedOutList2 = driver.findElements(By.xpath("//div[@id='content_left']/div[contains(@class,'result')][@tpl!='recommend_list'][not(descendant::span[text()='广告'])]"));
		int actualSearchOutNum2 = searchedOutList2.size();
		System.out.println("Search by last day, There are " + actualSearchOutNum2 + " item totally.");
		
		List<WebElement> advList2 = driver.findElements(By.xpath("//span[text() = '广告']"));
		int advNum2 = advList2.size();
		System.out.println("Search by last day, There are " + advNum2 + " advertisement.");
		
		for(WebElement el:searchedOutList2) {
//			String titleLink = el.getAttribute("mu");
			WebElement titleLinkEl = el.findElement(By.cssSelector("h3.c-title>a"));
			String title = titleLinkEl.getText();
			try {
				WebElement timeInfoEl = el.findElement(By.cssSelector("span.c-color-gray2"));
				String timeInfo = timeInfoEl.getText();
				if(!timeInfo.contains("小时前") && !timeInfo.contains("1天前")) {
					Assert.assertTrue(false, "Search ERROR, this item is not in the last day, the time info is :"+timeInfo+", the title is :" + title);
				}
			}
			catch(NoSuchElementException e) {
				Set<String> winHandlesBefore = driver.getWindowHandles();
				
				// No time display in current page, need to open it in a new page.
				js.executeScript("arguments[0].click();", titleLinkEl);
				
				// Switch to new window opened
				for(String winHandle : driver.getWindowHandles()){
				    if(!winHandlesBefore.contains(winHandle)) {
				    	driver.switchTo().window(winHandle);
				    }
				}
				
				// Scroll down to bottom of page
				js.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
				
				WebElement timeInfoEl = el.findElement(By.cssSelector("ul.reference-list li span[2]"));
				String actualTimeInfo = timeInfoEl.getText();
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String expectedTimeInfo = formatter.format(date);
				Assert.assertEquals(actualTimeInfo, expectedTimeInfo,"Expected time is:" + expectedTimeInfo + ", actual is :" + actualTimeInfo);
			}
		}	
    }

	private void validateSearchResult() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Exclude advertisement and recommend list
		List<WebElement> searchedOutList = driver.findElements(By.xpath("//div[@id='content_left']/div[contains(@class,'result')][@tpl!='recommend_list'][not(descendant::span[text()='广告'])]"));
		int actualSearchOutNum = searchedOutList.size();
		System.out.println("There are " + actualSearchOutNum + " item totally.");
		
		int expectedSearhOutNum = 20;
		Assert.assertEquals(actualSearchOutNum, expectedSearhOutNum, "The expected searched out num is " + expectedSearhOutNum + ", but got " + actualSearchOutNum);
		
		List<WebElement> advList = driver.findElements(By.xpath("//span[text() = '广告']"));
		int advNum = advList.size();
		System.out.println("There are " + advNum + " advertisement.");
	}
}
