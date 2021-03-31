package ru.rassokhindanila.googleoauth.service;

/**
 * OAuth client interface
 */
public interface OAuthWebClient {
    /**
     * @return Authorization url
     */
    String getAuthUrl();

    /**
     * @param code Authorization code
     * @return Url for exchanging authorization code to access token
     */
    String getExchangeUrl(String code);
}
