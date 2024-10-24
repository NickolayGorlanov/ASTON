package mts;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MtsByTest {

    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeAll
    public static void setUp() {

        io.github.bonigarcia.wdm.WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testPaymentSystemLogos() {
        driver.get("https://mts.by");
        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();

        WebElement visaLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pay-section']//img[@alt='Visa']")));
        WebElement mastercardLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pay-section']//img[@alt='MasterCard']")));
        WebElement belkartLogo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='pay-section']//img[@alt='Белкарт']")));
        assertNotNull(visaLogo);
        assertNotNull(mastercardLogo);
        assertNotNull(belkartLogo);
    }

    @Test
    public void testMoreAboutServiceLink() {
        driver.get("https://mts.by");
        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();

        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Подробнее о сервисе")));
        assertNotNull(linkElement);
        linkElement.click();

        WebElement breadcrumbElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@itemprop='name' and text()='Порядок оплаты и безопасность интернет платежей']")));
        assertNotNull(breadcrumbElement);
        assertEquals("Порядок оплаты и безопасность интернет платежей", breadcrumbElement.getText());
    }

    @Test
    public void testContinueButton() {
        driver.get("https://mts.by");
        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();

        homePage.fillPhoneAndAmount("297777777", "100");
        homePage.clickContinue();

        checkForNewWindowOrIframe();

        PaymentPage paymentPage = new PaymentPage(driver);
        paymentPage.switchToPaymentFrame();

        assertTrue(paymentPage.isCardNumberInputDisplayed(), "Поле ввода номера карты не отображается");
        assertTrue(paymentPage.isCvcInputDisplayed(), "Поле ввода CVC не отображается");
        assertTrue(paymentPage.isExpirationDateInputDisplayed(), "Поле ввода срока действия карты не отображается");
        assertTrue(paymentPage.isCardHolderInputDisplayed(), "Поле ввода имени держателя карты не отображается");

        assertTrue(paymentPage.isPaymentFormDisplayed(), "Форма оплаты не видна на странице");


        assertEquals("100.00 BYN", paymentPage.getAmountDisplayed(), "Сумма отображается некорректно");
        assertEquals("375297777777", paymentPage.getPhoneNumberDisplayed(), "Номер телефона отображается некорректно");


        assertEquals("Номер карты", paymentPage.getCardNumberLabelText(), "Надпись над полем номера карты отображается некорректно");
        assertEquals("Срок действия", paymentPage.getExpirationDateLabelText(), "Надпись над полем срока действия карты отображается некорректно");
        assertEquals("CVC", paymentPage.getCvcLabelText(), "Надпись над полем CVC отображается некорректно");
        assertEquals("Имя держателя (как на карте)", paymentPage.getCardHolderLabelText(), "Надпись над полем имени держателя карты отображается некорректно");


        assertTrue(paymentPage.arePaymentIconsDisplayed(), "Иконки платежных систем не отображаются");

        driver.switchTo().defaultContent();
    }

    @Test
    public void testSelectOptionsAndPlaceholders() {
        driver.get("https://mts.by");
        HomePage homePage = new HomePage(driver);
        homePage.acceptCookies();

        String[] options = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};
        String[][] placeholders = {
                {"connection-phone", "Номер телефона"},
                {"internet-phone", "Номер абонента"},
                {"score-instalment", "Номер счета на 44"},
                {"score-arrears", "Номер счета на 2073"}
        };

        for (int i = 0; i < options.length; i++) {
            homePage.selectOption(options[i]);
            assertEquals(options[i], homePage.getSelectedOptionText(), "Выбранный пункт не соответствует ожидаемому");

            for (int j = 0; j < placeholders[i].length; j += 2) {
                assertEquals(placeholders[i][j + 1], homePage.getPlaceholderText(placeholders[i][j]), "Placeholder не соответствует ожидаемому");
            }
        }
    }

    private void checkForNewWindowOrIframe() {
        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        if (allWindows.size() > 1) {
            for (String windowHandle : allWindows) {
                if (!originalWindow.contentEquals(windowHandle)) {
                    driver.switchTo().window(windowHandle);
                    break;
                }
            }
        } else {
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            System.out.println("Found " + iframes.size() + " iframes on the page after clicking 'Продолжить'.");
            for (WebElement iframe : iframes) {
                System.out.println("Iframe src: " + iframe.getAttribute("src"));
                if (iframe.getAttribute("src").contains("checkout.bepaid.by")) {
                    driver.switchTo().frame(iframe);
                    break;
                }
            }
        }
    }
}