package com.example.studdybuddy;

import static com.example.studdybuddy.AuthActivity.USER_INTENT_KEY;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class JwtFilterValidation extends AuthChain{

    @Override
    public boolean doFilter(User user, Activity activity) {
        if (user.getJwt() == null){
            String token = JwtTokenManager.getTokenFromDevice(activity);
            if (token.length() > 1 ){
                user.setJwt(token);
                Bundle bundle = new Bundle();
                bundle.putString(USER_INTENT_KEY, user.getJwt());
                Intent intent = new Intent(activity,MainActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        }
        return false;
    }



}
