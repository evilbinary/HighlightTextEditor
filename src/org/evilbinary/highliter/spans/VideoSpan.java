
package org.evilbinary.highliter.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

public class VideoSpan extends ImageSpan {
    @Deprecated
    public VideoSpan(Bitmap b) {
        super(b);
    }

    @Deprecated
    public VideoSpan(Bitmap b, int verticalAlignment) {
        super(b, verticalAlignment);
    }

    public VideoSpan(Context context, Bitmap b) {
        super(context, b);
    }

    public VideoSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }

    public VideoSpan(Drawable d) {
        super(d);
    }

    public VideoSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }

    public VideoSpan(Drawable d, String source) {
        super(d, source);
    }

    public VideoSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }

    public VideoSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public VideoSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public VideoSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public VideoSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }
}