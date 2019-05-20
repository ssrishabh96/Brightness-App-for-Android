package androbright.brightness.rishabh.androbright.Utililty;

import android.content.Context;

public class Utils {
    public static float convertPixelsToDp(float px, Context context) {
        return px / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }
}
