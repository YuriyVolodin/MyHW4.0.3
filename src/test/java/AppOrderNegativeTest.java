import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.jupiter.api.Assertions.*;

public class AppOrderNegativeTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void shouldShowErrorIfNameEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"))
                .getText().trim();

        assertEquals("Поле обязательно для заполнения", errorText);
    }

    @Test
    void shouldShowErrorIfNameInLatin() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Ivan Ivanov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"))
                .getText().trim();

        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorText);
    }

    @Test
    void shouldShowErrorIfPhoneInvalid() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("89991234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"))
                .getText().trim();

        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например +79012345678.", errorText);
    }

    @Test
    void shouldShowErrorIfCheckboxNotSelected() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79991234567");
        driver.findElement(By.cssSelector("button.button")).click();

        boolean isErrorDisplayed = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"))
                .isDisplayed();

        assertTrue(isErrorDisplayed);
    }
}
