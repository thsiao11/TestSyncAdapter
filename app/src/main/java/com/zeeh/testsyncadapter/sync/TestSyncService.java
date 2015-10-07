package com.zeeh.testsyncadapter.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class TestSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static TestSyncAdapter sTestSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("TestSyncService", "onCreate - TestSyncService");
        synchronized (sSyncAdapterLock) {
            if (sTestSyncAdapter == null) {
                sTestSyncAdapter = new TestSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sTestSyncAdapter.getSyncAdapterBinder();
    }
}