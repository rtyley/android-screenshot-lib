# Android Screenshot library

A library intended to provide on-demand screenshots during Android integration tests.

This framework uses ddmlib to capture screenshots from your device, and addresses some issues around 
taking screenshots with ddmlib:

* ddmlib image capture is slow, around 600ms+ per image
* if the device screen is updating, the captured image is likely to show
  a partially-updated framebuffer
* ddmlib is invoked on the development environment side of your setup
  (ie your machine executing the Maven build) - your app code can't
  easily take screenshots itself, but in order to get best screenshot
  results your device screen should not be changing when the screenshot
  is taken

## How it works

The system is in two parts, _paparazzo & celebrity_. Crucially, the paparazzo _only takes photos
when the celebrity asks_.

* _**paparazzo** - taking the photos_ : your desktop development environment runs an
  [OnDemandScreenshotService](https://github.com/rtyley/android-screenshot-lib/blob/master/paparazzo/src/main/java/com/github/rtyley/android/screenshot/paparazzo/OnDemandScreenshotService.java),
  using ddmlib to listen for log messages tagged with the special tag `screenshot_request`.
* _**celebrity** - the subject being photographed_ : your Android device runs your integration tests as it would normally, 
  just writing a `screenshot_request` log message whenever it wants a screenshot taken -  the _paparazzo_ service then 
  obliges by capturing an image from the device. 

To get the best results, your test code should pause for a second after writing the log message, so that the screenshot
is of a stationary screen. That simple operation is wrapped up for convenience in the
[Screenshots](https://github.com/rtyley/android-screenshot-lib/blob/master/celebrity/src/main/java/com/github/rtyley/android/screenshot/celebrity/Screenshots.java)
class packaged into the [android-screenshot-celebrity](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22android-screenshot-celebrity%22) artifact - but you don't _have_ to use it, if you want to write the log-and-sleeping statements yourself.

## Usage (for app developers)

Your app is the _celebrity_ here - add `poseForScreenshot()` calls to your Android integration tests wherever you want a screenshot taken.

See this [example robotium test](https://github.com/github/gauges-android/blob/gauges-android-1.1/integration-tests/src/main/java/com/github/mobile/gauges/test/AppearanceTest.java#L88-109)
from the open-source _Gaug.es_ android app by GitHub - once the build finishes, the resulting animated-gif
looks like this:

![animated gif](http://f.cl.ly/items/3k3j2N26242K1z1A3y2U/android-screenshot-lib.gif)

You'll need to ensure that you're using a build system (ie Maven) that includes the _paparazzo_
`OnDemandScreenshotService`. If you're not, there'll be no negative
effect - you're just writing log messages - but no screenshots will be taken.

## Build systems

The [android-maven plugin](http://code.google.com/p/maven-android-plugin/) will include
support for `android-screenshot-lib` as of [v3.1.2](http://code.google.com/p/maven-android-plugin/wiki/Changelog#Android_Maven_Plugin_3.1.2),
capturing screenshots during the integration-tests step. _([pull-request 104](https://github.com/jayway/maven-android-plugin/pull/104))_
