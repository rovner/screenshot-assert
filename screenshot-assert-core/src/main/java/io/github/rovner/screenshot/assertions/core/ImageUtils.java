package io.github.rovner.screenshot.assertions.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtils {

    private ImageUtils() {
    }

    public static final Base64.Encoder BASE64 = Base64.getEncoder();

    /**
     * Convert buffered image to byte array.
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
     * @param image to convert
     * @return base64 string
     */
    public static String toBase64(BufferedImage image) {
        return BASE64.encodeToString(toByteArray(image));
    }
}
