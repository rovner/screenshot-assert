package io.github.rovner.screenshot.assertions.core.driver;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;

import java.util.function.Predicate;

import static org.openqa.selenium.Platform.*;

/**
 * Web driver predicates factory.
 */
public class WebDriverPredicates {

    private WebDriverPredicates() {
    }

    /**
     * Test if platform is any
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> any() {
        return webDriver -> true;
    }

    /**
     * Test if platform is desktop
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> isDesktop() {
        return webDriver -> {
            Platform platform = wrap(webDriver).getPlatform();
            return is(platform, WINDOWS) || is(platform, MAC) || is(platform, UNIX);
        };
    }

    /**
     * Test if platform is ios
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> isIos() {
        return webDriver -> is(wrap(webDriver).getPlatform(), IOS);
    }

    /**
     * Test if platform is android
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> isAndroid() {
        return webDriver -> is(wrap(webDriver).getPlatform(), ANDROID);
    }

    /**
     * Test if browser is safari
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> isSafari() {
        return webDriver -> wrap(webDriver).getBrowserName().equalsIgnoreCase("safari");
    }

    /**
     * Test if platform is chrome
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> isChrome() {
        return webDriver -> wrap(webDriver).getBrowserName().equalsIgnoreCase("chrome");
    }

    /**
     * Test if device is ipad
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> isIpad() {
        return webDriver -> wrap(webDriver).getDeviceName().toLowerCase().contains("ipad");
    }

    /**
     * Test if device is iphone
     *
     * @return new predicate.
     */
    public static Predicate<WebDriver> isIphone() {
        return webDriver -> wrap(webDriver).getDeviceName().toLowerCase().contains("iphone");
    }

    private static boolean is(Platform actual, Platform expected) {
        if (actual == null) {
            return false;
        }
        if (actual.is(expected)) {
            return true;
        }
        if (actual.family() == null) {
            return false;
        }
        return actual.family().is(expected);
    }

    private static WebDriverWrapper wrap(WebDriver webDriver) {
        return new WebDriverWrapper(webDriver);
    }
}
