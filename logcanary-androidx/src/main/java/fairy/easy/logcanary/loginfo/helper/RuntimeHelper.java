package fairy.easy.logcanary.loginfo.helper;


import java.io.IOException;
import java.util.List;

import fairy.easy.logcanary.loginfo.util.ArrayUtil;

public class RuntimeHelper {


    public static Process exec(List<String> args) throws IOException {
        return Runtime.getRuntime().exec(ArrayUtil.toArray(args, String.class));
    }

    public static void destroy(Process process) {
        // if we're in JellyBean, then we need to kill the process as root, which requires all this
        // extra UnixProcess logic
        process.destroy();
    }

}