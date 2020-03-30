package com.example.top10downloader;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ListView listApps;
    private String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=%d/xml";
    private int feedLimit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listApps = findViewById(R.id.xmlListView);

        downloadUrl("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String feedUrl;

        switch (id) {
            case R.id.mnuFree:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml";
                break;
            case R.id.mnuPaid:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/toppaidapplications/limit=10/xml";
                break;
            case R.id.mnuSongs:
                feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=10/xml";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        downloadUrl(feedUrl);
        return true;
    }

    private void downloadUrl(String url) {
        Log.d(TAG, "downloadUrl: starting asyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute(url);
        Log.d(TAG, "downloadUrl: done");
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        private static final String TAG = "DownloadData";

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);
            if (rssFeed == null)
                Log.e(TAG, "doInBackground: Error downloading");
            return rssFeed;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: parameter is " + s);
            ParserApplications parserApplications = new ParserApplications();
            parserApplications.parse(s);

//            ArrayAdapter<FeedEntry> arrayAdapter = new ArrayAdapter<FeedEntry>(
//                    MainActivity.this, R.layout.list_item, parserApplications.getApplications());
//            listApps.setAdapter(arrayAdapter);

            FeedAdapter feedAdapter = new FeedAdapter(
                    MainActivity.this, R.layout.list_record, parserApplications.getApplications());
            listApps.setAdapter(feedAdapter);
        }

        private String downloadXML(String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "DownloadXml: responce code is: " + response);
//                InputStream inputStream = connection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];

                while (true) {
                    charsRead = bufferedReader.read(inputBuffer);
                    if (charsRead < 0)
                        break;
                    if (charsRead > 0)
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                }
                bufferedReader.close();

                return xmlResult.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "DownloadXml: Invalid URL " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "DownloadXml: IOException reading data " + e.getMessage());
            }

            return null;
        }
    }
}
