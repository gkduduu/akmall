
package com.ak.android.akplaza.common;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;

import com.ak.android.akplaza.BuildConfig;
import com.ak.android.akplaza.R;
import com.ak.android.akplaza.http.AkHttpClient;
import com.ak.android.akplaza.mobileevent.model.PhotoEventResult;
import com.ak.android.akplaza.mobileevent.model.PhotoEventSource;

public class AkPlazaAPI {

    public static final String TAG = "AkPlazaAPI";
    public static final boolean DBG = BuildConfig.DEBUG & true;
    private static final Charset DEFAULT_ENCODING = Charset.forName(HTTP.UTF_8);

    private Context mContext;
    private static AkHttpClient mClient;

    public AkPlazaAPI(Context context) {
        mContext = context;
        mClient = new AkHttpClient(context);
    }

    public boolean signinDevice() {

        String token = SharedUtil.getSharedString(mContext, "C2DM", "token");
        String version = "1.0";

        try {
            version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            Log.w(TAG, "Don't get versionCode.. so default verionCode set 1..");
        }

        if (token.length() == 0) {
            return false;
        }

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters
                .add(new BasicNameValuePair("act", mContext.getString(R.string.act_signin_device)));
        parameters.add(new BasicNameValuePair("token", token));
        parameters.add(new BasicNameValuePair("version", version));

        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
            HttpPost post = new HttpPost(Const.URL_LIB);
            post.setEntity(entity);

            String response = mClient.execute(post);
            if (DBG) {
                Log.d(TAG, "signinDevice response: " + response);
            }

            JSONObject jsonResult = new JSONObject(response);
            String result = jsonResult.getString("result");

            if ("success".equals(result)) {
                return true;
            }

            return false;

        } catch (IOException e) {
            Log.e(TAG, "fail signin device..", e);
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "fail signin device..", e);
            return false;
        }
    }

    public boolean registPushDeviceToken(String token, String appid, String deny) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("act", "pushDeviceRegist"));
        parameters.add(new BasicNameValuePair("token", token));
        parameters.add(new BasicNameValuePair("phonetype", "1"));
        parameters.add(new BasicNameValuePair("appid", appid));
        parameters.add(new BasicNameValuePair("deny", deny));

        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
            HttpPost post = new HttpPost(Const.URL_LIB);
            post.setEntity(entity);

            String response = mClient.execute(post);
            if (DBG) {
                Log.d(TAG, "registPushDeviceToken response: " + response);
            }

            JSONObject jsonResult = new JSONObject(response);
            String result = jsonResult.getString("result");

            if ("success".equals(result)) {
                return true;
            }

            return false;

        } catch (IOException e) {
            Log.e(TAG, "fail regist push device token..", e);
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "fail regist push device token..", e);
            return false;
        }
    }

    public PhotoEventResult joinPhotoEvent(PhotoEventSource source) {

        PhotoEventResult result = new PhotoEventResult();

        File attachFile = null;

        try {

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
                    null, DEFAULT_ENCODING);
            entity.addPart("act", new StringBody("joinEvent", DEFAULT_ENCODING));
            entity.addPart("type", new StringBody("json", DEFAULT_ENCODING));
            entity.addPart("event_index", new StringBody(source.getEventIndex(), DEFAULT_ENCODING));
            entity.addPart("event_token", new StringBody(source.getEventToken(), DEFAULT_ENCODING));
            entity.addPart("title", new StringBody(source.getTitle(), DEFAULT_ENCODING));
            entity.addPart("content", new StringBody(source.getContent(), DEFAULT_ENCODING));
            entity.addPart("name", new StringBody(source.getName(), DEFAULT_ENCODING));
            entity.addPart("phone", new StringBody(source.getPhone(), DEFAULT_ENCODING));

            Uri attach = source.getContentImageUri();
            if (attach != null) {
                attachFile = new File(mContext.getCacheDir(), "tmp_" + System.currentTimeMillis()
                        + ".jpg");
                if (!ImageUtils.resizeImage(mContext, ContentUtils.getFileToUri(mContext, attach),
                        attachFile, 1024, 1024)) {
                    throw new IOException("fail resize image..");
                }
                entity.addPart("attach", new FileBody(attachFile, "image/jpeg"));
            }

            HttpPost post = new HttpPost(Const.URL_LIB);
            post.setEntity(entity);

            String response = mClient.execute(post);
            if (DBG) {
                Log.d(TAG, "joinPhotoEvent response: " + response); 
            }

            JSONObject json = new JSONObject(response);

            result.setResult(json.getString("result"));
            result.setMessage(json.getString("message"));

        } catch (Exception e) {
            result.setResult("error");
            result.setMessage("포토 이벤트 응모에 실패하였습니다.");
            if (DBG) {
                Log.w(TAG, "fail join photo event..", e);
            }
        } finally {
            if (attachFile != null) {
                attachFile.deleteOnExit();
            }
        }

        return result;
    }

    public PhotoEventResult getPhotoEventDetial(String eventIndex, String entryIndexno) {

        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("act", "getEntryDetail"));
        parameters.add(new BasicNameValuePair("event_index", eventIndex));
        parameters.add(new BasicNameValuePair("entry_indexno", entryIndexno));

        PhotoEventResult result = new PhotoEventResult();

        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, "UTF-8");
            HttpPost post = new HttpPost(Const.URL_LIB);
            post.setEntity(entity);

            String response = mClient.execute(post);
            if (DBG) {
                Log.d(TAG, "registPushDeviceToken response: " + response);
            }

            JSONObject jsonResult = new JSONObject(response);
            result.setResult(jsonResult.getString("result"));
            result.setMessage(jsonResult.getString("message"));

            if ("success".equals(result.getResult())) {

                JSONObject entry = jsonResult.getJSONObject("entry");
                PhotoEventSource entrySource = new PhotoEventSource();
                entrySource.setEntryIndexno(entry.getString("ENTRY_INDEXNO"));
                entrySource.setEventIndex(entry.getString("EVENT_INDEX"));
                entrySource.setTitle(entry.getString("TITLE"));
                entrySource.setContentImage(entry.getString("CONTENT_IMAGE"));
                String contentImg = Const.URL_BASE + "/" + entry.getString("CONTENT_IMAGE_URI");
                entrySource.setContentImageUri(Uri.parse(contentImg));
                entrySource.setContent(entry.getString("CONTENT"));
                entrySource.setUserid(entry.getString("USERID"));
                entrySource.setUsername(entry.getString("NAME"));
                entrySource.setRegDate(entry.getString("REG_DATE"));

                result.setEntry(entrySource);
            }

        } catch (Exception e) {
            result.setResult("error");
            result.setMessage("이벤트 응모 정보를 조회 할 수 없습니다.");
            if (DBG) {
                Log.w(TAG, "fail get photo event detail..", e);
            }
        }

        return result;
    }

    public PhotoEventResult updatePhotoEvent(PhotoEventSource source) {

        PhotoEventResult result = new PhotoEventResult();

        File attachFile = null;

        try {

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,
                    null, DEFAULT_ENCODING);
            entity.addPart("act", new StringBody("updateEntry", DEFAULT_ENCODING));
            entity.addPart("type", new StringBody("json", DEFAULT_ENCODING));
            entity.addPart("event_index", new StringBody(source.getEventIndex(), DEFAULT_ENCODING));
            entity.addPart("entry_indexno", new StringBody(source.getEntryIndexno(),
                    DEFAULT_ENCODING));
            entity.addPart("title", new StringBody(source.getTitle(), DEFAULT_ENCODING));
            entity.addPart("content", new StringBody(source.getContent(), DEFAULT_ENCODING));
            entity.addPart("name", new StringBody(source.getName(), DEFAULT_ENCODING));
            entity.addPart("phone", new StringBody(source.getPhone(), DEFAULT_ENCODING));

            Uri attach = source.getContentImageUri();
            if (attach != null) {
                attachFile = new File(mContext.getCacheDir(), "tmp_" + System.currentTimeMillis()
                        + ".jpg");
                if (!ImageUtils.resizeImage(mContext, ContentUtils.getFileToUri(mContext, attach),
                        attachFile, 1024, 1024)) {
                    throw new IOException("fail resize image..");
                }
                entity.addPart("attach", new FileBody(attachFile, "image/jpeg"));
            }

            HttpPost post = new HttpPost(Const.URL_LIB);
            post.setEntity(entity);

            String response = mClient.execute(post);
            if (DBG) {
                Log.d(TAG, "updatePhotoEvent response: " + response);
            }

            JSONObject json = new JSONObject(response);

            result.setResult(json.getString("result"));
            result.setMessage(json.getString("message"));

        } catch (Exception e) {
            result.setResult("error");
            result.setMessage("이벤트 응모 내역 수정에 실패하였습니다.");
            if (DBG) {
                Log.w(TAG, "fail update photo event..", e);
            }
        } finally {
            if (attachFile != null) {
                attachFile.deleteOnExit();
            }
        }

        return result;
    }

}
