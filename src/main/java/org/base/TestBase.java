package org.base;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.utils.TestContext;


public abstract class TestBase {

	public static Properties data=null;
	private static boolean isInitalized=false;
	public static Logger log = null;
	public static WebDriver driver = null;
	//public static WebDriverWait wait = null;
	private static FileInputStream ip = null;

	protected TestBase() {
		if(!isInitalized){
			initLogs();
			initConfig();
		}
	}

	/**
	 * Initialize Logger.
	 */
	private static void initLogs(){
		if (log == null){
			// Initialize Log4j logs
			DOMConfigurator.configure(System.getProperty("user.dir")+File.separator+"config"+File.separator+"log4j.xml");
			log = Logger.getLogger("Get Package Logger");
			log.info("Logger is initialized..");
		}
	}


	private static void initConfig() {
		if (data == null) {
			try {
				//initialize data properties file
				data = new Properties();
				String data_fileName = "data.properties";
				String data_path = System.getProperty("user.dir") + File.separator+ "config" + File.separator + data_fileName;
				FileInputStream data_ip = new FileInputStream(data_path);
				data.load(data_ip);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Initialize Driver.
	 */
	@BeforeMethod(alwaysRun = true)
	@Parameters({"browser"})
	public void init(@Optional ("CHROME") String browser) {this.initDriver(browser);}
	public void initDriver(String browser) {
		if(browser.equalsIgnoreCase("FIREFOX") ){
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+ File.separator+"drivers"+ File.separator  +"geckodriver.exe");
			driver = new FirefoxDriver();
			log.info(browser + " driver is initialized..");
		}
		else if (browser.equals("CHROME")){
			System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+ File.separator +"drivers"+ File.separator +"chromedriver.exe");
			// To remove message "You are using an unsupported command-line flag: --ignore-certificate-errors.
			// Stability and security will suffer."
			// Add an argument 'test-type'
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			//options.addArguments("--headless", "window-size=1280,1024", "--no-sandbox"); // Enable for headless option
			driver = new ChromeDriver(options);
			log.info(browser + " driver is initialized..");
		}else if (browser.equalsIgnoreCase("EDGE")) {
			// Set the driver path
			System.setProperty("webdriver.edge.driver",System.getProperty("user.dir")+ File.separator +"drivers"+ File.separator +"msedgedriver.exe");
			// Start Edge Session
			driver = new EdgeDriver();
			log.info(browser + " driver is initialized..");
		}

		String waitTime = "30";
		driver.manage().timeouts().implicitlyWait(Long.parseLong(waitTime), TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();

	}


	
	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		quitDriver();
	}

	/**
	 * Read Properties.
	 */
	protected static String getPropertyValue(String PropertyKey){
		Properties props=null;
		FileInputStream fin =null;
		String PropertyValue = null;

		try {
			File f = new File(System.getProperty("user.dir")+File.separator+"config"+File.separator+"env.properties");
			fin = new FileInputStream(f);
			props = new Properties();
			props.load(fin);
			PropertyValue = props.getProperty(PropertyKey);
		} catch(Exception e){
			System.out.println(e.getMessage());
		} 

		return PropertyValue;
	}


	/**
	 * Define path for Screenshot file.
	 */
	protected String getScreenshotSavePath() {
		String packageName = this.getClass().getPackage().getName();
		File dir = new File(System.getProperty("user.dir")+File.separator+"screenshot"+File.separator + packageName + File.separator);
		dir.mkdirs();
		return dir.getAbsolutePath();
	}


	/**
	 * Take Screenshot on failure.
	 */
	protected void getScreenshot() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String date = sdf.format(new Date());
		String url = driver.getCurrentUrl().replaceAll("[\\/:*\\?\"<>\\|]", "_");
		String ext = ".png";
		String path = getScreenshotSavePath() + File.separator + date + "_" + url + ext;

		try {
			if (driver instanceof TakesScreenshot) {
				File tmpFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				org.openqa.selenium.io.FileHandler.copy(tmpFile, new File(path));
				log.error("Captured Screenshot for Failure: "+path);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * Assert Actual and Expected Strings
	 */

	protected void assertStrings(String actual, String expected){
		try{
			Assert.assertEquals(actual, expected);
			log.info("Actual string: [ "+actual+" ] does match with Expected string: [ "+expected+" ]");		

		}catch(AssertionError e){
			log.error("Actual string: [ "+actual+" ] does not match with Expected string: [ "+expected+" ]");
			getScreenshot();
			Assert.fail();
		}
	}

	/**
	 * Assert Condition
	 */

	protected void assertCondition(Boolean condition){
		try{
			Assert.assertTrue(condition);
			log.info("Given Condition: is true");

		}catch(AssertionError e){
			log.error("Given Condition: is false");
			getScreenshot();
			Assert.fail();
		}
	}

	/**
	 * Quit Driver.
	 */
	public static void quitDriver() {
		driver.quit();
		log.info("Closing Browser.");
	}

}
