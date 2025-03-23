package com.example.clockinfragment.fragment;

import com.example.clockinfragment.R;

public class ImageViewSrcSingleton {
    private static int imageResId = R.drawable.walter;
    private ImageViewSrcSingleton() {
    }
    public static ImageViewSrcSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
    public void setImageResId(int resId) {
        imageResId = resId;
    }
    public int getImageResId() {
        return imageResId;
    }
    private static class SingletonHolder {
        private static final ImageViewSrcSingleton INSTANCE = new ImageViewSrcSingleton();
    }
}
