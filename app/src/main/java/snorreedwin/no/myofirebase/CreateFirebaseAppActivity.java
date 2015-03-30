package snorreedwin.no.myofirebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import snorreedwin.no.myofirebase.models.Account;
import snorreedwin.no.myofirebase.models.FirebaseCreateResponse;
import snorreedwin.no.myofirebase.rest.FirebaseAdmin;
import snorreedwin.no.myofirebase.rest.FirebaseAdminAPI;

public class CreateFirebaseAppActivity extends Activity {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.create_firebase_app_activity);
        context = this;
    }

    public void createApp(View v) {
        EditText appName = (EditText) findViewById(R.id.appName);
        String name = appName.getText().toString();
        createFirebaseApp(name);

    }

    private boolean createFirebaseApp(final String name) {
        FirebaseAdmin admin = new FirebaseAdmin();
        FirebaseAdminAPI service = admin.getService();
        String token = App.getObscurifier().getUncryptFirebaseAdminToken();
        token = token.split("=")[1];
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, "posting to firebase with token "+token, duration);

        toast.show();

        if(token != null) {
            service.createFirebase(name, token, name, new Callback<FirebaseCreateResponse>() {

                @Override
                public void success(FirebaseCreateResponse res, Response response) {
                    if (response.getReason().equals("OK") && res.getSuccess()) {
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, "Firebase created with name " + name, duration);
                        toast.show();
                        finish();
                    }
                    else {
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, "Firebase created failes with error: " + res.getError(), duration);
                        toast.show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "Error when creating " + name + ": " + error.getResponse(), duration);
                    toast.show();
                }
            });
        }
        return false;
    }


}
