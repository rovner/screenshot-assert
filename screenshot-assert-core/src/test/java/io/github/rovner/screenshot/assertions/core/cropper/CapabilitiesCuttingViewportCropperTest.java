package io.github.rovner.screenshot.assertions.core.cropper;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Rectangle;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static io.github.rovner.screenshot.assertions.core.cropper.ViewportCroppers.capabilities;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CapabilitiesCuttingViewportCropperTest {

    @Mock
    ImageCropper imageCropper;
    @Mock
    WebDriverWrapper wrapper;
    BufferedImage image = new BufferedImage(10, 20, TYPE_4BYTE_ABGR);

    @Test
    @DisplayName("Should crop viewport")
    void shouldCrop() {
        when(wrapper.getCapability("viewportRect")).thenReturn(new HashMap<String, Long>() {{
            put("top", 1L);
            put("left", 2L);
            put("height", 18L);
            put("width", 8L);
        }});
        capabilities().crop(image, imageCropper, wrapper, 2);
        verify(imageCropper, times(1))
                .crop(image, new Rectangle(2, 1, 18, 8));
    }
}
