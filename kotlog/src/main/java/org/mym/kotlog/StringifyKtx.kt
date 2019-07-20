package org.mym.kotlog

import android.util.Log

/**
 * Generate log string for this throwable.
 */
fun Throwable?.toLogString(): String = Log.getStackTraceString(this).orEmpty()