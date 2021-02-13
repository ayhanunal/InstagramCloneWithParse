package com.ayhanunal.instagramparse;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG); //logcat de yapılacak uyarıları belirledik.

        //parse initialize/baslatma
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("v0UzMh5KM4fFwmi0NrLNINikMGzJd1H2yClWKtZn")
                .clientKey("tOKXfcoPvc6tF2v39QQsgZFFv8NXbIzDL7GBOVfp")
                .server("https://parseapi.back4app.com/")
                .build()
        );


    }
}
