package io.rovner.screenshot.assertions.core.screenshot;

import io.rovner.screenshot.assertions.core.cropper.DefaultImageCropper;
import io.rovner.screenshot.assertions.core.cropper.ImageCropper;

/**
 * Takes screenshot of browser viewport and crops desired area from the screenshot.
 * @param <T> screenshot real type
 */
public abstract class CroppedScreenshot<T extends CroppedScreenshot<?>> implements Screenshot {

    protected ImageCropper imageCropper = new DefaultImageCropper();

    public T croppedWith(ImageCropper imageCropper) {
        this.imageCropper = imageCropper;
        //noinspection unchecked
        return (T) this;
    }

}
