package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.exceptions.InvalidCoordinatesException;
import org.openqa.selenium.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Default image cropper implementation that crops exact area from original area.
 */
public class DefaultImageCropper implements ImageCropper {
    @Override
    public BufferedImage crop(BufferedImage original, Rectangle areaToCrop) {
        if (!isAreaInsideImage(original, areaToCrop)) {
            throw new InvalidCoordinatesException("Can not crop image because desired area is outside of image");
        }
        BufferedImage cropped = new BufferedImage(areaToCrop.width, areaToCrop.height, original.getType());
        Graphics graphics = cropped.getGraphics();
        graphics.drawImage(
                original,
                0, 0, areaToCrop.width, areaToCrop.height,
                areaToCrop.x, areaToCrop.y, areaToCrop.x + areaToCrop.width, areaToCrop.y + areaToCrop.height,
                null
        );
        graphics.dispose();
        return cropped;
    }

    private static boolean isAreaInsideImage(BufferedImage image, Rectangle area) {
        return new java.awt.Rectangle(0, 0, image.getWidth(), image.getHeight())
                .contains(new java.awt.Rectangle(area.x, area.y, area.width, area.height));
    }
}
