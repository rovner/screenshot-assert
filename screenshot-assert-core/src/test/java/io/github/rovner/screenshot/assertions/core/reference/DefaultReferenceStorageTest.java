package io.github.rovner.screenshot.assertions.core.reference;

import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DefaultReferenceStorageTest {

    String id = "test";
    BufferedImage image = new BufferedImage(10, 10, TYPE_INT_RGB);
    Path references = Paths.get("build/tmp/test").resolve(this.getClass().getName());
    DefaultReferenceStorage referenceStorage = new DefaultReferenceStorage(references);

    @Test
    @DisplayName("Should write and read reference")
    void shouldReadAdnWriteReference() throws IOException {
        referenceStorage.write(id, image);
        assertThat(references.resolve(id + ".png")).exists();
        BufferedImage actual = referenceStorage.read(id);
        assertThat(new DefaultImageDiffer().makeDiff(actual, image, emptyList())).isEmpty();
    }

    @Test
    @DisplayName("Should describe reference")
    void shouldDescribeReference() {
        assertThat(referenceStorage.describe(id))
                .isEqualTo(references.resolve(id + ".png").toString());
    }

    @Test
    @DisplayName("Should throw exceprion if not able to write reference")
    void shouldThrowException() {
        referenceStorage = new DefaultReferenceStorage(Paths.get("/dev/nonexistentfolder"));
        assertThatThrownBy(() -> referenceStorage.write(id, image))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unable to save reference file to path /dev/nonexistentfolder/test.png");
    }
}
