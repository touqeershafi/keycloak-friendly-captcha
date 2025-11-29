package org.touqeer.config;


import com.friendlycaptcha.jvm.sdk.FriendlyCaptchaClientOptions;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.RealmModel;
import org.touqeer.ui.FriendlyCaptchaTabProvider;

import java.util.Optional;

public class FriendlyCaptchaConfig {

    public static final String SITE_KEY = "friendly-captcha-site-key";
    public static final String SITE_SECRET = "friendly-captcha-site-secret";
    public static final String SITE_VALIDATION_ENDPOINT = "friendly-captcha-site-validation-endpoint";
    public static final String FRIENDLY_CAPTCHA_SCRIPT = "friendly-captcha-script-url";

    private static final String GLOBAL_VALIDATION_ENDPOINT = "https://global.frcapi.com/api/v2/captcha/siteverify";
    private static final String EU_VALIDATION_ENDPOINT = "https://eu.frcapi.com/api/v2/captcha/siteverify";
    public static final String FRIENDLY_CAPTCHA_DEFAULT_SCRIPT_URL = "https://cdn.jsdelivr.net/npm/@friendlycaptcha/sdk@0.1.36/site.compat.min.js";

    private static final Logger logger = Logger.getLogger(FriendlyCaptchaConfig.class);

    RealmModel realmModel;
    ComponentModel componentModel;

    public FriendlyCaptchaConfig(RealmModel realmModel) {
        this.realmModel = realmModel;

        componentModel = realmModel.getComponentsStream()
                .filter(component ->
                        FriendlyCaptchaTabProvider.PROVIDER_ID.equals(component.getName()) ||
                                FriendlyCaptchaTabProvider.PROVIDER_ID.equals(component.getProviderId())
                )
                .findFirst()
                .orElse(null);
    }

    public FriendlyCaptchaClientOptions getFriendlyCaptchaClientOptions() {
        FriendlyCaptchaClientOptions config = new FriendlyCaptchaClientOptions();
        String endpoint = "EU".equals(getRegion()) ? EU_VALIDATION_ENDPOINT : GLOBAL_VALIDATION_ENDPOINT;
        config.setApiEndpoint(endpoint);
        config.setSitekey(getSiteKey());
        config.setApiKey(getSiteSecret());

        return config;
    }

    public String getSiteKey() {
        return componentModel.get(SITE_KEY);
    }

    public String getSiteSecret() {
        return componentModel.get(SITE_SECRET);
    }

    public String getScriptUrl() {
        String script = componentModel.get(FRIENDLY_CAPTCHA_SCRIPT);
        if(script == null || script.isEmpty()) {
            return FRIENDLY_CAPTCHA_DEFAULT_SCRIPT_URL;
        }

        return script;
    }

    public String getRegion() {
        return Optional.ofNullable(componentModel.get(SITE_VALIDATION_ENDPOINT)).orElse("GLOBAL")
                .toLowerCase();
    }
}
