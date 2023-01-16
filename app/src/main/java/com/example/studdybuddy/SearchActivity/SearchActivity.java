package com.example.studdybuddy.SearchActivity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.studdybuddy.AuthActivity;
import com.example.studdybuddy.JwtTokenManager;
import com.example.studdybuddy.MapsActivity.GoogleMapsActivity;
import com.example.studdybuddy.R;
import com.example.studdybuddy.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private Spinner age;
    private Spinner gender;
    private Spinner university;
    private Spinner range;
    private Button searchNearby;
    private Button submit;
    private Button logout;

    private Fragment initImageFragment;


    private final String[] ageArr = {"Any", "18-20", "20-22", "22-24", "24-26"};
    private final String[] genderArr = {"Any", "male", "female"};
    private final String[] rangeArr = {"Any", "2km", "5km", "30km"};
    private final String[] universityArr = {"Any", "ETF", "RAF", "MATF", "PMF", "SINGIDUNUM"};

    private Location location;
    private LocationManager lm;



    public static final String SEARCH_URL = "http://10.0.2.2:8080/api/user/search?";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        initComponents();
        inflateSpinners();
        lm = (LocationManager)getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( permissions, 112);
            }
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
    }




    private void initComponents() {
        age = findViewById(R.id.search_age);
        gender = findViewById(R.id.search_gender);
        university = findViewById(R.id.search_university);
        range = findViewById(R.id.search_range);
        searchNearby = findViewById(R.id.search_nearby);
        logout = findViewById(R.id.auth_logout);
        submit = findViewById(R.id.search_submit);
        submit.setOnClickListener(this);
        logout.setOnClickListener(this);
        initImageFragment = new SearchImageFragment();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.show(initImageFragment);
        transaction.commit();
    }


    private void inflateSpinners() {
        ArrayAdapter<String> ageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ageArr);
        ArrayAdapter<String> rangeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rangeArr);
        ArrayAdapter<String> universityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, universityArr);
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, genderArr);

        age.setAdapter(ageAdapter);
        gender.setAdapter(genderAdapter);
        range.setAdapter(rangeAdapter);
        university.setAdapter(universityAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == submit.getId()) {
            AsyncTask<String, Void, String> execute = new SearchHttpRequest().execute();
            try {
                List<User> users = new ArrayList<>();
                String response = execute.get();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject userJson = jsonArray.getJSONObject(i);
                    User user = new User();
                    user.setId(userJson.getInt("id"));
                    user.setName(userJson.getString("name"));
                    user.setEmail(userJson.getString("email"));
                    user.setLastname(userJson.getString("lastName"));
                    user.setCurrentAddress(userJson.getString("currentStudyBuddyAddress"));
                    user.setUniversity(userJson.getString("university"));
                    user.setImagePath(userJson.getString("imagePath"));
                    user.setLat(userJson.getString("lat"));
                    user.setLng(userJson.getString("lng"));
                    //                    user.setBirthdate(userJson.getString("birthdate"));
                    user.setGender(userJson.getBoolean("gender"));


                    users.add(user);
                }
                ListView listView = findViewById(R.id.user_list);
                UserAdapter userAdapter = new UserAdapter(this,R.layout.response_list_layout,users);
                listView.setAdapter(userAdapter);
                listView.setClickable(true);
                listView.setOnItemClickListener((adapterView, view1, i, l) -> {
                    User itemAtPosition = (User) adapterView.getItemAtPosition(i);
                    System.out.println(itemAtPosition.getName());
                    Intent intent = new Intent(getApplicationContext(), GoogleMapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("studyUser", itemAtPosition);
                    intent.putExtras(bundle);

                    startActivity(intent);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(view.getId() == logout.getId()){
            SharedPreferences myPrefs = getSharedPreferences("myPrefs", 0);
            myPrefs.edit().remove(JwtTokenManager.JWT_TOKEN_KEY).apply();
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.location = location;
    }



    protected class SearchHttpRequest extends AsyncTask<String, Void, String>{

        private StringBuilder sb = new StringBuilder();

        @Override
        protected void onPreExecute() {
            sb.append(SEARCH_URL);

            if (!age.getSelectedItem().toString().equalsIgnoreCase("any")) {
                checkIfFirstParam(sb);
                addAgeParamToUrl();
            }
            if (!gender.getSelectedItem().toString().equalsIgnoreCase("any")) {
                checkIfFirstParam(sb);
                String s = gender.getSelectedItem().toString();
                Boolean g = !s.equalsIgnoreCase("male");
                sb.append("gender=").append(g);
            }
            if (!university.getSelectedItem().toString().equalsIgnoreCase("any")) {
                checkIfFirstParam(sb);
                sb.append("university=").append(university.getSelectedItem().toString());
            }
            if (!range.getSelectedItem().toString().equalsIgnoreCase("any")) {
                checkIfFirstParam(sb);
                String s = range.getSelectedItem().toString();
                String substring = s.substring(0, s.length() - 2);
                sb.append("distance=").append(substring);
            }
            Location currentLocation = getCurrentLocation();
            if (currentLocation != null){
                checkIfFirstParam(sb);
                sb.append("lat=").append(currentLocation.getLatitude()).append("&lng=").append(currentLocation.getLongitude());
            }else if(SearchActivity.this.location != null ){
                sb.append("lat=").append(SearchActivity.this.location.getLatitude()).append("&lng=").append(SearchActivity.this.location.getLongitude());
            }else System.out.println("fuck");

        }

        private void addAgeParamToUrl(){
            String ageSelected = age.getSelectedItem().toString();
            switch (ageSelected){
                case "18-20":
                    sb.append("ageGroup=").append("1");
                    break;
                case "20-22":
                    sb.append("ageGroup=").append("2");
                    break;
                case "22-24":
                    sb.append("ageGroup=").append("3");
                    break;
                case "24-26":
                    sb.append("ageGroup=").append("4");
                    break;
                default:
                    sb.append("ageGroup=").append("0");
                    break;
            }
        }

        private void checkIfFirstParam(StringBuilder sb) {
            if (!sb.substring(sb.toString().length() - 1).equals("&") && !sb.substring(sb.toString().length() - 1).equals("?")) {
                sb.append("&");
            }

        }

        private Location getCurrentLocation() {
            if (ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SearchActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }else{
                return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }



        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(sb.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YXNrb3BvcGFAZ21haWwuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MC9hcGkvdXNlci9sb2dpbiIsImV4cCI6MTY4ODA0NTEwNX0.uZt_ojafn8nbR7-6sV6cdI3r87aValtlpMYj6WHMq7Y");
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                System.out.println("RESPONE CODE: "  + connection.getResponseCode());
                InputStream inputStream = connection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                for (String line; (line = br.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                inputStream.close();
                connection.disconnect();
                return total.toString();


            }catch(Exception e){
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            System.out.println(sb);
            System.out.println(s);
            sb.delete(0,sb.length()-1);
        }


    }


}
