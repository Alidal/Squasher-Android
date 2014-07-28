package com.vladsid.squasher.app;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontOverrider {

	public static void applyFont(final Context context, final View root, final String fontName) {
		if (root instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) root;
			for (int i = 0; i < viewGroup.getChildCount(); i++)
				applyFont(context, viewGroup.getChildAt(i), fontName);
		} else if (root instanceof TextView)
			((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
	}
}
