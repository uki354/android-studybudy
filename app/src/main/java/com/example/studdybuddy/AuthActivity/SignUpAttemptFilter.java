package com.example.studdybuddy.AuthActivity;

import static com.example.studdybuddy.AuthActivity.AuthActivity.SIGNUP_URL;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.studdybuddy.AuthActivity.AuthChain;
import com.example.studdybuddy.AuthActivity.LoginAttemptFilter;
import com.example.studdybuddy.User;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpAttemptFilter extends AuthChain {

    private User user;
    private Activity activity;

    @Override
    public boolean doFilter(User user, Activity activity) {
        this.user = user;
        this.activity = activity;
        HttpRequest httpRequest = new HttpRequest();
        String json = "{\"email\":\"" + user.getEmail() + "\",\"password\":\"" + user.getPassword() + "\","
                + "\"name\":\"" + user.getName() + "\",\"lastname\":\"" + user.getLastname() + "\","
                +"\"gender\":\"" + user.getGender() + "\"}";

        httpRequest.execute(json);
        return false;
    }


    private class HttpRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(SIGNUP_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStream outStream = connection.getOutputStream();
                OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, "UTF-8");
                outStreamWriter.write(strings[0]);
                outStreamWriter.flush();
                outStreamWriter.close();
                outStream.close();

                if (connection.getResponseCode() == 201){
                    LoginAttemptFilter loginAttemptFilter = new LoginAttemptFilter();
                    loginAttemptFilter.doFilter(user, activity);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

