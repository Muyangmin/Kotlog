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
import java.io.Closeable
import java.io.Flushable

@Suppress("unused", "MemberVisibilityCanBePrivate")
/**
 * Print logs as you wish!
 *
 * If you met problems please concat me via [GitHub](https://github.com/Muyangmin/Kotlog).
 */
object L {
    internal lateinit var logEngine: LogEngine

    /**
     * Install log engine to setup global settings. It is strongly recommended to call this method in Application.onCreate().
     *
     * If you call any api without calling this method before, you will receive an [IllegalStateException].
     *
     * @param[autoTag] Whether to use auto tag, default is `true`.
     * @param[needLineNumber] Whether to print line number, default is `true`, and it is recommended.
     * @param[needThreadInfo] Whether to print thread info, default is `true`.
     * @param[enableDefaultPrinter] Whether to enable default(logcat) printer, default is `true`.
     */
    fun install(autoTag: Boolean = true, needLineNumber: Boolean = true,
                needThreadInfo: Boolean = true, enableDefaultPrinter: Boolean = true) {
        logEngine = LogEngine()

        if (autoTag) {
            addDecorator(AutoTagDecorator())
        }

        if (needLineNumber) {
            addDecorator(LineNumberDecorator())
        }

        if (needThreadInfo) {
            addDecorator(ThreadInfoDecorator())
        }
        if (enableDefaultPrinter) {
            addPrinter(DebugPrinter())
        }
    }

    /**
     * Add application Interceptors into log engine.
     * @see Interceptor
     */
    fun addApplicationInterceptor(vararg interceptor: Interceptor) = executeIfEngineInstalled {
        interceptor.forEach {
            logEngine.appInterceptors.add(it)
        }
    }

    /**
     * Add log Interceptors into log engine.
     *
     * @see Interceptor
     */
    fun addLogInterceptor(vararg interceptor: Interceptor) = executeIfEngineInstalled {
        interceptor.forEach {
            logEngine.logInterceptors.add(it)
        }
    }

    /**
     * Add decorators into log engine.
     * @see Decorator
     */
    fun addDecorator(vararg decorator: Decorator) = executeIfEngineInstalled {
        decorator.forEach {
            logEngine.decorators.add(it)
        }
    }

    /**
     * Add printer implementations into log engine.
     */
    fun addPrinter(vararg printer: Printer) = executeIfEngineInstalled {
        printer.forEach {
            logEngine.printers.add(it)
        }
    }

    private fun executeIfEngineInstalled(action: () -> Unit) {
        if (!this::logEngine.isInitialized) {
            throw IllegalStateException("You must install log engine before doing this.")
        }
        action.invoke()
    }

    /**
     * Print multi objects one time, using [Log.DEBUG] level.
     */
    @JvmStatic
    fun objects(vararg params: Any) = print(msg = params.foldIndexed("",
            { index, str, param -> "$str\nparam[$index]=$param" }))

    /**
     * Print log with level [Log.VERBOSE].
     */
    @JvmStatic
    @JvmOverloads
    fun v(msg: String?, tag: String? = null, group: String? = null, stackOffset: Int = 0) =
            print(tag = tag, msg = msg, level = Log.VERBOSE, group = group, stackOffset = stackOffset)


    /**
     * Print log with level [Log.DEBUG].
     */
    @JvmStatic
    @JvmOverloads
    fun d(msg: String?, tag: String? = null, group: String? = null, stackOffset: Int = 0) =
            print(tag = tag, msg = msg, level = Log.DEBUG, group = group, stackOffset = stackOffset)


    /**
     * Print log with level [Log.INFO].
     */
    @JvmStatic
    @JvmOverloads
    fun i(msg: String?, tag: String? = null, group: String? = null, stackOffset: Int = 0) =
            print(tag = tag, msg = msg, level = Log.INFO, group = group, stackOffset = stackOffset)


    /**
     * Print log with level [Log.WARN].
     */
    @JvmStatic
    @JvmOverloads
    fun w(msg: String?, tag: String? = null, group: String? = null, stackOffset: Int = 0) =
            print(tag = tag, msg = msg, level = Log.WARN, group = group, stackOffset = stackOffset)


    /**
     * Print log with level [Log.ERROR].
     */
    @JvmStatic
    @JvmOverloads
    fun e(msg: String?, tag: String? = null, group: String? = null, stackOffset: Int = 0) =
            print(tag = tag, msg = msg, level = Log.ERROR, group = group, stackOffset = stackOffset)

    private fun print(tag: String? = null, msg: String?, level: Int = Log.DEBUG, group: String? = null, stackOffset: Int = 0) {
        executeIfEngineInstalled {
            val request = LogRequest(tag, msg, level, group, stackOffset)
            logEngine.proceed(request)
        }
    }

    /**
     * Flush printers if they are [Flushable].
     *
     * NOTE: This method is only for convenience; it is not mandatory to be flushable.
     */
    @JvmStatic
    fun flushPrinters() {
        executeIfEngineInstalled {
            logEngine.printers.forEach { (it as? Flushable)?.flush() }
        }
    }

    /**
     * Close printers if they are [Closeable].
     *
     * NOTE: This method is only for convenience; it is not mandatory to be flushable.
     */
    @JvmStatic
    fun closePrinters() {
        executeIfEngineInstalled {
            logEngine.printers.forEach { (it as? Closeable)?.close() }
        }
    }
}