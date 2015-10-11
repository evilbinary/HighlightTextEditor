
package org.evilbinary.managers;
import android.text.TextUtils;


import java.util.Map;
import java.util.TreeMap;

import org.evilbinary.highliter.R;

public class EmoticonsManager {
    public static class EmoticonInfo {
        public final int resId;
        public final String resFileName;

        private EmoticonInfo(int resId, String resFileName) {
            this.resId = resId;
            this.resFileName = resFileName;
        }
    }

    private static final Map<String, EmoticonInfo> sEmoticons = new TreeMap<String, EmoticonInfo>();

    static {
        sEmoticons.put(":amazed:",
                new EmoticonInfo(R.drawable.emo_amazed, "emo_amazed.png"));
        sEmoticons.put(":angel:",
                new EmoticonInfo(R.drawable.emo_angel, "emo_angel.png"));
        sEmoticons.put(":angry:",
                new EmoticonInfo(R.drawable.emo_angry, "emo_angry.png"));
        sEmoticons.put(":beaten:",
                new EmoticonInfo(R.drawable.emo_beaten, "emo_beaten.png"));
        sEmoticons.put(":bored:",
                new EmoticonInfo(R.drawable.emo_bored, "emo_bored.png"));
        sEmoticons.put(":clown:",
                new EmoticonInfo(R.drawable.emo_clown, "emo_clown.png"));
        sEmoticons.put(":confused:",
                new EmoticonInfo(R.drawable.emo_confused, "emo_confused.png"));
        sEmoticons.put(":cool:",
                new EmoticonInfo(R.drawable.emo_cool, "emo_cool.png"));
        sEmoticons.put(":cry:",
                new EmoticonInfo(R.drawable.emo_cry, "emo_cry.png"));
        sEmoticons.put(":devil:",
                new EmoticonInfo(R.drawable.emo_devil, "emo_devil.png"));
        sEmoticons.put(":doubtful:",
                new EmoticonInfo(R.drawable.emo_doubtful, "emo_doubtful.png"));
        sEmoticons.put(":emo:",
                new EmoticonInfo(R.drawable.emo_emo, "emo_emo.png"));
        sEmoticons.put(":frozen:",
                new EmoticonInfo(R.drawable.emo_frozen, "emo_frozen.png"));
        sEmoticons.put(":grin:",
                new EmoticonInfo(R.drawable.emo_grin, "emo_grin.png"));
        sEmoticons.put(":indian:",
                new EmoticonInfo(R.drawable.emo_indian, "emo_indian.png"));
        sEmoticons.put(":karate:",
                new EmoticonInfo(R.drawable.emo_karate, "emo_karate.png"));
        sEmoticons.put(":kiss:",
                new EmoticonInfo(R.drawable.emo_kiss, "emo_kiss.png"));
        sEmoticons.put(":laugh:",
                new EmoticonInfo(R.drawable.emo_laugh, "emo_laugh.png"));
        sEmoticons.put(":love:",
                new EmoticonInfo(R.drawable.emo_love, "emo_love.png"));
        sEmoticons.put(":millionaire:",
                new EmoticonInfo(R.drawable.emo_millionaire, "emo_millionaire.png"));
        sEmoticons.put(":nerd:",
                new EmoticonInfo(R.drawable.emo_nerd, "emo_nerd.png"));
        sEmoticons.put(":ninja:",
                new EmoticonInfo(R.drawable.emo_ninja, "emo_ninja.png"));
        sEmoticons.put(":party:",
                new EmoticonInfo(R.drawable.emo_party, "emo_party.png"));
        sEmoticons.put(":pirate:",
                new EmoticonInfo(R.drawable.emo_pirate, "emo_pirate.png"));
        sEmoticons.put(":punk:",
                new EmoticonInfo(R.drawable.emo_punk, "emo_punk.png"));
        sEmoticons.put(":sad:",
                new EmoticonInfo(R.drawable.emo_sad, "emo_sad.png"));
        sEmoticons.put(":santa:",
                new EmoticonInfo(R.drawable.emo_santa, "emo_santa.png"));
        sEmoticons.put(":shy:",
                new EmoticonInfo(R.drawable.emo_shy, "emo_shy.png"));
        sEmoticons.put(":sick:",
                new EmoticonInfo(R.drawable.emo_sick, "emo_sick.png"));
        sEmoticons.put(":smile:",
                new EmoticonInfo(R.drawable.emo_smile, "emo_smile.png"));
        sEmoticons.put(":speechless:",
                new EmoticonInfo(R.drawable.emo_speechless, "emo_speechless.png"));
        sEmoticons.put(":sweating:",
                new EmoticonInfo(R.drawable.emo_sweating, "emo_sweating.png"));
        sEmoticons.put(":tongue:",
                new EmoticonInfo(R.drawable.emo_tongue, "emo_tongue.png"));
        sEmoticons.put(":vampire:",
                new EmoticonInfo(R.drawable.emo_vampire, "emo_vampire.png"));
        sEmoticons.put(":wacky:",
                new EmoticonInfo(R.drawable.emo_wacky, "emo_wacky.png"));
        sEmoticons.put(":wink:",
                new EmoticonInfo(R.drawable.emo_wink, "emo_wink.png"));
    }
    EmoticonsManager() {
    }

    public EmoticonInfo getEmoticonInfo(String emoticonPath) {
        if (TextUtils.isEmpty(emoticonPath) || emoticonPath.equals("false")
                || !sEmoticons.containsKey(emoticonPath)) {
            return null;
        }
        return sEmoticons.get(emoticonPath);
    }
}
