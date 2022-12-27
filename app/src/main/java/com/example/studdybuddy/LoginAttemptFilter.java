package com.example.studdybuddy;

import static com.example.studdybuddy.AuthActivity.LOGIN_URL;

import android.app.Activity;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginAttemptFilter extends AuthChain {

    private User user;
    private HttpRequest httpRequest;
    private Activity activity;

    @Override
    public boolean doFilter(User user, Activity activity) {
        this.activity = activity;
        attemptLogin(user);
        return true;
    }


    public void attemptLogin(User user){
        this.user = user;
        httpRequest = new HttpRequest();
        httpRequest.execute();
    }

    private void successfulLogin(Activity activity){
        JwtTokenManager.storeTokenToDevice(activity);
        // move to another activity
    }



    protected class HttpRequest extends  AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(LOGIN_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                for (String line; (line = br.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                inputStream.close();
                return total.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            if (s.length() > 1) {
                successfulLogin(activity);
                user.setJwt(s);
            }
            else{
                // set login failed
            }

        }


    }
}
