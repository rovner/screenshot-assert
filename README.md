## Screenshot assert

Java library for screenshot based testing with selenium and allure report
<br>
Benefits:

- Easy screenshot comparison for page elements from the box.
- Possibility to ignore different diffs by element, area or diff hash code.
- Allure report [integration](https://github.com/allure-framework/allure2/tree/master/plugins/screen-diff-plugin) from
  the box.
- Easy customization for any part.

### Dependency

Maven:

```xml

<dependency>
    <groupId>io.github.rovner</groupId>
    <artifactId>screenshot-assert-core</artifactId>
    <version>${screenshotAssertVersion}</version>
    <scope>test</scope>
</dependency>

        <!--extension for junit 5-->
<dependency>
<groupId>io.github.rovner</groupId>
<artifactId>screenshot-assert-junit5</artifactId>
<version>${screenshotAssertVersion}</version>
<scope>test</scope>
</dependency>
```

Gradle:

```groovy
testImplementation 'io.github.rovner:screenshot-assert-core:${screenshotAssertVersion}'
//extension for junit 5
testImplementation 'io.github.rovner:screenshot-assert-junit5:${screenshotAssertVersion}'
```

### Usage

- Junit5 with hard assertions

```java
//junit5 with hard assertions
@RegisterExtension
ScreenshotAssertExtension screenshotAssert=new ScreenshotAssertExtension(()->webDriver);

@Test
void test(){
    //test actions 
    screenshotAssert.assertThat(screenshotOfViewport())
        .isEqualToReferenceId("some_reference_id");
}
```

- Junit5 with soft assertions

```java
@RegisterExtension
private final SoftScreenshotAssertExtension softScreenshotAssert=new SoftScreenshotAssertExtension(()->wd);

@Test
void test(){
    //test actions
    screenshotAssert.assertThat(screenshotOfElementFoundBy(cssSelector(".element-1")))
        .isEqualToReferenceId("element_reference_1");
    screenshotAssert.assertThat(screenshotOfElementFoundBy(cssSelector(".element-2")))
        .isEqualToReferenceId("element_reference_2");
    }
```

- No framework with hard assertions

```java
ScreenshotAssertBuilder screenshotAssert=ScreenshotAssertBuilder.builder()
    .setWebDriver(wd)
    .setReferenceStorage(new DefaultReferenceStorage(Paths.get("some/path/to/references")));

@Test
void test(){
    //test actions
    screenshotAssert.assertThat(screenshotOfViewport())
        .isEqualToReferenceId("some_reference_id");
    }
```

- No framework with soft assertions

```java
ScreenshotAssertBuilder screenshotAssert=ScreenshotAssertBuilder.builder()
    .setWebDriver(wd)
    .setReferenceStorage(new DefaultReferenceStorage(Paths.get("some/path/to/references")))
    .setSoft(true);

@Test
void test(){
    //test actions
    screenshotAssert.assertThat(screenshotOfElementFoundBy(cssSelector(".element-1")))
        .setReferenceStorage("element_reference_1");
    screenshotAssert.assertThat(screenshotOfElementFoundBy(cssSelector(".element-2")))
        .isEqualToReferenceId("element_reference_2");
}
```

See more in [examples](examples/src/test/java/io/github/rovner/screenshot/assertions/examples/)

### Types of screenshots

- Of viewport (part of the page visible in window)

```java
screenshotAssert.assertThat(Screenshots.screenshotOfViewport())
    .isEqualToReferenceId("id");
```
- Of whole page (document). Be cautious with method because it scrolls page and could change it state.  

```java
screenshotAssert.assertThat(Screenshots.screenshotOfWholePage())
    .isEqualToReferenceId("id");
```

- Of some area by coordinates in the view port. Coordinates are relative to the viewport.

```java
screenshotAssert.assertThat(Screenshots.screenshotOfViewportArea(20,140,640,120))
    .isEqualToReferenceId("id");
```

- Of some area by coordinates in the whole page. Coordinates are relative to the document.

```java
screenshotAssert.assertThat(Screenshots.screenshotOfOageArea(20,140,640,120))
    .isEqualToReferenceId("id");
```

- Of web element

```java
WebElement element=webDriver.findElement(cssSelector(".element"));
screenshotAssert.assertThat(Screenshots.screenshotOfElement(element))
    .isEqualToReferenceId("id");
```

- Of web element locatable by selector

```java
screenshotAssert.assertThat(Screenshots.screenshotOfElementFoundBy(cssSelector(".element")))
    .isEqualToReferenceId("id");
```

See all [types](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/screenshot/Screenshots.java)

### Types of ignoring

- Of viewport area

```java
screenshotAssert.assertThat(screenshotOfViewport())
    .ignoring(area(10,10,40,50))
    .ignoring(area(new Rectangle(60,30,10,20))) //pay attention to order of arguments
    .isEqualToReferenceId("id");
```

- Of element/elements

```java
WebElement element=webDriver.findElement(cssSelector(".element"));
screenshotAssert.assertThat(screenshotOfViewport())
    .ignoring(element(element))
    .isEqualToReferenceId("id");
```

- Of elements found by selector

```java
screenshotAssert.assertThat(screenshotOfViewport())
    .ignoring(elementBy(cssSelector(".element")))
    .isEqualToReferenceId("id");
```

- Of diff hash codes

```java
screenshotAssert.assertThat(screenshotOfViewport())
    .ignoring(hashes(123,321)) //diff hash could be found in allure report
    .isEqualToReferenceId("id");
```

### Configuration

Add file named `screenshot-assert.properties` to resources dir or use `-D` properties

- Property: `io.github.rovner.screenshot.assert.is.update.reference.image`.
  <br>Default: `false`.
  <br>Whatever save actual image as reference when there is difference. Useful to update images after changes.
  <br><br>
- Property: `io.github.rovner.screenshot.assert.is.save.reference.image.when.missing`.
  <br>Default: `true`.
  <br>Whatever save actual image as reference when no reference exists. Recommended to set to `false` on CI.
  <br><br>
- Property: `io.github.rovner.screenshot.assert.is.soft`.
  <br>Default: `false`.
  <br>Makes all assertions soft.
  <br><br>
- Property: `io.github.rovner.screenshot.assert.references.base.dir`
  <br>Default: `src/test/resources/references`
  <br>[Only for junit5] Base directory where screenshots will be stored.

### Mobile browsers
Screenshots provided by appium from mobile devices besides the viewport contain additional 
bars(phone bar, browser bar) as well. The height and location of these bars are different on
different browsers/devices so such configurations should be done in the target test/framework
with knowledge of what devices will be used. Here are some examples of how it could be achieved:
```java
@RegisterExtension
private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
    .addPlatformScreenshooter("ipad-safari",
        //ipad contains only top header which height could be different depending on browser scroll position
        new CuttingBrowserScreenshooter(ofMillis(100), floatingHeader()) { 
            @Override
            public boolean accept(Capabilities capabilities) {
                return isIpadSafari(capabilities);
            }
        });
```
```java
@RegisterExtension
private final ScreenshotAssertExtension screenshotAssert = new ScreenshotAssertExtension(() -> wd)
    .addPlatformScreenshooter("iphone-safari",
        //phone contains fixed phone bar at top and floating browser footer at the bottom
        new CuttingBrowserScreenshooter(ofMillis(100), asList(fixedHeader(47), floatingFooter())) {
            @Override
            public boolean accept(Capabilities capabilities) {
                return isIphoneSafari(capabilities);
            }
        });
```
See more in [examples](examples/src/test/java/io/github/rovner/screenshot/assertions/examples/)

### Customizing

- To change how diff is represented in allure implement
  [AllureListener](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/allure/AllureListener.java)
  and set it to
  [ScreenshotAssertBuilder](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ScreenshotAssertBuilder.java)

- To change how screenshot is cropped implement
  [ImageCropper](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/cropper/ImageCropper.java)
  and set it to
  [ScreenshotAssertBuilder](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ScreenshotAssertBuilder.java)

- To change how screenshot is compared to reference implement
  [ImageDiffer](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/diff/ImageDiffer.java)
  and set it to
  [ScreenshotAssertBuilder](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ScreenshotAssertBuilder.java)

- To add custom ignoring implement
  [Ignoring](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ignoring/Ignoring.java)

- To change how references are stored implement
  [ReferenceStorage](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/reference/ReferenceStorage.java)
  and set it to
  [ScreenshotAssertBuilder](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ScreenshotAssertBuilder.java)

- To add custom screenshot implement
  [Screenshot](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/screenshot/Screenshot.java)

- To change how soft assertions are handled implement
  [SoftExceptionCollector](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/soft/SoftExceptionCollector.java)
  and set it to
  [ScreenshotAssertBuilder](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ScreenshotAssertBuilder.java)

- To change how images from browsers/devices with `dpr > 1` are scaled implement
  [ImageScaler](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/scaler/ImageScaler.java)
  and set it to
  [ScreenshotAssertBuilder](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ScreenshotAssertBuilder.java)

- To change how screenshots are taken from device/browser implement
  [PlatformScreenshoter](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/platform/PlatformScreenshoter.java)
  and set it to
  [ScreenshotAssertBuilder](screenshot-assert-core/src/main/java/io/github/rovner/screenshot/assertions/core/ScreenshotAssertBuilder.java)

  