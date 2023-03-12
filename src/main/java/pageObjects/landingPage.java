package pageObjects;


import browserFactory.appConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class landingPage {
    WebDriver driver= appConfig.driver;
    appConfig objBaseSetup= new appConfig();
    By btnLogin = By.xpath("//a[@class='btn c-btn c-btn--brand'][contains(text(),'Login')]");
    By txtUsrN = By.xpath("//input[@name='username']");
    By txtPswrd = By.xpath("//input[@name='password']");
    By btnLogin2 = By.xpath("//button[@name='login']");


    public String navToItrade() {
        driver.get("https://itrade.dev.xinfinit.com/");
        driver.manage().window().maximize();
        String title = driver.getTitle();
        return title;
    }

    public String clickLogin() {
        //driver.findElement(By.xpath("//a[@class='btn c-btn c-btn--brand'][contains(text(),'Login')]")).click();
        driver.findElement(btnLogin).click();
        String loginTitle = driver.getTitle();
        return loginTitle;
    }
    public String login() throws InterruptedException {
        driver.findElement(txtUsrN).sendKeys("eshan.herath@xinfinit.com");
        driver.findElement(txtPswrd).sendKeys("1qaz2wsx@A");
        driver.findElement(btnLogin2).click();
        objBaseSetup.waitForLowerTime();
        //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(260));
        String loggedInTitle = driver.getTitle();
        return loggedInTitle;
    }
}
