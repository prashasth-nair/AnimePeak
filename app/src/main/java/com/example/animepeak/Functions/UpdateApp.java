package com.example.animepeak.Functions;


import static com.example.animepeak.Activity.MainActivity.is_home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Environment;

import android.util.Log;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.example.animepeak.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Objects;

public class UpdateApp {
    @SuppressLint("StaticFieldLeak")

    private static long downloadId;

    public static class update_app extends AsyncTask<Void, Void, String> {
        static Activity activity;

        public update_app(Activity activity) {
            update_app.activity = activity;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            HttpURLConnection urlConnection = null;
            try {

                URL url = new URL("https://prashasth-nair.github.io/api/appdownloader/result.json");
                urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }

                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {

                JSONObject jsonObject = new JSONObject(result);
                String version = jsonObject.getString("Version");
                String download = jsonObject.getString("Download");
                // Get the version name of the currently installed APK
                String currentVersionName = BuildConfig.VERSION_NAME;

                if (!isUpdateAvailable(currentVersionName, version)) {
                    checkForUpdates(download);
                }else {
                    if (!is_home) {
                        Toast.makeText(activity, "No Update Available", Toast.LENGTH_LONG).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private static void checkForUpdates(String url) {
            // Check if a new version of the app is available

            // Show a dialog box informing the user that a new update is available
            new AlertDialog.Builder(activity)
                    .setTitle("New Update Available")
                    .setMessage("A new version of the app is available. Do you want to update?")

                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Start the download and install process
                            downloadApk(url);
                        }
                    })
                    .setNegativeButton("Not Now", null)
                    .create()
                    .show();

        }

        private static boolean isUpdateAvailable(String current, String new_version) {
            // Implement your logic to check if a new version of the app is available
            // Return true if a new version is available, false otherwise
            return Objects.equals(new_version, current);
        }

        private static void downloadApk(String url) {
            DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setTitle("AnimePeak Update");
            request.setDescription("Downloading update...");
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "AnimePeak.apk");
            downloadId = downloadManager.enqueue(request);
            // Save the download ID somewhere so you can query the status later
            // Register a broadcast receiver to listen to the download completion event
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                    if (referenceId == downloadId) {
                        // Download completed, initiate the installation process
                        Toast.makeText(ctxt, "Download complete install update by clicking on the notification.", Toast.LENGTH_LONG).show();
                        installApk(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/AnimePeak.apk"));
                    }
                }
            };
            activity.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }

        private static void installApk(File apkFile) {
            // Get the Uri of the downloaded APK file using FileProvider
            Uri apkUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", apkFile);
            // Create the intent to install the APK
            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            intent.setData(apkUri);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);

            // Launch the intent to install the APK
            activity.startActivityForResult(intent, 100);
        }


    }

}
