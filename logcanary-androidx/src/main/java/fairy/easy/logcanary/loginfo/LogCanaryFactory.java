package fairy.easy.logcanary.loginfo;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
import static android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
import static android.content.pm.PackageManager.DONT_KILL_APP;

public class LogCanaryFactory {

    private static class SingleThreadFactory implements ThreadFactory {

        private final String threadName;

        SingleThreadFactory(String threadName) {
            this.threadName = "LogCanary-" + threadName;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, threadName);
        }
    }

    private static final Executor fileIoExecutor = newSingleThreadExecutor("File-IO");

    private static void setEnabledLogging(Context appContext,
                                          Class<?> componentClass,
                                          boolean enabled) {
        ComponentName component = new ComponentName(appContext, componentClass);
        PackageManager packageManager = appContext.getPackageManager();
        int newState = enabled ? COMPONENT_ENABLED_STATE_ENABLED : COMPONENT_ENABLED_STATE_DISABLED;
        // Logs on IPC.
        packageManager.setComponentEnabledSetting(component, newState, DONT_KILL_APP);
    }

    private static void executeOnFileIoThread(Runnable runnable) {
        fileIoExecutor.execute(runnable);
    }

    private static Executor newSingleThreadExecutor(String threadName) {
        return Executors.newSingleThreadExecutor(new SingleThreadFactory(threadName));
    }

    public static void setEnabled(Context context,
                                  final Class<?> componentClass,
                                  final boolean enabled) {
        final Context appContext = context.getApplicationContext();
        executeOnFileIoThread(new Runnable() {
            @Override
            public void run() {
                setEnabledLogging(appContext, componentClass, enabled);
            }
        });
    }
}
