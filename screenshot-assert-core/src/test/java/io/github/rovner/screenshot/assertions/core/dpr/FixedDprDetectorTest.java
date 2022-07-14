package io.github.rovner.screenshot.assertions.core.dpr;

import io.github.rovner.screenshot.assertions.core.driver.WebDriverWrapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static io.github.rovner.screenshot.assertions.core.dpr.FixedDprDetector.fixed;
import static org.assertj.core.api.Assertions.assertThat;

public class FixedDprDetectorTest {

    @Mock
    WebDriverWrapper webDriver;

    @Test
    @DisplayName("Should return fixed dpr")
    void fixedTest() {
        assertThat(fixed(3).detect(webDriver))
                .isEqualTo(3);
    }
}
