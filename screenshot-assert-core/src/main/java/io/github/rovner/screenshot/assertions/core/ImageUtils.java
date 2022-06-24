package io.github.rovner.screenshot.assertions.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import static java.awt.RenderingHints.*;

public class ImageUtils {

    private ImageUtils() {
    }

    public static final Base64.Encoder BASE64 = Base64.getEncoder();

    /**
     * Convert buffered image to byte array.
     *
     * @param image image to convert.
     * @return bytes.
     */
    public static byte[] toByteArray(BufferedImage image) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert buffered image byte array", e);
        }
    }

    /**
     * Convert bytes to buffered image.
     *
     * @param bytes bytes to convert.
     * @return buffered image.
     */
    public static BufferedImage toBufferedImage(byte[] bytes) {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert byte array to buffered image", e);
        }
    }

    /**
     * Converts buffered image to base64 encoded string
     *
     * @param image to convert
     * @return base64 string
     */
    public static String toBase64(BufferedImage image) {
        return BASE64.encodeToString(toByteArray(image));
    }

    /**
     * Scales image to dpr.
     *
     * @param image image to scale.
     * @param dpr   device pixel ratio
     * @return scaled image
     */
    public static BufferedImage scale(BufferedImage image, double dpr) {
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
