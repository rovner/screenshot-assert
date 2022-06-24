package io.github.rovner.screenshot.assertions.core.platform;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;

import static org.openqa.selenium.Platform.ANDROID;
import static org.openqa.selenium.Platform.IOS;
import static org.openqa.selenium.remote.CapabilityType.PLATFORM_NAME;

/**
 * Platform matchers
 */
public class Platforms {

    private Platforms() {
    }

    /**
     * Test whatever actual platform is exactly equals expected, or it's family equals expected.
     *
     * @param actual   actual platform
     * @param expected expected platform
     * @return true or false.
     */
    public static boolean is(Platform actual, Platform expected) {
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

    /**
     * Test whatever target platform is Safari browser on iPhone.
     * @param capabilities web driver capabilities.
     * @return true or false.
     */
    public static boolean isIpadSafari(Capabilities capabilities) {
        String deviceName = getDeviceName(capabilities);
        return isIosSafari(capabilities)
                && deviceName.toLowerCase().contains("ipad");
    }

    /**
     * Test whatever target platform is Safari browser on iPhad.
     * @param capabilities web driver capabilities.
     * @return true or false.
     */
    public static boolean isIphoneSafari(Capabilities capabilities) {
        String deviceName = getDeviceName(capabilities);
        return isIosSafari(capabilities)
                && deviceName.toLowerCase().contains("iphone");
    }

    /**
     * Test whatever target platform is iOS.
     * @param capabilities web driver capabilities.
     * @return true or false.
     */
    public static boolean isIos(Capabilities capabilities) {
        return is((Platform) capabilities.getCapability(PLATFORM_NAME), IOS);
    }

    /**
     * Test whatever target platform is Android.
     * @param capabilities web driver capabilities.
     * @return true or false.
     */
    public static boolean isAndroid(Capabilities capabilities) {
        return is((Platform) capabilities.getCapability(PLATFORM_NAME), ANDROID);
    }

    /**
     * Test whatever target platform is Safari browser on iOS.
     * @param capabilities web driver capabilities.
     * @return true or false.
     */
    public static boolean isIosSafari(Capabilities capabilities) {
        return isIos(capabilities) && capabilities.getBrowserName().equalsIgnoreCase("safari");
    }

    /**
     * Test whatever target platform is Chrome browser on Android.
     * @param capabilities web driver capabilities.
     * @return true or false.
     */
    public static boolean isAndroidChrome(Capabilities capabilities) {
        return isAndroid(capabilities) && capabilities.getBrowserName().equalsIgnoreCase("chrome");
    }

    private static String getDeviceName(Capabilities capabilities) {
        return (String) capabilities.getCapability("deviceName");
    }
}
