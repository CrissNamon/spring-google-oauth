package ru.rassokhindanila.googleoauth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents access token information
 */
public class GoogleTokenInfo {

    private String accessToken;
    private long expiresIn;
    private String tokenType;
    private String scope;
    private String refreshToken;

    public GoogleTokenInfo(){}

    @JsonCreator
    public GoogleTokenInfo(@JsonProperty("access_token") String accessToken,
                           @JsonProperty("expires_in") long expiresIn,
                           @JsonProperty("token_type") String tokenType,
                           @JsonProperty("scope") String scope,
                           @JsonProperty("refresh_token") String refreshToken)
    {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.scope = scope;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
