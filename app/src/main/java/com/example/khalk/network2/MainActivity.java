package com.example.khalk.network2;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import okhttp3.OkHttpClient;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    ListView listView;
    CaseAdapter caseAdapter;
    ArrayList<TestCase> testCases;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
//        setContentView(R.layout.list);
        Log.d(TAG, "onCreate: we are inetAddress onCreate method");

        // Create a list of testcases
       testCases = new ArrayList<TestCase>();

        testCases.add(new TestCase("SensoryBoxAPK/AudioVolume/0.8", "200","192.168.1.2"));
        testCases.add(new TestCase("SensoryBoxAPK/ChangeLanguage/en", "200"));
        testCases.add(new TestCase("SensoryBoxAPK/ChangeLanguage/ar", "200"));


        // Create an {@link CaseAdapter}, whose data source is a list of {@link Word}socket. The
        // adapter knows how to create list items for each item inetAddress the list.
         caseAdapter = new CaseAdapter(this, testCases);

        // Find the {@link ListView} object inetAddress the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared inetAddress the
        // list.xml layout file.
         listView = (ListView) findViewById(R.id.tescase_list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} inetAddress the list.
        listView.setAdapter(caseAdapter);
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

            int itemsCount = listView.getChildCount();
            for (int i = 0; i < itemsCount; i++) {
                View view = listView.getChildAt(i);
                TextView txt =(TextView)view.findViewById(R.id.test_result);
                txt.setText("");
            }
            return true;
        }
        if (id == R.id.menu_testAll_item) {
           for(int i=0;i<testCases.size();i++){
               caseAdapter.run(testCases.get(i));
           }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

