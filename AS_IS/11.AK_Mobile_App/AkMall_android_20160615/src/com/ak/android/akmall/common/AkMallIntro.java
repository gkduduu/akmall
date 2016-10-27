package com.ak.android.akmall.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.ak.android.akmall.activity.AkMallActivity;

public class AkMallIntro extends AsyncTask <String,String,Map<String,String>>{
	
	
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		TimerTask mTask;
        Timer mTimer;
    	mTask = new TimerTask() {
            @Override
            public void run() {
            	AkMallIntro intro = new AkMallIntro();
            	intro.cancel(true);
            }
        };
         
        mTimer = new Timer();
         
        mTimer.schedule(mTask, 5000);
		//cancel(true);
	}

	@Override
	protected Map<String, String> doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		Map<String,String> map =  new HashMap <String, String>();
		
		try {
			//map = getIntroXml();
			//Map<String, String> map = new HashMap<String, String>();

	        XMLLoader loader = new XMLLoader(AkMallActivity.APP_INTRO_IMAGE);
	        Document doc = loader.getDocument();
	        NodeList appIntro = doc.getElementsByTagName("appIntro");

	        if (appIntro != null) {
	            for (int i = 0; i < appIntro.getLength(); i++) {
	                Node nodeList = appIntro.item(i);
	                NodeList childs = nodeList.getChildNodes();

	                for (int j = 0; j < childs.getLength(); j++) {
	                    Node child = childs.item(j);
	                    String nodeName = child.getNodeName();

	                    if ("use_yn".equalsIgnoreCase(nodeName)) {
	                        map.put("use_yn", URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
	                    }

	                    if ("link".equalsIgnoreCase(nodeName)) {
	                        map.put("link",
	                                URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
	                    }

	                    if ("dtime".equalsIgnoreCase(nodeName)) {
	                        map.put("dtime", URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
	                    }
	                }
	            }
	        } else {
	            map = null;
	        }

		}catch (Exception e) {
			// TODO: handle exception
			cancel(true);
		}
		return map;
	}

	public static Bitmap setIntro(Map<String, String> map){
			
		Bitmap bmImg = null;
		try{
        	
        	String useYn = map.get("use_yn");
        	String link = map.get("link");
        	String dtime = map.get("dtime");
        	
        	if(useYn.equalsIgnoreCase("y")){
        		ImageDown down = new ImageDown();
        		
        		bmImg = down.execute(link).get();
        		
        		down.cancel(true);
        	}
        	
        }catch(Exception e){
        	e.printStackTrace();
        }
		
		return bmImg;
	}
	
	public static Map<String, String> getIntroXml() throws UnsupportedEncodingException {

        Map<String, String> map = new HashMap<String, String>();

        XMLLoader loader = new XMLLoader(AkMallActivity.APP_INTRO_IMAGE);
        Document doc = loader.getDocument();
        NodeList appIntro = doc.getElementsByTagName("appIntro");

        if (appIntro != null) {
            for (int i = 0; i < appIntro.getLength(); i++) {
                Node nodeList = appIntro.item(i);
                NodeList childs = nodeList.getChildNodes();

                for (int j = 0; j < childs.getLength(); j++) {
                    Node child = childs.item(j);
                    String nodeName = child.getNodeName();

                    if ("use_yn".equalsIgnoreCase(nodeName)) {
                        map.put("use_yn", URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
                    }

                    if ("link".equalsIgnoreCase(nodeName)) {
                        map.put("link",
                                URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
                    }

                    if ("dtime".equalsIgnoreCase(nodeName)) {
                        map.put("dtime", URLDecoder.decode(XMLLoader.getNodeValue(child), "UTF-8"));
                    }
                }
            }
        } else {
            map = null;
        }

        return map;
    }


}

class ImageDown extends AsyncTask<String, Integer,Bitmap>{
    
	Bitmap bmImg;
	Bitmap bm = null; 
	HttpClient httpclient = null;  
    HttpGet getRequest = null;  
    HttpResponse response;  
    
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		
		
		
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		
	}
	
	

	

	@Override
	protected Bitmap doInBackground(String... urls) {
	    // TODO Auto-generated method stub
	  
	    return downloadBitmap(urls[0]);
	}
	
	private Bitmap downloadBitmap(String imageUrl) {  
         
        httpclient = new DefaultHttpClient();  
        getRequest = new HttpGet(imageUrl);  
        try {  
            response = httpclient.execute(getRequest);  
            int statusCode = response.getStatusLine().getStatusCode();  
            if (statusCode != HttpStatus.SC_OK) {   
                Log.w("egg", "Error " + statusCode + " while retrieving bitmap from " + imageUrl);   
                return null;  
            }  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {  
                InputStream inputStream = null;  
                try {  
                    inputStream = entity.getContent();   
                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);  
                    bm = bitmap;  
                    //eturn bitmap;  
                } finally {  
                    if (inputStream != null) {  
                        inputStream.close();    
                    }  
                    entity.consumeContent();  
                }  
            }  
        } catch (ClientProtocolException e) { e.printStackTrace(); }   
        catch (IOException e) { e.printStackTrace(); }  
        return bm;  
    }  
	
	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		
		httpclient.getConnectionManager().shutdown();
	}
}

