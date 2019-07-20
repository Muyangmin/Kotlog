package org.mym.kotlog.sample

import android.app.Application
import org.mym.kotlog.GlobalTagDecorator
import org.mym.kotlog.L
import org.mym.kotlog.LogRequest
import org.mym.kotlog.Printer

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()
        with(L) {
            install()
            addApplicationInterceptor({ !BuildConfig.DEBUG })
            addDecorator(GlobalTagDecorator("SampleApp"))
            addPrinter(object :Printer {
                override fun invoke(p1: LogRequest) {
                    println("New Printer are writing to file...")
                }
            })
        }
    }
}