package au.com.wsit.project11;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by guyb on 4/01/17.
 */

public class PinsApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("qQwleETMgMqMgj1KcykEfrov2FNCaSZX8pft6KOl")
                .clientKey("RSG132Ug5u5o7AJlnpsnDVNPpuXyc5b4NXpCS1Yn")
                .server("https://parseapi.back4app.com")
                .build());


    }
}
