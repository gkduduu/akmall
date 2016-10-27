/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.ak.android.akplaza.common.sns.twitter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.ak.android.akplaza.common.SharedUtil;


public class TwitterController {
	
	private static final String TAG = "TwitterController";
	private static Twitter twitter;
	private static AccessToken acToken;
	private static RequestToken rqToken;

	private static String tac = "";
	private static String tacs = "";
	private static Context mContext = null;
	private static String mTwitt_id = "";
	
	//트위터 로그인
	public static void login(Activity at){
		twitter= new TwitterFactory().getInstance();
	    twitter.setOAuthConsumer(C.TWITTER_CONSUMER_KEY, C.TWITTER_CONSUMER_SECRET);
	    rqToken = null; 
	    

	    try{
	    	rqToken = twitter.getOAuthRequestToken();
	    	
	    }catch(TwitterException e){
	    	e.printStackTrace();
	    	rqToken = null;
	    }
//	    Log.d("rqToken", rqToken.getToken());
	   
	    if(rqToken.equals(null)){
//	    	Log.d("123rqToken", rqToken.getToken());
	    }else{
	    	Intent intent = new Intent(at, TwitterLogin.class);
			intent.putExtra("auth_url", rqToken.getAuthorizationURL());
			at.startActivityForResult(intent, C.TWITTER_LOGIN_CODE);
	    }
	    
	}
	

	//트위터 로그인상태 채크
	public static boolean checkLoginState(Context context){
		boolean is = false;
		tac = SharedUtil.getSharedString(context, "TWITTER", "tac");
		tacs = SharedUtil.getSharedString(context, "TWITTER", "tacs");
		mTwitt_id = SharedUtil.getSharedString(context, "TWITTER", "id");
		
		if( !tac.equals("") && !tacs.equals("") && !mTwitt_id.equals("")){
			is = true;
		}
		
		return is;
	}
	
	public static void setToken(Context context, Intent data){
		acToken = null;
		
		try{
			acToken = twitter.getOAuthAccessToken(rqToken, data.getStringExtra("oauth_verifier"));
			tac = acToken.getToken();
			tacs = acToken.getTokenSecret();
			mTwitt_id = twitter.getScreenName();
			SharedUtil.setSharedString(context, "TWITTER", "tac", tac);
			SharedUtil.setSharedString(context, "TWITTER", "tacs", tacs);
			SharedUtil.setSharedString(context, "TWITTER", "id", mTwitt_id);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//로그아웃
	public static void  logout(Activity at)
	  {
				
	    Intent intent = new Intent(C.MOVE_TWITTER_LOGIN);
	    intent.putExtra("auth_url", C.TWITTER_LOGOUT_URL);
	    at.startActivity(intent);
	    
     	new AlertDialog.Builder(at)
     	.setMessage("트위터 성공적으로 로그아웃 되었습니다.")
     	.setPositiveButton("확인",null)
     	.show();
	    
		SharedUtil.setSharedString(at, "TWITTER", "tac", "");
		SharedUtil.setSharedString(at, "TWITTER", "tacs", "");
		SharedUtil.setSharedString(at, "TWITTER", "id", "");
	  }	
	
	  
	public static String getUserScreenName(){
		return mTwitt_id;
	}
	//업로드
	public static void write(String content, Activity at){
//		Log.d(TAG, "content : " + content);
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
	    String fileName = "example.jpg";
	    InputStream is = null;
	    
	    try
	    {
	      if (new File(path + File.separator + fileName).exists())
	        is = new FileInputStream(path + File.separator + fileName);
	      else
	        is = null;


	      ConfigurationBuilder cb = new ConfigurationBuilder();
	      String oAuthAccessToken = acToken.getToken();
	      String oAuthAccessTokenSecret = tacs;
	      String oAuthConsumerKey = C.TWITTER_CONSUMER_KEY;
	      String oAuthConsumerSecret = C.TWITTER_CONSUMER_SECRET;
	      cb.setOAuthAccessToken(oAuthAccessToken);
	      cb.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
	      cb.setOAuthConsumerKey(oAuthConsumerKey);
	      cb.setOAuthConsumerSecret(oAuthConsumerSecret);
	      Configuration config = cb.build();
	      OAuthAuthorization auth = new OAuthAuthorization(config);
	      
	      TwitterFactory tFactory = new TwitterFactory(config);
	      Twitter twitter = tFactory.getInstance();
//	      ImageUploadFactory iFactory = new ImageUploadFactory(getConfiguration(C.TWITPIC_API_KEY));
//	      ImageUpload upload = iFactory.getInstance(MediaProvider.TWITPIC, auth);
	      
	      if (is != null)
	      {
	//        String strResult = upload.upload("example.jpg", is, mEtContent.getText().toString());
	//        twitter.updateStatus(mEtContent.getText().toString() + " " + strResult);
	      }
	      else
	        twitter.updateStatus(content);
	     	new AlertDialog.Builder(at)
	     	.setMessage("트위터 성공적으로 등록 되었습니다.")
	     	.setPositiveButton("확인",null)
	     	.show();	      
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	     	new AlertDialog.Builder(at)
	     	.setMessage("이미 등록한 트윗 입니다")
	     	.setPositiveButton("확인",null)
	     	.show();
	    }
	    finally
	    {
	      try
	      {
	        is.close();
	      }
	      catch (Exception e)
	      {}
	    }


	}
	
	
}
