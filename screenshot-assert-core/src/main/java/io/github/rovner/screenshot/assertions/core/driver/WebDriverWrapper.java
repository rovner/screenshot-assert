package io.github.rovner.screenshot.assertions.core.driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.ImageUtils.toBufferedImage;
import static org.openqa.selenium.OutputType.BYTES;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

/**
 * Web driver wrapper aimed to simplify some web driver interactions like executing scripts or taking screenshots
 */
@Slf4j
public class WebDriverWrapper {

    private final WebDriver webDriver;

    public WebDriverWrapper(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Returns unwrapped web driver.
     *
     * @return unwrapped web driver.
     */
    public WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * Takes screenshot and converts it to buffered image.
     *
     * @return screenshot
     */
    public BufferedImage takeScreenshot() {
        disableCaretBlinking();
        return toBufferedImage(((TakesScreenshot) webDriver).getScreenshotAs(BYTES));
    }

    /**
     * Executes javascript
     *
     * @param script    javascript to execute.
     * @param arguments script arguments.
     * @param <T>       type of script result.
     * @return script result.
     */
    public <T> T executeScript(String script, Object... arguments) {
        if (!(webDriver instanceof JavascriptExecutor)) {
            throw new IllegalStateException(String.format("Web Driver implementation %s does not implement %s",
                    webDriver.getClass().getCanonicalName(), JavascriptExecutor.class.getCanonicalName()));
        }
        //noinspection unchecked
        return (T) ((JavascriptExecutor) webDriver).executeScript(script, arguments);
    }

    /**
     * Returns browser version from capabilities
     *
     * @return browser version
     */
    public String getBrowserVersion() {
        return getCapabilities().getBrowserVersion();
    }

    /**
     * Returns browser name from capabilities.
     *
     * @return browser name.
     */
    public String getBrowserName() {
        return getCapabilities().getBrowserName();
    }

    /**
     * Returns device name (such as iPad or iPhone) from capabilities.
     *
     * @return device name.
     */
    public String getDeviceName() {
        return (String) getCapabilities().getCapability("deviceName");
    }

    /**
     * Returns platform (such as windows, android or ios) from capabilities.
     *
     * @return platform
     */
    public Platform getPlatform() {
        return (Platform) getCapabilities().getCapability(PLATFORM_NAME);
    }

    /**
     * Returns capability.
     *
     * @param capabilityName capability name.
     * @param <T>            type of capability.
     * @return capability value.
     */
    public <T> T getCapability(String capabilityName) {
        Object capability = getCapabilities().getCapability(capabilityName);
        //noinspection unchecked
        return (T) capability;
    }

    /**
     * Returns all capabilities.
     *
     * @return all capabilities.
     */
    public Capabilities getCapabilities() {
        if (!(webDriver instanceof HasCapabilities)) {
            throw new IllegalStateException(String.format("Web Driver implementation %s does not implement %s",
                    webDriver.getClass().getCanonicalName(), HasCapabilities.class.getCanonicalName()));
        }
        return ((HasCapabilities) webDriver).getCapabilities();
    }

    private void disableCaretBlinking() {
        if (!(webDriver instanceof HasCapabilities)) {
            return;
        }
        Capabilities capabilities = ((HasCapabilities) webDriver).getCapabilities();
        boolean isNativeApp = capabilities.getCapability("app") != null
                || capabilities.getCapability("bundleId") != null
                || capabilities.getCapability("appPackage") != null
                || capabilities.getCapability("appActivity") != null;

        if (!isNativeApp) {
            try {
                executeScript("var caretStyle = ';caret-color: transparent !important;';\n" +
                        "var body = document.getElementsByTagName('body')[0];\n" +
                        "var style = body.getAttribute('style') || '';\n" +
                        "if (style.indexOf(caretStyle) === -1) {\n" +
                        "   body.setAttribute('style', style + caretStyle)\n" +
                        "}");
            } catch (RuntimeException e) {
                log.warn("Failed to disable caret blinking", e);
            }
        }
    }
}
