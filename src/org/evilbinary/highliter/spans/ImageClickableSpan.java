
package org.evilbinary.highliter.spans;

import android.text.style.ClickableSpan;
import android.view.View;

public class ImageClickableSpan extends ClickableSpan {
    private String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public void onClick(View view) {

    }
}
