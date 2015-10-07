package com.zeeh.testsyncadapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * The service which allows the sync adapter framework to access the authenticator_txt.
 * SunshineSyncService - This service provides framework access to your TestSyncAdapter.
 * This too is almost exactly the same as the stub code from the Developer Guide on sync adapters.
 */
public class TestAuthenticatorService extends Service {
    // Instance field that stores the authenticator_txt object
    private TestAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator_txt object
        mAuthenticator = new TestAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator_txt's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
