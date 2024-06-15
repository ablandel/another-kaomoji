# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

Add the changes here.

### Added

- Configuration of the default logger with rolling policy to trace server logs in a file

### Updated

- Update of the dev tools configuration - removing of the broken bootRunDev `gradle` task
- Add `kaomojis` mappedBy association in the `Tag` table (to `Kaomoji` table)
- Add `@Transaction` annotation on `Tag` and `Kaomoji` services CREATE, DELETE and UPDATE/REPLACE methods

### Fix

- Fix N+1 request problem during `Kaomoji` entity fetch
- Prevent update or replacement of resources in the services if attributes already used by other resources

## [1.1.0] - 2024-06-07

### Added

- Configuration of dev tools to easier developments

### Updated

- Update of the dependencies

## [1.0.0] - 2024-05-02

### Added

- First version of the kaomoji CRUD endpoints
- First version of the kaomoji GraphQL endpoints
- First version of the tag CRUD endpoints
- First version of the tag GraphQL endpoints

[unreleased]: https://github.com/ablandel/another-kaomoji/compare/1.1.0..main

[1.1.0]: https://github.com/ablandel/another-kaomoji/compare/1.0.0..1.1.0

[1.0.0]: https://github.com/ablandel/another-kaomoji/tree/1.0.0
