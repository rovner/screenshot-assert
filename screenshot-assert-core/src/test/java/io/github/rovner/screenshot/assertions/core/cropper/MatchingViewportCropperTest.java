package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.aggregating;
import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.matching;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchingViewportCropperTest {

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
        when(viewportCropper2.crop(image1, imageCropper, wrapper, 2)).thenReturn(image3);
        BufferedImage cropped = matching()
                .match(webDriver -> false, viewportCropper1)
                .match(webDriver -> true, viewportCropper2)
                .crop(image1, imageCropper, wrapper, 2);
        assertThat(cropped).isEqualTo(image3);
    }

    @Test
    @DisplayName("Should throw exception if none matched")
    void shouldThrowException() {
        MatchingViewportCropper cropper = matching()
                .match(webDriver -> false, viewportCropper1)
                .match(webDriver -> false, viewportCropper2);
        assertThatThrownBy(() -> cropper.crop(image1, imageCropper, wrapper, 2))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No viewport cropper found that match web driver");
    }
}
