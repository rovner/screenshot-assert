package com.github.rovner.screenshot.assertions.core.ignoring;

import org.openqa.selenium.WebDriver;

/**
 * Service interface to set up web driver in ignorings that requires web driver.
 */
public interface WebDriverInit {

    /**
     * Initializes web driver.
     *
     * @param webDriver driver
     */
    void initWebDriver(WebDriver webDriver);
}
