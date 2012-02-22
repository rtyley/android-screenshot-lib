# Android Screenshot library

A library intended to provide on-demand screenshots during Android integration tests.

## Usage

Update your Android integration tests with `poseForScreenshot()` calls wherever you want a screenshot taken.

On your development machine (ie your laptop or desktop) you will need to have
the `OnDemandScreenshotService` running while your integration tests are being run.
The service just listens for 'screenshot_request' log messages from your app,
and when reads one, it'll take a screenshot, and then do whatever you've configured it
to do with the resulting image - for instance save it to a file or create an animated gif.
