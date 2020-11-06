package com.pinalopes.informationspositives.editor;

import dagger.Module;
import dagger.Provides;

@Module
public class EditorModule {

    private String lastName;
    private String firstName;
    private String nickName;
    private SocialNetworks socialNetworks;

    public EditorModule(String lastName, String firstName, String nickName, SocialNetworks socialNetworks) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.nickName = nickName;
        this.socialNetworks = socialNetworks;
    }

    @Provides
    static SocialNetworks providesSocialNetworks(String email, String linkedIn, String instagram, String facebook) {
        return new SocialNetworks(email, linkedIn, instagram, facebook);
    }

    @Provides
    Editor providesEditor() {
        return new Editor(lastName, firstName, nickName, socialNetworks);
    }
}
