package io.github.rovner.screenshot.assertions.core;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.ConfigFactory;

import static org.aeonbits.owner.Config.LoadType.MERGE;

@LoadPolicy(MERGE)
@Sources({
        "classpath:screenshot-assert.properties",
        "system:properties",
        "system:env"
})
public interface ScreenshotAssertionProperties extends Config {

    @Key("io.github.rovner.screenshot.assert.references.base.dir")
    @DefaultValue("src/test/resources/references")
    String referencesBaseDir();

    @Key("io.github.rovner.screenshot.assert.is.update.reference.image")
    @DefaultValue("false")
    boolean isUpdateReferenceImage();

    @Key("io.github.rovner.screenshot.assert.is.save.reference.image.when.missing")
    @DefaultValue("true")
    boolean isSaveReferenceImageWhenMissing();

    @Key("io.github.rovner.screenshot.assert.is.soft")
    @DefaultValue("false")
    boolean isSoft();

    static ScreenshotAssertionProperties properties() {
        return ConfigFactory.create(ScreenshotAssertionProperties.class);
    }
}
