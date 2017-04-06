package com.example.khalk.network;
        import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class DatabaseUtil {
    //You need to declare permission
    // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    //in your Manifest file in order to use this class

    //______________________________________________________________________________________________

    //todo -> rename the database according to your application
//    final static String DATABASE_NAME = "MyDatabase.sqlite";
    final static String DATABASE_NAME = "/data/data/com.example.khalk.network/databases/test_case.db";

    //example WhatsApp :  /data/data/com.whatsapp/databases/msgstore.db
//    final static String FOLDER_EXTERNAL_DIRECTORY = Environment.getExternalStorageDirectory() + "/shanraisshan";

    final static String FOLDER_EXTERNAL_DIRECTORY = "/mnt/extSdCard/";
    private static final String TAG ="" ;

    //______________________________________________________________________________________________
    /**
     * Call this method from any activity in your app (
     * for example ->    DatabaseUtil.copyDatabaseToExtStg(MainActivity.this);
     * this method will copy the database of your application into SDCard folder "shanraisshan/MyDatabase.sqlite" (DATABASE_NAME)
     */
    public static void copyDatabaseToExtStg(Context ctx) {
        //external storage file
        File externalDirectory = new File(FOLDER_EXTERNAL_DIRECTORY);
        if (!externalDirectory.exists())
            externalDirectory.mkdirs();
        File toFile = new File(externalDirectory, DATABASE_NAME);
        //internal storage file
        //https://developer.android.com/reference/android/content/Context.html#getDatabasePath(java.lang.String)
        File fromFile = ctx.getDatabasePath(DATABASE_NAME);
        //example WhatsApp :  /data/data/com.whatsapp/databases/msgstore.db
        if (fromFile.exists()) {

            copy(fromFile, toFile);
        } else {
            Log.d(TAG, "copyDatabaseToExtStg:   file doesn't exist ++++++++++++++++++++++++++ " + fromFile.getPath());

        }
    }


    //______________________________________________________________________________________________ Utility function
    /**
     * @param fromFile source location
     * @param toFile destination location
     * copy file from 1 location to another
     */
    static void copy(File fromFile, File toFile) {
        try {
            FileInputStream is = new FileInputStream(fromFile);
            FileChannel src = is.getChannel();
            FileOutputStream os = new FileOutputStream(toFile);
            FileChannel dst = os.getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();	is.close();
            dst.close();	os.close();
        } catch (Exception e) {
            //todo in case of exception
            Log.e(TAG, "copy: ", e );
        }
    }
}