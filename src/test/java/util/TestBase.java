package util;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {
//	public WebDriver driver;	
	
	public WebDriver getDriver(String methodName) {
		return map.get(methodName);
	}

	private Map<String, WebDriver> map = new HashMap<String, WebDriver>();
	
	@BeforeMethod(alwaysRun = true)
	public void setUp(Method method){
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
		
		// maximize browser
		driver.manage().window().maximize();
		
		// launch URL
		driver.get("https://www.baidu.com/");
		map.put(method.getName(), driver);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("return document.readyState").toString().equals("complete");
	}
	
	@AfterMethod(alwaysRun = true)
	public void tearDown(Method method){
		WebDriver driver = map.get(method.getName());
		driver.close();
		driver.quit();
	}
}
