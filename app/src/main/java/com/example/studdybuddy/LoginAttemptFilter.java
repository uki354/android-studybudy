package com.example.studdybuddy;

import static com.example.studdybuddy.AuthActivity.LOGIN_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.studdybuddy.SearchActivity.SearchActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
        String json = "{\"email\":\"" + user.getEmail() + "\",\"password\":\"" + user.getPassword() + "\"}";
        httpRequest.execute(json);
    }

    private void successfulLogin(Activity activity){
        JwtTokenManager.storeTokenToDevice(activity,user.getJwt());
        Intent intent = new Intent(activity.getApplicationContext(), SearchActivity.class);
        activity.startActivity(intent);
    }



    protected class HttpRequest extends  AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(LOGIN_URL);
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


                if((connection.getResponseCode() == 200)){
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder total = new StringBuilder();
                    for (String line; (line = br.readLine()) != null; ) {
                        total.append(line);
                    }
                    inputStream.close();
                    return total.toString();
                }else{
                    return "";
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            if (s.length() > 1) {
                user.setJwt(s);
                successfulLogin(activity);
            }
            else{
                // set login failed
            }

        }


    }
}
