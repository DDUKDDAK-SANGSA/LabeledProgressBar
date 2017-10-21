package leo.me.la.labeledprogressbarlib;

import android.content.res.Resources;

/**
 * Created by conme on 10/20/2017.
 */

public class Utils {
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
