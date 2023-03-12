package pageObjects;

import browserFactory.appConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.IOException;

public class workspacePage extends appConfig {

    WebDriver driver= appConfig.driver;
    appConfig objBaseSetup = new appConfig();

    public void navToWorkspace(){
        driver.findElement(By.xpath("(//div[@class='c-side-panel__menu-icon '])[5]")).click();
    }

    public void addWidgets() throws InterruptedException, IOException, AWTException {
        objBaseSetup.waitForHighTime();

        driver.findElement(By.xpath("//div[@rel='globalLayout']")).click();
        objBaseSetup.widgetAdd(widgets.getProperty("advancedChart"));
        objBaseSetup.flexWidgetSymbolSearch(widgets.getProperty("advancedChart"),"EURUSD","KRAKEN");
        objBaseSetup.flexMaximize();
        objBaseSetup.minimize();
        objBaseSetup.getHeader();
        objBaseSetup.setHeader();
        //objBaseSetup.flexWidgetClose();
        objBaseSetup.widgetAdd(widgets.getProperty("news"));
        objBaseSetup.flexWidgetClose();
        //objBaseSetup.widgetNewPopOutWindow(widgets.getProperty("advancedChart"));

        objBaseSetup.takeTestEvidenceAtTheEndOfTheTest();
    }

    public void wrongXpath() throws IOException {

        try {
            driver.findElement(By.xpath("//button[@rel='dropDownButtonabcd']")).click();
        } catch (Exception e) {
            //error screenshot

            objBaseSetup.takeErrorScreenshot();
            objBaseSetup.findErrors();
        }
    }
}
