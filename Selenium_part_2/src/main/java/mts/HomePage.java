package mts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By cookieAgreeButton = By.id("cookie-agree");
    private By phoneInput = By.id("connection-phone");
    private By amountInput = By.id("connection-sum");
    private By continueButton = By.xpath("//button[text()='Продолжить']");
    private By selectHeader = By.cssSelector(".select__header");
    private By selectList = By.cssSelector(".select__list");
    private By selectOptions = By.cssSelector(".select__option");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40)); // Увеличено время ожидания
    }

    public void acceptCookies() {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement cookieAgreeButtonElement = wait.until(ExpectedConditions.elementToBeClickable(cookieAgreeButton));
                cookieAgreeButtonElement.click();
                break;
            } catch (Exception e) {
                driver.navigate().refresh();
            }
        }
    }

    public void fillPhoneAndAmount(String phone, String amount) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput)).sendKeys(phone);
        wait.until(ExpectedConditions.visibilityOfElementLocated(amountInput)).sendKeys(amount);
    }

    public void clickContinue() {
        WebElement continueButtonElement = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueButtonElement.click();
    }

    public void openSelectDropdown() {
        WebElement selectHeaderElement = wait.until(ExpectedConditions.elementToBeClickable(selectHeader));
        selectHeaderElement.click();
    }

    public void selectOption(String optionText) {
        openSelectDropdown();
        WebElement selectListElement = wait.until(ExpectedConditions.visibilityOfElementLocated(selectList));
        WebElement optionElement = selectListElement.findElement(By.xpath(".//p[text()='" + optionText + "']"));
        wait.until(ExpectedConditions.elementToBeClickable(optionElement)).click();
    }

    public String getSelectedOptionText() {
        WebElement selectHeaderElement = wait.until(ExpectedConditions.visibilityOfElementLocated(selectHeader));
        return selectHeaderElement.findElement(By.cssSelector(".select__now")).getText();
    }

    public String getPlaceholderText(String inputId) {
        WebElement inputElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(inputId)));
        return inputElement.getAttribute("placeholder");
    }
}