package com.matthewkrueger.KDS.utils.OAuth;

import com.matthewkrueger.KDS.Settings;
import com.matthewkrueger.KDS.utils.AES;
import com.squareup.square.Environment;
import com.squareup.square.SquareClient;
import com.squareup.square.api.OAuthApi;
import com.squareup.square.models.ObtainTokenRequest;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class OAuthSubroutines {


    /**
     * Contains the response. 0 is access token, 1 is expires time, and 2 is refresh token
     */
    static String[] responses;

    static CountDownLatch LATCH;

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void startOAuth() {
        LOGGER.info("Test");
        // check if any part of OAUTH is missing

        Settings.loadProperties();

        if (!Settings.APP_PROPERTIES.containsKey("OAUTH_TOKEN_EXP") || !Settings.APP_PROPERTIES.containsKey("OAUTH_TOKEN") || !Settings.APP_PROPERTIES.containsKey("OAUTH_TOKEN_REFRESH")) {

            LOGGER.warning("App properties does not contain required data. Opening webpage");

            LOGGER.info("OAUTH_TOKEN_EXP present? " + Settings.APP_PROPERTIES.containsKey("OAUTH_TOKEN_EXP"));
            LOGGER.info("OAUTH_TOKEN present? " + Settings.APP_PROPERTIES.containsKey("OAUTH_TOKEN"));
            LOGGER.info("OAUTH_TOKEN_REFRESH present? " + Settings.APP_PROPERTIES.containsKey("OAUTH_TOKEN_REFRESH"));

            String[] oauthProperties = OAuthMiniServer.startMiniHTTPSServer();

            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN", oauthProperties[0]);
            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN_EXP", oauthProperties[1]);
            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN_REFRESH", oauthProperties[2]);
            Settings.saveProperties();

        }

        // format the date to a day, then add fifteen days as that is the renew cutoff
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
        TemporalAccessor accessor = timeFormatter.parse(Settings.APP_PROPERTIES.getProperty("OAUTH_TOKEN_EXP"));
        Date date = Date.from(Instant.from(accessor));
        Date today = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusDays(14);
        Date date2 = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        if (date2.compareTo(today) < 0) {

            LOGGER.warning("Refresh token has expiered, prompting reauth");

            String[] oauthProperties = OAuthMiniServer.startMiniHTTPSServer();

            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN", oauthProperties[0]);
            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN_EXP", oauthProperties[1]);
            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN_REFRESH", oauthProperties[2]);
            Settings.saveProperties();
        } else if (date2.compareTo(today) > 0 && date.compareTo(today) < 0) {

            LOGGER.warning("Refresh token is still valid, refreshing.");

            String[] oauthProperties = OAuthSubroutines.refreshOAuthToken(Settings.APP_PROPERTIES.getProperty("OAUTH_TOKEN"));

            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN", oauthProperties[0]);
            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN_EXP", oauthProperties[1]);
            Settings.APP_PROPERTIES.setProperty("OAUTH_TOKEN_REFRESH", oauthProperties[2]);
            Settings.saveProperties();
        }
    }

    public static String[] refreshOAuthToken(String currentAccessToken) {

        OAuthApi api = Settings.CLIENT.getOAuthApi();

        ObtainTokenRequest body = new ObtainTokenRequest.Builder(
                getOAuthID(),
                getOAuthKey(),
                "refresh_token")
                .refreshToken(currentAccessToken)
                .build();

        responses = new String[3];

        LATCH = new CountDownLatch(1);
        api.obtainTokenAsync(body).thenAccept(result -> {
            responses[0] = result.getAccessToken();
            responses[1] = result.getExpiresAt();
            responses[2] = result.getRefreshToken();
            LATCH.countDown();
        }).exceptionally(exception -> {

            try {
                com.matthewkrueger.KDS.utils.GenericErrorDisplay.getGenericErrorDisplay("Problem refreshing OAuth Token", "<html>"
                                + "<h1>Error while refreshing OAuth Token</h1>"
                                + "<p>The OAuth token is the \"password\" to your account. It expires every 30 days. After that,<br />"
                                + "the application needs to get a new token. Something happend during this exchange with square.</p></html>",
                        com.matthewkrueger.KDS.utils.GenericErrorDisplay.GenericErrorSettings.FATAL,
                        exception).LATCH.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            exception.printStackTrace();
            return null;
        });

        try {
            LATCH.await();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return responses;

    }


    /**
     * @param codeFromAuthorize The code received from authorize
     * @return OAuthSubroutines.responses
     */
    static String[] getAccessTokenFromAuthorize(String codeFromAuthorize) {

        OAuthApi api = Settings.CLIENT.getOAuthApi();

        ObtainTokenRequest body = new ObtainTokenRequest.Builder(
                getOAuthID(),
                getOAuthKey(),
                "authorization_code")
                .code(codeFromAuthorize)
                .build();

        responses = new String[3];

        LATCH = new CountDownLatch(1);

        api.obtainTokenAsync(body).thenAccept(result -> {
            responses[0] = result.getAccessToken();
            responses[1] = result.getExpiresAt();
            responses[2] = result.getRefreshToken();
            LATCH.countDown();
        }).exceptionally(exception -> {
            exception.fillInStackTrace();
            System.out.println(exception.getLocalizedMessage());
            exception.printStackTrace();

            try {
                com.matthewkrueger.KDS.utils.GenericErrorDisplay.getGenericErrorDisplay("Error exchanging OAuth Authorize token for Token", "<html>"
                                + "<h1>Error while exchanging the OAuth Authorize token.</h1>"
                                + "<p>The Authorize token is given to us by square when you filled out the previous form. Something<br />"
                                + "happened while we tried to exchange that for an OAuth token. The OAuth token is a restricted \"password\" to your<br />"
                                + "account that only lets us see and modify what you agreed to on the last page. Please try again.</p>"
                                + "<p>Should there be a problem again, try changing your square password or contacting us.</p></html>",
                        com.matthewkrueger.KDS.utils.GenericErrorDisplay.GenericErrorSettings.FATAL,
                        exception).LATCH.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        });

        try {
            LATCH.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return responses;

    }

    public static void openDefaultBrowserOAuthAuthorize() {
        String scope = urlEncode("INVENTORY_READ ITEMS_READ MERCHANT_PROFILE_READ");
        String URI = "https://connect.squareup" + ((Settings.IS_PRODUCTION) ? "" : "sandbox" )+ ".com/oauth2/authorize?client_id=" + getOAuthID() +
                "&scope=" + scope;

        System.out.println("URI " + URI);
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(URI));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }else{
            System.err.println("System does not support opening browser.");
            System.exit(-1);
        }
    }

    private static String getOAuthID(){
        String keyValues = "oauth-" + ((Settings.IS_PRODUCTION) ? "production" : "sandbox") + "-id";
        return AES.decrypt(Settings.OAUTH_PROPERTIES.getProperty(keyValues), Settings.OAUTH_PROPERTIES.getProperty("oauth-key"));
    }

    private static String getOAuthKey() {
        String keyValues = "oauth-" + ((Settings.IS_PRODUCTION) ? "production" : "sandbox");
        return AES.decrypt(Settings.OAUTH_PROPERTIES.getProperty(keyValues), Settings.OAUTH_PROPERTIES.getProperty("oauth-key"));
    }

    public static void initSquareClient(){
        SquareClient.Builder builder = new SquareClient.Builder();
        builder.accessToken(getOAuthKey())
                .environment(Environment.PRODUCTION);
        Settings.CLIENT = builder.build();
    }


    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

}