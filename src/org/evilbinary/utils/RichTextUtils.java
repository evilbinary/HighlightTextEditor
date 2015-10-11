/*
 * Copyright (C) 2014 ChatWing
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.evilbinary.utils;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.CharacterStyle;

import java.net.MalformedURLException;
import java.net.URL;
public class RichTextUtils {
    public static <A extends CharacterStyle, B extends CharacterStyle> Spannable replaceAll(Spanned original,
                                                                                            Class<A> sourceType,
                                                                                            SpanConverter<A, B> converter) {
        SpannableString result = new SpannableString(original);
        A[] spans = result.getSpans(0, result.length(), sourceType);

        for (A span : spans) {
            int start = result.getSpanStart(span);
            int end = result.getSpanEnd(span);
            int flags = result.getSpanFlags(span);

            result.removeSpan(span);
            result.setSpan(converter.convert(span), start, end, flags);
        }

        return (result);
    }

    public static boolean isUrl(String imageUrl) {
        try {
            new URL(imageUrl);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public interface SpanConverter<A extends CharacterStyle, B extends CharacterStyle> {
        B convert(A span);
    }

    public static String autoInsertBBCode(String message) {
        // Separate input by spaces ( URLs don't have spaces )
        String[] parts = message.split("\\s");
        StringBuilder builder = new StringBuilder();
        // Attempt to convert each item into an URL.
        for (String item : parts) {
            try {
                URL url = new URL(item);
                // If possible then replace with anchor...
                builder.append("[url]" + url + "[/url] ");
            } catch (MalformedURLException e) {
                // If there was an URL that was not it!...
                builder.append(item + " ");
            } catch (StringIndexOutOfBoundsException e) {
                //Fix weird bug on android 2.3 and possibly honeycomb
                //https://code.google.com/p/android/issues/detail?id=15960
                builder.append(item + " ");
            }
        }
        return builder.toString().trim();
    }
}