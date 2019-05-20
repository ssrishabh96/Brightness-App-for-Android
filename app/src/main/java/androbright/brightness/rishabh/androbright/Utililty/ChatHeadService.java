package androbright.brightness.rishabh.androbright.Utililty;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import androbright.brightness.rishabh.androbright.MainActivity;
import androbright.brightness.rishabh.androbright.R;
import androbright.brightness.rishabh.androbright.customView.DummyView;

public class ChatHeadService extends Service {
    int NOTIFICATION_ID = 119238;
    int curAlpha = 70;
    boolean isOn = false;
    IBinder mBinder = new LocalBinder();
    DummyView t;
    private WindowManager windowManager;

    public class LocalBinder extends Binder {
        public ChatHeadService getServerInstance() {
            return ChatHeadService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 1;
    }

    public void onCreate() {
        super.onCreate();
        if (VERSION.SDK_INT < 23 || Settings.canDrawOverlays(this)) {
            initInstances();
        }
    }

    private void initInstances() {
        this.windowManager = (WindowManager) getSystemService("window");
        LayoutParams params = new LayoutParams(-1, -1, 2006, 768, -3);
        params.gravity = 51;
        if (VERSION.SDK_INT >= 17) {
            DisplayMetrics metrics = new DisplayMetrics();
            this.windowManager.getDefaultDisplay().getRealMetrics(metrics);
            int max = metrics.heightPixels;
            if (max < metrics.widthPixels) {
                max = metrics.widthPixels;
            }
            params.height = max;
            params.width = max;
        }
        this.t = new DummyView(this);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.curAlpha = sharedPrefs.getInt("b", 70);
        this.isOn = sharedPrefs.getBoolean("isOn", false);
        setBrightness(this.curAlpha);
        this.windowManager.addView(this.t, params);
        int idNotiIcon = getResources().getIdentifier("ic_stat_image_brightness_7", "drawable", getPackageName());
        if (idNotiIcon != 0) {
            Builder builder = new Builder(this).setOngoing(true).setSmallIcon(idNotiIcon).setContentTitle(getString(R.string.noti_title)).setContentText(getString(R.string.noti_detail));
            builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0));
            startForeground(this.NOTIFICATION_ID, builder.build());
        }
    }

    public void setBrightness(int val) {
        if (this.t != null) {
            this.curAlpha = val;
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("b", this.curAlpha).apply();
            val = 255 - ((val * 255) / 100);
            if (val > 240) {
                val = 240;
            }
            this.t.setAlpha(val);
            this.t.invalidate();
        }
    }

    public int getBrightness() {
        return this.curAlpha;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.t != null) {
            this.windowManager.removeView(this.t);
        }
        ((NotificationManager) getSystemService("notification")).cancel(this.NOTIFICATION_ID);
    }
}
