package mts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PaymentPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Локаторы для элементов на странице оплаты
    private By cardNumberInput = By.cssSelector("input#cc-number");
    private By cvcInput = By.cssSelector("input[formcontrolname='cvc']");
    private By expirationDateInput = By.cssSelector("input.date-input");
    private By cardHolderInput = By.cssSelector("input[autocomplete='cc-name']");
    private By paymentForm = By.xpath("//div[contains(@class, 'card-page__card')]");
    private By amountDisplay = By.cssSelector(".pay-description__cost span");
    private By phoneNumberDisplay = By.cssSelector(".pay-description__text span");
    private By cardNumberLabel = By.cssSelector("label.ng-tns-c46-1");
    private By expirationDateLabel = By.cssSelector("label.ng-tns-c46-4");
    private By cvcLabel = By.cssSelector("label.ng-tns-c46-5");
    private By cardHolderLabel = By.cssSelector("label.ng-tns-c46-3");
    private By paymentIcons = By.cssSelector(".cards-brands__container img");

    public PaymentPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Увеличено время ожидания
    }

    public void switchToPaymentFrame() {
        WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("iframe.bepaid-iframe")));
        driver.switchTo().frame(iframe);
    }

    public boolean isCardNumberInputDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberInput)).isDisplayed();
    }

    public boolean isCvcInputDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cvcInput)).isDisplayed();
    }

    public boolean isExpirationDateInputDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(expirationDateInput)).isDisplayed();
    }

    public boolean isCardHolderInputDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cardHolderInput)).isDisplayed();
    }

    public boolean isPaymentFormDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(paymentForm)).isDisplayed();
    }

    // Методы для проверки отображения суммы и номера телефона
    public String getAmountDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(amountDisplay)).getText();
    }

    public String getPhoneNumberDisplayed() {
        String fullText = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneNumberDisplay)).getText();
        // Извлекаем только цифры из строки
        return fullText.replaceAll("[^0-9]", "");
    }

    // Методы для проверки надписей над полями ввода
    public String getCardNumberLabelText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cardNumberLabel)).getText();
    }

    public String getExpirationDateLabelText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(expirationDateLabel)).getText();
    }

    public String getCvcLabelText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cvcLabel)).getText();
    }

    public String getCardHolderLabelText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cardHolderLabel)).getText();
    }

    // Метод для проверки наличия иконок платежных систем
    public boolean arePaymentIconsDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(paymentIcons)).size() > 0;
    }
}