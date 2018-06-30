/*
 * Copyright (C) 2018 Muyangmin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mym.kotlog

import android.util.Log

/**
 * Print log into standard output, i.e. Logcat.
 */
internal class DebugPrinter : Printer {
    companion object {
        //Actual limit is 4k
        private const val LOGCAT_FRAGMENT = 3900
    }

    override fun invoke(request: LogRequest) {
        if (request.msg == null) {
            return
        }

        val totalLength = request.msg.length
        for (x in 0 until totalLength step LOGCAT_FRAGMENT) {
            val endIndex = if ((x + LOGCAT_FRAGMENT) >= totalLength) {
                totalLength
            } else {
                x + LOGCAT_FRAGMENT
            }
            invokeNative(request.level, request.tag
                    ?: "Null Tag", request.msg.substring(x until endIndex))
        }

    }

    private fun invokeNative(level: Int?, tag: String, msg: String) {
        when (level) {
            Log.VERBOSE -> Log.v(tag, msg)
            Log.DEBUG -> Log.d(tag, msg)
            Log.INFO -> Log.i(tag, msg)
            Log.WARN -> Log.w(tag, msg)
            Log.ERROR -> Log.e(tag, msg)
        }
    }
}
