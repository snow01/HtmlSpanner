/*
 * Copyright (C) 2012 Dominik Schürmann
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

package net.nightwhistler.htmlspanner.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import net.nightwhistler.htmlspanner.HtmlSpanner;

/**
 * This class is similar to Android's BulletSpan with the following differences:
 *
 * - Less options
 *
 * - Instead of drawing a circle we are using a utf8 sign as a bullet (for more
 * compatibility)
 *
 * - Also supports ordered lists with numbers in front of the item
 */
public class ListItemSpan implements LeadingMarginSpan {
    private final int mNumber;
    private final String listStyle;

    private static final int BULLET_RADIUS = 3;
    private static final int NUMBER_RADIUS = 5;

    //Gap should be about 1em
    public static final int STANDARD_GAP_WIDTH = HtmlSpanner.HORIZONTAL_EM_WIDTH;

    public ListItemSpan() {
        mNumber = -1;
        listStyle = null;
    }

    public ListItemSpan(String listStyle, int number) {
        mNumber = number;
        this.listStyle = listStyle;
    }

    public int getLeadingMargin(boolean first) {
        if (mNumber != -1) {
            return 2 * NUMBER_RADIUS + STANDARD_GAP_WIDTH + 40;
        } else {
            return 2 * BULLET_RADIUS + STANDARD_GAP_WIDTH;
        }
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top,
                                  int baseline, int bottom, CharSequence text, int start, int end,
                                  boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();

            p.setStyle(Paint.Style.FILL);

            if (mNumber != -1) {
                c.drawText(getListNumber() + ")", x + dir, baseline, p);
            } else {
                c.drawText("\u2022", x + dir, baseline, p);
            }

            p.setStyle(style);
        }
    }

    private String getListNumber() {
        switch (listStyle) {
            case "upper-alpha":
                return String.valueOf((char)('A' + mNumber - 1));
            case "lower-alpha":
                return String.valueOf((char)('a' + mNumber - 1));
            default:
                return String.valueOf(mNumber);
        }
    }
}