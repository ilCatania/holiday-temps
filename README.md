[![Build](https://github.com/ilCatania/holiday-temps/actions/workflows/ci.yml/badge.svg)](https://github.com/ilCatania/holiday-temps/actions/workflows/ci.yml)

Holiday temperatures
====================

A small REST service that returns bank holidays for an input time period and location, and the corresponding max and min
temperatures.

How to run
=========

Make sure you have Gradle version 6.8 or later installed, or that you are able to download and run Gradle packages via
the included wrapper.

To run the web service locally, execute `gradle bootRun`, you will then be able to query the webservice, for example:

```bash
curl -H "Accept: application/json" "http://localhost:8080/bank-holidays/London/temps?startDate=2020-01-01&endDate=2020-01-30"
```

Known issues
============

Please see the Issues tab on GitHub for a list of known issues and potential improvements.
