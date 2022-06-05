package pages;

/*******************************************************************************************
 * Page Factory class Template
 *******************************************************************************************/

import com.testautomationguru.ocular.snapshot.Snap;
import org.base.PageBase;
import org.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.enums.WaitStrategy;

@Snap("LoginPage.png")
public class LoginPage extends PageBase {

	public LoginPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}



	/*******************************************************************************************
	 * All WebElements and Page locators Identification
	 *******************************************************************************************/

	@FindBy(css = ".dialog-body-wrapper")
	private WebElement errorMessage;
	@FindBy(css = ".error.ng-star-inserted")
	private WebElement inputError;


	private final By emailText = By.cssSelector("input[formcontrolname='userName']");
	private final By passwordText = By.cssSelector("input[formcontrolname='password']");
	private final By subTitle = By.cssSelector(".sub-title span");
	private final By loginBtn = By.cssSelector("button.gp-submit");
	private final By emailAndPasswordField = By.id("mat-tab-label-0-1");

	/*******************************************************************************************
	 * All Methods for performing actions
	 *******************************************************************************************/

	public Boolean isLoaded() {
		return wait.until(webDriver -> ExpectedConditions.textToBe(subTitle, "GetPackage").apply(webDriver));
	}

	public Boolean isHomeOk() {return wait.until(webDriver -> ExpectedConditions.urlToBe(TestBase.data.getProperty("base.url") + "home").apply(webDriver));}

	public LoginPage load() {
		load("login");
		isLoaded();
		log.info("Login page is successfully loaded");
		return this;
	}


	public LoginPage enterEmail(String text_email) {
		clearAndSendKeys(emailText, text_email, WaitStrategy.PRESENCE, "email field");
		return this;
	}

	public LoginPage enterPassword(String text_password) {
		//wait.until(ExpectedConditions.visibilityOfElementLocated(passwordText)).sendKeys(text_password);
		sendKeys(passwordText, text_password, WaitStrategy.PRESENCE, "password field");
		return this;
	}

	public LoginPage clickEmailAndPasswordLoginLink() {
		click(emailAndPasswordField, WaitStrategy.CLICKABLE, "Click To Login By Email and Password");
		return this;
	}

	public String getGeneralLoginError() {
		log.info("Getting wrong email or password message");
		return wait.until(webDriver -> ExpectedConditions.visibilityOf(errorMessage).apply(webDriver)).getText();
	}

	public String getInputErrorMessage() {
		log.info("Getting wrong input message");
		return wait.until(webDriver -> ExpectedConditions.visibilityOf(inputError).apply(webDriver)).getText();
	}


	public LoginPage singIn() {
		click(loginBtn, WaitStrategy.CLICKABLE, "click on login button");
		//return new NextAfterLoginPage(driver);
		return  this;
	}

	public CreateDeliveryPage goToCreateDelivery() throws InterruptedException {
		this.load();
		clickEmailAndPasswordLoginLink();
		enterEmail(TestBase.data.getProperty("real.email"));
		enterPassword(TestBase.data.getProperty("real.password"));
		singIn().isHomeOk();
		Thread.sleep(1000);
		load("create-delivery");
		return new CreateDeliveryPage(driver);
	}

}
