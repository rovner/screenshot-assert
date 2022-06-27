package io.github.rovner.screenshot.assertions.core.screenshot;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface KeepContextScreenshot extends Screenshot {
    BufferedImage getContextScreenshot(Color color);
}
