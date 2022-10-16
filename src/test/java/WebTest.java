import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

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
        driver = new ChromeDriver();
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearsDown() {
        driver.quit();
        driver = null;
    }

    @Test
    public void sendFormPositive() throws InterruptedException {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мак-Кинли Смит");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        Thread.sleep(2000); //ставлю остановки для своего удобства
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id=\"order-success\"]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", message.trim());
        Thread.sleep(2000);
    }

    @Test
    public void sendInvalidName() throws InterruptedException {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Vladimir");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String wrong = driver.findElement(By.className("input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", wrong.trim());
        Thread.sleep(2000);
    }

    @Test
    public void sendWithoutTel() throws InterruptedException {
        //List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Владимир");
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String wrongTel = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", wrongTel.trim());
        Thread.sleep(2000);
    }

    @Test
    public void sendWithoutName() throws InterruptedException {
        //List<WebElement> list = driver.findElements(By.className("input__sub"));
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String wrongTel = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", wrongTel.trim());
        Thread.sleep(2000);
    }

    @Test
    public void sendInvalidTel() throws InterruptedException {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мак-Кинли Смит");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+799999");
        Thread.sleep(2000); //ставлю остановки для своего удобства
        driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).click();
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        String message = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", message.trim());
        Thread.sleep(2000);
    }

    @Test
    public void withoutAgree() throws InterruptedException {
        driver.findElement(By.cssSelector("[type=\"text\"]")).sendKeys("Мак-Кинли Смит");
        driver.findElement(By.cssSelector("[type=\"tel\"]")).sendKeys("+79999999990");
        Thread.sleep(2000); //ставлю остановки для своего удобства
        driver.findElement(By.cssSelector("[type=\"button\"]")).click();
        assertTrue(driver.findElement(By.cssSelector("[data-test-id=\"agreement\"]")).isDisplayed());
        Thread.sleep(2000);
    }
}
