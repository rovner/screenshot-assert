package io.github.rovner.screenshot.assertions.core.screenshot;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

import static io.github.rovner.screenshot.assertions.core.ImageUtils.toBufferedImage;
import static java.awt.RenderingHints.*;
import static org.openqa.selenium.OutputType.BYTES;

/**
 * Takes screenshot of the browser viewport (whole visible page) and scales image to original resolution in case of retina displays.
 */
public final class WholePageScreenshot implements Screenshot {

    WholePageScreenshot() {
    }

    @Override
    public BufferedImage take(WebDriver webDriver) {
        @SuppressWarnings({"unchecked", "rawtypes"})
        Map<String, Long> viewport = (Map) ((JavascriptExecutor) webDriver).executeScript("return {" +
                "width: Math.max(document.documentElement.clientWidth || 0, window.innerWidth || 0), " +
                "height: Math.max(document.documentElement.clientHeight || 0, window.innerHeight || 0)" +
                "}");

        BufferedImage screenshot = toBufferedImage(((TakesScreenshot) webDriver).getScreenshotAs(BYTES));
        if (screenshot.getWidth() == viewport.get("width") && screenshot.getHeight() == viewport.get("height")) {
            return screenshot;
        } else {
            //retina display
            int dprX = (int) (screenshot.getWidth() / viewport.get("width"));
            int dprY = (int) (screenshot.getHeight() / viewport.get("height"));
            return scaleRetinaScreenshot(screenshot, dprX, dprY);

        }
    }

    private BufferedImage scaleRetinaScreenshot(BufferedImage screenshot, int dprX, int dprY) {
        int scaledWidth = screenshot.getWidth() / dprX;
        int scaledHeight = screenshot.getHeight() / dprY;

        final BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_3BYTE_BGR);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(screenshot, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();
        return bufferedImage;
    }

    @Override
    public String describe() {
        return "the whole page";
    }
}
