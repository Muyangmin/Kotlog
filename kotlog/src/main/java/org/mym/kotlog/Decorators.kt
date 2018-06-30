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
 * Concat a global tag for each single log.
 */
class GlobalTagDecorator(private val globalTag: String = "GlobalTag",
                         private val concat: String = "-") : Decorator {

    override fun invoke(request: LogRequest): LogRequest {
        return request.copy(tag = "$globalTag$concat${request.tag}")
    }
}

/**
 * Detect and assign a best match log tag for each request.
 */
class AutoTagDecorator : Decorator {
    override fun invoke(request: LogRequest): LogRequest {
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
class LineNumberDecorator : Decorator {
    override fun invoke(request: LogRequest): LogRequest {
        val trace = L.logEngine.traceInfo
        return if (trace != null) {
            request.copy(msg = "[(${trace.fileName
                    ?: "Unknown Source"}:${trace.lineNumber}):${trace.methodName}]${request.msg}")
        } else {
            request
        }
    }
}

/**
 * Detect and insert Thread information into the beginning of log content.
 */
class ThreadInfoDecorator : Decorator {
    override fun invoke(request: LogRequest): LogRequest {
        val thread = L.logEngine.threadInfo
        return if (thread != null) {
            request.copy(msg = "$thread${request.msg}")
        } else {
            request
        }
    }
}