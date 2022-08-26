package com.lahoriagency.cikolive.Classes;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageLoader {
    private LruCache<String, Bitmap> mMemoryCache;

    public ImageLoader() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);


        final int cacheSize = maxMemory / 4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {

                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    public void removeBitmapFromMemoryCache(String key) {
        mMemoryCache.remove(key);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }


    public void downloadImage(final String photoUrl, final ImageView imageView) {
        Bitmap bitmap = getBitmapFromMemCache(photoUrl);
        Picasso.get().load(photoUrl).into(imageView);
        imageView.setImageBitmap(bitmap);

    }
}
