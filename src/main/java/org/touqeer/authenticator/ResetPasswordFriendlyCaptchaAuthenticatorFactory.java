package org.touqeer.authenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class ResetPasswordFriendlyCaptchaAuthenticatorFactory implements AuthenticatorFactory {

    private static final String PROVIDER_ID = "friendly-captcha-reset-password";

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
    }

    @Override
    public String getDisplayType() {
        return "Friendly Captcha User Chooser";
    }

    @Override
    public String getReferenceCategory() {
        return "";
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
        return "Choose a user to reset credentials for, with friendly captcha";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new ResetPasswordFriendlyCaptchaAuthenticator();
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }
}
