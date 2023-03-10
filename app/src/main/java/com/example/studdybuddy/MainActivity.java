package com.example.studdybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.studdybuddy.AuthActivity.AuthActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentChangeListener{

    private final String[] landingPageText= {"Connect with other students to study together.",
            "Collaborate and share knowledge to excel in your classes.",
            "Register and search for a buddy"};
    private int count;
    private Button button;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfAppIsUsedBefore();
        setContentView(R.layout.activity_main);
        initComponents();
    }

    @Override
    public void onClick(View view) {
        count++;
        change(fragment);
    }

    @Override
    public Fragment change(Fragment fragment) {
        if (fragment instanceof Fragment1) {
            if (count <= landingPageText.length - 1)
                ((Fragment1) fragment).getTextView().setText(landingPageText[count]);
            else if(count == landingPageText.length - 1){
                ((Fragment1) fragment).getTextView().setText(landingPageText[count]);
                button.setText("START");
            }else{
                Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                startActivity(intent);
            }
        }
        return fragment;
    }

    private void initComponents(){
        count = 0;
        button = findViewById(R.id.switch_fragments_button);
        button.setOnClickListener(this);
        fragment = new Fragment1();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container,fragment);
        transaction.commit();
    }

    private void checkIfAppIsUsedBefore(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        String firstTime = myPrefs.getString("firstTime", "");
        if (!firstTime.equals("")) {
            Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
            startActivity(intent);
        }else{
            myPrefs.edit().putString("firstTime", "firstTime").apply();
        }
    }
}