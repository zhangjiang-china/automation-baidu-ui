package util;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {
public WebDriver driver;	
	
	public WebDriver getDriver() {
		return driver;
	}

	@BeforeClass
	public void setUp(){
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(1000));
		
		// maximize browser
		driver.manage().window().maximize();
		
		// launch URL
		driver.get("https://www.baidu.com/");
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("return document.readyState").toString().equals("complete");
	}
	
	@AfterClass
	public void tearDown(){
		driver.close();
		driver.quit();
	}
}
