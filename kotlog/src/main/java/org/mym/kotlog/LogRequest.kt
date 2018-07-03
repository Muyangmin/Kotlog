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
 * Log request entity definition.
 *
 * @property[tag] Log tag to identify where does this log call. It is recommended to use auto tag.
 * @property[msg] Log content.
 * @property[level] Log level, must be one of [Log.VERBOSE],[Log.DEBUG], [Log.INFO], [Log.WARN] and [Log.ERROR].
 * @property[group] Log group; usually this is a dimension to filter logs and not printed by default. If you wish them to appear in log, you can add a [Decorator].
 * @property[stackOffset] Log offset, usually combined with wrapper methods.
 */
@Suppress("MemberVisibilityCanBePrivate")
data class LogRequest(val tag: String?, val msg: String?, val level: Int?, val group: String?, val stackOffset: Int = 0)