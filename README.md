[![build and analyse](https://github.com/github/docs/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/terrestris/terrestris-utils/actions/workflows/build.yml) [![Coverage Status](https://coveralls.io/repos/terrestris/terrestris-utils/badge.svg?branch=main)](https://coveralls.io/r/terrestris/terrestris-utils?branch=main)

# Basic Java utilities

Simple jar module for generic Java utilities that can be used in many projects.

Currently, this includes a simple `ZipUtils` class to extract zip files into a folder and
a small `XmlUtils` class with a couple of StAX and DOM utility functions.

## Usage

### Maven

```xml
            <dependency>
                <groupId>de.terrestris</groupId>
                <artifactId>terrestris-utils</artifactId>
                <version>0.3.1</version>
            </dependency>
```

Note that log4j2 is used for logging, make sure you add that dependency to your project.
