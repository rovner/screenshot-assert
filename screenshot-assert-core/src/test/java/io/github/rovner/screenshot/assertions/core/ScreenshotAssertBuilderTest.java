package io.github.rovner.screenshot.assertions.core;

import io.github.rovner.screenshot.assertions.core.exceptions.SoftAssertionError;
import io.github.rovner.screenshot.assertions.core.soft.SoftExceptionCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScreenshotAssertBuilderTest {

    @Mock
    ScreenshotAssertConfig config;
    @Mock
    SoftExceptionCollector collector;
    ScreenshotAssertBuilder builder;


    @BeforeEach
    void before() {
        builder = ScreenshotAssertBuilder
                .builder()
                .setConfig(config)
                .setSoftExceptionsCollector(collector);
    }

    @Test
    @DisplayName("Should throw exception if not soft")
    void shouldThrowIllegalStateException() {
        when(config.isSoft()).thenReturn(false);
        assertThatThrownBy(() -> builder.assertAll())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("io.github.rovner.screenshot.assertions.core.ScreenshotAssertBuilder.assertAll supposed to be called only when isSoft=true");
    }

    @Test
    @DisplayName("Should not throw exception when assertion is soft for single assertion and no exceptions")
    void shouldNotThrowExceptionSingle() {
        when(collector.getAll()).thenReturn(emptyList());
        builder.setSoft(true).assertAll();
        verify(collector, times(1)).getAll();
    }

    @Test
    @DisplayName("Should not throw exception when assertion is soft globally and no exceptions")
    void shouldNotThrowExceptionGlobal() {
        when(config.isSoft()).thenReturn(true);
        when(collector.getAll()).thenReturn(emptyList());
        builder.assertAll();
        verify(collector, times(1)).getAll();
    }

    @Test
    @DisplayName("Should throw exception when there are exceptions")
    void shouldNotThrowException() {
        when(config.isSoft()).thenReturn(true);
        AssertionError er1 = new AssertionError("1");
        AssertionError er2 = new AssertionError("2");
        when(collector.getAll()).thenReturn(asList(er1, er2));
        assertThatThrownBy(() -> builder.assertAll())
                .isInstanceOf(SoftAssertionError.class)
                .hasMessage("2 assertion(s) failed")
                .hasSuppressedException(er1)
                .hasSuppressedException(er2);
    }
}
