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

import java.net.URI;

import com.ak.android.akplaza.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class TwitterLogin extends Activity {

	Intent mIntent;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.twitter_login);
	    WebView webView = (WebView) findViewById(R.id.twitt_webView);
	    webView.setWebViewClient(new WebViewClient(){
	    	public void onPageFinished(WebView view, String url){
	    		 super.onPageFinished(view, url);
//	    		 Log.d("Twitter", url);
	    		 if (url != null && url.equals("http://mobile.twitter.com/")){
	    			 finish();
	    		 }else if (url != null && url.startsWith(C.TWITTER_CALLBACK_URL)){
//	    			 String[] urlParameters = url.split("\\?")[1].split("&");
	    			 String oauthToken = "";
	    			 String oauthVerifier = "";
//
	    			 try{
//	    				 if (urlParameters[0].startsWith("oauth_token")){
//	    					 oauthToken = urlParameters[0].split("=")[1];
//	    				 }else if (urlParameters[1].startsWith("oauth_token")){
//	    					 oauthToken = urlParameters[1].split("=")[1];
//	    				 }
//
//	    				 if (urlParameters[0].startsWith("oauth_verifier")){
//	    					 oauthVerifier = urlParameters[0].split("=")[1];
//	    				 }else if (urlParameters[1].startsWith("oauth_verifier")){
//	    					 oauthVerifier = urlParameters[1].split("=")[1];
//	    				 }

//	    				 Log.d("TLOGIN", oauthToken + " , " + oauthVerifier);
	    				 Uri uri = Uri.parse(url);
	    				 oauthToken = uri.getQueryParameter("oauth_token");
	    				 oauthVerifier = uri.getQueryParameter("oauth_verifier");
	    				 if(oauthToken!= null && oauthVerifier !=null){
//	    					 Log.d("oauth_token",oauthToken);
//		    				 Log.d("oauth_verifier",oauthVerifier);
	    				 }
	    			 mIntent.putExtra("oauth_token", oauthToken);
    				 mIntent.putExtra("oauth_verifier", oauthVerifier);
	    	            
	    				 setResult(RESULT_OK, mIntent);
	    				 finish();
	    			 }catch(Exception e){
	    				 e.printStackTrace();
	    			 }
	    		 }
	    	}
	    });
	    
	    mIntent = getIntent();
	    String url1 = mIntent.getStringExtra("auth_url");
	    webView.loadUrl(url1);
	    
	}
}
