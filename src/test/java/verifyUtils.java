import browserFactory.appConfig;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pageObjects.landingPage;
import pageObjects.workspacePage;

import java.awt.*;
import java.io.IOException;

public class verifyUtils extends appConfig {

    appConfig base= new appConfig();
    landingPage land;
    workspacePage wp;

    @BeforeTest
    public void initDriver(){
        base.setup();
    }

    @Test(priority = 0)
    public void verifyLanding(){
        land=new landingPage();
        land.navToItrade();
    }
    @Test(priority=1, dependsOnMethods = {"verifyLanding"})
    public void verifyNavLogin(){
        land.clickLogin();
    }

    @Test(priority=2, dependsOnMethods = {"verifyLanding"})
    public void verifyLogin() throws InterruptedException{land.login();}

    @Test(priority=3, dependsOnMethods = {"verifyLogin"})
    public void navWorkspace() throws InterruptedException, IOException, AWTException {
        workspacePage workspace = new workspacePage();
        workspace.navToWorkspace();
        workspace.addWidgets();
        workspace.wrongXpath();
    }

}
