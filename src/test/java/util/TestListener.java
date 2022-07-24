package util;

import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;


public class TestListener extends TestListenerAdapter {
//	private Logger log = LoggerFactory.getLogger(TestListener.class);
	@Override
	public void onTestFailure(ITestResult tr) {		
		try {
			TestBase tb = (TestBase) tr.getInstance();
			String methodName = tr.getMethod().getMethodName();

			WebDriver driver = tb.getDriver(methodName);	
			// shrink the page, just like clicking ctrl+- to let all the elements show on page
//			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript("document.body.style.zoom='0.8';");
			ScreenShot ss = new ScreenShot(driver);
			ss.takeCaseScreenshot(methodName);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {			
			e.printStackTrace();
		}
	}
}
