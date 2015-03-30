package snorreedwin.no.myofirebase;

import android.app.Application;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;
import com.squareup.otto.Bus;

import snorreedwin.no.myofirebase.utils.Obscurifier;

public class App extends Application {
    public static Obscurifier obscurifier;
    private static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        obscurifier = new Obscurifier(
                getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
        );
        bus = new Bus();
    }

    public static Obscurifier getObscurifier() {
        return obscurifier;
    }

    public static Bus getBus() {
        return bus;
    }
}
