package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.noCrop;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class NoCropViewportCropperTest {

    @Mock
    ImageCropper imageCropper;
    @Mock
    WebDriverWrapper wrapper;
    BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);

    @Test
    @DisplayName("Should not crop for desktop cropper")
    void shouldNotCrop() {
        BufferedImage cropped = noCrop().crop(image, imageCropper, wrapper, 1);
        assertThat(cropped).isEqualTo(image);
    }
}
