package androbright.brightness.rishabh.androbright.Utililty;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

public class MyReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean("isOn", true)) {
            context.startService(new Intent(context, ChatHeadService.class));
        }
    }
}
