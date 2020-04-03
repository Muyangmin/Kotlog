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

/**
 * **Core api class**, using this class to print logs as you wish!
 *
 * If you met problems please concat me via [GitHub](https://github.com/Muyangmin/Kotlog).
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object L {
    internal lateinit var logEngine: LogEngine

    /**
     * Install log engine to setup global settings. It is strongly recommended to call this method in Application.onCreate().
     *
     * If you call any api without calling this method before, you will receive an [IllegalStateException].
     *
     * @param[enableDefaultDecorators] Whether to enable default decorators, default is `true`. Strongly recommend to set `true` to keep consistence for future compatibility.
     * @param[enableDefaultPrinter] Whether to enable default(logcat) printer, default is `true`.
     */
    fun install(enableDefaultDecorators: Boolean = true, enableDefaultPrinter: Boolean = true) {
        logEngine = LogEngine()

        if (enableDefaultDecorators) {
            addDecorator(
                //param order is arbitrary; they will be sorted internally by their `order` property.
                AutoTagDecorator(),
                GroupTagDecorator(),
                LineNumberDecorator(),
                ThreadInfoDecorator()
            )
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
        logEngine.decorators.apply {
            addAll(decorator)
            sortBy { it.order }
        }
    }

    /**
     * Add all decorators into log engine.
     */
    fun addDecorators(decorators: Collection<Decorator>) = executeIfEngineInstalled {
        logEngine.decorators.apply {
            addAll(decorators)
            sortedBy { it.order }
        }
    }

    /**
     * Add printer implementations into log engine.
     *
     * Note: Add printer will **NOT** remove previous printers.
     *
     * @see Printer
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
     * Print multi objects via a single call, using [Log.DEBUG] level.
     */
    fun objects(vararg params: Any?) = d(
        msg = params.foldIndexed("",
            { index, str, param -> "$str\nparam[$index]=$param" })
    )

    /**
     * Print log with level [Log.VERBOSE], [extraOptions] can be used to specify extra options.
     *
     * See [LogRequest] for param details.
     */
    fun v(msg: String?, extraOptions: (LogRequestBuilder.() -> Unit)? = null) {
        val req = LogRequestBuilder(msg = msg, level = Log.VERBOSE)
        extraOptions?.invoke(req)
        printLogRequest(req.build())
    }

    /**
     * Print log with level [Log.DEBUG], [extraOptions] can be used to specify extra options.
     *
     * See [LogRequest] for param details.
     */
    fun d(msg: String?, extraOptions: (LogRequestBuilder.() -> Unit)? = null) {
        val req = LogRequestBuilder(msg = msg, level = Log.DEBUG)
        extraOptions?.invoke(req)
        printLogRequest(req.build())
    }

    /**
     * Print log with level [Log.INFO], [extraOptions] can be used to specify extra options.
     *
     * See [LogRequest] for param details.
     */
    fun i(msg: String?, extraOptions: (LogRequestBuilder.() -> Unit)? = null) {
        val req = LogRequestBuilder(msg = msg, level = Log.INFO)
        extraOptions?.invoke(req)
        printLogRequest(req.build())
    }

    /**
     * Print log with level [Log.WARN], [extraOptions] can be used to specify extra options.
     *
     * See [LogRequest] for param details.
     */
    fun w(msg: String?, extraOptions: (LogRequestBuilder.() -> Unit)? = null) {
        val req = LogRequestBuilder(msg = msg, level = Log.WARN)
        extraOptions?.invoke(req)
        printLogRequest(req.build())
    }

    /**
     * Print log with level [Log.ERROR], [extraOptions] can be used to specify extra options.
     *
     * See [LogRequest] for param details.
     */
    fun e(msg: String?, extraOptions: (LogRequestBuilder.() -> Unit)? = null) {
        val req = LogRequestBuilder(msg = msg, level = Log.ERROR)
        extraOptions?.invoke(req)
        printLogRequest(req.build())
    }

    private fun printLogRequest(request: LogRequest) {
        executeIfEngineInstalled {
            logEngine.proceed(request)
        }
    }

    /**
     * Flush printers if they are [Flushable].
     *
     * NOTE: This method is only for convenience; it is not mandatory to be flushable.
     */
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
    fun closePrinters() {
        executeIfEngineInstalled {
            logEngine.printers.forEach { (it as? Closeable)?.close() }
        }
    }
}