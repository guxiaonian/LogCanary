package fairy.easy.logcanary;

import android.content.Context;
import android.util.Log;

import java.util.HashMap;

import androidx.core.content.ContextCompat;

/**
 * @author: linjizong
 * @date: 2019/4/30
 * @desc:
 */
public class TagColorUtil {
    private static final HashMap<Integer, Integer> TEXT_COLOR = new HashMap<>(6);
    private static final HashMap<Integer, Integer> TEXT_COLOR_EXPAND = new HashMap<>(6);
    private static final HashMap<Integer, Integer> LEVEL_COLOR = new HashMap<>(6);
    private static final HashMap<Integer, Integer> LEVEL_BG_COLOR = new HashMap<>(6);

    static {
        TEXT_COLOR.put(Log.DEBUG, R.color.log_canary_color_000000);
        TEXT_COLOR.put(Log.INFO, R.color.log_canary_color_000000);
        TEXT_COLOR.put(Log.VERBOSE, R.color.log_canary_color_000000);
        TEXT_COLOR.put(Log.ASSERT, R.color.log_canary_color_8F0005);
        TEXT_COLOR.put(Log.ERROR, R.color.log_canary_color_FF0006);
        TEXT_COLOR.put(Log.WARN, R.color.log_canary_color_0099dd);

        TEXT_COLOR_EXPAND.put(Log.DEBUG, R.color.log_canary_color_FFFFFF);
        TEXT_COLOR_EXPAND.put(Log.INFO, R.color.log_canary_color_FFFFFF);
        TEXT_COLOR_EXPAND.put(Log.VERBOSE, R.color.log_canary_color_FFFFFF);
        TEXT_COLOR_EXPAND.put(Log.ASSERT, R.color.log_canary_color_8F0005);
        TEXT_COLOR_EXPAND.put(Log.ERROR, R.color.log_canary_color_FF0006);
        TEXT_COLOR_EXPAND.put(Log.WARN, R.color.log_canary_color_0099dd);

        LEVEL_BG_COLOR.put(Log.DEBUG, R.color.log_canary_background_debug);
        LEVEL_BG_COLOR.put(Log.ERROR, R.color.log_canary_background_error);
        LEVEL_BG_COLOR.put(Log.INFO, R.color.log_canary_background_info);
        LEVEL_BG_COLOR.put(Log.VERBOSE, R.color.log_canary_background_verbose);
        LEVEL_BG_COLOR.put(Log.WARN, R.color.log_canary_background_warn);
        LEVEL_BG_COLOR.put(Log.ASSERT, R.color.log_canary_background_wtf);

        LEVEL_COLOR.put(Log.DEBUG, R.color.log_canary_foreground_debug);
        LEVEL_COLOR.put(Log.ERROR, R.color.log_canary_foreground_error);
        LEVEL_COLOR.put(Log.INFO, R.color.log_canary_foreground_info);
        LEVEL_COLOR.put(Log.VERBOSE, R.color.log_canary_foreground_verbose);
        LEVEL_COLOR.put(Log.WARN, R.color.log_canary_foreground_warn);
        LEVEL_COLOR.put(Log.ASSERT, R.color.log_canary_foreground_wtf);
    }

    public static int getTextColor(Context context, int level, boolean expand) {
        HashMap<Integer, Integer> map = expand ? TEXT_COLOR_EXPAND : TEXT_COLOR;
        Integer result = map.get(level);
        if (result == null) {
            result = map.get(Log.VERBOSE);
        }
        return ContextCompat.getColor(context, result);
    }

    public static int getLevelBgColor(Context context, int level) {
        Integer result = LEVEL_BG_COLOR.get(level);
        if (result == null) {
            result = LEVEL_BG_COLOR.get(Log.VERBOSE);
        }
        return ContextCompat.getColor(context, result);
    }

    public static int getLevelColor(Context context, int level) {
        Integer result = LEVEL_COLOR.get(level);
        if (result == null) {
            result = LEVEL_COLOR.get(Log.VERBOSE);
        }
        return ContextCompat.getColor(context, result);
    }
}
