package com.ak.android.akplaza.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageUtils {

	public static final String TAG = "ImageUtils";

	public static boolean resizeImage(Context context, File srcFile, File dstFile, int dstWidth, int dstHeight) {

		Bitmap srcBitmap = BitmapFactory.decodeFile(srcFile.getAbsolutePath());

		int srcWidth = srcBitmap.getWidth();
		int srcHeight = srcBitmap.getHeight();
		double srcRatio = (double) srcWidth / (double) srcHeight;
		double destRatio = (double) dstWidth / (double) dstHeight;

		// change the dstWidht or dstHeight size to fit the original ratio.
		if (destRatio < srcRatio) {
			dstHeight = (int) (dstWidth / srcRatio);
		} else {
			dstWidth = (int) (dstHeight * srcRatio);
		}

		if (srcWidth < dstWidth && srcHeight < dstHeight) {
			dstWidth = srcWidth;
			dstHeight = srcHeight;
		} else if (srcWidth < dstWidth) {
			dstWidth = srcWidth;
		} else if (srcHeight < dstHeight) {
			dstHeight = srcHeight;
		}

		Bitmap dstBitmap =	Bitmap.createScaledBitmap(srcBitmap, dstWidth, dstHeight, true);

		OutputStream out;
		try {
			out = new FileOutputStream(dstFile);
		} catch (FileNotFoundException e) {
			Log.w(TAG, e);
			return false;
		}

		return dstBitmap.compress(CompressFormat.JPEG, 80, out);
	}
}
