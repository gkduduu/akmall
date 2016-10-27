package com.ak.android.akmall.util;

import java.io.File;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ContentUtils {

	public static File getFileToUri(ContentResolver cr, Uri uri) {

		String scheme = uri.getScheme();

		// Support all Uri with "content" scheme
		if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)) {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = cr.query(uri, projection, null, null, null);

			if (cursor == null) {
				//throw new IOException("can not access resource..");
				return null;
			}

			try {

				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				String filePath = cursor.getString(column_index);
				if(filePath == null) {
					//throw new IOException("can not access resource..");
					return null;
				}
				File file = new File(filePath);

				return file;

			} finally {
				cursor.close();
			}

		} else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)) {
			return new File(uri.getPath());
		}

		throw null;
	}
}
