package org.mym.kotlog.sample;

import org.mym.kotlog.L;

/**
 * This class shows how java class call Kotlog apis.
 */
public class JavaUsage {

    public static void sample() {
        L.d("Kotlog is compatible with Java usage", null, "JavaGroup");
        L.w("This log uses explicitly tag", "ExplicitTag");

    }
}
