package com.example.studdybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.studdybuddy.SearchActivity.SearchActivity;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private EditText email;
    private EditText password;
    private Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_layout, container, false);
        email = view.findViewById(R.id.login_email);
        password = view.findViewById(R.id.login_password);
        submit = view.findViewById(R.id.login_submit);
        submit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == submit.getId()){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }
    }

    public Button getSubmitBtn(){
        return  this.submit;
    }



}
