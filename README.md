![Java CI](https://github.com/ilCatania/holiday-temps/workflows/Java%20CI/badge.svg)

Holiday temperatures
====================

A small REST service that returns bank holidays for an input time period and location, and
the corresponding max and min temperatures.

How to run
=========

Make sure you have Gradle version 6.8 or later installed, or that you are able to download
and run Gradle packages via the included wrapper.

To run the web service locally, execute `./gradlew bootRun` then navigate to
`http://localhost:8080/bank-holidays/london/temps`

Known issues
============

Please see the Issues tab on GitHub for a list of known issues and potential improvements.
