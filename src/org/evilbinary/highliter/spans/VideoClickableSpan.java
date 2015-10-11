
package org.evilbinary.highliter.spans;
import android.text.style.ClickableSpan;
import android.view.View;


public class VideoClickableSpan extends ClickableSpan {
    private String videoUrl;

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public void onClick(View widget) {

    }
}
