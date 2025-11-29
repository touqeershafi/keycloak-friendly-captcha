package org.touqeer.form;

import org.keycloak.forms.login.LoginFormsProvider;
import org.touqeer.config.FriendlyCaptchaConfig;

public class FormUtility {

    private static final String FRIENDLY_CAPTCHA_ENABLED = "friendly_captcha_enabled";
    private static final String FRIENDLY_CAPTCHA_SITE_KEY = "friendly_captcha_site_key";
    private static final String FRIENDLY_CAPTCHA_ENDPOINT = "friendly_captcha_endpoint";

    public static final String AUTH_NOTE_ATTEMPT_COUNT = "attempts_count";
    public static final String FRIENDLY_CAPTCHA_FORM_KEY = "frc-captcha-response";

    public static void enableCaptcha(FriendlyCaptchaConfig friendlyCaptchaConfig, LoginFormsProvider formsProvider) {
        formsProvider.setAttribute(FRIENDLY_CAPTCHA_ENABLED, "enabled");
        formsProvider.setAttribute(FRIENDLY_CAPTCHA_SITE_KEY, friendlyCaptchaConfig.getSiteKey());
        formsProvider.setAttribute(FRIENDLY_CAPTCHA_ENDPOINT, friendlyCaptchaConfig.getRegion());
        formsProvider.addScript(friendlyCaptchaConfig.getScriptUrl());
    }
}
