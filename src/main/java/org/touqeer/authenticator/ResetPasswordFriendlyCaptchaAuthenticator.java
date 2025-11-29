package org.touqeer.authenticator;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.resetcred.ResetCredentialChooseUser;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.utils.FormMessage;
import org.touqeer.captcha.CaptchaStatus;
import org.touqeer.config.FriendlyCaptchaConfig;
import org.touqeer.form.FormUtility;
import org.touqeer.service.FriendlyCaptchaService;

import java.util.Map;

import static org.touqeer.form.FormUtility.enableCaptcha;

public class ResetPasswordFriendlyCaptchaAuthenticator extends ResetCredentialChooseUser implements Authenticator {

    private static final String IS_ENABLED = "IS_ENABLED";
    private static final Logger logger = Logger.getLogger(ResetPasswordFriendlyCaptchaAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        if(isCaptchaRequired(context)) {
            FriendlyCaptchaConfig friendlyCaptchaConfig = new FriendlyCaptchaConfig(context.getRealm());
            enableCaptcha(friendlyCaptchaConfig, context.form());
        }

        super.authenticate(context);
    }

    @Override
    public void action(AuthenticationFlowContext context) {

        if(isCaptchaRequired(context)) {
            FriendlyCaptchaConfig friendlyCaptchaConfig = new FriendlyCaptchaConfig(context.getRealm());
            enableCaptcha(friendlyCaptchaConfig, context.form());

            CaptchaStatus captchaStatus = null;
            FriendlyCaptchaService friendlyCaptchaService = new FriendlyCaptchaService(friendlyCaptchaConfig);
            captchaStatus = friendlyCaptchaService.verify(context.getHttpRequest().getDecodedFormParameters().getFirst(FormUtility.FRIENDLY_CAPTCHA_FORM_KEY));

            if (captchaStatus != null && captchaStatus != CaptchaStatus.SUCCESS) {
                context.challenge(context.form()
                        .addError(new FormMessage(captchaStatus.name().toLowerCase()))
                        .createPasswordReset());
                return;
            }
        }

        super.action(context);
    }

    private boolean isCaptchaRequired(AuthenticationFlowContext context) {

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
