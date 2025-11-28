package org.touqeer.authenticator;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.models.AuthenticatorConfigModel;
import org.jboss.logging.Logger;
import org.keycloak.models.utils.FormMessage;
import org.touqeer.captcha.CaptchaStatus;
import org.touqeer.config.FriendlyCaptchaConfig;
import org.touqeer.form.FormUtility;
import org.touqeer.service.FriendlyCaptchaService;

import java.util.Map;

import static org.touqeer.form.FormUtility.AUTH_NOTE_ATTEMPT_COUNT;
import static org.touqeer.form.FormUtility.enableCaptcha;

public class LoginFriendlyCaptchaAuthenticator extends UsernamePasswordForm implements Authenticator {

    public static final String IS_ENABLED = "IS_ENABLED";
    public static final String MAX_ATTEMPTS_BEFORE_CAPTCHA = "MAX_ATTEMPTS_BEFORE_CAPTCHA";

    private static final Logger logger = Logger.getLogger(LoginFriendlyCaptchaAuthenticator.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        boolean captchaRequired = isCaptchaRequired(context);
        if (captchaRequired) {
            FriendlyCaptchaConfig friendlyCaptchaConfig = new FriendlyCaptchaConfig(context.getRealm());
            enableCaptcha(friendlyCaptchaConfig, context.form());
        }

        super.authenticate(context);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        boolean captchaRequired = isCaptchaRequired(context);
        FriendlyCaptchaConfig friendlyCaptchaConfig = new FriendlyCaptchaConfig(context.getRealm());
        CaptchaStatus captchaStatus = null;
        if (captchaRequired) {
            enableCaptcha(friendlyCaptchaConfig, context.form());

            FriendlyCaptchaService friendlyCaptchaService = new FriendlyCaptchaService(friendlyCaptchaConfig);
            captchaStatus = friendlyCaptchaService.verify(context.getHttpRequest().getDecodedFormParameters().getFirst(FormUtility.FRIENDLY_CAPTCHA_FORM_KEY));

            if (captchaStatus != null && captchaStatus != CaptchaStatus.SUCCESS) {
                context.challenge(context.form()
                        .addError(new FormMessage(captchaStatus.name().toLowerCase()))
                        .createLoginUsernamePassword());
                return;
            }
        }

        incrementAttempt(context);
        boolean captchaRequiredForNext = isCaptchaRequired(context);
        if (captchaRequiredForNext) {
            enableCaptcha(friendlyCaptchaConfig, context.form());
        }

        String executionIdBefore = context.getExecution().getId();

        super.action(context);

        boolean flowSucceeded = (context.getUser() != null) ||
                (!executionIdBefore.equals(context.getExecution().getId()));

        if (flowSucceeded) {
            logger.debugf("[FriendlyCaptcha] Authentication flow succeeded, resetting attempt count");
            resetAttempt(context);
        }
    }

    private int getMaxAttemptsBeforeCaptcha(AuthenticationFlowContext context) {
        AuthenticatorConfigModel authenticatorConfig = context.getAuthenticatorConfig();

        if (authenticatorConfig == null) {
            logger.debug("Authenticator config is null, returning -1");
            return -1;
        }

        Map<String, String> config = authenticatorConfig.getConfig();
        String maxAttemptsStr = config.get(MAX_ATTEMPTS_BEFORE_CAPTCHA);

        if (maxAttemptsStr == null || maxAttemptsStr.trim().isEmpty()) {
            logger.debug("MAX_ATTEMPTS_BEFORE_CAPTCHA not found in config, using default: 0 (always show)");
            return 0; // Default: always show captcha
        }

        try {
            int maxAttempts = Integer.parseInt(maxAttemptsStr.trim());
            logger.debugf("Max attempts before captcha: %d", maxAttempts);

            if (maxAttempts <= 0) {
                logger.warnf("Invalid max attempts value: %d, treating as 0 (always show)", maxAttempts);
                return 0;
            }

            return maxAttempts;
        } catch (NumberFormatException e) {
            logger.errorf("Failed to parse MAX_ATTEMPTS_BEFORE_CAPTCHA value '%s' as integer, using default: 0",
                    maxAttemptsStr);
            return 0; // Default: always show captcha on parse error
        }
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

        int maxAttempts = getMaxAttemptsBeforeCaptcha(context);
        logger.debugf("[FriendlyCaptcha] Config value before captcha is required: %d", maxAttempts);
        if (maxAttempts == 0) {
            logger.debug("[FriendlyCaptcha] Captcha is required because value is 0");
            return true;
        } else if (maxAttempts > 0) {

            int currentAttemptsCount = currentAttemptsCount(context);
            logger.debugf("[FriendlyCaptcha] Checking attempts %d > %d", currentAttemptsCount, maxAttempts);
            return currentAttemptsCount >= maxAttempts;
        }

        return false;
    }

    private int currentAttemptsCount(AuthenticationFlowContext context) {
        String attemptsCountStr = context.getAuthenticationSession().getAuthNote(AUTH_NOTE_ATTEMPT_COUNT);

        if (attemptsCountStr == null || attemptsCountStr.trim().isEmpty()) {
            logger.debug("No attempts count found in auth notes, returning 0");
            return 0;
        }

        try {
            int attempts = Integer.parseInt(attemptsCountStr.trim());
            logger.debugf("Current attempts count from auth notes: %d", attempts);

            if (attempts < 0) {
                logger.warnf("Invalid attempts count value: %d, treating as 0", attempts);
                return 0;
            }

            return attempts;
        } catch (NumberFormatException e) {
            logger.errorf("Failed to parse attempts count value '%s' as integer, using default: 0", attemptsCountStr);
            return 0;
        }
    }

    private void incrementAttempt(AuthenticationFlowContext context) {
        int currentAttemptsCount = currentAttemptsCount(context);
        ++currentAttemptsCount;
        context.getAuthenticationSession().setAuthNote(AUTH_NOTE_ATTEMPT_COUNT, String.valueOf(currentAttemptsCount));
    }

    private void resetAttempt(AuthenticationFlowContext context) {
        context.getAuthenticationSession().setAuthNote(AUTH_NOTE_ATTEMPT_COUNT, "0");
    }

}
