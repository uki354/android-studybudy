package com.example.studdybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class SignUpFragment extends Fragment {

    private EditText email;
    private EditText password;
    private EditText name;
    private EditText lastName;
    private Spinner university;
    private DatePicker birthdate;
    private RadioGroup gender;

    public static final String[] universities= {"ETF", "PMF", "FON", "SINGIDUNUM", "RAF"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.signup_layout,container,false);
       email = view.findViewById(R.id.signup_email);
       name = view.findViewById(R.id.signup_name);
       password = view.findViewById(R.id.signup_password);
       lastName= view.findViewById(R.id.signup_lastName);
       university = view.findViewById(R.id.signup_university);
       birthdate = view.findViewById(R.id.date_picker);
       gender = view.findViewById(R.id.radio_group_gender);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,universities);
        university.setAdapter(adapter);
        return view;
    }


}
