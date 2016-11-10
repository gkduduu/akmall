package android.ak.com.akmall.utils.gcm;

import android.ak.com.akmall.R;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by gkduduu on 2016. 11. 10..
 */

public class RegistrationIntentService extends IntentService{

    public RegistrationIntentService() {
        super("");
    }
    @Override
    public void onHandleIntent(Intent intent) {
        // ...
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i("jhy","GCM Token ==> " + token);
        }catch(IOException e) {
            e.getMessage();
        }

        // ...
    }
}
