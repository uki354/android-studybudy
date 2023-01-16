package com.example.studdybuddy;

import android.app.Activity;
import android.os.Build;
import java.time.Instant;
import java.util.Date;
import java.util.regex.Pattern;

public class SyntaxValidationFilter extends AuthChain{

    private static final String emailRegex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    @Override
    public boolean doFilter(User user, Activity activity) {
        boolean check = true;

        if (user.getEmail() != null){
            Pattern pattern = Pattern.compile(emailRegex);
            check = pattern.matcher(user.getEmail()).matches();
        }
        if(user.getPassword() != null){
            check = user.getPassword().length() > 8;
        }
        if (user.getName() != null){
            check = user.getName().length() > 3;
        }
        if(user.getLastname() != null){
            check = user.getLastname().length() > 3;
        }

        checkNext(user, activity);
        return check;

    }
}
