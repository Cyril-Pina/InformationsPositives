package com.pinalopes.informationspositives.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.pinalopes.informationspositives.Constants.BLACK_ICON;
import static com.pinalopes.informationspositives.Constants.DRAWABLE;

public class ResourceUtils {

    private ResourceUtils() {
        throw new AssertionError();
    }

    public static Bitmap imgResToBitmap(Context context, int imgRes) {
        return BitmapFactory.decodeResource(context.getResources(), imgRes);
    }

    public static int getCategoryBlackIcon(Context context, int idWhiteIcon) {
        Resources resources = context.getResources();
        String blackIconFileName = resources.getResourceEntryName(idWhiteIcon) + BLACK_ICON;
        return resources.getIdentifier(blackIconFileName, DRAWABLE, context.getPackageName());
    }
}
