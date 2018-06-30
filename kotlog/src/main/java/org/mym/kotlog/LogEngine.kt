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

/**
 * Internal implementation to proceed log chains.
 */
class LogEngine {

    internal val appInterceptors = mutableListOf<Interceptor>()
    internal val logInterceptors = mutableListOf<Interceptor>()
    internal val decorators = mutableListOf<Decorator>()
    internal val printers = mutableListOf<Printer>()

    //Temporarily save thread info for multi interceptor and decorator to use it.
    var threadInfo: Thread? = null
        get() {
            field = field ?: resolveThreadInfo()
            return field
        }


    var traceInfo: StackTraceElement? = null
        get() {
            field = field ?: resolveTraceInfo()
            return field
        }

    /**
     * Core data flow method.
     */
    internal fun proceed(request: LogRequest) {
        //Use a run block to extract common actions before or after core flow, e.g. clean up.
        run coreFlow@ {
            if (appInterceptors.any {
                        it.invoke(request)
                    }) {
                return@coreFlow
            }

            //chained decoration
            val finalRequest = decorators.fold(request, { thisRequest, decorator ->
                decorator.invoke(thisRequest)
            })

            if (logInterceptors.any { it.invoke(finalRequest) }) {
                return@coreFlow
            }

            printers.forEach {
                it.invoke(finalRequest)
            }
        }

        clean()
    }

    /**
     * clean cached fields.
     */
    private fun clean() {
        threadInfo = null
        traceInfo = null
    }

    private fun resolveTraceInfo(): StackTraceElement? {
        val stack = threadInfo?.stackTrace ?: return null

        var hasEnterLibrary = false
        stack.forEach { element ->
            if (isClassInKotlogPackage(element.className)) {
                if (!hasEnterLibrary) {
                    hasEnterLibrary = true
                }
                return@forEach
            }
            //Leave library, and we found it!
            else if (hasEnterLibrary) {
                return element
            }
        }
        return stack[stack.lastIndex]
    }

    private fun isClassInKotlogPackage(className: String?): Boolean {
        return (className?.substringBeforeLast(".") == L::class.qualifiedName?.substringBeforeLast("."))
    }

    private fun resolveThreadInfo(): Thread = Thread.currentThread()
}