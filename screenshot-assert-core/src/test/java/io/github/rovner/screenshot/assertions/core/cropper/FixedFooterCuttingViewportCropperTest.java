package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.fixedFooterCutting;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FixedFooterCuttingViewportCropperTest {

    @Mock
    ImageCropper imageCropper;
    @Mock
    WebDriverWrapper wrapper;
    BufferedImage image = new BufferedImage(10, 20, TYPE_4BYTE_ABGR);

    @Test
    @DisplayName("Should crop footer")
    void shouldCrop() {
        fixedFooterCutting(2).crop(image, imageCropper, wrapper, 1);
        verify(imageCropper, times(1))
                .crop(image, new Rectangle(0, 0, 18, 10));
    }
}
