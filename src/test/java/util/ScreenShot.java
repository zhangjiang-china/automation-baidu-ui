package util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScreenShot {
	private static Logger log = LoggerFactory.getLogger(ScreenShot.class);
	public WebDriver driver;

	public ScreenShot(WebDriver driver) {
		this.driver = driver;
	}

	private void takeScreenshot(String screenPath) {
		try {
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(screenPath));
		} catch (IOException e) {
			log.error("Screen shot error: " + screenPath);
		}
	}

	public void takeCaseScreenshot(String methodName) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		String time = formatter.format(System.currentTimeMillis());
		String screenName = methodName + "_" + time + ".jpg";
		File dir = new File("test-output/snapshot");
		if (!dir.exists())
			dir.mkdirs();
		String screenPath = dir.getAbsolutePath() + "/" + screenName;
		this.takeScreenshot(screenPath);		
	}
}
