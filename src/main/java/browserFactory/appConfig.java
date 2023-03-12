package browserFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.ScreenshotException;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Properties;

public class appConfig {
    public static WebDriver driver;
    public static Properties widgets;

    public void waitForHighTime(){
        try{Thread.sleep(10000);}
        catch(InterruptedException e){e.getMessage();}
    }

    public void waitForLowerTime(){
        try{Thread.sleep(3500);}
        catch(InterruptedException e){e.getMessage();}
    }

    public WebDriver setup(){

        WebDriverManager.chromedriver().setup();
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable notifications");
        DesiredCapabilities cp= new DesiredCapabilities();
        cp.setCapability(ChromeOptions.CAPABILITY, options);
        options.merge(cp);
        driver = new ChromeDriver(options);
        return driver;
    }

    static {
        initializeProperties();
    }

    private static void initializeProperties() {
        try {
            widgets = new Properties();
            FileInputStream ip2 = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/widgets.properties");
            widgets.load(ip2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeTestEvidenceAtTheEndOfTheTest() throws IOException {
        try{
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String currentDir = System.getProperty("user.dir");
            FileUtils.copyFile(scrFile, new File(currentDir + "/testEvidence/" + System.currentTimeMillis() + ".png"));
            System.out.println("=================== Test Evidence ========================");
            System.out.println("Saved to"+" "+currentDir+"/testEvidence");
            System.out.println("==========================================================");
        }catch (ScreenshotException e) {e.printStackTrace(); throw new RuntimeException ("It is impossible to take the test evidence");}
    }

    public void takeErrorScreenshot() throws IOException {
        try{File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String currentDir = System.getProperty("user.dir");
            FileUtils.copyFile(scrFile, new File(currentDir + "/errorScreenshots/" + System.currentTimeMillis() + ".png"));
            System.out.println("=================== Error Screenshot ========================");
            System.out.println("Saved to"+" "+currentDir+"/errorScreenshots");
            System.out.println("=============================================================");
        }catch (ScreenshotException e) {e.printStackTrace(); throw new RuntimeException ("It is impossible to capture the error area");}
    }

    public void findErrors() {
        System.out.println("================== Errors ===========================");
        //waitForLowerTime();
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        for (LogEntry entry : logEntries) {
            System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }System.out.println("=======================================================");
    }

    public void widgetAdd(String widgetName) throws AWTException, IOException, InterruptedException {
        waitForLowerTime();
        //isWidgetNameNullOrEmpty(widgetName);
        driver.findElement(By.xpath("//div[@rel='topHrMenu']")).click();
        waitForLowerTime();
        driver.findElement(By.xpath("//button[@rel='dropDownButton']")).click();
        try{driver.findElement(By.xpath( "//*[@rel='"+widgetName+"']")).isDisplayed();
            Actions action = new Actions(driver);

            //action.moveToElement(driver.findElement(By.xpath( "//*[@class='btn-group btn-widget-library open']//*[@rel='"+widgetName+"']")))
            action.moveToElement(driver.findElement(By.xpath("//ul[@class='c-widget-lib__list']//*[@rel='"+widgetName+"']")))
                    .click()
                    .pause(2000)
                    .clickAndHold()
                    .moveByOffset(-700,50)
                    .pause(2000)
                    .release().build().perform();
            waitForLowerTime();
            if(driver.findElement(By.xpath( "//*[@widget-type='"+widgetName+"']")).isDisplayed()){
                takeTestEvidenceAtTheEndOfTheTest();
                //waitForHighTime();
                System.out.println("Drag & drop"+" "+widgetName+" "+"widget: Pass");
            }else {throw new NoSuchElementException("Drag & drop"+" "+widgetName+" "+"widget: Fail");}
        }catch (NoSuchElementException e){e.printStackTrace(); throw new RuntimeException(widgetName+" "+"widget is not available in the widget library!");}
    }

    public void flexWidgetClose() throws IOException {
        String parentWindow = driver.getWindowHandle();

        try {driver.findElement(By.xpath("//*[@class='lm_controls']//*[@class='lm_close']")).isDisplayed();
            driver.findElement(By.xpath("//*[@class='lm_controls']//*[@class='lm_close']")).click();
        }catch (NoSuchElementException e){e.printStackTrace(); throw new RuntimeException("widget close option is not available!");}

    }

    public void flexMaximize() {
        try {
            driver.findElement(By.xpath("//li[@class='lm_maximise']")).isDisplayed();
            driver.findElement(By.xpath("//li[@class='lm_maximise']")).click();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new RuntimeException("widget maximize option is not available!");
        }
    }

    public void minimize(){
        try{
            driver.findElement(By.xpath("//button[@rel='fullScreenMaximize']")).isDisplayed();
            driver.findElement(By.xpath("//button[@rel='fullScreenMaximize']")).click();
        }catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new RuntimeException("widget minimize option is not available!");
        }
    }

    public void gridWindMaximize(){
        try{
            driver.findElement(By.xpath("//li[@ng-show=\"showFullScreenCtrl === 'true'\"]")).isDisplayed();
            driver.findElement(By.xpath("//li[@ng-show=\"showFullScreenCtrl === 'true'\"]")).click();
        }catch (NoSuchElementException e) {
            e.printStackTrace();
            throw new RuntimeException("widget maximize option is not available!");}

    }
    public void flexWidgetSymbolSearch(String widgetName, String pair, String exchange) throws IOException, InterruptedException {

        try{driver.findElement(By.xpath("//*[@class='lm_controls']//*[@rel='searchControl']")).isDisplayed();
            driver.findElement(By.xpath("//*[@class='lm_controls']//*[@rel='searchControl']")).click();
            if (driver.findElement(By.xpath("//input[@rel='symbolInput']")).isDisplayed()){
                driver.findElement(By.xpath("//input[@rel='symbolInput']")).clear();
                driver.findElement(By.xpath("//input[@rel='symbolInput']")).sendKeys(pair+"."+exchange);
                waitForLowerTime();
                takeTestEvidenceAtTheEndOfTheTest();
                Thread.sleep(3500);
                driver.findElement(By.xpath("//*[@rel='result_0']")).click();
                waitForHighTime();

                if (driver.findElement(By.xpath("//div[@widget-header]")).getText().contains(pair+"."+exchange)){
                    System.out.println(widgetName+" "+"widget symbol search: Pass");

              /*  if (driver.findElement(By.xpath("//*[@widget-type='"+widgetName+"']//*[@rel='headerTxt']")).getText().contains(pair+"."+exchange)){
                    System.out.println(widgetName+" "+"widget symbol search: Pass");*/
                }else {throw new InvalidArgumentException(widgetName+" "+"widget header is not changed according to the searched symbol!");}
            }else {throw  new NoSuchElementException(widgetName+" "+"widget search field is not available!");}
        }catch (NoSuchElementException e){e.printStackTrace(); throw new RuntimeException(widgetName+" "+"widget search option is not available!");}
    }

    public void gridWindWidgetSymbolSearch(String widgetName, String pair, String exchange) throws IOException, InterruptedException {

        try{driver.findElement(By.xpath("//li[@rel='searchControl']")).isDisplayed();
            driver.findElement(By.xpath("//li[@rel='searchControl']")).click();
            if (driver.findElement(By.xpath("//input[@rel='symbolInput']")).isDisplayed()){
                driver.findElement(By.xpath("//input[@rel='symbolInput']")).clear();
                driver.findElement(By.xpath("//input[@rel='symbolInput']")).sendKeys(pair+"."+exchange);
                waitForLowerTime();
                takeTestEvidenceAtTheEndOfTheTest();
                Thread.sleep(3500);
                driver.findElement(By.xpath("//*[@rel='result_0']")).click();
                waitForHighTime();

                if (driver.findElement(By.xpath("//div[@widget-header]")).getText().contains(pair+"."+exchange)){
                    System.out.println(widgetName+" "+"widget symbol search: Pass");

              /*  if (driver.findElement(By.xpath("//*[@widget-type='"+widgetName+"']//*[@rel='headerTxt']")).getText().contains(pair+"."+exchange)){
                    System.out.println(widgetName+" "+"widget symbol search: Pass");*/
                }else {throw new InvalidArgumentException(widgetName+" "+"widget header is not changed according to the searched symbol!");}
            }else {throw  new NoSuchElementException(widgetName+" "+"widget search field is not available!");}
        }catch (NoSuchElementException e){e.printStackTrace(); throw new RuntimeException(widgetName+" "+"widget search option is not available!");}
    }

    public void getHeader(){
        String header = driver.findElement(By.xpath("//*[@rel='tabTitleWrapper']")).getText();
        if (header.contains("EURUSD")){
            System.out.println("Header text contains "+header);
        }else{
            throw new InvalidArgumentException("Header does not contain the expected text");
        }
    }

    public void setHeader() {
        if (driver.findElement(By.xpath("(//span[@rel=\"tabTitle\"])[2]")).isDisplayed()) {
            Actions action = new Actions(driver);
            action.moveToElement(driver.findElement(By.xpath("(//span[@rel=\"tabTitle\"])[2]")))
                    .doubleClick()
                    .sendKeys(Keys.BACK_SPACE)
                   /* .keyDown(Keys.CONTROL)
                    .sendKeys("a")
                    .keyUp(Keys.CONTROL)
                    .sendKeys(Keys.BACK_SPACE)*/
                    .sendKeys("New Header")
                    .build()
                    .perform();

        }else{
            throw new InvalidArgumentException("Header is not set");
        }
    }

    public void tearDown(){driver.quit();}

}
