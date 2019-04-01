package com.example.asus.text2;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this,"d5aba9425f391a42d5feea74e0c46714");
    }
}
