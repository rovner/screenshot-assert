package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.aggregating;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AggregatingViewportCropperTest {

    @Mock
    ImageCropper imageCropper;
    @Mock
    WebDriverWrapper wrapper;
    @Mock
    ViewportCropper viewportCropper1;
    @Mock
    ViewportCropper viewportCropper2;
    BufferedImage image1 = new BufferedImage(10, 20, TYPE_4BYTE_ABGR);
    BufferedImage image2 = new BufferedImage(10, 20, TYPE_4BYTE_ABGR);
    BufferedImage image3 = new BufferedImage(10, 20, TYPE_4BYTE_ABGR);

    @Test
    @DisplayName("Should crop viewport")
    void shouldCrop() {
        when(viewportCropper1.crop(image1, imageCropper, wrapper, 2)).thenReturn(image2);
        when(viewportCropper2.crop(image2, imageCropper, wrapper, 2)).thenReturn(image3);
        BufferedImage cropped = aggregating(viewportCropper1, viewportCropper2).crop(image1, imageCropper, wrapper, 2);
        assertThat(cropped).isEqualTo(image3);
    }
}
