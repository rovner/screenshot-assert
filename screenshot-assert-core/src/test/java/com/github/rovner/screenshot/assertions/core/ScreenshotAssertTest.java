package com.github.rovner.screenshot.assertions.core;

import com.github.rovner.screenshot.assertions.core.allure.AllureListener;
import com.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import com.github.rovner.screenshot.assertions.core.diff.ImageDiffer;
import com.github.rovner.screenshot.assertions.core.exceptions.NoReferenceException;
import com.github.rovner.screenshot.assertions.core.ignoring.Ignorings;
import com.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.awt.image.BufferedImage.TYPE_4BYTE_ABGR;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.deleteIfExists;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Sets.newHashSet;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldCompareWithReference() throws IOException {
        String id = "screenshot-id";
        Path reference = getReferencePath(id);
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        ImageIO.write(image, "png", reference.toFile());
        when(screenshot.take(webDriver)).thenReturn(image);
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.empty());
        ScreenshotAssert.builder()
                .webDriver(webDriver)
                .screenshot(screenshot)
                .references(reference.getParent())
                .imageDiffer(differ)
                .allureListener(allureListener)
                .build()
                .ignoring(Ignorings.hash(1))
                .ignoring(Ignorings.hash(2), Ignorings.hash(3))
                .ignoring(singletonList(Ignorings.hash(4)))
                .isEqualToReferenceId(id);
        verify(differ, times(1)).makeDiff(eq(image), any(), ArgumentMatchers.eq(newHashSet(asList(
                Ignorings.hash(1), Ignorings.hash(2), Ignorings.hash(3), Ignorings.hash(4)
        ))));
        verify(allureListener, times(1)).handleNoDiff(any());
    }

    @Test
    void shouldThrowExceptionOnDiff() throws IOException {
        String id = "screenshot-id";
        Path reference = getReferencePath(id);
        BufferedImage image = new BufferedImage(10, 10, TYPE_4BYTE_ABGR);
        ImageIO.write(image, "png", reference.toFile());
        when(screenshot.take(webDriver)).thenReturn(image);
        when(screenshot.describe()).thenReturn("test-element");
        when(differ.makeDiff(any(), any(), anyCollection())).thenReturn(Optional.of(ImageDiff.builder().build()));
        System.setProperty("allure.results.directory", reference.getParent().toAbsolutePath().toString());
        ScreenshotAssert screenshotAssert = ScreenshotAssert.builder()
                .webDriver(webDriver)
                .screenshot(screenshot)
                .references(reference.getParent())
                .imageDiffer(differ)
                .allureListener(allureListener)
                .build();
        assertThatThrownBy(() -> screenshotAssert.isEqualToReferenceId(id))
                .isInstanceOf(AssertionError.class)
                .hasMessage("Expected screenshot of test-element to be equal to reference screenshot-id, but is was not");
        verify(allureListener, times(1)).handleDiff(any());
    }

    @Test
    void shouldWriteNewReferences() throws IOException {
        String id = "non-existing-screenshot-id";
        Path reference = getReferencePath(id);
        deleteIfExists(reference);
        when(screenshot.take(webDriver)).thenReturn(new BufferedImage(10, 10, TYPE_4BYTE_ABGR));
        ScreenshotAssert screenshotAssert = ScreenshotAssert.builder()
                .webDriver(webDriver)
                .screenshot(screenshot)
                .references(reference.getParent())
                .imageDiffer(differ)
                .allureListener(allureListener)
                .build();
        assertThatThrownBy(() -> screenshotAssert.isEqualToReferenceId(id))
                .isInstanceOf(NoReferenceException.class)
                .hasMessage("No reference image at path " +
                        "build/tmp/test/com.github.rovner.screenshot.assertions.core.ScreenshotAssertTest/non-existing-screenshot-id.png, " +
                        "current screenshot saved as reference");
        assertThat(reference).exists();
        verify(allureListener, times(1)).handleNoReference(any());
    }

    private Path getReferencePath(String id) throws IOException {
        Path reference = Paths.get("build/tmp/test/")
                .resolve(this.getClass().getName())
                .resolve(id + ".png");
        createDirectories(reference.getParent());
        return reference;
    }
}
