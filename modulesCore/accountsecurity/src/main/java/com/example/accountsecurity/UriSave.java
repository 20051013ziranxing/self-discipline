package com.example.accountsecurity;

import android.net.Uri;

public class UriSave {
    private static volatile UriSave instance;
    private Uri UriImage;
    private UriSave() {}
    public static UriSave getInstance() {
        if (instance == null) {
            synchronized (UriSave.class) {
                if (instance == null) {
                    instance = new UriSave();
                }
            }
        }
        return instance;
    }

    public Uri getUriImage() {
        return UriImage;
    }

    public void setUriImage(Uri uriImage) {
        UriImage = uriImage;
    }
}
