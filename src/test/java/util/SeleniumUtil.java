package util;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumUtil {
	private static final String JQUERY_ACTIVE_CONNECTIONS_QUERY = "return $.active == 0;";
	// ExpectedCondition for executing js codes to detect if exist any jquery connection
	private static ExpectedCondition<Boolean> noActiveConnections = new ExpectedCondition<Boolean>() {
		@Override
		public Boolean apply(WebDriver driver) {
			JavascriptExecutor jsExec = (JavascriptExecutor) driver;
			return (Boolean) jsExec.executeScript(JQUERY_ACTIVE_CONNECTIONS_QUERY);
		}
	};
	
	public static WebDriverWait createWait(WebDriver driver, long timeOutInSeconds) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
	}
	
	public static boolean waitUntilAllAjaxRequestCompletes(WebDriver driver, int seconds) {
		Boolean status = false;
		try {
			status = SeleniumUtil.createWait(driver, seconds).until(noActiveConnections);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

}
