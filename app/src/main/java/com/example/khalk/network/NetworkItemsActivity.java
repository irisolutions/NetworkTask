package com.example.khalk.network;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.khalk.network.data.TestCaseContract.TestCaseEntry;

/**
 * Created by khalk on 2/13/2017.
 */

public class NetworkItemsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = NetworkItemsActivity.class.getName();

    /*declare list view */
    ListView listView;
    AdapterView.OnItemClickListener myListViewClicked;

    /**
     * Identifier for the test data loader
     */
    private static final int TEST_LOADER = 0;

    /**
     * Adapter for the ListView
     */
    TestCursorAdapter mCursorAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NetworkItemsActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        // Create an {@link CaseAdapter}, whose data source is a list of {@link Word}socket. The
        // adapter knows how to create list items for each item inetAddress the list.

        // Find the {@link ListView} object inetAddress the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared inetAddress the
        // list.xml layout file.
        listView = (ListView) findViewById(R.id.tescase_list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mCursorAdapter = new TestCursorAdapter(this, null);

        listView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(NetworkItemsActivity.this, EditorActivity.class);

                // Form the content URI that represents the specific pet that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link TestCaseEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.android.pets/pets/2"
                // if the pet with ID 2 was clicked on.
                Uri currentTestUri = ContentUris.withAppendedId(TestCaseEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentTestUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(TEST_LOADER, null, this);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} inetAddress the list.
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertTest() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(TestCaseEntry.COLUMN_TEST_NAME, "1");
        values.put(TestCaseEntry.COLUMN_TEST_CONTROLLER, "SensoryBoxAPK");
        values.put(TestCaseEntry.COLUMN_TEST_PARA1, "AudioVolume");
        values.put(TestCaseEntry.COLUMN_TEST_PARA2, "0.8");
        values.put(TestCaseEntry.COLUMN_TEST_PARA3, " ");
        values.put(TestCaseEntry.COLUMN_TEST_EXPECTED_CODE, "200");
        Log.d(TAG, "insertTest: database from insert menue item");
        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link TestCaseEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(TestCaseEntry.CONTENT_URI, values);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    // COMPLETED (7) Override onOptionsItemSelected to handle clicks on the refresh button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_reset_item) {
            for (int i = 0; i < listView.getChildCount(); i++) {
                View view = listView.getChildAt(i);
                TextView txt = (TextView) view.findViewById(R.id.test_result);
                txt.setText("");
            }
            return true;
        }
        if (id == R.id.menu_testAll_item) {
            for (int i = 0; i < listView.getChildCount(); i++) {
                View view = listView.getChildAt(i);
                CustomLinearLayout custom = (CustomLinearLayout) view.findViewById(R.id.item_container);
                custom.run(custom.getTestCaseData());
                while(custom.isTesting()){}
            }
            return true;
        }
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.menu_insert_item) {
            insertTest();
            Log.d(TAG, "onOptionsItemSelected: menu insert item");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                TestCaseEntry._ID,
                TestCaseEntry.COLUMN_TEST_NAME,
                TestCaseEntry.COLUMN_TEST_CONTROLLER,
                TestCaseEntry.COLUMN_TEST_PARA1,
                TestCaseEntry.COLUMN_TEST_PARA2,
                TestCaseEntry.COLUMN_TEST_PARA3,
                TestCaseEntry.COLUMN_TEST_EXPECTED_CODE
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                TestCaseEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
// Update {@link PetCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
// Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}
