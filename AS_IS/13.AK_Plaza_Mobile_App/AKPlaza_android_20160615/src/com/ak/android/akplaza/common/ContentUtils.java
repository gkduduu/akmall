package com.ak.android.akplaza.common;

import java.io.File;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

public class ContentUtils {

	public static File getFileToUri(Context mContext, Uri uri) {

			String scheme = uri.getScheme();

			// Support all Uri with "content" scheme
			if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(scheme)){
		        File file = new File(getPath(mContext,uri));
		        return file;
			} else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(scheme)){
				Log.d("getFileToUri", "getFileToUri flag3");
				return new File(uri.getPath());
			}

			return null;
		}
	
	
	public static String getPath(Context mcContext, Uri uri)
	{
//	    String[] projection = { MediaStore.Images.Media.DATA };
//	    Cursor cursor = managedQuery(uri, projection, null, null, null);
//	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//	    cursor.moveToFirst();
//	    return cursor.getString(column_index);
		
		
		final boolean isAndroidVersionKitKat = Build.VERSION.SDK_INT >=  19; // 킷캣 버전 체크
		// Check Google Drive.  
	    if(isGooglePhotoUri(uri)) {  
	        return uri.getLastPathSegment();  
	    }  
		
	 // 1. 안드로이드 버전 체크  
	    // com.android.providers.media.documents/document/image :: uri로 전달 받는 경로가 킷캣으로 업데이트 되면서 변경 됨.  
	    if(isAndroidVersionKitKat && isMediaDocument(uri)) {  
	      
	        //com.android.providers.media.documents/document/image:1234 ...  
	        //  
	            final String docId = getDocumentId(uri);  
	            final String[] split = docId.split(":");  
	            final String type = split[0];  
	  
	            Uri contentUri = null;  
	              
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
	                  
	            } else if ("video".equals(type)) {  
	                return null; // 필자는 이미지만 처리할 예정이므로 비디오 및 오디오를 불러오는 부분은 작성하지 않음.  
	                  
	            } else if ("audio".equals(type)) {  
	                return null;  
	            }  
	              
	            final String selection = Images.Media._ID + "=?";  
	            final String[] selectionArgs = new String[] {  
	                    split[1]  
	            };  
	  
	            return getDataColumn(mcContext, contentUri, selection, selectionArgs);  
	          
	    }  
	      
	    // content://media/external/images/media/....  
	    // 안드로이드 버전에 관계없이 경로가 com.android... 형식으로 집히지 않을 수 도 있음. [ 겔럭시S4 테스트 확인 ]  
	    if(isPathSDCardType(uri)) {  
	          
	        final String selection = Images.Media._ID + "=?";  
	           final String[] selectionArgs = new String[] {  
	                   uri.getLastPathSegment()  
	           };  
	          
	        return getDataColumn(mcContext,  MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, selectionArgs);  
	    }  
	      
	    // File 접근일 경우  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	      
	    return null;  
	    
	}
	
	// URI 를 받아서 Column 데이터 접근.  
	public static String getDataColumn(Context context, Uri uri, String selection,  
	        String[] selectionArgs) {  
	  
	    Cursor cursor = null;  
	    final String column = "_data";  
	    final String[] projection = {  
	            column  
	    };  
	  
	    try {  
	          
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs ,null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int column_index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(column_index);  
	        }  
	          
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	      
	    return null;  
	}  
	  
	// 킷캣에서 추가된  document식 Path  
	public static boolean isMediaDocument(Uri uri) {  
	      
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	// 기본 경로 ( 킷캣 이전버전 )  
	public static boolean isPathSDCardType(Uri uri) {  
	    // Path : external/images/media/ID(1234...)  
	    return "external".equals(uri.getPathSegments().get(0));  
	}  
	  
	// 구글 드라이브를 통한 업로드 여부 체크.  
	public static boolean isGooglePhotoUri(Uri uri) {  
	      
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}
	
	public static String getDocumentId(Uri documentUri) {
		final List<String> paths = documentUri.getPathSegments();
		if (paths.size() < 2) {
			throw new IllegalArgumentException("Not a document: " + documentUri);
		}
		if (!"document".equals(paths.get(0))) {
			throw new IllegalArgumentException("Not a document: " + documentUri);
		}
		return paths.get(1);
	}
}
