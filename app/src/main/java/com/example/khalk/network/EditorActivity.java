package com.example.khalk.network;

/**
 * Created by khalk on 2/21/2017.
 */

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.khalk.network.data.TestCaseContract.TestCaseEntry;
/**
 * Allows user to create a new test or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the test data loader */
    private static final int EXISTING_TEST_LOADER = 0;

    /** Content URI for the existing test (null if it's a new test) */
    private Uri mCurrentTestUri;

    /** EditText field to enter the test's name */
    private EditText mNameEditText;

    /** EditText field to enter the test's controller */
    private EditText mControllerEditText;

    /** EditText field to enter the test's para1 */
    private EditText mPara1EditText;

    /** EditText field to enter the test's para2 */
    private EditText mPara2EditText;

    /** EditText field to enter the test's para3 */
    private EditText mPara3EditText;

    /** EditText field to enter the test's expected code */
    private EditText mExpectedCodeEditText;


    /** Boolean flag that keeps track of whether the test has been edited (true) or not (false) */
    private boolean mTestHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mTestHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mTestHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new test or editing an existing one.
        Intent intent = getIntent();
        // TODO: 2/21/2017 get intenet from prev activity
        mCurrentTestUri = intent.getData();

        // If the intent DOES NOT contain a test content URI, then we know that we are
        // creating a new test.
        if (mCurrentTestUri == null) {
            // This is a new test, so change the app bar to say "Add a Test"
            setTitle(getString(R.string.editor_activity_title_new_test));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a test that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            // Otherwise this is an existing test, so change app bar to say "Edit Test"
            setTitle(getString(R.string.editor_activity_title_edit_test));

            // Initialize a loader to read the test data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_TEST_LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_test_name);
        mControllerEditText=(EditText)findViewById(R.id.edit_controller_name);
        mPara1EditText=(EditText)findViewById(R.id.edit_para1_name);
        mPara2EditText=(EditText)findViewById(R.id.edit_para2_name);
        mPara3EditText=(EditText)findViewById(R.id.edit_para3_name);
        mExpectedCodeEditText=(EditText)findViewById(R.id.edit_expected_code);

        // Setup OnTouchListeners on all the input fields, so we can determine if the user
        // has touched or modified them. This will let us know if there are unsaved changes
        // or not, if the user tries to leave the editor without saving.
        mNameEditText.setOnTouchListener(mTouchListener);
        mControllerEditText.setOnTouchListener(mTouchListener);
        mPara1EditText.setOnTouchListener(mTouchListener);
        mPara2EditText.setOnTouchListener(mTouchListener);
        mPara3EditText.setOnTouchListener(mTouchListener);
        mExpectedCodeEditText.setOnTouchListener(mTouchListener);
    }


    /**
     * Get user input from editor and save test into database.
     */
    private void saveTest() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String controllerString = mControllerEditText.getText().toString().trim();
        String para1String = mPara1EditText.getText().toString().trim();
        String para2String = mPara2EditText.getText().toString().trim();
        String para3String = mPara3EditText.getText().toString().trim();
        String expectedCodeString = mExpectedCodeEditText.getText().toString().trim();

        // Check if this is supposed to be a new test
        // and check if all the fields in the editor are blank
        if (mCurrentTestUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(controllerString) &&
                TextUtils.isEmpty(para1String) && TextUtils.isEmpty(para2String )&& TextUtils.isEmpty(para3String )
                && TextUtils.isEmpty(expectedCodeString )) {
            // Since no fields were modified, we can return early without creating a new test.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }

        // Create a ContentValues object where column names are the keys,
        // and test attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(TestCaseEntry.COLUMN_TEST_NAME, nameString);
        values.put(TestCaseEntry.COLUMN_TEST_CONTROLLER, controllerString);
        values.put(TestCaseEntry.COLUMN_TEST_PARA1, para1String);
        values.put(TestCaseEntry.COLUMN_TEST_PARA2,para2String);
        values.put(TestCaseEntry.COLUMN_TEST_PARA3,para3String);
        values.put(TestCaseEntry.COLUMN_TEST_EXPECTED_CODE,expectedCodeString);

        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.

        // Determine if this is a new or existing test by checking if mCurrentTestUri is null or not
        if (mCurrentTestUri == null) {
            // This is a NEW test, so insert a new test into the provider,
            // returning the content URI for the new test.
            Uri newUri = getContentResolver().insert(TestCaseEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_test_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_test_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise this is an EXISTING test, so update the test with content URI: mCurrentTestUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentTestUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentTestUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_test_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_test_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new test, hide the "Delete" menu item.
        if (mCurrentTestUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save test to database
                saveTest();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the test hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mTestHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the test hasn't changed, continue with handling back button press
        if (!mTestHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Since the editor shows all test attributes, define a projection that contains
        // all columns from the test table
        String[] projection = {
                TestCaseEntry._ID,
                TestCaseEntry.COLUMN_TEST_NAME,
                TestCaseEntry.COLUMN_TEST_CONTROLLER,
                TestCaseEntry.COLUMN_TEST_PARA1,
                TestCaseEntry.COLUMN_TEST_PARA2,
                TestCaseEntry.COLUMN_TEST_PARA3
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentTestUri,         // Query the content URI for the current test
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of test attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_NAME);
            int controllerColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_CONTROLLER);
            int para1ColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA1);
            int para2ColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA2);
            int para3ColumnIndex = cursor.getColumnIndex(TestCaseEntry.COLUMN_TEST_PARA3);


            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String controller = cursor.getString(controllerColumnIndex);
            String para1=cursor.getString(para1ColumnIndex);
            String para2=cursor.getString(para2ColumnIndex);
            String para3=cursor.getString(para3ColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mControllerEditText.setText(controller);
            mPara1EditText.setText(para1);
            mPara2EditText.setText(para2);
            mPara3EditText.setText(para3);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mControllerEditText.setText("");
        mPara1EditText.setText("");
        mPara2EditText.setText("");
        mPara3EditText.setText("");
    }

    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the test.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this test.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the test.
                deleteTest();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the test.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the test in the database.
     */
    private void deleteTest() {
        // Only perform the delete if this is an existing test.
        if (mCurrentTestUri != null) {
            // Call the ContentResolver to delete the test at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentTestUri
            // content URI already identifies the test that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentTestUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_test_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_test_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}