package com.example.studdybuddy;

import androidx.annotation.Nullable;
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
    public static final String LOGIN_URL = "http://10.0.2.2:8080/api/user/login";
    public static final String SIGNUP_URL = "http://10.0.2.2:8080/api/user/create";


    private Button loginBtnTab;
    private Button signUpBtnTab;
    private Button submit;
    private Button signUp;




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

        loginBtnTab = findViewById(R.id.signup);
        signUpBtnTab = findViewById(R.id.login);

        loginBtnTab.setOnClickListener(this);
        signUpBtnTab.setOnClickListener(this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        submit =  ((LoginFragment) loginFragment).getSubmitBtn();
        submit.setOnClickListener(this);
        signUp =  ((SignUpFragment) signUpFragment).getSignUpBtn();
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() ==  R.id.login || view.getId() == R.id.signup) {
            alterLoginSignUpColors(view);
            return;
        }
        filterChain.add(new SyntaxValidationFilter());
        if(view.getId() == submit.getId()){
            user.setEmail(((LoginFragment)loginFragment).getEmail().getText().toString());
            user.setPassword(((LoginFragment)loginFragment).getPassword().getText().toString());
            filterChain.add(new LoginAttemptFilter());
        }
        if(view.getId() == signUp.getId()){
            user.setName(((SignUpFragment) signUpFragment).getName().getText().toString());
            user.setLastname(((SignUpFragment) signUpFragment).getLastName().getText().toString());
            user.setUniversity(((SignUpFragment) signUpFragment).getUniversity().toString());
            user.setGender(Boolean.parseBoolean(((SignUpFragment) signUpFragment).getGender().toString()));
            user.setEmail(((SignUpFragment) signUpFragment).getEmail().getText().toString());
            user.setPassword(((SignUpFragment) signUpFragment).getPassword().getText().toString());
            filterChain.add(new SignUpAttemptFilter());
        }

        filterChain.doFilter(user,this);

    }


    private void alterLoginSignUpColors(View view){
        if(active instanceof SignUpFragment && view.getId() == R.id.login){
            loginBtnTab.setBackgroundColor(getResources().getColor(R.color.white));
            loginBtnTab.setTextColor(getResources().getColor(R.color.black));
            signUpBtnTab.setBackgroundColor(getResources().getColor(R.color.black));
            signUpBtnTab.setTextColor(getResources().getColor(R.color.white));
            changeActiveFragment();

            active = loginFragment;
            notActive = signUpFragment;
        }else if(active instanceof LoginFragment && view.getId() == R.id.signup){
            loginBtnTab.setBackgroundColor(getResources().getColor(R.color.black));
            loginBtnTab.setTextColor(getResources().getColor(R.color.white));
            signUpBtnTab.setBackgroundColor(getResources().getColor(R.color.white));
            signUpBtnTab.setTextColor(getResources().getColor(R.color.black));
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

    public void setLoginSubmit(Button submit){
        this.submit = submit;
    }





    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}