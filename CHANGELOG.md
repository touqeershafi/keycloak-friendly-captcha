# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

## [0.0] - Reset Password and Realm Settings Improvement

- Reset Password Form Protection: Added Friendly Captcha support for password reset flows
  - New `ResetPasswordFriendlyCaptchaAuthenticator` for protecting password reset requests
  - Dedicated `login-reset-password.ftl` template with CAPTCHA integration
  - Immediate CAPTCHA display when enabled (no attempt threshold)
- Custom Script URL Configuration: Added ability to configure custom JavaScript CDN URL for Friendly Captcha SDK
  - New "Script URL" field in Realm Settings → Friendly Captcha configuration panel
  - Defaults to official Friendly Captcha CDN if not provided
  - Allows using custom CDN endpoints or self-hosted scripts

## [0.0] - Initial Release

### Added

- Login Form Protection: Initial implementation of Friendly Captcha for Keycloak login flows
  - Username and Password form with integrated Friendly Captcha widget
  - Configurable attempt threshold (Max Attempts) before showing CAPTCHA
  - Admin Console integration via Realm Settings → Friendly Captcha tab
  - Support for both Global and EU API endpoints
  - Custom `friendly-captcha` theme with CAPTCHA widget integration
  - Error handling and user-friendly error messages
  - Multi-region support (GLOBAL/EU endpoints)

[Unreleased]: https://github.com/touqeershafi/keycloak-friendly-captcha/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/touqeershafi/keycloak-friendly-captcha/releases/tag/v0.1.0
