package io.github.touqeershafi.form;

import org.keycloak.Config;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormActionFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class RegistrationFormCaptchaActionFactory implements FormActionFactory {

    private static final String PROVIDER_ID = "friendly-captcha-registration-action";

    private final static List<ProviderConfigProperty> CONFIG_PROPERTIES = new ArrayList<>();

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setDefaultValue("Disabled");
        property.setName(RegistrationFormCaptchaAction.IS_ENABLED);
        property.setLabel("Enable");
        property.setType(ProviderConfigProperty.LIST_TYPE);
        property.setOptions(List.of("Enabled", "Disabled"));
        property.setHelpText("Enable friendly captcha on login form");
        CONFIG_PROPERTIES.add(property);
    }

    @Override
    public String getDisplayType() {
        return "Friendly Captcha Registration Form";
    }

    @Override
    public String getReferenceCategory() {
        return "recaptcha";
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[] {
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.DISABLED
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Enables Friendly Captcha on the Registration Form";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public FormAction create(KeycloakSession session) {
        return new RegistrationFormCaptchaAction();
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
