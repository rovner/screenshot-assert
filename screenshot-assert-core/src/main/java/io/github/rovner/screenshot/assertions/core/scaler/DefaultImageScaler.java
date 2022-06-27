package io.github.rovner.screenshot.assertions.core.scaler;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.RenderingHints.*;

/**
 * Default image scaler.
 */
public class DefaultImageScaler implements ImageScaler {
    @Override
    public BufferedImage scale(BufferedImage image, double dpr) {
        if (dpr == 1) {
            return image;
        }
        int scaledWidth = (int) (image.getWidth() / dpr);
        int scaledHeight = (int) (image.getHeight() / dpr);

        final BufferedImage bufferedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_3BYTE_BGR);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
        graphics2D.dispose();
        return bufferedImage;
    }
}
