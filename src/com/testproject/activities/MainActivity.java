package com.testproject.activities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.testproject.CronosConnect;
import com.testproject.R;
import org.json.JSONObject;

public class MainActivity extends Activity {

    public boolean isNetworkConnected = false;

    private ProgressBar progressBar;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        isNetworkConnected = isNetworkAvailableAndConnected();

        if (!isNetworkConnected) {
            Toast.makeText(this, "Network is not available.", Toast.LENGTH_SHORT).show();
        }

        final EditText usernameTextView = (EditText) findViewById(R.id.usernameId);
        final EditText passwordTextView = (EditText) findViewById(R.id.passwordId);
        Button closeButton = (Button) findViewById(R.id.closeButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!isNetworkConnected) {
                    Toast.makeText(view.getContext(), "Network is not available.", Toast.LENGTH_SHORT).show();
                    return;
                }
//                ((AccountManager)getSystemService(Context.ACCOUNT_SERVICE))
//                ((AccountManager)getSystemService(Context.ACCOUNT_SERVICE)).getAccountsByType("com.google")
//                ((AccountManager)getSystemService(Context.ACCOUNT_SERVICE)).getPassword(((AccountManager)getSystemService(Context.ACCOUNT_SERVICE)).getAccountsByType("com.google")[0])
                username = usernameTextView.getText().toString();
                password = passwordTextView.getText().toString();
                authenticateUser(username, password);
                connectToServer(username, password);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.loading_progress);
        progressBar.setProgress(0);
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        return false;
    }

    private String username = null;
    private String password = null;
    private String str;

    public void setStr(String str) {
        this.str = str;
    }

    private void authenticateUser(String username, String password) {
        new CronosConnect(MainActivity.this, true).execute(username, password, "http://10.0.0.11:8080/cronos/auth/login.do");
    }

    public void connectToServer(String username, String password) {
        new CronosConnect(MainActivity.this, false).execute(username, password, "http://10.0.0.11:8080/cronos/services.cro");

        JSONObject result = new JSONObject();
        int i = 0;
    }
}
