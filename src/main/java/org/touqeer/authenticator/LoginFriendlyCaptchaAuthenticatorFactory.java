package org.touqeer.authenticator;


import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class LoginFriendlyCaptchaAuthenticatorFactory implements AuthenticatorFactory {

    private final static String PROVIDER_ID = "friendly-captcha-login";

    private final static List<ProviderConfigProperty> CONFIG_PROPERTIES = new ArrayList<>();

    static {

        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setDefaultValue("Disabled");
        property.setName(LoginFriendlyCaptchaAuthenticator.IS_ENABLED);
        property.setLabel("Enable");
        property.setType(ProviderConfigProperty.LIST_TYPE);
        property.setOptions(List.of("Enabled", "Disabled"));
        property.setHelpText("Enable friendly captcha on login form");
        CONFIG_PROPERTIES.add(property);

        property = new ProviderConfigProperty();
        property.setDefaultValue(3);
        property.setName(LoginFriendlyCaptchaAuthenticator.MAX_ATTEMPTS_BEFORE_CAPTCHA);
        property.setLabel("Max Attempts");
        property.setHelpText("Enable friendly captcha on login form, Zero Means always show");
        CONFIG_PROPERTIES.add(property);
    }

    @Override
    public String getDisplayType() {
        return "Username and Password form with Friendly Captcha";
    }

    @Override
    public String getReferenceCategory() {
        return PasswordCredentialModel.TYPE;
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[] {
                AuthenticationExecutionModel.Requirement.REQUIRED
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Authenticate user and verify captcha challenge";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public Authenticator create(KeycloakSession keycloakSession) {
        return new LoginFriendlyCaptchaAuthenticator();
    }

    @Override
    public void init(Config.Scope scope) {

    }

    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
