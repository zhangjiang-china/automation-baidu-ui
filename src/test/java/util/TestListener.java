package util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;


public class TestListener extends TestListenerAdapter {
	@Override
	public void onTestFailure(ITestResult tr) {		
		try {
			TestBase tb = (TestBase) tr.getInstance();
			WebDriver driver = tb.getDriver();	
			JavascriptExecutor js = (JavascriptExecutor) driver;
			// shrink the page, just like clicking ctrl+- to let all the elements show on page
			js.executeScript("document.body.style.zoom='0.8';");
			ScreenShot ss = new ScreenShot(driver);
			ss.takeScreenshot();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
		}
	}
}
