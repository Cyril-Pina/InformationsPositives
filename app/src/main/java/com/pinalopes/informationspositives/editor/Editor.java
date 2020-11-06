package com.pinalopes.informationspositives.editor;

import javax.inject.Inject;

public class Editor {

    private String lastName;
    private String firstName;
    private String nickName;
    SocialNetworks socialNetworks;

    @Inject
    public Editor(String lastName, String firstName, String nickName, SocialNetworks socialNetworks) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.nickName = nickName;
        this.socialNetworks = socialNetworks;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public SocialNetworks getSocialNetworks() {
        return socialNetworks;
    }

    public void setSocialNetworks(SocialNetworks socialNetworks) {
        this.socialNetworks = socialNetworks;
    }
}
