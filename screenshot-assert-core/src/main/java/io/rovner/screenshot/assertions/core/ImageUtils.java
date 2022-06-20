package io.rovner.screenshot.assertions.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    /**
     * Convert buffered image to byte array.
     * @param image to convert.
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
     * @param bytes to convert.
     * @return buffered image.
     */
    public static BufferedImage toBufferedImage(byte[] bytes) {
        try (InputStream is = new ByteArrayInputStream(bytes);) {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException("Unable to convert byte array to buffered image");
        }
    }
}
