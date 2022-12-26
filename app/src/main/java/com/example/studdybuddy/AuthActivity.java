package com.example.studdybuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener {

    private User user;
    private Fragment loginFragment;
    private Fragment signUpFragment;
    private Fragment active;
    private AuthChain filterChain;

    public static final String USER_INTENT_KEY = "USER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        filterChain = AuthChain.link(new JwtFilterValidation());
        filterChain.doFilter(user,this);

        loginFragment = getSupportFragmentManager().findFragmentById(R.id.loginFragment);
        signUpFragment = getSupportFragmentManager().findFragmentById(R.id.signUpFragment);
        active = loginFragment;
    }


    @Override
    public void onClick(View view) {
        filterChain.add(new SyntaxValidationFilter());
        if (active instanceof LoginFragment){

        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}