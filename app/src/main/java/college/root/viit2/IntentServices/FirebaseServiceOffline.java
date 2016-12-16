package college.root.viit2.IntentServices;

import android.app.IntentService;
import android.content.Intent;

import com.firebase.client.Firebase;

/**
 * Created by root on 14/12/16.
 */

public class FirebaseServiceOffline extends IntentService {


    Firebase firebase;
    public FirebaseServiceOffline() {
        super("college.root.viit2.FirebaseIntentService");
        firebase = new Firebase("https://finalone-c05f6.firebaseio.com/");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        firebase.goOffline();

    }
}
