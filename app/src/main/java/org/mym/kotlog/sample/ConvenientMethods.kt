package org.mym.kotlog.sample

import org.mym.kotlog.L

internal const val GROUP_UI = "UI"

fun uiLog(log: String) {
    L.d(msg = log, group = GROUP_UI, stackOffset = 1)
}