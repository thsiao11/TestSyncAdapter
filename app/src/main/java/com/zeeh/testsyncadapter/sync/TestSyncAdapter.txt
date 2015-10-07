package com.zeeh.testsyncadapter.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.zeeh.testsyncadapter.R;

/**
*    SunshineSyncAdapter - This is the actual sync adapter; it extends AbstractThreadedSyncAdapter. Note the following methods:
*
*        onPerformSync - This is the important method which is overridden when you subclass AbstractThreadedSyncAdapter.
*            This is what happens when a sync occurs. In our case, we’ll eventually want to move the network code here.
*
*        getSyncAccount() - This is a helper method we’ve coded for you. Whenever you request a sync, you need a sync account.
*            This is in case requesting the sync requires a login (in our situation,
*            when we don't need an account, we'll need to use a dummy account). This method gives us one of our dummy accounts.
*
*        syncImmediately() - This is a helper method we’ve coded for you.
*            It tells the system to perform a sync with our sync adapter immediately.
 *  http://developer.android.com/training/sync-adapters/running-sync-adapter.html
 *  http://developer.android.com/training/sync-adapters/index.html
 *
 *  Import notes: Add code to run sync in the onPerformSync() method. Also bring other methods to this class that do data synching
 * The control flow goes:
*  Your MainActivity is created and the sync adapter is initialized.
*  During initialization, getSyncAccount is called.
*  getSyncAccount will create a new account if no sunshine.example.com account exists.
 *  If this is the case, onAccountCreated will be called.
*  onAccountCreated configures the periodic sync and calls for an immediate sync.
 *  At this point, Sunshine will sync with the Open Weather API either every 3 hours
 *  (if the build version is less than KitKat) or everyone 1 hour (if the build version is greater than or equal to KitKat)
 **/

public class TestSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = TestSyncAdapter.class.getSimpleName();

    // Interval at which to sync, in seconds.
    // 60 seconds (1 minute) * 180 = 3 hours. Every 3 hours, synching will run.
    public static final int SYNC_INTERVAL = 60  * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;

    public TestSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");

        //Add code to run sync here. Code like http connect, update database, etc...

    }

    /**
     * Helper method to have the sync adapter sync immediately
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    /**
     * Helper method to get the fake account to be used with SyncAdapter, or make a new one
     * if the fake account doesn't exist yet.  If we make a new account, we call the
     * onAccountCreated method so we can initialize things.
     *
     * @param context The context used to access the account service
     * @return a fake account.
     */
    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account, using dummy accountID = R.string.app_name, account type, and no password
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    /**
     * Helper method to schedule the sync adapter periodic execution
     */
    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }


    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        TestSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}