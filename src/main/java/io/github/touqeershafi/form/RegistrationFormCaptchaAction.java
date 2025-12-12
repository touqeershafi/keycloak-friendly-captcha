package io.github.touqeershafi.form;

import io.github.touqeershafi.captcha.CaptchaStatus;
import io.github.touqeershafi.config.FriendlyCaptchaConfig;
import io.github.touqeershafi.service.FriendlyCaptchaService;
import io.github.touqeershafi.utility.FormUtility;
import jakarta.ws.rs.core.MultivaluedMap;
import org.jboss.logging.Logger;
import org.keycloak.authentication.FormAction;
import org.keycloak.authentication.FormContext;
import org.keycloak.authentication.ValidationContext;
import org.keycloak.events.Errors;
import org.keycloak.forms.login.LoginFormsProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.FormMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.touqeershafi.utility.FormUtility.enableCaptcha;

public class RegistrationFormCaptchaAction implements FormAction {

    private static final Logger logger = Logger.getLogger(RegistrationFormCaptchaAction.class);
    public static final String IS_ENABLED = "IS_ENABLED";

    @Override
    public void buildPage(FormContext context, LoginFormsProvider form) {
        if(isCaptchaRequired(context)) {
            FriendlyCaptchaConfig friendlyCaptchaConfig = new FriendlyCaptchaConfig(context.getRealm());
            enableCaptcha(friendlyCaptchaConfig, form);
        }
    }

    @Override
    public void validate(ValidationContext context) {
        if(!isCaptchaRequired(context)) {
            context.success();
            return;
        }

        FriendlyCaptchaConfig friendlyCaptchaConfig = new FriendlyCaptchaConfig(context.getRealm());
        FriendlyCaptchaService friendlyCaptchaService = new FriendlyCaptchaService(friendlyCaptchaConfig);
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        CaptchaStatus captchaStatus = friendlyCaptchaService.verify(formData.getFirst(FormUtility.FRIENDLY_CAPTCHA_FORM_KEY));

        List<FormMessage> errors = new ArrayList<>();

        if (captchaStatus != null && captchaStatus != CaptchaStatus.SUCCESS) {
            errors.add(new FormMessage(captchaStatus.name().toLowerCase()));
            context.error(Errors.INVALID_REGISTRATION);
            context.validationError(formData, errors);
            context.excludeOtherErrors();
            return;
        }

        context.success();
    }

    @Override
    public void success(FormContext context) {

    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {

    }

    @Override
    public void close() {

    }

    private boolean isCaptchaRequired(FormContext context) {

        AuthenticatorConfigModel authenticatorConfig = context.getAuthenticatorConfig();

        if (authenticatorConfig == null) {
            logger.debug("[FriendlyCaptcha] Authentication model config is null");
            return false;
        }

        Map<String, String> config = authenticatorConfig.getConfig();

        String isEnabled = config.getOrDefault(IS_ENABLED, "Disabled");
        if ("disabled".equalsIgnoreCase(isEnabled)) {
            logger.debug("[FriendlyCaptcha] Captcha is disabled");
            return false;
        }

        return true;
    }
}
