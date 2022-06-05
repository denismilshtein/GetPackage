package sanitySuite;

import org.base.TestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CreateDeliveryPage;
import pages.LoginPage;

public class CreateDeliveryTest extends TestBase {

    LoginPage login;
    CreateDeliveryPage createDelivery;

    @BeforeMethod
    public void beforeMethod(){
        login = new LoginPage(driver);
    }

    @Test(priority=5, description = "Navigate to create delivery page")
    public void  login_to_real_account_and_navigate_to_create_delivery() throws InterruptedException {
        log.info("login to real user account and navigate to create delivery page");
        Thread.sleep(1000);
        createDelivery = login.goToCreateDelivery();
        Thread.sleep(1000);
        assertCondition(createDelivery.isUrlOk());
        log.info("Perform base validation on first loading");
        assertCondition(createDelivery.basePageValidation());
    }

    /* covered in above test
    @Test (priority=6, description = "Create delivery base page validation", dependsOnMethods = { "login_to_real_account_and_navigate_to_create_delivery" })
    public void  verify_base_elements_presence_in_create_delivery() {
        log.info("Navigate to create delivery page and do base validation on first loading");
        createDelivery = login.goToCreateDelivery();
        assertCondition(createDelivery.basePageValidation());
    }*/
}
