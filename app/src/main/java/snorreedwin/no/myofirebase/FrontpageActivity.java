package snorreedwin.no.myofirebase;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import snorreedwin.no.myofirebase.events.StoppingMyoService;
import snorreedwin.no.myofirebase.models.Account;
import snorreedwin.no.myofirebase.myo.MyoService;
import snorreedwin.no.myofirebase.rest.FirebaseAdmin;
import snorreedwin.no.myofirebase.rest.FirebaseAdminAPI;
import snorreedwin.no.myofirebase.utils.Obscurifier;

public class FrontpageActivity extends Activity {

    private final int FirebaseWebview = 1;
    public Context context;
    private Bus bus;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.frontpage_activity);
        recordingButtonVisibility(MyoService.isServiceRunning());
        bus = App.getBus();
        bus.register(this);

        context = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Subscribe
    public void stoppingMyoServiceEvent(StoppingMyoService obj) {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, "failed to start myo service", duration);

        toast.show();
        recordingButtonVisibility(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FirebaseWebview) {

        }
    }

    public void startFirebaseConnection(View v) {
        Intent intent = new Intent(this, FirebaseLoginWebViewActivity.class);
        startActivityForResult(intent, FirebaseWebview);
    }

    public void clearCookieAndToken(View v) {
        CookieManager.getInstance().removeAllCookie();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().clear().apply();
    }

    public void getToken(View v) {
        Obscurifier obs = App.getObscurifier();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, obs.getUncryptFirebaseAdminToken(), duration);

        toast.show();

    }


    public void getApps(View v) {
        FirebaseAdmin admin = new FirebaseAdmin();
        FirebaseAdminAPI service = admin.getService();
        String token = App.getObscurifier().getUncryptFirebaseAdminToken();

        if(token != null) {
            token = token.split("=")[1];
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "getting value from api with "+token, duration);
            toast.show();
            service.fetchUser(token, new Callback<Account>(){

                @Override
                public void success(Account account, Response response) {
                    Intent intent = new Intent(context, ListFirebaseAppsActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra("result", gson.toJson(account));
                    startActivity(intent);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("lol", error.toString());
                }
            });
        }
        else {
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, "Please connect to firebase", duration);
            toast.show();
        }
    }

    public void createApp(View v) {
        Intent intent = new Intent(context, CreateFirebaseAppActivity.class);
        startActivity(intent);
    }

    public void startRecording(View v) {
        Intent intent = new Intent(context, MyoService.class);
        ComponentName status = startService(intent);

        recordingButtonVisibility(true);
    }

    public void stopRecording(View v) {
        Intent intent = new Intent(context, MyoService.class);
        stopService(intent);
        recordingButtonVisibility(false);
    }

    public void recordingButtonVisibility(boolean isRecording) {
        if(isRecording) {
            (findViewById(R.id.startRecording)).setVisibility(View.GONE);
            (findViewById(R.id.stopRecording)).setVisibility(View.VISIBLE);
        }
        else {
            (findViewById(R.id.stopRecording)).setVisibility(View.GONE);
            (findViewById(R.id.startRecording)).setVisibility(View.VISIBLE);
        }
    }


}
