import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebTest {
    private WebDriver driver;


    @BeforeAll
    static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUpTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        options.addArguments("--remote-debugging-port=9999");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearsDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void sendFormPositive() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мак-Кинли Смит");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id=\"order-success\"]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.trim());
    }

    @Test
    public void sendInvalidName() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Vladimir");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String wrong = driver.findElement(By.className("input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", wrong.trim());
    }

    @Test
    public void sendWithoutTel() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Владимир");
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String wrongTel = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", wrongTel.trim());
    }

    @Test
    public void sendWithoutName() {
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String wrongName = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", wrongName.trim());
    }

    @Test
    public void sendInvalidTel() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мак-Кинли Смит");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+799999");
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message.trim());
    }

    @Test
    public void withoutAgree() {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мак-Кинли Смит");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).isDisplayed());
    }
}
