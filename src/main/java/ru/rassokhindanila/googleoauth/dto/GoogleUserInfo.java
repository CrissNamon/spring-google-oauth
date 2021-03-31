package ru.rassokhindanila.googleoauth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents user information
 */
public class GoogleUserInfo {

    private String name;
    private String sub;
    private String familyName;
    private String givenName;
    private String picture;
    private String locale;

    public GoogleUserInfo() {}

    @JsonCreator
    public GoogleUserInfo(@JsonProperty("name") String name,
                          @JsonProperty("sub") String sub,
                          @JsonProperty("picture") String picture,
                          @JsonProperty("locale") String locale,
                          @JsonProperty("family_name") String familyName,
                          @JsonProperty("given_name") String givenName)
    {
        this.name = name;
        this.sub = sub;
        this.familyName = familyName;
        this.givenName = givenName;
        this.picture = picture;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
