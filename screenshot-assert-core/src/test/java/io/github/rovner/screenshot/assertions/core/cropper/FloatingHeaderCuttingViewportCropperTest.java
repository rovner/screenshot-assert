package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.floatingHeaderCutting;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FloatingHeaderCuttingViewportCropperTest {

    @Mock
    ImageCropper imageCropper;
    @Mock
    WebDriverWrapper wrapper;
    BufferedImage image = new BufferedImage(10, 20, TYPE_4BYTE_ABGR);

    @Test
    @DisplayName("Should crop header")
    void shouldCrop() {
        when(wrapper.executeScript(anyString())).thenReturn(9L);
        floatingHeaderCutting().crop(image, imageCropper, wrapper, 2);
        verify(imageCropper, times(1))
                .crop(image, new Rectangle(0, 2, 18, 10));
    }
}
