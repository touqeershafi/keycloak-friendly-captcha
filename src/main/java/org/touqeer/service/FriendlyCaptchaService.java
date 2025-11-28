package org.touqeer.service;

import com.friendlycaptcha.jvm.sdk.FriendlyCaptchaClient;
import com.friendlycaptcha.jvm.sdk.VerifyResult;
import org.jboss.logging.Logger;
import org.keycloak.models.KeycloakSession;
import org.touqeer.captcha.CaptchaStatus;
import org.touqeer.config.FriendlyCaptchaConfig;

public class FriendlyCaptchaService {

    private static final Logger logger = Logger.getLogger(FriendlyCaptchaService.class);

    FriendlyCaptchaConfig config;

    public FriendlyCaptchaService(FriendlyCaptchaConfig config) {
        this.config = config;
    }



    public CaptchaStatus verify(String solution) {

        if (solution == null || solution.isEmpty()) {
            logger.warn("Friendly Captcha solution is missing");
            return CaptchaStatus.SOLUTION_EMPTY;
        }

        try {
            FriendlyCaptchaClient friendlyCaptchaClient = new FriendlyCaptchaClient(config.getFriendlyCaptchaClientOptions());
            VerifyResult verifyResult = friendlyCaptchaClient.verifyCaptchaResponse(solution).get();

            if (!verifyResult.wasAbleToVerify()) {
                logger.errorf("[FriendlyCaptcha] Captcha Challenge was not able to verify");
                return CaptchaStatus.WAS_NOT_ABLE_TO_VERIFY;
            } else if (!verifyResult.shouldAccept()) {
                logger.errorf("Captcha challenge was not able to accept");
                return CaptchaStatus.WAS_NOT_ABLE_TO_ACCEPT;
            }

            return CaptchaStatus.SUCCESS;

        } catch (Exception e) {
            logger.error("Error verifying Friendly Captcha", e);
            return CaptchaStatus.ERROR;
        }
    }

}
