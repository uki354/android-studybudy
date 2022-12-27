package com.example.studdybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private Fragment loginFragment;
    private Fragment signUpFragment;
    private Fragment active;
    private Fragment notActive;
    private AuthChain filterChain;
    private FragmentManager manager;

    public static final String USER_INTENT_KEY = "USER";
    public static final String LOGIN_URL = "";
    public static final String SIGNUP_URL = "";


    private Button loginBtn;
    private Button signUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        user = new User();
        filterChain = AuthChain.link(new JwtFilterValidation());
        filterChain.doFilter(user,this);
        loginFragment = new LoginFragment();
        signUpFragment = new SignUpFragment();
        active = loginFragment;
        notActive = signUpFragment;
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.auth_fragments,loginFragment);
        transaction.add(R.id.auth_fragments,signUpFragment);
        transaction.hide(signUpFragment);
        transaction.show(active);
        transaction.commit();
        loginBtn = findViewById(R.id.signup);
        signUpBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(this);
        signUpBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() ==  R.id.login || view.getId() == R.id.signup) {
            alterLoginSignUpColors(view);
            return;
        }
        filterChain.add(new SyntaxValidationFilter());
        if (active instanceof LoginFragment){
            filterChain.add(new LoginAttemptFilter());
        }else if (active instanceof SignUpFragment){

        }
        filterChain.doFilter(user,this);

    }


    private void alterLoginSignUpColors(View view){
        if(active instanceof SignUpFragment && view.getId() == R.id.login){
            loginBtn.setBackgroundColor(getResources().getColor(R.color.white));
            loginBtn.setTextColor(getResources().getColor(R.color.black));
            signUpBtn.setBackgroundColor(getResources().getColor(R.color.black));
            signUpBtn.setTextColor(getResources().getColor(R.color.white));
            changeActiveFragment();

            active = loginFragment;
            notActive = signUpFragment;
        }else if(active instanceof LoginFragment && view.getId() == R.id.signup){
            loginBtn.setBackgroundColor(getResources().getColor(R.color.black));
            loginBtn.setTextColor(getResources().getColor(R.color.white));
            signUpBtn.setBackgroundColor(getResources().getColor(R.color.white));
            signUpBtn.setTextColor(getResources().getColor(R.color.black));
            changeActiveFragment();
            active = signUpFragment;
            notActive = loginFragment;
        }


    }

    private void changeActiveFragment(){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(active);
        transaction.show(notActive);
        transaction.commit();
    }





    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}