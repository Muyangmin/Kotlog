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

import org.mym.kotlog.Decorator.Companion.ORDER_AUTO_TAG
import org.mym.kotlog.Decorator.Companion.ORDER_GLOBAL_TAG
import org.mym.kotlog.Decorator.Companion.ORDER_GROUP_TAG
import org.mym.kotlog.Decorator.Companion.ORDER_LINE_NUMBER
import org.mym.kotlog.Decorator.Companion.ORDER_THREAD_INFO

/**
 * A Decorator can modify any property of a [LogRequest], e.g. tag and msg.
 *
 * Multi decorators are allowed, but you should be aware of the sequence of adding them if they may change same field.
 *
 * @see AutoTagDecorator
 * @see GlobalTagDecorator
 * @see LineNumberDecorator
 * @see ThreadInfoDecorator
 */
interface Decorator {

    companion object {
        const val ORDER_AUTO_TAG = 0
        const val ORDER_GROUP_TAG = 100
        const val ORDER_GLOBAL_TAG = 200
        const val ORDER_LINE_NUMBER = 400
        const val ORDER_THREAD_INFO = 500
    }

    /**
     * Whether the param request should be decorated by this decorator.
     */
    fun shouldDecorate(request: LogRequest): Boolean = true

    /**
     * Decorate request and return new one.
     */
    fun decorate(request: LogRequest): LogRequest

    /**
     * This allows decorators to be `ordered` to process requests. Order is asc, so order(0) is called before order(1).
     */
    val order: Int
}

/**
 * Concat a global tag for each single log.
 */
open class GlobalTagDecorator(
    private val globalTag: String = "GlobalTag",
    private val concat: String = "-",
    override val order: Int = ORDER_GLOBAL_TAG
) : Decorator {

    override fun decorate(request: LogRequest): LogRequest {
        return request.copy(tag = "$globalTag$concat${request.tag}")
    }
}

open class GroupTagDecorator(override val order: Int = ORDER_GROUP_TAG) : Decorator {
    override fun decorate(request: LogRequest): LogRequest {
        return if (request.group.isNullOrEmpty()) {
            request
        } else {
            request.copy(tag = "${request.tag}[${request.group}]")
        }
    }
}

/**
 * Detect and assign a best match log tag for each request.
 */
open class AutoTagDecorator(override val order: Int = ORDER_AUTO_TAG) : Decorator {
    override fun decorate(request: LogRequest): LogRequest {
        val trace = L.logEngine.traceInfo
        return when {
            request.tag != null -> request
            trace != null -> request.copy(tag = findLastClassName(trace.className))
            else -> request
        }
    }

    private fun findLastClassName(fullClassName: String): String {
        val pkgPath = fullClassName.split(".")
        return if (pkgPath.isNotEmpty()) {
            pkgPath[pkgPath.size - 1]
        } else {
            fullClassName
        }
    }
}

/**
 * Detect and insert line number information into the beginning of log content.
 */
open class LineNumberDecorator(override val order: Int = ORDER_LINE_NUMBER) : Decorator {
    override fun decorate(request: LogRequest): LogRequest {
        val trace = L.logEngine.traceInfo
        return if (trace != null) {
            request.copy(
                msg = "[(${trace.fileName
                    ?: "Unknown Source"}:${trace.lineNumber}):${trace.methodName}]${request.msg}"
            )
        } else {
            request
        }
    }
}

/**
 * Detect and insert Thread information into the beginning of log content.
 */
open class ThreadInfoDecorator(override val order: Int = ORDER_THREAD_INFO) : Decorator {
    override fun decorate(request: LogRequest): LogRequest {
        val thread = L.logEngine.threadInfo
        return if (thread != null) {
            request.copy(msg = "$thread${request.msg}")
        } else {
            request
        }
    }
}