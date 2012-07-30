package com.testproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import com.testproject.activities.MainActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by User: DmitriyS
 * Date: 28/07/12:13:18
 */
public class CronosConnect extends AsyncTask<String, Pair<Integer, String>, String> {
//    http://localhost:8080/cronos/administration/customize/site/projects.cro?j_username=micabral&j_password=

    ProgressDialog dialog;
    private Context context;
    private boolean isConnectingActive = false;

    public CronosConnect(Context appContext, boolean isConnectingActive) {
        this.context = appContext;
        this.isConnectingActive = isConnectingActive;
    }

    @Override
    protected void onPreExecute() {
//        super.onPreExecute();    //To change body of overridden methods use File | Settings | File Templates.
        if (isConnectingActive) {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Connecting...");
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);    //To change body of overridden methods use File | Settings | File Templates.
        ((MainActivity) context).setStr(s);
        if (isConnectingActive) {
            dialog.dismiss();
            Toast.makeText(context, "Logged in successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String username = strings[0];
        String password = strings[1];
        InputStream is = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(strings[2] + "?j_username=" + username + "&j_password=" + password);
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();
        } catch (Exception e) {
            Log.e("log_error", "Error occurred during http connect" + e.toString());
        }

        StringBuilder stringBuilder = null;
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            stringBuilder = new StringBuilder();
            stringBuilder.append(reader.readLine() + "\n");
            String line = "0";
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            is.close();
            result = stringBuilder.toString();
        } catch (Exception e) {
            Log.e("log_err", "Error occurred whyle trying to convert result" + e.toString());
        }
        Pair pair = new Pair(1, result);
        publishProgress(pair);
        return result;
    }

    @Override
    protected void onProgressUpdate(Pair... progress) {
//        super.onProgressUpdate(progress);    //To change body of overridden methods use File | Settings | File Templates.
        Object position = (Object) progress[0].first;
        JSONObject result = null;
        try {
            result = new JSONObject((String) progress[0].second);
        } catch (JSONException e) {
            Log.e("err", "parseerror");
//            Toast.makeText(context, "Can not parse string to json object.", Toast.LENGTH_SHORT).show();
        }
    }
}
