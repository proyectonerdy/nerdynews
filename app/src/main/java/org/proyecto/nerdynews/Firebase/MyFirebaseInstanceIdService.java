package org.proyecto.nerdynews.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by eloy on 18/11/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private String TAG = MyFirebaseInstanceIdService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);
    }
}
