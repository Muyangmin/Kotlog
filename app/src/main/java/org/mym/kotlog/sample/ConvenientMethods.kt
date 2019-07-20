package org.mym.kotlog.sample

import org.mym.kotlog.L

internal const val GROUP_UI = "UI"

fun L.uiLog(log: String) {
    d(msg = log, group = GROUP_UI, stackOffset = 1)
}