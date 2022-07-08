package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.allure.AllureListener;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import io.github.rovner.screenshot.assertions.core.exceptions.NoReferenceException;
import io.github.rovner.screenshot.assertions.core.exceptions.ScreenshotAssertionError;
import io.github.rovner.screenshot.assertions.core.reference.ReferenceStorage;
import io.github.rovner.screenshot.assertions.core.screenshot.KeepContextScreenshot;
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
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.openqa.selenium.By.cssSelector;

@ExtendWith(MockitoExtension.class)
public class ScreenshotAssertionTest {


    @Mock
    WebDriver webDriver;
    @Mock
    ReferenceStorage referenceStorage;
    @Mock
    Screenshot screenshot;
    @Mock
    KeepContextScreenshot keepContextScreenshot;
    @Mock
    ImageDiffer differ;
    @Mock
    AllureListener allureListener;
    @Mock
    ScreenshotAssertionProperties properties;
    String id = "screenshot-id";
    BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
    ScreenshotAssertionConfiguration configuration;
    ScreenshotAssertion assertion;

    @BeforeEach
    void beforeEach() {
        configuration = ScreenshotAssertionConfiguration.builder()
                .referenceStorage(referenceStorage)
                .imageDiffer(differ)
                .allureListener(allureListener)
                .build();
        assertion = new ScreenshotAssertion(webDriver, configuration, properties, screenshot);
    }

    @Test
    @DisplayName("Should be no difference during image comparison")
    void shouldCompareWithReference() throws IOException {
        when(screenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(differ.makeDiff(any(), any(), anySet(), anySet())).thenReturn(Optional.empty());
        when(properties.isSoft()).thenReturn(false);

        assertion.ignoring(hash(1))
                .ignoring(hash(2), hash(3))
                .ignoring(singletonList(elementsBy(cssSelector(".test"))))
                .isEqualToReferenceId(id);

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(1)).handleNoDiff(any());
        verify(differ, times(1)).makeDiff(eq(image), any(), anySet(), anySet());
    }

    @Test
    @DisplayName("Should be no difference during image comparison for soft assertions")
    void shouldCompareWithReferenceSoft() throws IOException {
        when(screenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(differ.makeDiff(any(), any(), anySet(), anySet())).thenReturn(Optional.of(ImageDiff.builder().build()));
        configuration.setSoft(true);

        assertion.isEqualToReferenceId(id);

        verify(allureListener, times(1)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
    }

    @Test
    @DisplayName("Should throw assertion error during image comparison when images are different")
    void shouldThrowExceptionOnDiff() throws IOException {
        when(screenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(screenshot.describe()).thenReturn("test-element");
        when(differ.makeDiff(any(), any(), anySet(), anySet())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(properties.isUpdateReferenceImage()).thenReturn(false);

        assertThatThrownBy(() -> assertion.isEqualToReferenceId(id))
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
        when(screenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(screenshot.describe()).thenReturn("test-element");
        when(differ.makeDiff(any(), any(), anySet(), anySet())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(properties.isUpdateReferenceImage()).thenReturn(false);

        configuration.setSoft(true);

        assertion.isEqualToReferenceId(id);
        assertion.isEqualToReferenceId(id);

        assertThat(configuration.getSoftExceptionCollector().getAll()).hasSize(2);
        verify(allureListener, times(2)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
    }

    @Test
    @DisplayName("Should not throw exception and save actual as reference during image comparison when property is true")
    void shouldSaveActualAsReference() throws IOException {
        when(screenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(differ.makeDiff(any(), any(), anySet(), anySet())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(properties.isUpdateReferenceImage()).thenReturn(true);

        assertion.isEqualToReferenceId(id);

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(0)).handleNoReference(any());
        verify(allureListener, times(1)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
        verify(referenceStorage, times(1)).write(id, image);
    }

    @Test
    @DisplayName("Should throw exception and save actual as reference when there are no reference error during image comparison")
    void shouldWriteNewReferences() throws IOException {
        when(screenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenThrow(new IOException());
        when(referenceStorage.describe(id)).thenReturn("/some/path");
        when(properties.isSaveReferenceImageWhenMissing()).thenReturn(true);

        assertThatThrownBy(() -> assertion.isEqualToReferenceId(id))
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
        when(screenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenThrow(new IOException());
        when(referenceStorage.describe(id)).thenReturn("/some/path");
        when(properties.isSaveReferenceImageWhenMissing()).thenReturn(false);

        assertThatThrownBy(() -> assertion.isEqualToReferenceId(id))
                .isInstanceOf(NoReferenceException.class)
                .hasMessage("No reference image /some/path, current screenshot saved as reference");

        verify(allureListener, times(0)).handleDiff(any());
        verify(allureListener, times(1)).handleNoReference(any());
        verify(allureListener, times(0)).handleDiffUpdated(any());
        verify(allureListener, times(0)).handleNoDiff(any());
        verify(referenceStorage, times(0)).write(id, image);
    }

    @Test
    @DisplayName("Should attach context screenshot")
    void shouldAttachContextScreenshot() throws IOException {
        assertion = new ScreenshotAssertion(webDriver, configuration, properties, keepContextScreenshot);
        when(keepContextScreenshot.take(any(), eq(configuration.getScreenshotConfiguration()))).thenReturn(image);
        when(referenceStorage.read(id)).thenReturn(image);
        when(keepContextScreenshot.describe()).thenReturn("test-element");
        when(keepContextScreenshot.getContextScreenshot(any())).thenReturn(image);
        when(differ.makeDiff(any(), any(), anySet(), anySet())).thenReturn(Optional.of(ImageDiff.builder().build()));
        when(properties.isUpdateReferenceImage()).thenReturn(false);

        assertThatThrownBy(() -> assertion.isEqualToReferenceId(id))
                .isInstanceOf(ScreenshotAssertionError.class);
        verify(allureListener, times(1)).handleContextScreenshot(image);
    }
}
