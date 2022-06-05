package pages;

import org.base.PageBase;
import org.base.TestBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.selenium.enums.WaitStrategy;


public class CreateDeliveryPage extends PageBase {

    public CreateDeliveryPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        isLoaded();
        log.info("Create Delivery page is successfully loaded");
    }



    /*******************************************************************************************
     * All WebElements and Page locators Identification
     *******************************************************************************************/



    private final By sideBorderTitle = By.cssSelector(".gp-side-border-title");
    private final By pickUpPoint = By.cssSelector("input[formcontrolname='pickUpPoint']");
    private final By packageSize = By.cssSelector(".packCube.safariHotFix.gp-max-width");
    private final By serviceType = By.cssSelector("div .gp-mt-4.safariHotFix");


    /*******************************************************************************************
     * All Methods for performing actions
     *******************************************************************************************/

    public Boolean isLoaded() {
        return wait.until(webDriver -> ExpectedConditions.textToBe(sideBorderTitle, "הזמנת משלוח").apply(webDriver));
    }

    public Boolean isUrlOk() {return wait.until(webDriver -> ExpectedConditions.urlToBe(TestBase.data.getProperty("base.url") + "create-delivery").apply(webDriver));}

    public CreateDeliveryPage load() {
        load("create-delivery");
        isLoaded();
        log.info("Create Delivery page is successfully loaded");
        return this;
    }



    //verify address and package size are visible right after page is loaded, service type block is not present
    public boolean basePageValidation() {
        return isElementExists(WaitStrategy.VISIBLE, pickUpPoint, "Address field") &&
               isElementExists(WaitStrategy.VISIBLE, packageSize, "Package size block") &&
               !isElementExists(WaitStrategy.VISIBLE, serviceType, "Service type selection block");
    }
}