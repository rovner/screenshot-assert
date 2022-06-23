package io.github.rovner.screenshot.assertions.core.allure;

import com.google.gson.Gson;
import io.github.rovner.screenshot.assertions.core.diff.DefaultImageDiffer;
import io.github.rovner.screenshot.assertions.core.diff.ImageDiff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static io.github.rovner.screenshot.assertions.core.screenshot.Screenshots.screenshotOfViewport;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.list;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class DefaultAllureListenerTest {

    public static final Path ALLURE_RESULTS = Paths.get("build/allure-results");
    public static final Gson GSON = new Gson();
    public static final String ACTUAL_BASE64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAIAAAACUFjqAAAADUlEQVR42mNgGAWkAwABNgABiY9t2AAAAABJRU5ErkJggg\u003d\u003d";
    public static final String REFERENCE_BASE64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAIAAADZF8uwAAAADklEQVR42mNgGAVDFQAAAbwAASVcUIEAAAAASUVORK5CYII\u003d";
    public static final String DIFF_BASE64 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAsAAAALCAIAAAAmzuBxAAAADUlEQVR42mNgGAX0BwABdgABQVFLuQAAAABJRU5ErkJggg\u003d\u003d";
    BufferedImage image1 = new BufferedImage(10, 10, TYPE_INT_RGB);
    BufferedImage image2 = new BufferedImage(11, 11, TYPE_INT_RGB);
    BufferedImage image3 = new BufferedImage(12, 12, TYPE_INT_RGB);

    Rectangle rectangle1 = new Rectangle(0, 0, 5, 5);
    Rectangle rectangle2 = new Rectangle(5, 0, 2, 2);
    DefaultAllureListener listener = new DefaultAllureListener();

    @Test
    @DisplayName("Should attach actual image when there is no diff")
    void shouldHandleNoDiff() throws IOException {
        List<Path> results = runAndGetNewResults(() -> listener.handleNoDiff(image1));
        assertThat(results).hasSize(1);
        assertThat(makeDiff(results)).isEmpty();
    }

    @Test
    @DisplayName("Should attach actual image when there is no reference")
    void shouldHandleNoReference() throws IOException {
        List<Path> results = runAndGetNewResults(() -> listener.handleNoReference(image1));
        assertThat(results).hasSize(1);
        assertThat(makeDiff(results)).isEmpty();
    }

    @Test
    @DisplayName("Should attach diff when there is difference")
    void shouldHandleDiff() throws IOException {
        ImageDiff imageDiff = ImageDiff.builder()
                .actual(image1)
                .diff(image2)
                .reference(image3)
                .diffHash(123)
                .ignoredAreas(new HashSet<>(asList(rectangle1, rectangle2)))
                .ignoredHashes(new HashSet<>(asList(321, 432)))
                .build();
        List<Path> results = runAndGetNewResults(() -> listener.handleDiff(imageDiff));
        assertThat(results).hasSize(2);

        Map<String, String> diff = readAllureDiff(findFirstWithExtension(results, ".json"));
        assertThat(diff).hasSize(3);
        assertThat(diff.get("actual")).isEqualTo(ACTUAL_BASE64);
        assertThat(diff.get("expected")).isEqualTo(REFERENCE_BASE64);
        assertThat(diff.get("diff")).isEqualTo(DIFF_BASE64);

        String diffInfo = read(findFirstWithExtension(results, ".txt"));
        assertThat(diffInfo).isEqualTo("hash code: 123\n" +
                "ignored hashes: 432,321\n" +
                "ignored areas: [x: 0, y: 0, width: 5, height: 5],[x: 5, y: 0, width: 2, height: 2]\n");
    }

    @Test
    @DisplayName("Should attach diff when diff updated")
    void shouldHandleDiffWithoutIgnorings() throws IOException {
        ImageDiff imageDiff = ImageDiff.builder()
                .actual(image1)
                .diff(image2)
                .reference(image3)
                .diffHash(123)
                .ignoredHashes(emptySet())
                .ignoredAreas(emptySet())
                .build();
        List<Path> results = runAndGetNewResults(() -> listener.handleDiffUpdated(imageDiff));
        assertThat(results).hasSize(2);

        String diffInfo = read(findFirstWithExtension(results, ".txt"));
        assertThat(diffInfo).isEqualTo("hash code: 123\n");
    }

    @Test
    @DisplayName("Should return step name")
    void shouldReturnStepName() {
        assertThat(listener.getCompareStepName(screenshotOfViewport(), "test"))
                .isEqualTo("Assert that the viewport is equal to reference with id 'test'");
    }

    private Map<String, String> readAllureDiff(Path path) throws IOException {
        String json = read(path);
        //noinspection unchecked
        return GSON.fromJson(json, Map.class);
    }

    private String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), UTF_8);
    }

    private Path findFirstWithExtension(List<Path> paths, String extension) {
        return paths.stream()
                .filter(path -> path.toString().endsWith(extension))
                .findFirst()
                .orElseThrow(() -> new AssertionError(String.format("No file with extension %s among results",
                        extension)));
    }

    private static List<Path> runAndGetNewResults(Runnable runnable) throws IOException {
        List<Path> resultsBefore = readAllureResultsFiles();
        runnable.run();
        List<Path> resultsAfter = readAllureResultsFiles();
        resultsAfter.removeAll(resultsBefore);
        return resultsAfter;
    }

    public static List<Path> readAllureResultsFiles() throws IOException {
        try (Stream<Path> paths = list(ALLURE_RESULTS)) {
            return paths.collect(toList());
        }
    }

    private Optional<ImageDiff> makeDiff(List<Path> results) throws IOException {
        BufferedImage actual = ImageIO.read(results.get(0).toFile());
        return new DefaultImageDiffer().makeDiff(actual, image1, emptyList());
    }
}
