[![build and analyse](https://github.com/terrestris/terrestris-utils/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/terrestris/terrestris-utils/actions/workflows/build.yml) [![Coverage Status](https://coveralls.io/repos/terrestris/terrestris-utils/badge.svg)](https://coveralls.io/r/terrestris/terrestris-utils)

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

# Deployment

Artifacts are deployed manually to the terrestris Reposilite instance (`https://maven.terrestris.de/releases` and `https://maven.terrestris.de/snapshots`).

Add a server entry with a Reposilite token that has write access to your `~/.m2/settings.xml` for each of the ids `reposilite-repository-releases` and `reposilite-repository-snapshots`:

```xml
<server>
  <id>reposilite-repository-releases</id>
  <username>YOUR_USER</username>
  <password>YOUR_TOKEN</password>
</server>
<server>
  <id>reposilite-repository-snapshots</id>
  <username>YOUR_USER</username>
  <password>YOUR_TOKEN</password>
</server>
```

## Release

Use the Maven release plugin. It sets the release version, tags it, deploys
the artifacts (`deploy` goal) and bumps to the next snapshot version:

```sh
mvn release:prepare
mvn release:perform
```

