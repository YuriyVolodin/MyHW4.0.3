package ru.netology;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;

import static org.junit.jupiter.api.Assertions.*;

public class AppOrderNegativeTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    @DisplayName("Ошибка при пустом поле имени")
    void shouldShowErrorIfNameEmpty() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79995552233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals("Поле обязательно для заполнения", errorText);
    }

    @Test
    @DisplayName("Ошибка при вводе имени латиницей")
    void shouldShowErrorIfNameInvalid() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Aleksey Popov");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79995552233");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText().trim();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorText);
    }

    @Test
    @DisplayName("Ошибка при неверном номере телефона")
    void shouldShowErrorIfPhoneInvalid() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Игорь Попов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("12345");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.className("button")).click();

        String errorText = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText().trim();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorText);
    }

    @Test
    @DisplayName("Ошибка при неустановленном флажке согласия")
    void shouldShowErrorIfNoAgreement() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сергей Петров");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79995552233");
        driver.findElement(By.className("button")).click();

        WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid"));
        assertTrue(checkbox.isDisplayed());
    }
}