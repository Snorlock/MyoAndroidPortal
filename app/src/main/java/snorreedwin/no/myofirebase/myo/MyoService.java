package snorreedwin.no.myofirebase.myo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.squareup.otto.Bus;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import snorreedwin.no.myofirebase.App;
import snorreedwin.no.myofirebase.events.StoppingMyoService;

public class MyoService extends Service {
    private static boolean isRunning = false;
    private Context context;
    private Bus bus;
    private MyoListener listener;
    private Firebase myFirebaseRef;
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        bus = App.getBus();
        listener = new MyoListener();
        prefs = getSharedPreferences("appname", MODE_PRIVATE);
        myFirebaseRef = new Firebase("https://"+ prefs.getString("appname", "null")+".firebaseio.com/");
    }

    public static boolean isServiceRunning() {
        return isRunning;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!isRunning) {
            Hub hub = Hub.getInstance();
            bus.register(this);
            if (!hub.init(this)) {
                Log.e("myoservice", "Could not initialize the Hub.");
                bus.post(new StoppingMyoService());
                this.stopSelf();
                return 0;
            }
            Hub.getInstance().setSendUsageData(false);
            Hub.getInstance().setLockingPolicy(Hub.LockingPolicy.STANDARD);
            Hub.getInstance().addListener(listener);


            Intent scanActivity = new Intent(context, ScanActivity.class);
            scanActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(scanActivity);
            isRunning = true;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        isRunning = false;
        bus.unregister(this);
        Hub.getInstance().removeListener(listener);
    }

    public void toastMessage(String message) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);

        toast.show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public class MyoListener implements DeviceListener {

        @Override
        public void onAttach(Myo myo, long l) {
            toastMessage("Attached myo");
        }

        @Override
        public void onDetach(Myo myo, long l) {

        }

        @Override
        public void onConnect(Myo myo, long l) {
            toastMessage("Connected myo");
        }

        @Override
        public void onDisconnect(Myo myo, long l) {

        }

        @Override
        public void onArmSync(Myo myo, long l, Arm arm, XDirection xDirection) {

        }

        @Override
        public void onArmUnsync(Myo myo, long l) {

        }

        @Override
        public void onUnlock(Myo myo, long l) {

        }

        @Override
        public void onLock(Myo myo, long l) {

        }

        @Override
        public void onPose(Myo myo, long l, Pose pose) {
            toastMessage("Recieved pose: "+pose.name());
            myFirebaseRef.child("pose").setValue(pose.name());
        }

        @Override
        public void onOrientationData(Myo myo, long l, Quaternion quaternion) {

        }

        @Override
        public void onAccelerometerData(Myo myo, long l, Vector3 vector3) {

        }

        @Override
        public void onGyroscopeData(Myo myo, long l, Vector3 vector3) {

        }

        @Override
        public void onRssi(Myo myo, long l, int i) {

        }
    }
}
