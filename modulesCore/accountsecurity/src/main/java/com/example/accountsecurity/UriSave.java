package com.example.accountsecurity;

import android.net.Uri;

import java.io.File;

public class UriSave {
    private static volatile UriSave instance;
    private File UriImage;
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

    /*public Uri getUriImage() {
        return UriImage;
    }

    public void setUriImage(Uri uriImage) {
        UriImage = uriImage;
    }*/

    public File getUriImage() {
        return UriImage;
    }

    public void setUriImage(File uriImage) {
        UriImage = uriImage;
    }
}
