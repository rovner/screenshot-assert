package io.github.rovner.screenshot.assertions.core.reference;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.createDirectories;

/**
 * Default implementation that stores references on disk.
 */
public class DefaultReferenceStorage implements ReferenceStorage {

    private final Path referencesDir;

    public DefaultReferenceStorage(Path referencesDir) {
        this.referencesDir = referencesDir;
    }

    @Override
    public BufferedImage read(String id) throws IOException {
        return ImageIO.read(getReferencePath(id));
    }

    @Override
    public void write(String id, BufferedImage image) {
        File referencePath = getReferencePath(id);
        try {
            createDirectories(referencesDir);
            ImageIO.write(image, "png", referencePath);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to save reference file to path %s", referencePath));
        }
    }

    @Override
    public String describe(String id) {
        return getReferencePath(id).toString();
    }

    private File getReferencePath(String id) {
        return referencesDir.resolve(id + ".png").toFile();
    }
}
