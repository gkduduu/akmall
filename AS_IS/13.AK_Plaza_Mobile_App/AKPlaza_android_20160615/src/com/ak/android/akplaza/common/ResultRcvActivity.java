package com.ak.android.akplaza.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ResultRcvActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // TODO Auto-generated method stub
        AKPlazaApplication myApp    = (AKPlazaApplication)getApplication();
        Intent            myIntent = getIntent();

        if ( myIntent.getData().getScheme().equals( "akplaza" ) == true )
        {
            myApp.b_type      = true;
            myApp.m_uriResult = myIntent.getData();
        }
        else
        {
            myApp.m_uriResult = null;
        }
        
        finish();
    }
}
