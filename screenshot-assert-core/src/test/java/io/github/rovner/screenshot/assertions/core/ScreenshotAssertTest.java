package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.cropper.ImageCropper;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.exceptions.NoReferenceException;
import io.github.rovner.screenshot.assertions.core.exceptions.ScreenshotAssertionError;
import io.github.rovner.screenshot.assertions.core.exceptions.SoftAssertionError;
import io.github.rovner.screenshot.assertions.core.reference.ReferenceStorage;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Optional;

import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.elementsBy;
import static io.github.rovner.screenshot.assertions.core.ignoring.Ignorings.hash;
import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.openqa.selenium.By.cssSelector;

@ExtendWith(MockitoExtension.class)
public class ScreenshotAssertTest {

    @Mock
    WebDriver webDriver;
    @Mock
    Screenshot screenshot;
    @Mock
    AllureListener allureListener;
    @Mock
    ImageDiffer differ;
    @Mock
    ImageCropper cropper;
    @Mock
    ReferenceStorage referenceStorage;
    @Mock
    ScreenshotAssertConfig cfg;

    BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
    String id = "screenshot-id";
    ScreenshotAssertBuilder screenshotAssertBuilder;

    @BeforeEach
    void before() {
        screenshotAssertBuilder = ScreenshotAssertBuilder.builder()
                .setWebDriver(webDriver)
                .setReferenceStorage(referenceStorage)
                .setImageDiffer(differ)
                .setImageCropper(cropper)
                .setAllureListener(allureListener)
                .setConfig(cfg);
    }

    @Test
    @DisplayName("Should be no difference during image comparison")
    void shouldCompareWithReference() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.empty());

        screenshotAssertBuilder.assertThat(screenshot)
                .ignoring(hash(1))
                .ignoring(hash(2), hash(3))
                .ignoring(singletonList(elementsBy(cssSelector(".test"))))
                .isEqualToReferenceId(id);

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(1)).handleNoDiff(any());
        verify(differ, times(1)).makeDiff(eq(image), any(), eq(newHashSet(asList(
                hash(1), hash(2), hash(3), elementsBy(cssSelector(".test"))
        ))));
    }

    @Test
    @DisplayName("Should be no difference during image comparison for soft assertions")
    void shouldCompareWithReferenceSoft() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.empty());

        screenshotAssertBuilder.setSoft(true)
                .assertThat(screenshot)
                .isEqualToReferenceId(id);

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(1)).handleNoDiff(any());
    }

    @Test
    @DisplayName("Should throw assertion error during image comparison when images are different")
    void shouldThrowExceptionOnDiff() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(screenshot.describe()).thenReturn("test-element");
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(cfg.isUpdateReferenceImage()).thenReturn(false);

        assertThatThrownBy(() -> screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id))
                .isInstanceOf(ScreenshotAssertionError.class)
                .hasMessage("Expected screenshot of test-element to be equal to reference screenshot-id, but is was not");
        verify(allureListener, times(1)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
    }

    @Test
    @DisplayName("Should throw soft exceptions for multiple failures for single assertion")
    void shouldThrowSoftExceptionOnDiffSingle() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(screenshot.describe()).thenReturn("test-element");
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(cfg.isUpdateReferenceImage()).thenReturn(false);

        screenshotAssertBuilder.setSoft(true);

        screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id);
        screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id);

        assertThatThrownBy(() -> screenshotAssertBuilder.assertAll())
                .isInstanceOf(SoftAssertionError.class)
                .hasMessage("2 assertion(s) failed");
        verify(allureListener, times(2)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
    }

    @Test
    @DisplayName("Should throw soft exceptions for multiple failures for all assertion")
    void shouldThrowSoftExceptionOnDiffAll() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(screenshot.describe()).thenReturn("test-element");
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(cfg.isSoft()).thenReturn(true);
        when(cfg.isUpdateReferenceImage()).thenReturn(false);

        screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id);
        screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id);

        assertThatThrownBy(() -> screenshotAssertBuilder.assertAll())
                .isInstanceOf(SoftAssertionError.class)
                .hasMessage("2 assertion(s) failed");
        verify(allureListener, times(2)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
    }

    @Test
    @DisplayName("Should not throw exception and save actual as reference during image comparison when property is true")
    void shouldSaveActualAsReference() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(cfg.isUpdateReferenceImage()).thenReturn(true);

        screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id);

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(1)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
        verify(referenceStorage, times(1)).write(id, image);
    }

    @Test
    @DisplayName("Should throw exception and save actual as reference when there are no reference error during image comparison")
    void shouldWriteNewReferences() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenThrow(new IOException());
        when(referenceStorage.describe(id)).thenReturn("/some/path");
        when(cfg.isSaveReferenceImageWhenMissing()).thenReturn(true);

        assertThatThrownBy(() -> screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id))
                .isInstanceOf(NoReferenceException.class)
                .hasMessage("No reference image /some/path, current screenshot saved as reference");

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(1)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
        verify(referenceStorage, times(1)).write(id, image);
    }

    @Test
    @DisplayName("Should throw exception and do not save actual as reference when there are no reference error during image comparison")
    void shouldNotWriteNewReferences() throws IOException {
        when(screenshot.take(webDriver, cropper)).thenReturn(image);
        when(referenceStorage.read(id)).thenThrow(new IOException());
        when(referenceStorage.describe(id)).thenReturn("/some/path");
        when(cfg.isSaveReferenceImageWhenMissing()).thenReturn(false);

        assertThatThrownBy(() -> screenshotAssertBuilder.assertThat(screenshot).isEqualToReferenceId(id))
                .isInstanceOf(NoReferenceException.class)
                .hasMessage("No reference image /some/path, current screenshot saved as reference");

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(1)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
        verify(referenceStorage, times(0)).write(id, image);
    }
}
