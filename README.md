## Screenshot assert

Java library for screenshot based testing with selenium and allure report

### How to:
1. Add dependency to your project
Maven:
```xml
<dependency>
    <groupId>io.github.rovner</groupId>
    <artifactId>screenshot-assert-core</artifactId>
    <version>0.0.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.github.rovner</groupId>
    <artifactId>screenshot-assert-junit5</artifactId>
    <version>0.0.1</version>
    <scope>test</scope>
</dependency>
```
Gradle:
```groovy
testImplementation 'io.github.rovner:screenshot-assert-core:0.0.1'
testImplementation 'io.github.rovner:screenshot-assert-junit5:0.0.1'
```

2. Create assertion:
With junit5:
```java
@RegisterExtension
ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> webDriver);

@Test
void testFullPageScreenshot() {
    //test actions
    screenshotAssert.assertThat(Screenshots.screenshotOfWholePage())
            .isEqualToReferenceId("screenshot_id");
}
```
See more in [examples](examples/src/test/java/io/github/rovner/screenshot/assertions/examples/junit5)

Without test framework:
```java
ScreenshotAssert.builder()
    .webDriver(webDriver)
    .screenshot(Screenshots.screenshotOfWholePage())
    .references(Paths.get("path/to/references/dir"))
    .imageDiffer(new DefaultImageDiffer())
    .allureListener(new DefaultAllureListener())
    .build()
    .isEqualToReferenceId("screenshot_id");
```
See more in [examples](examples/src/test/java/io/github/rovner/screenshot/assertions/examples/plain)