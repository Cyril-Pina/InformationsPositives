package com.pinalopes.informationspositives.editor;

public class SocialNetworks {

    private String email;
    private String linkedIn;
    private String instagram;
    private String facebook;

    public SocialNetworks(String email, String linkedIn, String instagram, String facebook) {
        this.email = email;
        this.linkedIn = linkedIn;
        this.instagram = instagram;
        this.facebook = facebook;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
}
