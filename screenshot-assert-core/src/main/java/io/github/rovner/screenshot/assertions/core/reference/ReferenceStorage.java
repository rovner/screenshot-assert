package io.github.rovner.screenshot.assertions.core.reference;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Reference storage IO interface.
 */
public interface ReferenceStorage {

    /**
     * Reads reference to buffered image from storage.
     *
     * @param id reference id
     * @return buffered reference image
     * @throws IOException if reference is missing
     */
    BufferedImage read(String id) throws IOException;

    /**
     * Writes image as reference to storage.
     *
     * @param id    reference id
     * @param image to save
     */
    void write(String id, BufferedImage image);

    /**
     * Describes reference with id.
     *
     * @param id reference id
     * @return description
     */
    String describe(String id);
}
