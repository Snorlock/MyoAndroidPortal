package snorreedwin.no.myofirebase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import snorreedwin.no.myofirebase.models.Account;
import snorreedwin.no.myofirebase.models.Firebase;

public class ListFirebaseAppsActivity extends Activity {

    private Context context;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.list_firbase_apps_activity);
        ListView listView = (ListView)findViewById(R.id.mylist);
        context = this;

        final SharedPreferences prefs = getSharedPreferences("appname", MODE_PRIVATE);
        prefs.getString("appname", "no app selected");
        final int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, "Selected app is: "+prefs.getString("appname", "no app selected"), duration);
        toast.show();

        Bundle data = getIntent().getExtras();
        Gson gson = new Gson();
        Account accountData = gson.fromJson(data.getString("result"), Account.class);
        ArrayList<String> firebases = new ArrayList<>();

        for (Map.Entry<String, Firebase> entry : accountData.getFirebases().entrySet())
        {
            firebases.add(entry.getValue().getFirebaseName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, firebases);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view).getText().toString();
                prefs.edit().putString("appname", name).commit();
                Toast toast = Toast.makeText(context, "Selected app is: "+name, duration);
                toast.show();
            }
        });

        listView.setAdapter(adapter);
    }


}
