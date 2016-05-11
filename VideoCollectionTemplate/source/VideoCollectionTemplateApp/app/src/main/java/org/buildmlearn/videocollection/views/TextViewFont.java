package org.buildmlearn.videocollection.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Anupam (opticod) on 11/5/16.
 */
public class TextViewFont extends TextView {

    public TextViewFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context);
    }

    public TextViewFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context);
    }

    public TextViewFont(Context context) {
        super(context);
    }

    public void setCustomFont(Context mContext) {
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "roboto_light.ttf");
        setTypeface(font);
    }
}
