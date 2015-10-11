
package org.evilbinary.highliter.spans;

import android.content.ActivityNotFoundException;
import android.text.style.URLSpan;
import android.view.View;

public class SafeURLSpan extends URLSpan {
 
    public SafeURLSpan(String url) {
        super(url);
     }

    @Override
    public void onClick(View widget) {
        try {
            super.onClick(widget);
        } catch (ActivityNotFoundException exc) {
           
        }
    }
}
