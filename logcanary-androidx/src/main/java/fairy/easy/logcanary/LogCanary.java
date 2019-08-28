package fairy.easy.logcanary;

import android.content.Context;

import fairy.easy.logcanary.loginfo.LogCanaryFactory;


public class LogCanary {

    private LogCanary() {
        throw new AssertionError();
    }

    private static int maxNum;

    public static void install(Context context) {
        install(context, 10000);
    }

    public static void install(Context context, int num) {
        maxNum = num;
        LogCanaryFactory.setEnabled(context, LogInfoActivity.class, true);
    }

    public static int getMaxNum() {
        return maxNum;
    }

}
