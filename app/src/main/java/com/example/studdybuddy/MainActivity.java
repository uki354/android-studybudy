package com.example.studdybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FragmentChangeListener{

    private final String[] landingPageText= {"Studying is more fun with a buddy",
            "Something something something",
            "Register and search for a buddy"};
    private int count;
    private Button button;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            if (count < landingPageText.length - 1)
                ((Fragment1) fragment).getTextView().setText(landingPageText[count]);
            else if(count == landingPageText.length - 1){
                ((Fragment1) fragment).getTextView().setText(landingPageText[count]);
                button.setText("START");
            }else{

            }
        }
        return fragment;
    }

    private void initComponents(){
        count = 0;
        button = findViewById(R.id.switch_fragments_button);
        button.setOnClickListener(this);
        fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_1);
        ((Fragment1)fragment).getTextView().setText(landingPageText[count]);
    }
}