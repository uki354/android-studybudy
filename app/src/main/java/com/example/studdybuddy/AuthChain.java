package com.example.studdybuddy;

import android.app.Activity;

public abstract class AuthChain {

    private AuthChain next;

    public static AuthChain link(AuthChain first, AuthChain... chain) {
        AuthChain head = first;
        for (AuthChain next : chain) {
            head.next = next;
            head = next;
        }
        return first;
    }

    public  AuthChain add(AuthChain x){
        AuthChain last = next;
        while (last != null){
            last = last.next;
        }
        last = x;
        return last;
    }

    public abstract boolean doFilter(User user, Activity activity);

    protected boolean checkNext(User user, Activity activity){
        if(next == null){
            return true;
        }
        return next.doFilter(user, activity);
    }
}
