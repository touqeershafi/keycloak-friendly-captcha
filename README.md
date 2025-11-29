<h1 align="center">Keycloak Friendly Captcha Extension</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Status-BETA-orange?style=for-the-badge" alt="Beta Status"/>
</p>

<p align="center">
  <strong>A privacy-friendly CAPTCHA integration for Keycloak authentication flows</strong>
</p>

> ⚠️ **BETA Notice:** This extension is currently in beta. While it is functional and tested, please use it with caution in production environments. Feedback and bug reports are welcome!

> 📢 **Disclaimer:** This is a community-developed extension and is **not officially affiliated with or endorsed by Friendly Captcha**. For official Friendly Captcha documentation and support, please visit [friendlycaptcha.com](https://friendlycaptcha.com/).

<p align="center">
  <a href="#features">Features</a> •
  <a href="#screenshots">Screenshots</a> •
  <a href="#compatibility">Compatibility</a> •
  <a href="#installation">Installation</a> •
  <a href="#configuration">Configuration</a> •
  <a href="#changelog">Changelog</a> •
  <a href="#license">License</a>
</p>

---

## Overview

This extension integrates [Friendly Captcha](https://friendlycaptcha.com/) into Keycloak, providing a privacy-respecting, GDPR-compliant CAPTCHA solution for your authentication flows. Unlike traditional CAPTCHAs, Friendly Captcha uses proof-of-work puzzles that are solved automatically by the user's browser, eliminating the need for image recognition challenges.

The extension supports both **login** and **reset password** flows, with configurable attempt thresholds for login and immediate protection for password resets. You can configure custom JavaScript CDN URLs for the Friendly Captcha SDK through the admin console, giving you flexibility in how the CAPTCHA script is loaded.

## Screenshots

<details>
<summary><strong>📸 Click to view screenshots</strong></summary>

### Realm Settings - Friendly Captcha Configuration

Configure your Friendly Captcha credentials (Site Key, API Key, and Region) in the Admin Console:

![Realm Settings - Friendly Captcha Tab](docs/screenshots/friendly-captcha-configuration.png)

### Authentication Flow Setup

Remove the default Username and Password form and replace it with the Friendly Captcha form:

![Authentication Flow](docs/screenshots/login-authentifcation-flow.png)

### Username Password Form Configuration

Configure the Friendly Captcha authenticator settings (Enable/Disable and Max Attempts):

![Username Password Form Configuration](docs/screenshots/username-password-form-configuration.png)

### Login Form with Friendly Captcha

The login form displaying the Friendly Captcha widget after the configured number of failed attempts:

![Login Form with Captcha](docs/screenshots/friendly-captcha-login-form.png)

### Reset Password Form with Friendly Captcha

The password reset form displaying the Friendly Captcha widget:

![Reset Password Form with Captcha](docs/screenshots/friendly-captcha-reset-password-form.png)

### Friendly Captcha Theme

The included `friendly-captcha` theme with the CAPTCHA widget integrated:

![Friendly Captcha Theme](docs/screenshots/friendly-captcha-login-theme.png)

</details>

## Features

### ✅ Currently Implemented

| Feature                             | Description                                                         |
| ----------------------------------- | ------------------------------------------------------------------- |
| **Login Form Protection**           | Username and Password form with integrated Friendly Captcha widget  |
| **Reset Password Protection**       | Password reset form with integrated Friendly Captcha widget         |
| **Configurable Attempts Threshold** | Show CAPTCHA only after a specified number of failed login attempts |
| **Admin Console Integration**       | Configure Friendly Captcha directly from Keycloak's Realm Settings  |
| **Multi-Region Support**            | Support for both Global and EU API endpoints                        |
| **Custom Script URL**               | Configure custom JavaScript CDN URL for Friendly Captcha SDK        |

### 🚧 Roadmap

| Feature                      | Status  |
| ---------------------------- | ------- |
| Registration Form Protection | Planned |

## Compatibility

This extension has been tested with the following Keycloak versions:

| Keycloak Version | Status     | Notes               |
| ---------------- | ---------- | ------------------- |
| 26.4.x           | ✅ Tested  | Fully compatible    |
| 26.3.x           | 🔄 Pending | Testing in progress |

> **Note:** This extension requires Java 17 or higher and uses the `declarative-ui` feature flag.

## Installation

### Prerequisites

- Keycloak 26.x or compatible version
- Java 17+
- Maven 3.6+
- A [Friendly Captcha](https://friendlycaptcha.com/) account with Site Key and API Key

> **Note:** For build and deployment instructions, see the [Building and Deployment](#building-and-deployment) section below.

## Configuration

Configuration is done in two places:

1. **Realm Settings** – Global Friendly Captcha credentials (Site Key, API Key, Region)
2. **Authentication Flow** – Per-flow settings (Enable/Disable, Max Attempts)

---

### Step 1: Configure Friendly Captcha Credentials (Realm Settings)

Navigate to **Realm Settings → Friendly Captcha** tab in the Keycloak Admin Console.

| Setting          | Description                                                                                               |
| ---------------- | --------------------------------------------------------------------------------------------------------- |
| **Site Key**     | Your Friendly Captcha site key from the [Friendly Captcha Console](https://friendlycaptcha.com/dashboard) |
| **API Key**      | Your Friendly Captcha API secret key                                                                      |
| **API Endpoint** | Select `GLOBAL` or `EU` based on your data residency requirements                                         |
| **Script URL**   | JavaScript CDN URL for the Friendly Captcha SDK (defaults to official CDN if not provided)                |

---

### Step 2: Configure Authentication Flow

#### For Login Form

1. Navigate to **Authentication → Flows**
2. Create a copy of the **Browser** flow (or use an existing custom flow)
3. Replace the default **Username Password Form** with **Username and Password form with Friendly Captcha**
4. Click the ⚙️ gear icon to configure the authenticator:

| Setting          | Description                                      | Default    |
| ---------------- | ------------------------------------------------ | ---------- |
| **Enable**       | Enable or disable Friendly Captcha for this flow | `Disabled` |
| **Max Attempts** | Number of failed attempts before showing CAPTCHA | `3`        |

#### For Reset Password Form

1. Navigate to **Authentication → Flows**
2. Create a copy of the **Reset Credentials** flow (or use an existing custom flow)
3. Replace the default **Choose User** authenticator with **Reset Password with Friendly Captcha**
4. Click the ⚙️ gear icon to configure the authenticator:

| Setting    | Description                                      | Default    |
| ---------- | ------------------------------------------------ | ---------- |
| **Enable** | Enable or disable Friendly Captcha for this flow | `Disabled` |

> **Note:** For the reset password flow, the CAPTCHA is shown immediately when enabled (no attempt threshold). This provides protection against automated password reset attacks.

---

### Max Attempts Behavior

The **Max Attempts** setting controls when the CAPTCHA widget is displayed:

| Value | Behavior                                                      |
| ----- | ------------------------------------------------------------- |
| `0`   | **Always show** – CAPTCHA is displayed on every login attempt |
| `1`   | Show CAPTCHA after 1 failed attempt                           |
| `3`   | Show CAPTCHA after 3 failed attempts (default)                |
| `N`   | Show CAPTCHA after N failed attempts                          |

> **Example:** If set to `3`, users will see a standard login form for their first 3 attempts. On the 4th attempt (and beyond), the Friendly Captcha widget will appear.

The attempt counter is reset upon successful authentication.

## Error Messages

The extension provides user-friendly error messages when Friendly Captcha verification fails. These messages are defined in `messages_en.properties` and are displayed on the login form when verification issues occur.

| Error Message Key        | Displayed Message                                                           | When It Occurs                                                                                                              |
| ------------------------ | --------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------- |
| `solution_empty`         | "Please complete the Friendly Captcha challenge"                            | The user submits the login form without completing the CAPTCHA challenge, or the CAPTCHA solution is missing/empty          |
| `was_not_able_to_verify` | "Friendly Captcha verification failed. Please try again."                   | The Friendly Captcha API was unable to verify the solution (e.g., invalid solution format, API communication issue)         |
| `was_not_able_to_accept` | "Friendly Captcha challenge was not accepted. Please try again."            | The solution was verified but should not be accepted (e.g., expired solution, rate limiting, or policy violation)           |
| `error`                  | "An error occurred during Friendly Captcha verification. Please try again." | An unexpected exception occurred during verification (e.g., network timeout, API endpoint unreachable, configuration error) |

> **Note:** All error messages are customizable by editing the `messages_en.properties` file in your theme. The messages use the lowercase enum name from `CaptchaStatus` as the key.

---

## Theme Integration

The extension provides a ready-to-use theme named `friendly-captcha` that includes the Friendly Captcha widget. To use it:

1. Navigate to **Realm Settings → Themes**
2. Set the **Login theme** to `friendly-captcha`
3. Save the settings

### Integrating with Custom Themes

To integrate Friendly Captcha into your own custom theme, add the following snippet to your login form template (typically `login.ftl`) or reset password form template (`login-reset-password.ftl`):

```ftl
<#if friendly_captcha_enabled?? && friendly_captcha_enabled == "enabled">
    <div class="${properties.kcFormGroupClass!}">
        <div id="kc-form-captcha">
            <div class="frc-captcha"
                 data-sitekey="${friendly_captcha_site_key}"
                 data-api-endpoint="${friendly_captcha_endpoint}"
                 lang="${lang}">
            </div>
        </div>
    </div>
</#if>
```

**Template Variables:**

- `friendly_captcha_enabled` - Set to `"enabled"` when CAPTCHA should be displayed
- `friendly_captcha_site_key` - Your Friendly Captcha site key
- `friendly_captcha_endpoint` - The API endpoint (GLOBAL or EU)
- `lang` - The current language code

> **Note:** The Friendly Captcha JavaScript SDK is automatically loaded via the script URL configured in Realm Settings. The script is injected by the authenticator, so you don't need to manually include the `<script>` tag in your templates. If you need to use a custom CDN URL, configure it in **Realm Settings → Friendly Captcha → Script URL**.

The snippet will automatically display the Friendly Captcha widget when enabled based on your authentication flow configuration.

---

## Building and Deployment

### Step 1: Clone the Repository

```bash
git clone https://github.com/touqeershafi/keycloak-friendly-captcha.git
cd keycloak-friendly-captcha
```

### Step 2: Build the Extension

Build the extension using Maven:

```bash
mvn clean package
```

The JAR file will be created at:

```
target/keycloak-friendly-captcha-0.0-SNAPSHOT.jar
```

### Step 3: Deploy to Keycloak

1. **Copy the JAR to Keycloak providers directory:**

   ```bash
   cp target/keycloak-friendly-captcha-0.0-SNAPSHOT.jar /path/to/keycloak/providers/
   ```

2. **Rebuild Keycloak:**

   ```bash
   bin/kc.sh build
   ```

3. **Start Keycloak with the declarative-ui feature:**

   ```bash
   bin/kc.sh start --features=declarative-ui
   ```

> **Note:** Make sure to replace `/path/to/keycloak/` with your actual Keycloak installation path.

## Dependencies

| Dependency               | Version | Purpose                 |
| ------------------------ | ------- | ----------------------- |
| Keycloak Server SPI      | 26.3.0  | Keycloak extension APIs |
| Friendly Captcha JVM SDK | 1.0.0   | CAPTCHA verification    |

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for a detailed list of changes and version history.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- [Friendly Captcha](https://friendlycaptcha.com/) for providing a privacy-friendly CAPTCHA solution
- [Keycloak](https://www.keycloak.org/) for the excellent identity and access management platform

---
