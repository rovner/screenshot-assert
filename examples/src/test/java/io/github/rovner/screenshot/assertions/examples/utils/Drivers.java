package io.github.rovner.screenshot.assertions.examples.utils;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

public class Drivers {

    public static final String APPIUM_URL = "http://localhost:4723/wd/hub";
    public static final String CHROME_URL = "http://localhost:9515/";

    public static WebDriver iphoneSafari() throws MalformedURLException {
        return iosSafari("iPhone 13");
    }

    public static WebDriver ipadSafari() throws MalformedURLException {
        return iosSafari("iPad (9th generation)");
    }

    public static WebDriver iosSafari(String value) throws MalformedURLException {
        DesiredCapabilities capabilities = ios(value);
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
        return new RemoteWebDriver(new URL(APPIUM_URL), capabilities);
    }

    public static WebDriver iphoneTestApp() throws MalformedURLException {
        return iosTestApp("iPhone 13");
    }

    public static WebDriver iosTestApp(String value) throws MalformedURLException {
        DesiredCapabilities capabilities = ios(value);
        capabilities.setCapability("bundleId", "com.apple.mobileslideshow");
        return new RemoteWebDriver(new URL(APPIUM_URL), capabilities);
    }

    public static WebDriver androidPhone() throws MalformedURLException {
        return androidChrome("Pixel 5");
    }

    public static WebDriver androidTablet() throws MalformedURLException {
        return androidChrome("Pixel C");
    }

    public static WebDriver chrome() throws MalformedURLException {
        WebDriver wd = new RemoteWebDriver(new URL(CHROME_URL), new ChromeOptions());
        wd.manage().window().setSize(new Dimension(800, 600));
        return wd;
    }

    public static WebDriver androidChrome(String device) throws MalformedURLException {
        DesiredCapabilities capabilities = android(device);
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        capabilities.setCapability("nativeWebScreenshot", true);
        return new RemoteWebDriver(new URL(APPIUM_URL), capabilities);
    }

    public static WebDriver androidTestApp(String device) throws MalformedURLException {
        DesiredCapabilities capabilities = android(device);
        capabilities.setCapability("appPackage", "com.google.android.apps.photos");
        capabilities.setCapability("appActivity", "com.google.android.apps.photos.home.HomeActivity");
        return new RemoteWebDriver(new URL(APPIUM_URL), capabilities);
    }

    private static DesiredCapabilities android(String device) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.0");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, device);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
        return capabilities;
    }

    private static DesiredCapabilities ios(String value) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.5");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, value);
        return capabilities;
    }
}
