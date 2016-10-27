package com.ak.android.akmall.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Log;

public class ImageUtils {

	public static final String TAG = "ImageUtils";

	public static boolean resizeImage(Context context, File srcFile, File dstFile, int dstWidth,
			int dstHeight) throws OutOfMemoryError, IOException {

		Bitmap srcBitmap = BitmapFactory.decodeFile(srcFile.getAbsolutePath());

		if(srcBitmap == null) {
			throw new IOException("not found image file..");
		}

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

		Matrix matrix = new Matrix();
		matrix.postScale((float) dstWidth / srcWidth, (float) dstHeight / srcHeight);
		matrix.postRotate(getOrientationDegrees(srcFile.toString()));
		Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth, srcHeight, matrix, true);

		OutputStream out;
		try {
			out = new FileOutputStream(dstFile);
		} catch (FileNotFoundException e) {
			Log.w(TAG, e);
			return false;
		}

		return dstBitmap.compress(CompressFormat.JPEG, 80, out);
	}

	public static boolean resizeImage(Context context, Uri uri, File dstFile, int dstWidth,
			int dstHeight) throws IOException, OutOfMemoryError {

		File srcFile = ContentUtils.getFileToUri(context.getContentResolver(), uri);
		if(srcFile != null) {
			return resizeImage(context, srcFile, dstFile, dstWidth, dstHeight);
		}

		// else ... is not local file..

		Bitmap srcBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));

		if(srcBitmap == null) {
			throw new IOException("not found image file..");
		}

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

		Matrix matrix = new Matrix();
		matrix.postScale((float) dstWidth / srcWidth, (float) dstHeight / srcHeight);
		Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth, srcHeight, matrix, true);

		OutputStream out;
		try {
			out = new FileOutputStream(dstFile);
		} catch (FileNotFoundException e) {
			Log.w(TAG, e);
			return false;
		}

		return dstBitmap.compress(CompressFormat.JPEG, 80, out);
	}


	public static float getOrientationDegrees(final String fileName) {

		ExifInterface exif;

		try {
			exif = new ExifInterface(fileName);
		} catch (IOException e) {
			return 0;
		}

		int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);


		switch (exifOrientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			return 90;

		case ExifInterface.ORIENTATION_ROTATE_180:
			return 180;

		case ExifInterface.ORIENTATION_ROTATE_270:
			return 270;

		default:
			return 0;
		}
	}

}
