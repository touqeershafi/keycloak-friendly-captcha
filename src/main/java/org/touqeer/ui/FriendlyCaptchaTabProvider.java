package org.touqeer.ui;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.services.ui.extend.UiTabProvider;
import org.keycloak.services.ui.extend.UiTabProviderFactory;
import org.touqeer.config.FriendlyCaptchaConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendlyCaptchaTabProvider implements UiTabProvider, UiTabProviderFactory<ComponentModel> {

    public static final String PROVIDER_ID = "Friendly Captcha Config";
    private static final Logger logger = Logger.getLogger(FriendlyCaptchaTabProvider.class);



    @Override
    public String getHelpText() {
        return "Configure Friendly Captcha settings for this realm";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        final ProviderConfigurationBuilder builder = ProviderConfigurationBuilder.create();
        builder.property()
                .name(FriendlyCaptchaConfig.SITE_KEY)
                .label("Site Key")
                .helpText("Site Key from Friendly Captcha Console")
                .type(ProviderConfigProperty.STRING_TYPE)
                .add();

        builder.property()
                .name(FriendlyCaptchaConfig.SITE_SECRET)
                .label("Api Key")
                .helpText("API Secret from Friendly Captcha Console")
                .type(ProviderConfigProperty.PASSWORD)
                .add();

        builder.property()
                .name(FriendlyCaptchaConfig.SITE_VALIDATION_ENDPOINT)
                .label("Endpoint")
                .helpText("Validation endpoint")
                .type(ProviderConfigProperty.LIST_TYPE)
                .options(List.of("GLOBAL", "EU"))
                .add();

        builder.property()
                .name(FriendlyCaptchaConfig.FRIENDLY_CAPTCHA_SCRIPT)
                .label("Script URL")
                .helpText("Javascript SDK Url, you can use CDN link")
                .defaultValue(FriendlyCaptchaConfig.FRIENDLY_CAPTCHA_DEFAULT_SCRIPT_URL)
                .type(ProviderConfigProperty.STRING_TYPE)
                .add();

        return builder.build();
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {
        // No cleanup needed
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getPath() {
        return "/:realm/realm-settings/:tab?";
    }

    @Override
    public Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("tab", "friendly-captcha");
        return params;
    }
}

