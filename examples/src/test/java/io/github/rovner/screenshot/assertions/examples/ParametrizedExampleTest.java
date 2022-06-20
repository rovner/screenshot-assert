package io.github.rovner.screenshot.assertions.examples;

import io.github.rovner.screenshot.assertions.core.screenshot.Screenshots;
import io.github.rovner.screenshot.assertions.junit.ScreenshotAssertExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;

@Epic("Screenshot asserts")
@Feature("Parametrized tests")
public class ParametrizedExampleTest {
    private WebDriver wd;

    @RegisterExtension
    private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd);

    @BeforeEach
    void beforeEach() throws IOException {
        wd = new RemoteWebDriver(new URL("http://localhost:9515/"), new ChromeOptions());
        wd.manage().window().setSize(new Dimension(800, 600));
        wd.get("https://github.com/");
    }

    @AfterEach
    void afterEach() {
        wd.quit();
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = {1, 2})
    @DisplayName("Screenshot for parametrized tests")
    void testParametrized(int id) {
        screenshotAssert.assertThat(Screenshots.screenshotOfWholePage())
                .isEqualToReferenceId("full_page");
    }
}
