package com.kontakt.sample.loader;

import android.content.Context;

import com.kontakt.sdk.android.common.model.Profile;
import com.kontakt.sdk.android.http.HttpResult;
import com.kontakt.sdk.android.http.KontaktApiClient;
import com.kontakt.sdk.android.http.exception.ClientException;

import java.util.Collections;
import java.util.List;


public class ProfilesLoader extends AbstractLoader<List<Profile>> {

    private final DisableableContentObserver observer;

    private final KontaktApiClient kontaktApiClient;

    public ProfilesLoader(Context context) {
        super(context);
        observer = new DisableableContentObserver(new ForceLoadContentObserver());
        kontaktApiClient = new KontaktApiClient();
    }

    @Override
    protected void onStartLoading() {
        observer.setEnabled(true);
        super.onStartLoading();
    }

    @Override
    protected void onAbandon() {
        observer.setEnabled(false);
    }

    @Override
    protected void onReset() {
        super.onReset();
        observer.setEnabled(false);
        kontaktApiClient.close();
    }

    @Override
    public List<Profile> loadInBackground() {
        try {
            HttpResult<List<Profile>> profilesResult = kontaktApiClient.listProfiles();
            return profilesResult.isPresent() ? profilesResult.get() : Collections.<Profile>emptyList();
        } catch (ClientException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



}
