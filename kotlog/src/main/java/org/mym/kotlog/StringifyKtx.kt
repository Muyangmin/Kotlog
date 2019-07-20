package org.mym.kotlog

import android.util.Log

/**
 * Generate log string for this throwable.
 */
fun Throwable?.toLogString(): String = Log.getStackTraceString(this).orEmpty()

/**
 * Wrap string with two dash lines, maybe useful to emphasize some key logs, e.g. exceptions.
 */
fun String?.emphasizeWithDashLine(): String =
    "-------------------- BEGIN ---------------------\n$this------------------ END -----------------------"