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
 * An Interceptor can decide whether this log can be printed.
 *
 * Multi interceptors are allowed. For each log request, if previous interceptor has intercepted it, later interceptors won't be called.
 *
 * There are two type of Interceptors: **appInterceptor**, which will be called with *origin request*; and **logInterceptor**, which will be called with *decorated request*(i.e. after all [Decorator]s called)..
 *
 * @see [L.addApplicationInterceptor]
 * @see [L.addLogInterceptor]
 */
typealias Interceptor = (LogRequest) -> Boolean

/**
 *
 * A Printer can implement own concrete print logic, e.g. to logcat, or to Files.
 *
 * NOTE:
 * 1. Printers are allowed to have their own intercept logic.
 * 2. Printers are not required to be a `Flushable` or `Closable`, but if you implemented it, you can batch manage them via convenient method [L.flushPrinters] and [L.closePrinters].
 */
typealias Printer = (LogRequest) -> Unit
