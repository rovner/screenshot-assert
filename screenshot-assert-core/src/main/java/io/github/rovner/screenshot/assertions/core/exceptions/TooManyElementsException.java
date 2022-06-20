package io.github.rovner.screenshot.assertions.core.exceptions;

import org.openqa.selenium.WebDriverException;

public class TooManyElementsException extends WebDriverException {

    public TooManyElementsException(String reason) {
        super(reason);
    }
}
