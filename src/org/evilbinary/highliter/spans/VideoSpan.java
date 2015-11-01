/* Copyright (C) 2015 evilbinary.
 * rootdebug@163.com
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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