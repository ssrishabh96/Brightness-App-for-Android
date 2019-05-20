package androbright.brightness.rishabh.androbright;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutActivity extends AppCompatActivity {

    private View aboutPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StringBuilder contents=new StringBuilder(getResources().getString(R.string.app_name));

        contents.append(" is a utility application for Android devices. The UI is minimal and very flexible application. With visually appealing interface, we are constantly working hard for seamless user experience.");

        aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription(contents.toString())
                .setImage(R.mipmap.background)
                .addItem(new Element().setTitle("Version 1.1"))
                .addGroup("Connect with us")
                .addEmail("rishabh0148@gmail.com")
                .addWebsite("http://googlelle.com")
                .addTwitter("ssrishabh96")
                .addYoutube("UCGCq6dmpP3sqRhfg8Dk_5XQ")
                .addPlayStore(getApplication().getPackageName())
                .addGitHub("ssrishabh96")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);



    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.mipmap.ic_launcher);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutActivity.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
}
