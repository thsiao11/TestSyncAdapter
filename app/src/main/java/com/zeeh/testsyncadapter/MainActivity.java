package com.zeeh.testsyncadapter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zeeh.testsyncadapter.sync.TestSyncAdapter;

/**
* To use sync adapter:
 * 0. copy all files under sync and data folders. Also so xml files and AdnroidManifest.txt from temp_res folder
 * 1. Add code for performing sync in TestSyncAdapter.onPerformSync()
 * 2. Change the SYNC_INTERVAL and SYNC_FLEXTIME for frequency of synch
 * 3. Add executeSync() below when needing to synch immediately
 **/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize the SyncAdapter and start synching on its own
        TestSyncAdapter.initializeSyncAdapter(this);

        // Only run executeSync() if you want to sync immediately. This method should only be run when user wnat to refresh righ away
//        executeSync();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // Below is the call to sync immediately!
    private void executeSync() {
        TestSyncAdapter.syncImmediately(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
