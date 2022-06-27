package io.github.rovner.screenshot.assertions.examples.utils;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Drivers {

    public static final String APPIUM_URL = "http://localhost:4723/wd/hub";
    public static final String CHROME_URL = "http://localhost:9515/";

    public static WebDriver iphone() throws MalformedURLException {
        return ios("iPhone 13");
    }

    public static WebDriver ipad() throws MalformedURLException {
        return ios("iPad (9th generation)");
    }

    public static WebDriver ios(String value) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "15.5");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, value);

        return new RemoteWebDriver(new URL(APPIUM_URL), capabilities);
    }

    public static WebDriver androidPhone() throws MalformedURLException {
        return android("Pixel 5");
    }

    public static WebDriver androidTablet() throws MalformedURLException {
        return android("Pixel C");
    }

    private static WebDriver android(String Pixel_C) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.0");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Pixel_C);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UIAutomator2");
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
        capabilities.setCapability("nativeWebScreenshot", true);

        return new RemoteWebDriver(new URL(APPIUM_URL), capabilities);
    }

    public static WebDriver chrome() throws MalformedURLException {
        WebDriver wd = new RemoteWebDriver(new URL(CHROME_URL), new ChromeOptions());
        wd.manage().window().setSize(new Dimension(800, 600));
        return wd;
    }
}
