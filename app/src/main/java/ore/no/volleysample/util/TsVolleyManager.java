package ore.no.volleysample.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class TsVolleyManager {
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context sAppContext;
    private static TsVolleyManager sInstance;

    private TsVolleyManager(Context context) {
        sAppContext = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(
                mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                }
        );
    }

    public static synchronized TsVolleyManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TsVolleyManager(context);
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(sAppContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}

