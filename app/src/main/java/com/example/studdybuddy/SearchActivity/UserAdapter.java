package com.example.studdybuddy.SearchActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.studdybuddy.R;
import com.example.studdybuddy.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    private List<User> users;
    public static final String imagePath = "https://www.pngfind.com/pngs/m/676-6764065_default-profile-picture-transparent-hd-png-download.png";
    public UserAdapter(Context context, int resource, List<User> users){
        super(context, resource,users);
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.response_list_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.user_image);
        TextView nameView = convertView.findViewById(R.id.user_name);
        TextView locationView = convertView.findViewById(R.id.user_location);
        TextView universityView = convertView.findViewById(R.id.user_university);
        TextView genderView = convertView.findViewById(R.id.user_gender);
        TextView ageView = convertView.findViewById(R.id.user_age);

        Glide.with(getContext()).load(imagePath).into(imageView);


        nameView.setText(user.getName() + " " + user.getLastname());
        locationView.setText(user.getCurrentAddress());
        universityView.setText(user.getUniversity());
        genderView.setText(user.getGender() ? "Male" : "Female");
        ageView.setText(String.valueOf((user.getBirthdate())));
        return convertView;
    }
    }

