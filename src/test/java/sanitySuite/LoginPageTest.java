package sanitySuite;

import com.testautomationguru.ocular.Ocular;
import org.base.TestBase;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import java.nio.file.Path;
import java.nio.file.Paths;


public class LoginPageTest extends TestBase {

	LoginPage login;
	String dimension = "1220x880";
	String browserForName;

	@BeforeMethod
	public void beforeMethod(){
		login = new LoginPage(driver);

	}

	@Test (priority=1, description = "Not valid email test case")
	public void wrong_email_login_attempt() {
		log.info("trying to login with fake email");
		login.load().clickEmailAndPasswordLoginLink().enterEmail(data.getProperty("not.valid.email"));
		assertStrings(login.getInputErrorMessage(),"* כתובת האימייל אינה תקינה");
		login.enterEmail(data.getProperty("fake.email")).enterPassword(data.getProperty("real.password")).singIn();
		assertCondition(login.getGeneralLoginError().contains("אירעה שגיאה.. אנא נסו שנית"));
	}

	@Test (priority=2, description = "Wrong password test case")
	public void  wrong_password_login_attempt() {
		log.info("trying to login without password, then with wrong password");
		login.load().clickEmailAndPasswordLoginLink().enterEmail(data.getProperty("real.email")).singIn();
		assertStrings(login.getInputErrorMessage(),"* נדרש");
		login.enterPassword(data.getProperty("fake.password")).singIn();
		assertCondition(login.getGeneralLoginError().contains("אירעה שגיאה.. אנא נסו שנית"));
	}

	@Test (priority=4, description = "Real login test case")
	public void  real_email_and_password_login_verify() {
		log.info("trying to login with real email and password");
		login
				.load()
				.clickEmailAndPasswordLoginLink()
				.enterEmail(data.getProperty("real.email"))
				.enterPassword(data.getProperty("real.password"))
				.singIn();

		assertCondition(login.isHomeOk());
	}

	@Test (priority=3, description = "Responsive design test")
	public void  responsive_login_page_test() {
		log.info("set source and result directories for responsive design test of login page");
		browserForName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		Path snapShotPath = Paths.get("./src/test/resources/source/" + dimension,  browserForName);
		Ocular.config()
				.snapshotPath(snapShotPath)
				.resultPath(Paths.get("./src/test/resources/result/" + dimension,  browserForName))
				.globalSimilarity(95);
		log.info("set dimension for responsive design test of login page");
		int width = Integer.parseInt(dimension.split("x")[0]);
		int height = Integer.parseInt(dimension.split("x")[1]);
		driver.manage().window().setSize(new Dimension(width, height));
		login.load();
		Assert.assertTrue(login.compare().isEqualsImages());
	}
}
