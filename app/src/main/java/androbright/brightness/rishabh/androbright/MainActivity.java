package androbright.brightness.rishabh.androbright;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidsx.rateme.RateMeDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import androbright.brightness.rishabh.androbright.customView.SeekCircle;
import androbright.brightness.rishabh.androbright.databinding.ActivityMainBinding;
import androbright.brightness.rishabh.androbright.model.User;

public class MainActivity extends AppCompatActivity {
    private SeekCircle brightnessSeekbar;
    private TextView brightnessTV;
    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean permission = false;
    private float curBrightnessValue = 0;
    private int screen_brightness;
    private final int MY_PERMISSIONS_REQUEST_WRITE_SETTIGS = 123;
    private final int minimum = 2;
    private ActivityMainBinding binding;
    private AlertDialog.Builder builder;
    private AdView mAdView;

    private SharedPreferences prefs;
    private String usernameKey = "username", usernameDefault = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Log.i(TAG, "onCreate: fired");

        startWorking();

    }

    private void startWorking() {
        MobileAds.initialize(this, "ca-app-pub-9816038748687358~6486464425");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        permission = checkforPermission();

        if (!permission) {

            showGetSecurityDialog();

        } else {

            prefs = PreferenceManager.getDefaultSharedPreferences(this);

            String username = prefs.getString(usernameKey, usernameDefault);
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_LONG).show();

            init();

            brightnessSeekbar.setOnSeekCircleChangeListener(new SeekCircle.OnSeekCircleChangeListener() {
                @Override
                public void onProgressChanged(SeekCircle seekCircle, int progress, boolean fromUser) {
                    brightnessTV.setText(progress + "%");

                    if (progress > minimum) {
                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) (progress * 2.55));
                        brightnessTV.setText(progress + "%");
                    } else {
                        seekCircle.setProgress(minimum);
                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, minimum);
                        brightnessTV.setText(minimum + "%");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekCircle seekCircle) {
                }

                @Override
                public void onStopTrackingTouch(SeekCircle seekCircle) {
                }
            });

            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/satisfy.ttf");
            TextView playButton = (TextView) findViewById(R.id.appTitle);
            playButton.setTypeface(tf);

            try {
                curBrightnessValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            } catch (Settings.SettingNotFoundException se) {
                Log.i(TAG, "onCreate: Current Brightness Value: EXCEPTION occurred, " + se.getMessage());
            }

            screen_brightness = (int) Math.round(curBrightnessValue / 2.55);
            brightnessTV.setText(screen_brightness+"%");
            brightnessSeekbar.setProgress(screen_brightness);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: fired ");

        switch (item.getItemId()) {

            case R.id.action_share:

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Hey! check out this awesome app: " + getString(R.string.app_name));
                    String sAux = "\nI use this app and its awesome. You gotta try it: \n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }


                break;
            case R.id.menu_settings:

                startActivity(new Intent(this, SettingsActivity.class));

                break;
            case R.id.about:

                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.rate:

                showRateMeDialog();
                break;

            case R.id.exit:
                finish();


        }

        return true;
    }

    private void init() {
        Log.i(TAG, "Initializaing the views");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myToolbar.setLogo(getDrawable(R.mipmap.ic_launcher));
        }
        setSupportActionBar(myToolbar);

        brightnessSeekbar = binding.seekCircle;
        brightnessTV = binding.brightnessTextView;

        User user = new User("Sarah", "Gibbons");
        binding.setUser(user);

    }

    private void showRateMeDialog() {
        Log.i(TAG, "showRateMeDialog: fired ");
        new RateMeDialog.Builder(getPackageName(), getString(R.string.app_name))
                .setHeaderBackgroundColor(getResources().getColor(R.color.dialog_primary))
                .setBodyBackgroundColor(getResources().getColor(R.color.dialog_primary_light))
                .setBodyTextColor(getResources().getColor(R.color.dialog_text_foreground))
                .showAppIcon(R.mipmap.ic_launcher)
                .setShowShareButton(true)
                .setRateButtonBackgroundColor(getResources().getColor(R.color.dialog_primary))
                .setRateButtonPressedBackgroundColor(getResources().getColor(R.color.dialog_primary_dark))
                .build()
                .show(getFragmentManager(), "custom-dialog");
    }

    private boolean checkforPermission() {

        Log.i(TAG, "checkforPermission: fired");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(this);
        } else {
            permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }


        return permission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: fired");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_SETTIGS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "That's all things required for the app.", Toast.LENGTH_SHORT).show();

                } else {
                    Log.i(TAG, "onRequestPermissionsResult: permission not granted");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request
        }

    }


    public void startService(View view) {
        startService(new Intent(this, BrightnessService.class));
    }

    // Stop the service
    public void stopService(View view) {
        stopService(new Intent(this, BrightnessService.class));
    }


    public void showGetSecurityDialog() {
        Log.i(TAG, "showGetSecurityDialog: ditch");
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setView(R.layout.dialog_security);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.go_to_setting), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                Log.i(TAG, "checkforPermission: setting intent for permission request");
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MY_PERMISSIONS_REQUEST_WRITE_SETTIGS);
                builder=null;
            }
        });

        builder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Application need permission to set System Brightness. Run the app again to grant permission.", Toast.LENGTH_LONG).show();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
        });

        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: fired ");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                // Do stuff here

                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        }

        startWorking();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
