package ru.rassokhindanila.googleoauth.service.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import ru.rassokhindanila.googleoauth.dto.GoogleTokenInfo;
import ru.rassokhindanila.googleoauth.dto.GoogleUserInfo;
import ru.rassokhindanila.googleoauth.service.OAuthWebClient;


/**
 * OAuth client for Google OAuth
 */
@Service
public class GoogleWebClient implements OAuthWebClient {

    /**
     * OAuth authorization url
     */
    @Value("${google.oauth.url}")
    private String url;
    /**
     * Authorization code exchange to access token url
     */
    @Value("${google.oauth.exchange.url}")
    private String exchangeUrl;
    /**
     * Google client_id
     */
    @Value("${google.oauth.client.id}")
    private String clientId;
    /**
     * Google client_secret
     */
    @Value("${google.oauth.client.secret}")
    private String clientSecret;
    /**
     * OAuth scopes
     */
    @Value("${google.oauth.scopes}")
    private String scopes;
    /**
     * Url for retrieving user information
     */
    @Value("${google.api.profile}")
    private String profileApiUrl;

    /**
     * Redirect url after authorization
     */
    @Value("${google.oauth.return.url}")
    private String returnUrl;

    private final Logger logger;

    public GoogleWebClient()
    {
        logger = LoggerFactory.getLogger(GoogleWebClient.class);
    }

    @Override
    public String getAuthUrl() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        return uriComponentsBuilder
                .scheme("https")
                .host(url)
                .queryParam("scope", scopes)
                .queryParam("access_type", "online")
                .queryParam("redirect_uri", returnUrl)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId).build().toString();
    }

    @Override
    public String getExchangeUrl(String code)
    {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        return uriComponentsBuilder
                .scheme("https")
                .host(exchangeUrl)
                .queryParam("code", code)
                .queryParam("scope", "")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("grant_type", "authorization_code")
                .queryParam("redirect_uri", returnUrl)
                .build().toString();
    }

    /**
     * @param token Access token
     * @return Url for getting user information
     */
    public String getUserInfoUrl(String token)
    {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        return uriComponentsBuilder
                .scheme("https")
                .host(profileApiUrl)
                .queryParam("access_token", token)
                .build()
                .toString();
    }

    /**
     * Retrieves user information from Google account
     * @param token Access token
     * @return User information from Google account
     */
    public GoogleUserInfo getUserInfo(String token){
        WebClient webClient = WebClient.create(
                getUserInfoUrl(token)
        );
        try {
            return webClient
                    .get()
                    .retrieve()
                    .bodyToMono(GoogleUserInfo.class)
                    .block();
        }catch(WebClientResponseException e){
            logger.error("Error while retrieving user information: "+e.getMessage());
            logger.error(e.getResponseBodyAsString());
            return null;
        }
    }

    /**
     * Exchange authorization code to access token
     * @param code Authorization code
     * @return Access token information
     */
    public GoogleTokenInfo getTokenInfo(String code)
    {
        WebClient webClient = WebClient.create(getExchangeUrl(code));
        try {
            return webClient
                    .post()
                    .retrieve()
                    .bodyToMono(GoogleTokenInfo.class).block();
        }catch(WebClientResponseException e){
            logger.error("Error while getting access token: "+e.getMessage());
            logger.error(e.getResponseBodyAsString());
            return null;
        }
    }
}
