package fairy.easy.loginfo;

import android.app.Application;

import fairy.easy.logcanary.LogCanary;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogCanary.install(this);


    }
}