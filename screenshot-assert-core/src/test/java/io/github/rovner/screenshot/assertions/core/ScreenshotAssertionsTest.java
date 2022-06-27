package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.exceptions.SoftAssertionError;
import io.github.rovner.screenshot.assertions.core.reference.ReferenceStorage;
import io.github.rovner.screenshot.assertions.core.screenshot.Screenshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScreenshotAssertionsTest {

    @Mock
    WebDriver webDriver;
    @Mock
    ScreenshotAssertionProperties properties;
    @Mock
    ReferenceStorage referenceStorage;
    @Mock
    Screenshot screenshot;
    ScreenshotAssertionConfiguration configuration;
    ScreenshotAssertions assertions;

    @BeforeEach
    void beforeEach() {
        configuration = ScreenshotAssertionConfiguration.builder()
                .referenceStorage(referenceStorage)
                .build();
        assertions = new ScreenshotAssertions(webDriver, configuration, properties);
    }

    @Test
    @DisplayName("Should instantiate assertion")
    void shouldConstructAssertion() {
        assertThat(assertions.assertThat(screenshot)).isNotNull();
    }

    @Test
    @DisplayName("Should assert all for one assertion")
    void shouldAssertAllOne() {
        configuration.setSoft(true);
        AssertionError throwable1 = new AssertionError("test 1");
        AssertionError throwable2 = new AssertionError("test 1");
        configuration.getSoftExceptionCollector().add(throwable1);
        configuration.getSoftExceptionCollector().add(throwable2);

        assertThatThrownBy(() -> assertions.assertAll())
                .isInstanceOf(SoftAssertionError.class)
                .hasMessage("2 assertion(s) failed")
                .hasSuppressedException(throwable1)
                .hasSuppressedException(throwable2);
    }

    @Test
    @DisplayName("Should assert all globally")
    void shouldAssertAllGlobal() {
        when(properties.isSoft()).thenReturn(true);
        assertions.assertAll();
    }

    @Test
    @DisplayName("Should throw exception if not soft")
    void shouldThrowExceptionIfNotSoft() {

        assertThatThrownBy(() -> assertions.assertAll())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("io.github.rovner.screenshot.assertions.core.ScreenshotAssertions.assertAll supposed to be called only when isSoft=true");
    }

}
