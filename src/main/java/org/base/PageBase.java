package org.base;

import com.testautomationguru.ocular.Ocular;
import com.testautomationguru.ocular.comparator.OcularResult;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.selenium.enums.WaitStrategy;


public abstract class PageBase {

	/**
	 * The Driver.
	 */
	protected WebDriver driver = null;
	protected WebDriverWait wait;
	public Logger log = TestBase.log;


	public PageBase(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 10);
	}

	public void load(String endPoint) {
		driver.get(TestBase.data.getProperty("base.url") + endPoint);
		log.info("Navigating to : " + TestBase.data.getProperty("base.url") + endPoint);
	}

	public OcularResult compare() {
		return Ocular.snapshot()
				.from(this)
				.sample()
				.using(driver)
				.compare();
	}


	protected void click(By by, WaitStrategy waitStrategy, String elementName) {
		performExplicitWait(waitStrategy, by).click();
		log.info(elementName + " is clicked");
	}

	protected void sendKeys(By by, String value, WaitStrategy waitStrategy, String elementName) {
		performExplicitWait(waitStrategy, by).sendKeys(value);
		log.info(value + " is entered successfully in " + elementName);

	}

	protected void clear(By by, WaitStrategy waitStrategy, String elementName) {
		performExplicitWait(waitStrategy, by).clear();
		log.info("Clearing the field " + elementName);
	}

	protected void clearAndSendKeys(By by, String value, WaitStrategy waitStrategy, String elementName) {
		WebElement element = performExplicitWait(waitStrategy, by);
		element.clear();
		element.sendKeys(value);
		log.info(value + " is entered successfully in " + elementName);
	}

	protected boolean isElementExists(WaitStrategy waitStrategy, By by, String elementName) {
		boolean present;
		try{
			performExplicitWait(waitStrategy, by);
			present = true;
			log.info(elementName + " is present");
		}catch(Exception e){
			present = false;
			log.info(elementName + " is not present");
		}
		return  present;
	}


	public WebElement performExplicitWait(WaitStrategy waitStrategy, By by) {

		WebElement element = null;
		if (waitStrategy == WaitStrategy.CLICKABLE) {
			element = wait
					.until(webDriver -> ExpectedConditions.elementToBeClickable(by).apply(webDriver));
		}
		else if (waitStrategy == WaitStrategy.PRESENCE) {
			element = wait
					.until(webDriver -> ExpectedConditions.presenceOfElementLocated(by).apply(webDriver));
		}
		else if (waitStrategy == WaitStrategy.VISIBLE) {
			element = wait
					.until(webDriver -> ExpectedConditions.visibilityOfElementLocated(by).apply(webDriver));
		}
		else if (waitStrategy == WaitStrategy.NONE) {
			System.out.println("Not Waiting for anything");
			element = driver.findElement(by);
		}
		return element;

	}

}
